package pl.edu.mimuw.ag291541.task2.nocheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.DbFix;
import pl.edu.mimuw.ag291541.task2.DeclarativeTransactionTest;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class ContentServiceTest extends DeclarativeTransactionTest {
	private static final String NON_EXISTING_CONTENT_TITLE = "Nieistniejący content";

	private static final String NEW_UNIQUE_AMONG_CONTENT_TITLE = "A to jest niepowtarzalny tytuł";

	private Logger log = LoggerFactory.getLogger(ContentServiceTest.class);

	private final String title = "To jest tytuł.";
	private final String body = "To jest ciało.";

	@Test
	@Transactional
	public void createContent() {
		Content c = contentService.createContent(title, body);
		assertEquals(c.getTitle(), title);
		assertEquals(c.getBody(), body);
		log.info("Content creation is ok.");
	}

	@Test
	@Transactional
	public void getContent() {
		Content c = contentService.getContent(fix.gazetaId);
		assertEquals(c.getTitle(), DbFix.gazetaTitle);
		assertEquals(c.getBody(), DbFix.gazetaBody);
		log.info("Content retrieveing seems to be ok.");
	}

	@Test
	@Transactional
	public void getContentByTitle() {
		Content c = contentService.getContent(DbFix.gazetaTitle);
		assertEquals(c.getTitle(), DbFix.gazetaTitle);
		c = contentService.getContent(NON_EXISTING_CONTENT_TITLE);
		assertNull(c);
		log.info("Content retrieving by its title is ok.");
	}

	@Test
	@Transactional
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
	@Transactional
	public void getAnnouncementByTitle() {
		Announcement a = contentService.getAnnouncement(DbFix.apelTitle);
		assertEquals(a.getTitle(), DbFix.apelTitle);
		assertEquals(a.getId(), fix.apelId);
		a = contentService.getAnnouncement(DbFix.gazetaTitle);
		assertNull(a);
		log.info("Getting announcement by its title is ok.");
	}

	@Test
	@Transactional
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
	@Transactional
	public void updateContent() {
		Content apel = contentService.getContent(fix.apelId);
		apel.setTitle(NEW_UNIQUE_AMONG_CONTENT_TITLE);
		contentService.updateContent(apel);
	}

	@Transactional
	private void justDeleteContent(Content c) {
		contentService.deleteContent(c);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private Content getContent(Long id) {
		return contentService.getContent(id);
	}

	@Test
	@Transactional
	public void deleteContent() {
		justDeleteContent(contentService.getContent(fix.gazetaId));
		Content gazeta = getContent(fix.gazetaId);
		assertNull(gazeta);
		log.info("Content deletion succeeded.");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private Announcement getAnnouncement(Long id) {
		return contentDao.getAnnouncement(id);
	}

	@Test
	@Transactional
	public void deleteAnnouncement() {
		justDeleteContent(contentService.getAnnouncement(fix.apelId));
		Announcement apel = getAnnouncement(fix.apelId);
		assertNull(apel);
		log.info("Announcement deleting succeeded.");
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getAllContents() {
		Set<Content> got = new HashSet<Content>(contentService.getAllContents());
		Set<Content> expected = new HashSet<Content>();
		expected.add(contentService.getContent(fix.apelId));
		expected.add(contentService.getContent(fix.gazetaId));
		expected.add(contentService.getContent(fix.zakazaneId));
		assertEquals(got, expected);
		log.info("Getting all the contents is ok.");
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getAllAnnouncements() {
		Set<Announcement> got = new HashSet<Announcement>(
				contentService.getAllAnnouncements());
		Set<Announcement> expected = new HashSet<Announcement>();
		expected.add(contentService.getAnnouncement(fix.apelId));
		assertEquals(got, expected);
		log.info("Getting all the announcements seems to be ok.");
	}
}