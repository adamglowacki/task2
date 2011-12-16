package pl.edu.mimuw.ag291541.task2.security.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.security.service.AclUtil;
import pl.edu.mimuw.ag291541.task2.util.UserUtil;

public class AclGrant implements PostInsertEventListener {
	private static final long serialVersionUID = 6157501295776229143L;

	private Logger log = LoggerFactory.getLogger(AclGrant.class);

	@Autowired
	private UserUtil userUtil;
	@Autowired
	private AclUtil aclUtil;
	@Autowired
	private ACLService aclService;

	// private SessionFactory sessionFactory;

	@Autowired
	private DuringProcessingSet duringProcessingInListener;

	private JdbcTemplate jdbcTemplate;

	// public SessionFactory getSessionFactory() {
	// return sessionFactory;
	// }
	//
	// public void setSessionFactory(SessionFactory sessionFactory) {
	// this.sessionFactory = sessionFactory;
	// }

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	// @Transactional//(propagation = Propagation.REQUIRES_NEW)
	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		final User sender = userUtil.getUser();
		if (sender != null) {
			if (entity instanceof Announcement) {
				// // TransactionStatus txStatus =
				// txManager.getTransaction(null);
				// // log.debug("New transaction: {}",
				// // txStatus.isNewTransaction());
				final Announcement a = (Announcement) entity;
				// if (!duringProcessingInListener.isDuringAccessCheck(a)) {
				// duringProcessingInListener.setDuringAccessCheck(a);
				// try {
				// // Transaction tx = event.getSession().beginTransaction();
				// // // TransactionStatus txStatus =
				// // txManager.getTransaction(null);
				// // try {
				// TransactionDefinition txDef = new
				// DefaultTransactionDefinition(
				// DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
				// TransactionStatus txStatus = txManager.getTransaction(txDef);
				log.debug("About to grant WRITE on {} to {}", a, sender);
				// aclService.addInstanceAccess(a, ACLRights.WRITE, sender);
				dealWithAnnouncement(a, sender);
				// txManager.commit(txStatus);
				// // tx.commit();
				// // } catch (RuntimeException e) {
				// // tx.rollback();
				// // throw e;
				// // }
				// } finally {
				// duringProcessingInListener.clear(a);
				// }
				// }
				// // txStatus.flush();
				// // txManager.commit(txStatus);
			} else if (entity instanceof AnnouncementInstance) {
				final AnnouncementInstance ai = (AnnouncementInstance) entity;
				final Announcement a = ai.getAnnouncement();
				final User receiver = ai.getReceiver();
				// // TransactionStatus txStatus =
				// // txManager.getTransaction(null);
				// // event.getSession().flush();
				// SessionFactory sessionFactory = event.getSession()
				// // .getSessionFactory();
				// // Session session = sessionFactory.openSession();
				// // Transaction transaction = session.beginTransaction();
				// // try {
				// if (!duringProcessingInListener.isDuringAccessCheck(ai)) {
				// duringProcessingInListener.setDuringAccessCheck(ai);
				// try {
				// // Transaction tx = event.getSession().beginTransaction();
				// // try {
				// TransactionDefinition txDef = new
				// DefaultTransactionDefinition(
				// DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
				// TransactionStatus txStatus = txManager.getTransaction(txDef);
				log.debug("About to grant WRITE on {} to {}", ai, receiver);
				log.debug("About to grant READ on {} to {}", a, receiver);
				dealWithAnnouncementInstance(a, receiver, ai);
				// txManager.commit(txStatus);
				// // tx.commit();
				// // } catch (RuntimeException e) {
				// // tx.rollback();
				// // throw e;
				// // }
				// } finally {
				// duringProcessingInListener.clear(ai);
				// }
				// }
				// // transaction.commit();
				// // } catch (RuntimeException e) {
				// // transaction.rollback();
				// // throw e;
				// // } finally {
				// // session.close();
				// // }
				// // txStatus.flush();
				// // txManager.commit(txStatus);
			}
		}
	}

	private Long getNextId() {
		return jdbcTemplate
				.queryForLong("SELECT nextval ('public.hibernate_sequence')");
	}

	private void deleteInstanceAce(Long objectId, User user) {
		jdbcTemplate
				.update("DELETE FROM INSTANCEACE WHERE OBJECTHASHCODE = ? AND USER_ID = ?",
						objectId, user.getId());
	}

	private void insertInstanceAce(Long objectId, ACLRights rights, User user) {
		jdbcTemplate
				.update("INSERT INTO INSTANCEACE (id, objecthashcode, rightstype, user_id) VALUES (?, ?, ?, ?) ",
						getNextId(), objectId, rights.ordinal(), user.getId());
	}

	private void dealWithAnnouncement(Announcement a, User sender) {
		Long objectId = aclUtil.getObjectId(a);
		deleteInstanceAce(objectId, sender);
		insertInstanceAce(objectId, ACLRights.WRITE, sender);
	}

	private ACLRights getCurrentInstanceRights(Long objectId, User user) {
		return jdbcTemplate
				.query("SELECT rightstype FROM instanceace WHERE objecthashcode = ? AND user_id = ?",
						new ResultSetExtractor<ACLRights>() {
							@Override
							public ACLRights extractData(ResultSet rs)
									throws SQLException, DataAccessException {
								/* We take only the first value (if any). */
								if (rs.next()) {
									Integer rights = rs.getInt(1);
									ACLRights[] rightsValues = ACLRights
											.values();
									return rights != null && rights >= 0
											&& rights < rightsValues.length ? rightsValues[rights
											.intValue()] : null;
								} else
									return null;
							}
						}, objectId, user.getId());
	}

	private void increaseInstanceAce(Long objectId, ACLRights rights, User user) {
		ACLRights currentRights = getCurrentInstanceRights(objectId, user);
		if (currentRights == null || currentRights.ordinal() < rights.ordinal()) {
			deleteInstanceAce(objectId, user);
			insertInstanceAce(objectId, rights, user);
		} /* Otherwise user has already desired access privilege. */
	}

	private void dealWithAnnouncementInstance(Announcement a, User receiver,
			AnnouncementInstance ai) {
		Long announcementId = aclUtil.getObjectId(a);
		Long announcementInstanceId = aclUtil.getObjectId(ai);
		deleteInstanceAce(announcementInstanceId, receiver);
		insertInstanceAce(announcementInstanceId, ACLRights.WRITE, receiver);
		increaseInstanceAce(announcementId, ACLRights.READ, receiver);
	}
	// private void execSeparately(PostInsertEvent event, Executable exec) {
	// Session session = event.getSession().getFactory().openSession();
	// Transaction tx = session.beginTransaction();
	// try {
	// exec.run();
	// tx.commit();
	// } catch (RuntimeException e) {
	// tx.rollback();
	// throw e;
	// } finally {
	// session.close();
	// }
	// }
	//
	// private interface Executable {
	// public void run();
	// }
}
