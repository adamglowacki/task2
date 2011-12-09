package pl.edu.mimuw.ag291541.task2.dao.hibernate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
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
	private DaoUtilLibrary util = new DaoUtilLibrary();

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
	@Transactional
	public Content getContent(Long id) {
		return getHibernateTemplate().get(Content.class, id);
	}

	@Override
	@Transactional
	public Announcement getAnnouncement(Long id) {
		return getHibernateTemplate().get(Announcement.class, id);
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
	private List<AnnouncementInstance> listByCriteria(Criterion[] criteria) {
		DetachedCriteria d = DetachedCriteria
				.forClass(AnnouncementInstance.class);
		for (Criterion c : criteria)
			d.add(c);
		return getHibernateTemplate().findByCriteria(d);
	}

	@Override
	public AnnouncementInstance getAnnouncementInstance(Announcement a, User u) {
		List<AnnouncementInstance> ais = listByCriteria(new Criterion[] {
				Property.forName("announcement").eq(a),
				Property.forName("receiver").eq(u) });
		return util.unique(ais);
	}

	@Override
	public List<AnnouncementInstance> getUnreadAnnouncements(User u) {
		return listByCriteria(new Criterion[] {
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
