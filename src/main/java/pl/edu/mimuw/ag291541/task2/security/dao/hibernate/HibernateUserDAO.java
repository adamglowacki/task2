package pl.edu.mimuw.ag291541.task2.security.dao.hibernate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.daoUtil.DaoUtilLibrary;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

@Repository
public class HibernateUserDAO extends HibernateDaoSupport implements UserDAO {
	@Autowired
	DaoUtilLibrary util;

	/* Groups */

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Group createGroup(String name) {
		getHibernateTemplate().persist(new Group(name));
		return getGroup(name);
	}

	@Override
	@Transactional(readOnly = true)
	public Group getGroup(Long id) {
		return getHibernateTemplate().get(Group.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Group getGroup(String name) {
		return util.unique(getHibernateTemplate().findByNamedParam(
				"from Group where name = :name", "name", name));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteGroup(Group group) {
		getHibernateTemplate().delete(group);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Group> getAllGroups() {
		return getHibernateTemplate().loadAll(Group.class);
	}

	/* Users */

	@Override
	public User createUser(String name, String surname) {
		getHibernateTemplate().persist(new User(name, surname));
		return getUser(name, surname);
	}

	@Override
	public User getUser(Long id) {
		return getHibernateTemplate().get(User.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUser(String name, String surname) {
		return util.unique(getHibernateTemplate().findByNamedParam(
				"from User where name = :name and surname = :surname",
				new String[] { "name", "surname" },
				new String[] { name, surname }));
	}

	@Override
	public void deleteUser(User user) {
		getHibernateTemplate().delete(user);
	}

	@Override
	public List<User> getAllUsers() {
		return getHibernateTemplate().loadAll(User.class);
	}

}
