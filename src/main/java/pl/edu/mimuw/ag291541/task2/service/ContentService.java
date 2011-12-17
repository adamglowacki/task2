package pl.edu.mimuw.ag291541.task2.service;

import java.util.List;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.Content;

/**
 * Very simple box around <code>ContentDAO</code>. It differs from
 * <code>ContentDAO</code> managing transactions.
 */
public interface ContentService {
	/* Content */
	public Content createContent(String title, String body);

	public Content getContent(Long id);

	/**
	 * Since <code>title</code> is a business primary key of contents, we can
	 * find the content by its title.
	 * 
	 * @param title
	 *            A unique title of the content.
	 * @return The one only (at most) content with <code>title</code> as a
	 *         title.
	 */
	public Content getContent(String title);

	public Announcement getAnnouncement(Long id);

	/**
	 * Title is <code>Announcement</code>'s business primary key.
	 * 
	 * @param title
	 *            A unique title of the content.
	 * @return The announcement with provided title.
	 */
	public Announcement getAnnouncement(String title);

	public void updateContent(Content a);

	public void deleteContent(Content c);

	public List<Content> getAllContents();

	public List<Announcement> getAllAnnouncements();
}
