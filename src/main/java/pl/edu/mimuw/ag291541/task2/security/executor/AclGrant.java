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
import org.springframework.transaction.PlatformTransactionManager;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.service.AclUtilLibrary;
import pl.edu.mimuw.ag291541.task2.util.Executable;
import pl.edu.mimuw.ag291541.task2.util.TransactionUtilLibrary;
import pl.edu.mimuw.ag291541.task2.util.UserUtilLibrary;

public class AclGrant implements PostInsertEventListener {
	private static final long serialVersionUID = 6157501295776229143L;

	private static final String DELETE_INSTANCE_ACE = "DELETE FROM INSTANCEACE WHERE OBJECTHASHCODE = ? AND USER_ID = ?";
	private static final String INSERT_INSTANCE_ACE = "INSERT INTO INSTANCEACE (id, objecthashcode, rightstype, user_id) VALUES (?, ?, ?, ?) ";
	private static final String SELECT_INSTANCE_RIGHTS = "SELECT rightstype FROM instanceace WHERE objecthashcode = ? AND user_id = ?";

	private Logger log = LoggerFactory.getLogger(AclGrant.class);

	@Autowired
	private UserUtilLibrary userUtil;
	@Autowired
	private AclUtilLibrary aclUtil;
	@Autowired
	private TransactionUtilLibrary txUtil;

	private JdbcTemplate jdbcTemplate;
	/* Notice that it is not autowired because we have two transaction managers. */
	private PlatformTransactionManager jdbcTxManager;

	/*
	 * Through this setter Spring is intended to inject data source so that we
	 * can have JDBC template.
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void setJdbcTxManager(PlatformTransactionManager jdbcTxManager) {
		this.jdbcTxManager = jdbcTxManager;
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		Object entity = event.getEntity();
		final User sender = userUtil.getUser();
		if (sender != null) {
			if (entity instanceof Announcement) {
				final Announcement a = (Announcement) entity;
				log.debug("About to grant WRITE on {} to {}", a, sender);
				dealWithAnnouncement(a, sender);
			} else if (entity instanceof AnnouncementInstance) {
				final AnnouncementInstance ai = (AnnouncementInstance) entity;
				final Announcement a = ai.getAnnouncement();
				final User receiver = ai.getReceiver();
				log.debug("About to grant WRITE on {} to {}", ai, receiver);
				log.debug("About to grant READ on {} to {}", a, receiver);
				dealWithAnnouncementInstance(a, receiver, ai);
			}
		}
	}

	private Long getNextId() {
		return jdbcTemplate
				.queryForLong("SELECT nextval ('public.hibernate_sequence')");
	}

	private void deleteInstanceAce(Long objectId, User user) {
		jdbcTemplate.update(DELETE_INSTANCE_ACE, objectId, user.getId());
	}

	private void insertInstanceAce(Long objectId, ACLRights rights, User user) {
		jdbcTemplate.update(INSERT_INSTANCE_ACE, getNextId(), objectId,
				rights.ordinal(), user.getId());
	}

	private void dealWithAnnouncement(final Announcement a, final User sender) {
		txUtil.executeInTransaction(jdbcTxManager, new Executable() {
			@Override
			public void execute() {
				Long objectId = aclUtil.getObjectId(a);
				deleteInstanceAce(objectId, sender);
				insertInstanceAce(objectId, ACLRights.WRITE, sender);
			}
		});
	}

	private ACLRights getCurrentInstanceRights(Long objectId, User user) {
		return jdbcTemplate.query(SELECT_INSTANCE_RIGHTS,
				new ACLRightsExtractor(), objectId, user.getId());
	}

	private void increaseInstanceAce(Long objectId, ACLRights rights, User user) {
		ACLRights currentRights = getCurrentInstanceRights(objectId, user);
		if (currentRights == null || currentRights.ordinal() < rights.ordinal()) {
			deleteInstanceAce(objectId, user);
			insertInstanceAce(objectId, rights, user);
		} /* Otherwise user has already desired access privilege. */
	}

	private void dealWithAnnouncementInstance(final Announcement a,
			final User receiver, final AnnouncementInstance ai) {
		txUtil.executeInTransaction(jdbcTxManager, new Executable() {
			@Override
			public void execute() {
				Long announcementId = aclUtil.getObjectId(a);
				Long announcementInstanceId = aclUtil.getObjectId(ai);
				deleteInstanceAce(announcementInstanceId, receiver);
				insertInstanceAce(announcementInstanceId, ACLRights.WRITE,
						receiver);
				increaseInstanceAce(announcementId, ACLRights.READ, receiver);
			}
		});
	}

	private class ACLRightsExtractor implements ResultSetExtractor<ACLRights> {
		@Override
		public ACLRights extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			/* We take only the first value (if any). */
			if (rs.next()) {
				Integer rights = rs.getInt(1);
				return ACLRights.values()[rights];
			} else
				return null;
		}
	}
}
