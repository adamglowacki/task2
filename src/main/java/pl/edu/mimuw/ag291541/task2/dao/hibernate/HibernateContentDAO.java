package pl.edu.mimuw.ag291541.task2.dao.hibernate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.daoUtil.DaoUtilLibrary;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class HibernateContentDAO extends HibernateDaoSupport implements
		ContentDAO {
	@Autowired
	private DaoUtilLibrary util;

	@Override
	@Transactional
	public Content createContent(String title, String body) {
		Content c = new Content(title, body);
		getHibernateTemplate().persist(c);
		return c;
	}

	@Override
	@Transactional
	public Announcement createAnnouncement(String title, String body,
			Collection<User> recipients) {
		Announcement a = new Announcement(title, body);
		Set<User> distinctRecipients = new HashSet<User>(recipients);
		for (User r : distinctRecipients) {
			AnnouncementInstance i = new AnnouncementInstance(r, a);
			a.getInstances().add(i);
		}
		getHibernateTemplate().saveOrUpdate(a);
		return a;
	}

	@Override
	@Transactional
	public Content getContent(Long id) {
		return getHibernateTemplate().get(Content.class, id);
	}

	public Content getContent(String name) {
		return util
				.uniqueOrNull(listContentByCriteria(new Criterion[] { Property
						.forName("title").eq(name) }));
	}

	@Override
	@Transactional
	public Announcement getAnnouncement(Long id) {
		return getHibernateTemplate().get(Announcement.class, id);
	}

	@Override
	public Announcement getAnnouncement(String name) {
		Content candidate = getContent(name);
		if (candidate instanceof Announcement)
			return (Announcement) candidate;
		else
			return null;
	}

	@Override
	@Transactional
	public void updateContent(Content c) {
		getHibernateTemplate().update(c);
	}

	@Override
	@Transactional
	public void updateAnnouncement(Announcement a) {
		getHibernateTemplate().update(a);
	}

	@Override
	@Transactional
	public void deleteContent(Content c) {
		getHibernateTemplate().delete(c);
	}

	@SuppressWarnings("unchecked")
	private List<Content> listContentByCriteria(Criterion[] criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(Content.class);
		for (Criterion c : criteria)
			dc.add(c);
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	@Transactional
	public AnnouncementInstance createAnnouncementInstance(User receiver,
			Announcement announcement) {
		AnnouncementInstance ai = new AnnouncementInstance(receiver,
				announcement);
		getHibernateTemplate().persist(ai);
		return ai;
	}

	@Override
	@Transactional
	public AnnouncementInstance getAnnouncementInstance(Long id) {
		return getHibernateTemplate().get(AnnouncementInstance.class, id);
	}

	@SuppressWarnings("unchecked")
	private List<AnnouncementInstance> listAnnouncementInstanceByCriteria(
			Criterion[] criteria) {
		DetachedCriteria d = DetachedCriteria
				.forClass(AnnouncementInstance.class);
		for (Criterion c : criteria)
			d.add(c);
		return getHibernateTemplate().findByCriteria(d);
	}

	@Override
	public AnnouncementInstance getAnnouncementInstance(Announcement a, User u) {
		List<AnnouncementInstance> ais = listAnnouncementInstanceByCriteria(new Criterion[] {
				Property.forName("announcement").eq(a),
				Property.forName("receiver").eq(u) });
		return util.unique(ais);
	}

	@Override
	public List<AnnouncementInstance> getUnreadAnnouncements(User u) {
		return listAnnouncementInstanceByCriteria(new Criterion[] {
				Property.forName("receiver").eq(u),
				Property.forName("readStatus").eq(false) });
	}

	@Override
	@Transactional
	public void deleteAnnouncementInstance(AnnouncementInstance instance) {
		getHibernateTemplate().delete(instance);
	}

	@Override
	@Transactional
	public List<Content> getAllContents() {
		return getHibernateTemplate().loadAll(Content.class);
	}

	@Override
	@Transactional
	public List<Announcement> getAllAnnouncements() {
		return getHibernateTemplate().loadAll(Announcement.class);
	}

}
