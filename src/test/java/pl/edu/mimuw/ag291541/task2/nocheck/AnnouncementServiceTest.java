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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.DeclarativeTransactionTest;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.UserAuthenticationImpl;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class AnnouncementServiceTest extends DeclarativeTransactionTest {
	private Logger log = LoggerFactory.getLogger(ContentServiceTest.class);

	private static final String title = "Obwieszczenie dotygodniowe";
	private static final String body = "Dziś mamy wolne.";

	@Test
	@Transactional
	public void login() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		announcementService.login(kunegunda);
		Object auth = SecurityContextHolder.getContext().getAuthentication();
		assertTrue(auth instanceof UserAuthenticationImpl);
		User principal = ((UserAuthenticationImpl) auth).getUser();
		assertTrue(kunegunda.equals(principal));
		log.info("Logged in successfully.");
	}

	@Test
	@Transactional
	public void logout() {
		announcementService.logout();
		assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
		log.info("Logged out successfully.");
	}

	@Test
	@Transactional
	public void sendAnnouncement() {
		/*
		 * Here we do not log in as anybody so the privileges are not granted by
		 * the listener.
		 */
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
		log.info("Sending an announcement to separate users succeeded.");
	}

	@Test
	@Transactional
	public void sendAnnouncementToGroups() {
		Set<Group> groups = new HashSet<Group>();
		Group czytacze = userDao.getGroup(fix.czytaczeId);
		Group administratorzy = userDao.getGroup(fix.administratorzyId);
		groups.add(czytacze);
		groups.add(administratorzy);
		Announcement a = announcementService.sendAnnouncementToGroups(title,
				body, groups);
		Set<AnnouncementInstance> insts = a.getInstances();
		Set<User> expected = new HashSet<User>();
		for (User u : czytacze.getMembers())
			expected.add(u);
		for (User u : administratorzy.getMembers())
			expected.add(u);
		assertTrue(insts.size() == expected.size());
		Set<User> got = new HashSet<User>();
		for (AnnouncementInstance i : insts) {
			assertNull(i.getReadDate());
			assertFalse(i.isReadStatus());
			got.add(i.getReceiver());
		}
		assertEquals(expected, got);
		log.info("Sending an announcement to not disjoint groups succeeded.");
	}

	@Test
	@Transactional
	public void getAllUnread() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		Set<Announcement> unread = announcementService.getAllUnread(kunegunda);
		assertEquals(unread.size(), 1);
		Announcement a = contentService.getAnnouncement(fix.apelId);
		assertTrue(unread.contains(a));
		AnnouncementInstance ai = null;
		for (AnnouncementInstance i : a.getInstances()) {
			if (i.getReceiver().equals(kunegunda))
				if (ai == null)
					ai = i;
				else
					assertTrue(false);
		}
		assertFalse(ai.isReadStatus());
		assertNull(ai.getReadDate());
		log.info("Getting all unread for a user works.");
	}

	@Test
	@Transactional
	public void markRead() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		Announcement a = contentDao.getAnnouncement(fix.apelId);
		AnnouncementInstance ai = contentDao
				.getAnnouncementInstance(fix.apelDoKunegundyId);
		assertFalse(ai.isReadStatus());
		announcementService.markRead(a, kunegunda);
		assertTrue(ai.isReadStatus());
		log.info("Marking announcement read seems to be ok.");
	}
}
