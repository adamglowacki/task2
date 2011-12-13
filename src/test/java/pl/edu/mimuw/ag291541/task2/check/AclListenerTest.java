package pl.edu.mimuw.ag291541.task2.check;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.DbFix;
import pl.edu.mimuw.ag291541.task2.GenericTest;
import pl.edu.mimuw.ag291541.task2.entity.Content;

public class AclListenerTest extends GenericTest {
	private Logger log = LoggerFactory.getLogger(AclListenerTest.class);

	@Before
	public void loadData() {
		fix = new DbFix(template, factory, txManager, userDao, contentDao,
				aceDao);
		executeInSeparateTransaction(new Executable() {
			@Override
			@Transactional
			public void execute() {
				fix.loadData();
			}
		});
	}

	@After
	public void removeData() {
		executeInSeparateTransaction(new Executable() {
			@Override
			@Transactional
			public void execute() {
				fix.removeData();
			}
		});
	}

	private void login(Long id) {
		announcementService.login(userDao.getUser(id));
	}

	@Test
	// (expected = ActionForbiddenException.class)
	public void readForbidden() {
		executeInSeparateTransaction(new Executable() {
			@Override
			@Transactional
			public void execute() {
				login(fix.kunegundaId);
				contentService.getContent(fix.apelId);
			}
		});
	}

	@Test
	public void readAllowed() {
		executeInSeparateTransaction(new Executable() {
			@Override
			@Transactional
			public void execute() {
				login(fix.kunegundaId);
				contentService.getContent(fix.gazetaId);
			}
		});
		log.info("Reading allowed content is ok.");
	}

	@Test
	public void writeForbidden() {
		login(fix.kunegundaId);
		Content gazeta = contentService.getContent(fix.gazetaId);
		gazeta.setTitle("Blah...");
	}

	private void executeInSeparateTransaction(Executable exec) {
		TransactionStatus txStatus = txManager.getTransaction(null);
		// Session s = factory.openSession();
		// Transaction t = s.beginTransaction();
		exec.execute();
		txManager.getTransaction(null).flush();
		txManager.commit(txStatus);
		// t.commit();
		// s.close();
	}

	private interface Executable {
		public void execute();
	}
}