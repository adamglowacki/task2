package pl.edu.mimuw.ag291541.task2;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.service.AnnouncementService;
import pl.edu.mimuw.ag291541.task2.service.ContentService;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
public abstract class DbTest {
	@Autowired
	protected UserDAO userDao;
	@Autowired
	protected ContentDAO contentDao;
	@Autowired
	protected AceDAO aceDao;
	@Autowired
	protected ContentService contentService;
	@Autowired
	protected AnnouncementService announcementService;
	@Autowired
	protected ACLService aclService;
	@Autowired
	private JdbcTemplate template;
	@Autowired
	private SessionFactory factory;
	protected DbFix fix;
	private Logger log = LoggerFactory.getLogger(DbTest.class);

	@Before
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void loadData() {
		fix = new DbFix(template, factory, userDao, contentDao, aceDao);
		log.debug("Started loading db fixture...");
		fix.loadData();
		log.debug("Db fixture loaded.");
	}

	@After
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeData() {
		log.debug("Removing data after a test...");
		fix.removeData();
		log.debug("Data after a test removed.");
	}
}
