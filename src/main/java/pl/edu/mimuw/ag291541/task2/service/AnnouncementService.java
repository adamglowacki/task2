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
	 * Sends announcements to many users. It also grants the sender (the user
	 * that is logged in) the write access to the object and all the recipients
	 * the read access to the announcement and their announcements instances
	 * (that are also created here).
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
	 * It also grants the sender (the user that is logged in) the write access
	 * to the object and all the recipients the read access to the announcement
	 * and their announcements instances (that are also created here).
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
	 * Retrieves all the announcements that are not already read by the logged
	 * in user.
	 * 
	 * @return Set of all pending (not read) announcements whose recipient is
	 *         the logged in user.
	 */
	public Set<Announcement> getAllUnread();

	/**
	 * Marks the announcement as already read (with current timestamp).
	 * 
	 * @param a
	 *            An announcement that has been just read by the logged in user.
	 */
	public void markRead(Announcement a);
}