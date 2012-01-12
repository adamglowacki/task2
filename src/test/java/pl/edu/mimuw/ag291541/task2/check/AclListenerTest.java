package pl.edu.mimuw.ag291541.task2.check;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import pl.edu.mimuw.ag291541.task2.DbFix;
import pl.edu.mimuw.ag291541.task2.ManualTransactionTest;
import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.executor.exception.AccessForbiddenException;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.service.AnnouncementService;
import pl.edu.mimuw.ag291541.task2.service.ContentService;
import pl.edu.mimuw.ag291541.task2.util.Executable;
import pl.edu.mimuw.ag291541.task2.util.TransactionUtilLibrary;

public class AclListenerTest extends ManualTransactionTest {
	private static final String NEW_ANNOUNCEMENT_BODY_0 = "To jest bardzo ważne oświadczenie...0";
	private static final String NEW_ANNOUNCEMENT_TITLE_0 = "Witajcie!0";
	private static final String NEW_ANNOUNCEMENT_BODY_1 = "To jest bardzo ważne oświadczenie...1";
	private static final String NEW_ANNOUNCEMENT_TITLE_1 = "Witajcie!1";
	private static final String NEW_ANNOUNCEMENT_BODY_2 = "To jest bardzo ważne oświadczenie...2";
	private static final String NEW_ANNOUNCEMENT_TITLE_2 = "Witajcie!2";
	private static final String NEW_ANNOUNCEMENT_BODY_3 = "To jest bardzo ważne oświadczenie...3";
	private static final String NEW_ANNOUNCEMENT_TITLE_3 = "Witajcie!3";

	private Logger log = LoggerFactory.getLogger(AclListenerTest.class);

	public void injectDependencies(UserDAO userDAO, ContentDAO contentDAO,
			AceDAO aceDAO, ContentService contentService,
			AnnouncementService announcementService, ACLService aclService,
			JdbcTemplate jdbcTemplate,
			PlatformTransactionManager platformTransactionManager,
			TransactionUtilLibrary txUtil) {
		this.userDao = userDAO;
		this.contentDao = contentDAO;
		this.aceDao = aceDAO;
		this.contentService = contentService;
		this.announcementService = announcementService;
		this.aclService = aclService;
		this.template = jdbcTemplate;
		this.txManager = platformTransactionManager;
		this.txUtil = txUtil;
	}

	@Test(expected = AccessForbiddenException.class)
	public void readForbidden() {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(fix.kunegundaId);
				contentService.getContent(fix.zakazaneId);
			}
		});
	}

	@Test
	public void readAllowed() {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(fix.jerzyId);
				contentService.getContent(fix.apelId);
			}
		});
		log.info("Reading allowed content is ok.");
	}

	@Test(expected = AccessForbiddenException.class)
	public void writeForbidden() {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(fix.kunegundaId);
				Content gazeta = contentService.getContent(fix.gazetaId);
				gazeta.setTitle("Blah...");
				contentService.updateContent(gazeta);
			}
		});
	}

	@Test
	public void justSendAnnouncement() {
		/* Firstly make kunegunda more powerful. */
		makePowerfulEnoughToSend(fix.kunegundaId);
		/* Send an announcement from kunegunda to herself and jerzy. */
		sendFromTo(NEW_ANNOUNCEMENT_TITLE_0, NEW_ANNOUNCEMENT_BODY_0,
				fix.kunegundaId, fix.kunegundaId, fix.jerzyId);
		/* As jerzy read all the unread announcements addressed to him. */
		readAndMarkRead(fix.jerzyId, 2);
		log.info("Sending and reading an announcement was successful.");
	}

	private void sendFromTo(final String title, final String body,
			final Long from, final Long... to) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				User fromUser = userDao.getUser(from);
				login(fromUser.getId());
				Set<User> recipients = new HashSet<User>();
				for (Long toId : to)
					recipients.add(userDao.getUser(toId));
				announcementService.sendAnnouncement(title, body, recipients);
			}
		});
	}

	private void readAndMarkRead(final Long userId, final int expectedUnreadSize) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(userId);
				User user = userDao.getUser(userId);
				Set<Announcement> unread = announcementService
						.getAllUnread(user);
				assert (unread.size() == expectedUnreadSize);
				for (Announcement a : unread)
					announcementService.markRead(a, user);
				unread = announcementService.getAllUnread(user);
				assertTrue(unread.size() == 0);
			}
		});
	}

	private void getContentAsUser(final Long userId, final String title) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(userId);
				contentService.getContent(title);
			}
		});
	}

	private void getAllContentsAsUser(final Long userId) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(userId);
				contentService.getAllContents();
			}
		});
	}

	private void addInstanceToAnnouncement(final Long loggedUserId,
			final String title) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				login(loggedUserId);
				Announcement announcement = contentService
						.getAnnouncement(title);
				AnnouncementInstance instance = contentDao
						.createAnnouncementInstance(
								userDao.getUser(loggedUserId), announcement);
				announcement.getInstances().add(instance);
			}
		});
	}

	private void makePowerfulEnoughToSend(final Long userId) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				logout();
				User user = userDao.getUser(userId);
				aclService.addClassAccess(Announcement.class, ACLRights.WRITE,
						user);
				aclService.addClassAccess(AnnouncementInstance.class,
						ACLRights.WRITE, user);
			}
		});
	}

	private void revokeOnAnnouncementInstance(final Long recipientId,
			final ACLRights rights, final String title) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				logout();
				Announcement a = contentService.getAnnouncement(title);
				User recipient = userDao.getUser(recipientId);
				AnnouncementInstance desired = null;
				for (AnnouncementInstance ai : a.getInstances()) {
					if (ai.getReceiver().equals(recipient)) {
						desired = ai;
						break;
					}
				}
				assertNotNull(desired);
				aclService.revokeInstanceAccess(desired, rights, recipient);
			}
		});
	}

	/**
	 * Revokes on the instance, its class and all its subclasses.
	 * 
	 * @param userId
	 *            From whom the privileges shall be revoked.
	 * @param rights
	 *            What shall be revoked.
	 * @param title
	 *            A title of the announcement.
	 */
	private void revokeOnAnnouncement(final Long userId,
			final ACLRights rights, final String title) {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				logout();
				Announcement a = contentService.getAnnouncement(title);
				User user = userDao.getUser(userId);
				aclService.revokeInstanceAccess(a, rights, user);
				Class<?> clazz = a.getClass();
				while (clazz != null) {
					aclService.revokeClassAccess(clazz, rights, user);
					clazz = clazz.getSuperclass();
				}
			}
		});
	}

	/**
	 * Send and revoke WRITE privilege on announcement instance from recipient.
	 */
	@Test
	public void revokeWriteFromRecipient() {
		makePowerfulEnoughToSend(fix.kunegundaId);
		/* Send. */
		sendFromTo(NEW_ANNOUNCEMENT_TITLE_1, NEW_ANNOUNCEMENT_BODY_1,
				fix.kunegundaId, fix.kunegundaId, fix.jerzyId);
		/* Revoke. */
		revokeOnAnnouncementInstance(fix.jerzyId, ACLRights.WRITE,
				NEW_ANNOUNCEMENT_TITLE_1);
		/* Try to read and mark. */
		try {
			readAndMarkRead(fix.jerzyId, 2);
			assertTrue(false);
		} catch (AccessForbiddenException e) {
			log.info("Access control runs ok when revoked write on announcement instance from recipient.");
		}
	}

	/**
	 * Send and revoke READ privilege on announcement from recipient.
	 */
	@Test
	public void revokeReadFromRecipient() {
		makePowerfulEnoughToSend(fix.gapcioId);
		/* Send. */
		sendFromTo(NEW_ANNOUNCEMENT_TITLE_2, NEW_ANNOUNCEMENT_BODY_2,
				fix.gapcioId, fix.gapcioId, fix.wazniakId);
		/* Revoke. */
		revokeOnAnnouncement(fix.wazniakId, ACLRights.READ,
				NEW_ANNOUNCEMENT_TITLE_2);
		/* Try to read and mark. */
		try {
			readAndMarkRead(fix.wazniakId, 2);
			assertTrue(false);
		} catch (AccessForbiddenException e) {
			log.info("Access control runs ok when revoked read on announcement from recipient.");
		}
	}

	/**
	 * Create a content and revoke READ on the content from the creator.
	 */
	@Test
	public void revokeReadFromCreator() {
		makePowerfulEnoughToSend(fix.wladyslawId);
		sendFromTo(NEW_ANNOUNCEMENT_TITLE_3, NEW_ANNOUNCEMENT_BODY_3,
				fix.wladyslawId, fix.wladyslawId, fix.zdzislawId);
		revokeOnAnnouncement(fix.wladyslawId, ACLRights.READ,
				NEW_ANNOUNCEMENT_TITLE_3);
		try {
			getContentAsUser(fix.wladyslawId, NEW_ANNOUNCEMENT_TITLE_3);
			assertTrue(false);
		} catch (AccessForbiddenException e) {
			log.info("Access control runs ok when revoked read on content and attempted to get it.");
		}
	}

	/**
	 * Tries to get all contents having read on a whole class.
	 */
	@Test
	public void getAllWithPrivilege() {
		/* Grant kunegunda with READ on content class. */
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				aclService.addClassAccess(Content.class, ACLRights.READ,
						userDao.getUser(fix.hieronimId));
			}
		});
		getAllContentsAsUser(fix.hieronimId);
	}

	/**
	 * Tries to get all contents without having privileges to read them all.
	 */
	@Test(expected = AccessForbiddenException.class)
	public void getAllWithoutPrivilege() {
		getAllContentsAsUser(fix.bonifacyId);
	}

	/**
	 * Tries to change the announcement instances of an announcement without
	 * having privileges to do it.
	 */
	@Test
	public void updateCollectionWithoutPrivileges() {
		/*
		 * Grant WRITE on AnnouncementInstance to kunegunda so that exception is
		 * caused by adding to collection - not by creating a new instance.
		 */
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				aclService.addClassAccess(AnnouncementInstance.class,
						ACLRights.WRITE, userDao.getUser(fix.kunegundaId));
			}
		});
		addInstanceToAnnouncement(fix.kunegundaId, DbFix.apelTitle);
	}

	/**
	 * Tries to change the announcement instances of an announcement having
	 * needed privileges.
	 */
	@Test
	public void updateCollectionWithPrivileges() {
		makePowerfulEnoughToSend(fix.kunegundaId);
		addInstanceToAnnouncement(fix.kunegundaId, DbFix.apelTitle);
	}
}