package pl.edu.mimuw.ag291541.task2.service;

import java.util.List;

import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.Content;

public interface ContentService {
	/* Content */
	public Content createContent(String title, String body);

	public Content getContent(Long id);

	public Announcement getAnnouncement(Long id);

	public void updateContent(Content a);

	public void deleteContent(Content c);

	public List<Content> getAllContents();

	public List<Announcement> getAllAnnouncements();
}
