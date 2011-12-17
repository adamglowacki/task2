package pl.edu.mimuw.ag291541.task2.service;

import java.util.Collection;
import java.util.Set;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public interface AnnouncementService {
	/**
	 * Attempts to log in.
	 * 
	 * @param user
	 *            The one who is to be logged in.
	 */
	public void login(User user);

	/**
	 * Logs the current user out (if there is such a user).
	 */
	public void logout();

	/**
	 * Sends announcements to many users, creating also needed
	 * <code>AnnouncementInstance</code> objects.
	 * 
	 * @param title
	 *            Title of the announcement.
	 * @param body
	 *            The main text of the announcement.
	 * @param recipients
	 *            All the recipients.
	 * @return The announcement that was sent.
	 */
	public Announcement sendAnnouncement(String title, String body,
			Collection<User> recipients);

	/**
	 * Sends announcements to many users (all who are in the provided groups).
	 * 
	 * @param title
	 *            Title of the announcement.
	 * @param body
	 *            The main text of the announcement.
	 * @param recipients
	 *            All the users in the groups are considered as recipients.
	 * @return The announcement that was sent.
	 */
	public Announcement sendAnnouncementToGroups(String title, String body,
			Collection<Group> recipients);

	/**
	 * Retrieves all the announcements that are not already read by given user.
	 * 
	 * @param user
	 *            The receiver.
	 * 
	 * @return Set of all pending (not read) announcements whose recipient is
	 *         <code>user</code>.
	 */
	public Set<Announcement> getAllUnread(User user);

	/**
	 * Marks the announcement as already read (with current timestamp).
	 * 
	 * @param a
	 *            An announcement that has been just read by <code>user</code>.
	 * @param u
	 *            The receiver of <code>a</code>.
	 */
	public void markRead(Announcement a, User user);
}
