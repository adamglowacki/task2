package pl.edu.mimuw.ag291541.task2.dao.hibernate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class HibernateContentDAO extends HibernateDaoSupport implements
		ContentDAO {

	@Override
	public Content createContent(String title, String body) {
		Content c = new Content(title, body);
		getHibernateTemplate().persist(c);
		return c;
	}

	@Override
	public Announcement createAnnouncement(String title, String body,
			Collection<User> recipients) {
		Announcement a = new Announcement(title, body);
		Set<User> distinctRecipients = new HashSet<User>(recipients);
		Set<AnnouncementInstance> insts = new HashSet<AnnouncementInstance>();
		for (User r : distinctRecipients) {
			AnnouncementInstance i = new AnnouncementInstance(r, a);
			insts.add(i);
		}
		a.setInstances(insts);
		getHibernateTemplate().persist(a);
		return a;
	}

	@Override
	public Content getContent(Long id) {
		return getHibernateTemplate().get(Content.class, id);
	}

	@Override
	public Announcement getAnnouncement(Long id) {
		return getHibernateTemplate().get(Announcement.class, id);
	}

	@Override
	public void updateContent(Content c) {
		getHibernateTemplate().update(c);
	}

	@Override
	public void updateAnnouncement(Announcement a) {
		getHibernateTemplate().update(a);
	}

	@Override
	public void deleteContent(Content c) {
		getHibernateTemplate().delete(c);
	}

	@Override
	public AnnouncementInstance createAnnouncementInstance(User receiver,
			Announcement announcement) {
		AnnouncementInstance ai = new AnnouncementInstance(receiver,
				announcement);
		getHibernateTemplate().persist(ai);
		return ai;
	}

	@Override
	public AnnouncementInstance getAnnouncementInstance(Long id) {
		return getHibernateTemplate().get(AnnouncementInstance.class, id);
	}

	@Override
	public void deleteAnnouncementInstance(AnnouncementInstance instance) {
		getHibernateTemplate().delete(instance);
	}

	@Override
	public List<Content> getAllContents() {
		return getHibernateTemplate().loadAll(Content.class);
	}

	@Override
	public List<Announcement> getAllAnnouncements() {
		return getHibernateTemplate().loadAll(Announcement.class);
	}

}
