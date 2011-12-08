package pl.edu.mimuw.ag291541.task2;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.service.AnnouncementService;
import pl.edu.mimuw.ag291541.task2.service.ContentService;
import pl.edu.mimuw.ag291541.task2.types.TestTransactionDefinition;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class DbTest {
	@Autowired
	protected UserDAO userDao;
	@Autowired
	protected ContentDAO contentDao;
	@Autowired
	protected ContentService contentService;
	@Autowired
	protected AnnouncementService announcementService;
	@Autowired
	private PlatformTransactionManager txManager;
	protected DbFix fix;
	private TransactionStatus txStatus;

	@Before
	public void loadData() {
		txStatus = txManager.getTransaction(new TestTransactionDefinition());
		fix = new DbFix(userDao, contentDao);
		fix.loadData();
	}

	@After
	public void removeData() {
		txManager.rollback(txStatus);
	}
}
