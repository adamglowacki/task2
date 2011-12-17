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

	/**
	 * Creates the announcement and all the needed instances of
	 * <code>AnnouncementInstance</code> class.
	 * 
	 * @param title
	 *            The unique (among all persisted instances of
	 *            <code>Content</code> class.
	 * @param body
	 *            The body of the content.
	 * @param recipients
	 *            The collection of all who are to receive the announcement.
	 * @return
	 */
	public Announcement createAnnouncement(String title, String body,
			Collection<User> recipients);

	/**
	 * If such a <code>Content</code> instance exists, then it returns it.
	 * Otherwise returns <code>null</code>.
	 * 
	 * @param id
	 *            The database identity.
	 * @return The desired object or <code>null</code>.
	 */
	public Content getContent(Long id);

	/**
	 * If such a <code>Content</code> instance exists, then it returns it.
	 * Otherwise returns <code>null</code>.
	 * 
	 * @param name
	 *            The business primary key.
	 * @return The object or <code>null</code>.
	 */
	public Content getContent(String name);

	/**
	 * If such an <code>Announcement</code> instance exists, then it returns it.
	 * Otherwise returns <code>null</code>.
	 * 
	 * @param id
	 *            The database identity.
	 * @return The object or <code>null</code>.
	 */
	public Announcement getAnnouncement(Long id);

	/**
	 * If such an <code>Announcement</code> instance exists, then it returns it.
	 * Otherwise returns <code>null</code>.
	 * 
	 * @param name
	 *            The business primary key.
	 * @return The object or <code>null</code>.
	 */
	public Announcement getAnnouncement(String name);

	public void updateContent(Content c);

	/**
	 * Updates the announcement with all the instances of
	 * <code>AnnouncementInstance</class> associated with it.
	 * 
	 * @param a
	 *            The object to be updated.
	 */
	public void updateAnnouncement(Announcement a);

	/**
	 * Deletes any instance of <code>Content</code>. If it is an
	 * <code>Announcement</code> then delete is cascaded to all the
	 * <code>AnnouncementInstance</code> instances.
	 */
	public void deleteContent(Content c);

	public List<Content> getAllContents();

	public List<Announcement> getAllAnnouncements();

	/* Announcement instances */
	public AnnouncementInstance createAnnouncementInstance(User receiver,
			Announcement announcement);

	public AnnouncementInstance getAnnouncementInstance(Long id);

	public AnnouncementInstance getAnnouncementInstance(Announcement a, User u);

	/**
	 * Returns all the instances of <code>Announcement</code> that are
	 * associated with <code>user</code> via <code>AnnouncementInstance</code>
	 * that has field <code>readStatus</code> set to false.
	 * 
	 * @param u
	 *            The recipient;
	 * @return All the unread <code>Announcement</code> instances.
	 */
	public List<AnnouncementInstance> getUnreadAnnouncements(User u);

	public void deleteAnnouncementInstance(AnnouncementInstance instance);
}
