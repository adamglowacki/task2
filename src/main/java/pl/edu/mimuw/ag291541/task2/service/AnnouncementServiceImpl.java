package pl.edu.mimuw.ag291541.task2.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class AnnouncementServiceImpl implements AnnouncementService {

	@Override
	@Transactional
	public void login(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void logout() {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public Announcement sendAnnouncement(String title, String body,
			Collection<User> recipients) {
		// TODO Auto-generated method stub
		return null;
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
	public Set<Announcement> getAllUnread() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void markRead(Announcement a) {
		// TODO Auto-generated method stub

	}

}
