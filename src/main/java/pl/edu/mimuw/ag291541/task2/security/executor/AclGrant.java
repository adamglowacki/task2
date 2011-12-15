package pl.edu.mimuw.ag291541.task2.security.executor;

import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.util.UserUtil;

public class AclGrant implements PreInsertEventListener {
	private static final long serialVersionUID = 6157501295776229143L;

	private Logger log = LoggerFactory.getLogger(AclGrant.class);

	@Autowired
	private UserUtil userUtil;
	@Autowired
	private ACLService aclService;
	@Autowired
	private DuringProcessingSet duringProcessingInListener;

	@Override
	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean onPreInsert(PreInsertEvent event) {
		Object entity = event.getEntity();
		User sender = userUtil.getUser();
		if (sender != null) {
			if (!duringProcessingInListener.isDuringAccessCheck(entity)) {
				try {
					duringProcessingInListener.setDuringAccessCheck(entity);
					if (entity instanceof Announcement) {
						// TransactionStatus txStatus =
						// txManager.getTransaction(null);
						// log.debug("New transaction: {}",
						// txStatus.isNewTransaction());
						Announcement a = (Announcement) entity;
						// TransactionStatus txStatus =
						// txManager.getTransaction(null);
						log.debug("About to grant WRITE on {} to {}", a, sender);
						aclService
								.addInstanceAccess(a, ACLRights.WRITE, sender);
						// txStatus.flush();
						// txManager.commit(txStatus);
					} else if (entity instanceof AnnouncementInstance) {
						AnnouncementInstance ai = (AnnouncementInstance) entity;
						Object[] state = event.getState();
						Announcement a = (Announcement) state[0];
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
						// txStatus.flush();
						// txManager.commit(txStatus);
					}
				} finally {
					duringProcessingInListener.clear(entity);
				}
			}
		}
		return false;
	}
}
