package pl.edu.mimuw.ag291541.task2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.service.AnnouncementService;
import pl.edu.mimuw.ag291541.task2.service.ContentService;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class AnnouncementServiceTest {
	private Logger log = LoggerFactory.getLogger(ContentServiceTest.class);
	@Autowired
	private HibernateTemplate template;
	@Autowired
	private UserDAO userDao;
	@Autowired
	private ContentDAO contentDao;
	@Autowired
	private ContentService contentService;
	@Autowired
	private AnnouncementService announcementService;
	private DbFix fix;

	private static final String title = "Obwieszczenie dotygodniowe";
	private static final String body = "Dziś mamy wolne.";

	@Before
	public void loadData() {
		fix = new DbFix(template, userDao, contentDao);
		fix.loadData();
	}

	@After
	public void removeData() {
		fix.removeData();
	}

	@Test
	public void login() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		announcementService.login(kunegunda);
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		assertTrue(kunegunda.equals(principal));
		log.info("Logged in successfully.");
	}

	@Test
	public void logout() {
		announcementService.logout();
		assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
		log.info("Logged out successfully.");
	}

	@Test
	public void sendAnnouncement() {
		Set<User> recipients = new HashSet<User>();
		User kunegunda = userDao.getUser(fix.kunegundaId);
		User jerzy = userDao.getUser(fix.jerzyId);
		recipients.add(kunegunda);
		recipients.add(jerzy);
		Announcement a = announcementService.sendAnnouncement(title, body,
				recipients);
		Set<AnnouncementInstance> insts = a.getInstances();
		assertTrue(insts.size() == 2);
		Set<User> thoseWhoGot = new HashSet<User>();
		for (AnnouncementInstance i : insts) {
			assertNull(i.getReadDate());
			assertFalse(i.isReadStatus());
			thoseWhoGot.add(i.getReceiver());
		}
		assertEquals(recipients, thoseWhoGot);
		log.info("Sending an announcement succeeded.");
	}
}
