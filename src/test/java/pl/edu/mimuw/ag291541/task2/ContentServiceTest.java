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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.service.ContentService;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class ContentServiceTest {
	private Logger log = LoggerFactory.getLogger(ContentServiceTest.class);
	@Autowired
	private HibernateTemplate template;
	@Autowired
	private UserDAO userDao;
	@Autowired
	private ContentDAO contentDao;
	@Autowired
	private ContentService contentService;
	private DbFix fix;

	private final String title = "To jest tytuł.";
	private final String body = "To jest ciało.";

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
	public void createContent() {
		Content c = contentService.createContent(title, body);
		assertEquals(c.getTitle(), title);
		assertEquals(c.getBody(), body);
		log.info("Content creation is ok.");
	}

	@Test
	public void getContent() {
		Content c = contentService.getContent(fix.gazetaId);
		assertEquals(c.getTitle(), DbFix.gazetaTitle);
		assertEquals(c.getBody(), DbFix.gazetaBody);
		log.info("Content retrieveing seems to be ok.");
	}

	@Test
	public void getAnnouncement() {
		Announcement a = contentService.getAnnouncement(fix.apelId);
		assertEquals(a.getTitle(), DbFix.apelTitle);
		assertEquals(a.getBody(), DbFix.apelBody);
		Set<AnnouncementInstance> insts = a.getInstances();
		assertTrue(insts.size() == 2);
		Set<User> users = new HashSet<User>();
		users.add(userDao.getUser(DbFix.kunegundaName, DbFix.kunegundaSurname));
		users.add(userDao.getUser(DbFix.jerzyName, DbFix.jerzySurname));
		Set<User> foundUsers = new HashSet<User>();
		for (AnnouncementInstance i : insts) {
			foundUsers.add(i.getReceiver());
			assertNull(i.getReadDate());
			assertFalse(i.isReadStatus());
		}
		assertEquals(foundUsers, users);
		log.info("Getting anouncement is ok.");
	}

	@Test
	public void updateContentBody() {
		final String body = "To jest coś całkiem nowego.";
		Content c = contentService.getContent(fix.apelId);
		c.setBody(body);
		contentService.updateContent(c);
		c = contentService.getContent(fix.apelId);
		assertEquals(c.getBody(), body);
		log.info("Updating content body is ok.");
	}

	@Test
	public void updateContentTitle() {
		Content apel = contentService.getContent(fix.apelId);
		apel.setTitle(DbFix.gazetaTitle);
		contentService.updateContent(apel);
		log.error("Uniqueness of content titles is not preserved.");
	}

	@Test
	public void deleteContent() {
		contentService.deleteContent(contentService.getContent(fix.gazetaId));
		contentService.getContent(fix.gazetaId);
		log.error("Deletion content failed.");
	}

	@Test
	public void getAllContents() {
		Set<Content> got = new HashSet<Content>(contentService.getAllContents());
		Set<Content> expected = new HashSet<Content>();
		expected.add(contentService.getContent(fix.apelId));
		expected.add(contentService.getContent(fix.gazetaId));
		assertEquals(got, expected);
		log.info("Getting all the contents is ok.");
	}

	@Test
	public void getAllAnnouncements() {
		Set<Announcement> got = new HashSet<Announcement>(
				contentService.getAllAnnouncements());
		Set<Announcement> expected = new HashSet<Announcement>();
		expected.add(contentService.getAnnouncement(fix.apelId));
		assertEquals(got, expected);
		log.info("Getting all the announcements seems to be ok.");
	}
}