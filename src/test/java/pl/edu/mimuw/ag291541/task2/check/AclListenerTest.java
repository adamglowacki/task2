package pl.edu.mimuw.ag291541.task2.check;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import pl.edu.mimuw.ag291541.task2.DbFix;
import pl.edu.mimuw.ag291541.task2.GenericTest;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.executor.exception.ActionForbiddenException;

@TransactionConfiguration(defaultRollback = false)
public class AclListenerTest extends GenericTest {
	private Logger log = LoggerFactory.getLogger(AclListenerTest.class);

	@Before
	public void loadData() {
		fix = new DbFix(template, factory, txManager, userDao, contentDao,
				aceDao);
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				fix.loadData();
			}
		});
	}

	@After
	public void removeData() {
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				fix.removeData();
			}
		});
	}

	private void login(Long id) {
		announcementService.login(userDao.getUser(id));
	}

	@Test(expected = ActionForbiddenException.class)
	public void readForbidden() {
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				login(fix.kunegundaId);
				contentService.getContent(fix.zakazaneId);
			}
		});
	}

	@Test
	public void readAllowed() {
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				login(fix.kunegundaId);
				contentService.getContent(fix.gazetaId);
			}
		});
		log.info("Reading allowed content is ok.");
	}

	@Test(expected = ActionForbiddenException.class)
	public void writeForbidden() {
		executeInSeparateTransaction(new Executable() {
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
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				User kunegunda = userDao.getUser(fix.kunegundaId);
				aclService.addClassAccess(Announcement.class, ACLRights.WRITE,
						kunegunda);
				aclService.addClassAccess(AnnouncementInstance.class,
						ACLRights.WRITE, kunegunda);
			}
		});
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				User kunegunda = userDao.getUser(fix.kunegundaId);
				login(kunegunda.getId());
				Set<User> recipients = new HashSet<User>();
				recipients.add(userDao.getUser(fix.jerzyId));
				recipients.add(kunegunda);
				announcementService.sendAnnouncement("Witajcie!",
						"To jest bardzo ważne oświadczenie...", recipients);
			}
		});
		executeInSeparateTransaction(new Executable() {
			@Override
			public void execute() {
				User jerzy = userDao.getUser(fix.jerzyId);
				login(jerzy.getId());
				Set<Announcement> unread = announcementService
						.getAllUnread(jerzy);
				assertTrue(unread.size() == 2);
				for (Announcement a : unread)
					announcementService.markRead(a, jerzy);
				unread = announcementService.getAllUnread(jerzy);
				assertTrue(unread.size() == 0);
			}
		});
	}

	private void executeInSeparateTransaction(Executable exec) {

		TransactionDefinition txDef = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus txStatus = txManager.getTransaction(txDef);
		// Session s = factory.openSession();
		// Transaction tx = s.beginTransaction();
		// try {
		exec.execute();
		// tx.commit();
		// } catch (RuntimeException e) {
		// tx.rollback();
		// throw e;
		// }
		// s.close();
		// txStatus.flush();
		txManager.commit(txStatus);
		// } catch (RuntimeException e) {
		// txManager.rollback(txStatus);
		// throw e;
		// }
	}

	private interface Executable {
		public void execute();
	}
}
