package pl.edu.mimuw.ag291541.task2.security.dao;

import java.util.List;

import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public interface UserDAO {
	/* Groups */

	public Group createGroup(String name);

	public Group getGroup(Long id);

	public Group getGroup(String name);

	/**
	 * Attention: deleting groups does not cascades to the user.
	 * 
	 * @param group
	 *            Group that is to be deleted.
	 */
	public void deleteGroup(Group group);

	public List<Group> getAllGroups();

	/* Users */

	public User createUser(String name, String surname);

	public User getUser(Long id);

	public User getUser(String name, String surname);

	/**
	 * Attention: deleting users cascades to <code>AnnouncementInstance</code>
	 * objects associated with it.
	 * 
	 * @param user
	 *            User that is to be deleted.
	 */
	public void deleteUser(User user);

	public List<User> getAllUsers();
}
