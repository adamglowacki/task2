package pl.edu.mimuw.ag291541.task2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;

public class ContentServiceImpl implements ContentService {
	@Autowired
	ContentDAO contentDao;
	@Autowired
	UserDAO userDao;

	@Override
	@Transactional
	public Content createContent(String title, String body) {
		return contentDao.createContent(title, body);
	}

	@Override
	@Transactional(readOnly = true)
	public Content getContent(Long id) {
		return contentDao.getContent(id);
	}

	@Override
	public Content getContent(String title) {
		return contentDao.getContent(title);
	}

	@Override
	@Transactional(readOnly = true)
	public Announcement getAnnouncement(Long id) {
		return contentDao.getAnnouncement(id);
	}

	@Override
	public Announcement getAnnouncement(String title) {
		return contentDao.getAnnouncement(title);
	}

	@Override
	@Transactional
	public void updateContent(Content a) {
		contentDao.updateContent(a);
	}

	@Override
	@Transactional
	public void deleteContent(Content c) {
		contentDao.deleteContent(c);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Content> getAllContents() {
		return contentDao.getAllContents();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Announcement> getAllAnnouncements() {
		return contentDao.getAllAnnouncements();
	}
}
