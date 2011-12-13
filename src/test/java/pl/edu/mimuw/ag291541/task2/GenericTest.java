package pl.edu.mimuw.ag291541.task2;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.service.AnnouncementService;
import pl.edu.mimuw.ag291541.task2.service.ContentService;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class GenericTest {
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
	protected JdbcTemplate template;
	@Autowired
	protected SessionFactory factory;
	@Autowired
	protected PlatformTransactionManager txManager;
	protected DbFix fix;
}
