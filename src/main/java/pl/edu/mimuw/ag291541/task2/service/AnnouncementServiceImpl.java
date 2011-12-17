package pl.edu.mimuw.ag291541.task2.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.security.UserAuthenticationImpl;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.util.UserUtilLibrary;

public class AnnouncementServiceImpl implements AnnouncementService {
	@Autowired
	ContentDAO contentDao;
	@Autowired
	UserUtilLibrary userUtil;

	@Override
	@Transactional
	public void login(User user) {
		SecurityContextHolder.getContext().setAuthentication(
				new UserAuthenticationImpl(user));
	}

	@Override
	@Transactional
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Override
	@Transactional
	public Announcement sendAnnouncement(String title, String body,
			Collection<User> recipients) {
		return contentDao.createAnnouncement(title, body, recipients);
	}

	@Override
	@Transactional
	public Announcement sendAnnouncementToGroups(String title, String body,
			Collection<Group> recipients) {
		Set<User> users = new HashSet<User>();
		for (Group g : recipients)
			for (User u : g.getMembers())
				users.add(u);
		return sendAnnouncement(title, body, users);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Announcement> getAllUnread(User user) {
		List<AnnouncementInstance> ais = contentDao
				.getUnreadAnnouncements(user);
		Set<Announcement> as = new HashSet<Announcement>();
		for (AnnouncementInstance ai : ais)
			as.add(ai.getAnnouncement());
		return as;
	}

	@Override
	@Transactional
	public void markRead(Announcement a, User u) {
		contentDao.getAnnouncementInstance(a, u).markRead();
	}
}
