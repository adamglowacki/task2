package pl.edu.mimuw.ag291541.task2.security.executor;

import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.util.UserUtil;

public class AclGrant implements PostInsertEventListener {
	private static final long serialVersionUID = 6157501295776229143L;

	private Logger log = LoggerFactory.getLogger(AclGrant.class);

	@Autowired
	private UserUtil userUtil;
	@Autowired
	private ACLService aclService;
	@Autowired
	private DuringProcessingSet duringProcessingInListener;

	@Override
	@Transactional
	// (propagation = Propagation.REQUIRES_NEW)
	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		User sender = userUtil.getUser();
		if (sender != null) {
			if (entity instanceof Announcement) {
				// TransactionStatus txStatus = txManager.getTransaction(null);
				// log.debug("New transaction: {}",
				// txStatus.isNewTransaction());
				Announcement a = (Announcement) entity;
				// TransactionStatus txStatus =
				// txManager.getTransaction(null);
				log.debug("About to grant WRITE on {} to {}", a, sender);
				aclService.addInstanceAccess(a, ACLRights.WRITE, sender);
				// txStatus.flush();
				// txManager.commit(txStatus);
			} else if (entity instanceof AnnouncementInstance) {
				if (!duringProcessingInListener.isDuringAccessCheck(entity)) {
					try {
						duringProcessingInListener.setDuringAccessCheck(entity);
						AnnouncementInstance ai = (AnnouncementInstance) entity;
						Announcement a = ai.getAnnouncement();
						User receiver = ai.getReceiver();
						// TransactionStatus txStatus =
						// txManager.getTransaction(null);
						log.debug("About to grant WRITE on {} to {}", ai,
								receiver);
						aclService.addInstanceAccess(ai, ACLRights.WRITE,
								receiver);
						log.debug("About to grant READ on {} to {}", a,
								receiver);
						aclService.addInstanceAccess(a, ACLRights.READ,
								receiver);
					} finally {
						duringProcessingInListener.clear(entity);
					}
				}
				// txStatus.flush();
				// txManager.commit(txStatus);
			}
		}
	}
}
