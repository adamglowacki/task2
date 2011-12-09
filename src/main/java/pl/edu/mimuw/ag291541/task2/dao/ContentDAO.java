package pl.edu.mimuw.ag291541.task2.dao;

import java.util.Collection;
import java.util.List;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public interface ContentDAO {
	/* Content */
	public Content createContent(String title, String body);

	/*
	 * User is intended to create an announcement first and make the
	 * announcement instances later and tell them they should refer to this
	 * object.
	 */
	public Announcement createAnnouncement(String title, String body,
			Collection<User> recipients);

	public Content getContent(Long id);

	public Announcement getAnnouncement(Long id);

	public void updateContent(Content c);

	public void updateAnnouncement(Announcement a);

	public void deleteContent(Content c);

	public List<Content> getAllContents();

	public List<Announcement> getAllAnnouncements();

	/* Announcement instances */
	public AnnouncementInstance createAnnouncementInstance(User receiver,
			Announcement announcement);

	public AnnouncementInstance getAnnouncementInstance(Long id);

	public AnnouncementInstance getAnnouncementInstance(Announcement a, User u);

	public List<AnnouncementInstance> getUnreadAnnouncements(User u);

	public void deleteAnnouncementInstance(AnnouncementInstance instance);
}
