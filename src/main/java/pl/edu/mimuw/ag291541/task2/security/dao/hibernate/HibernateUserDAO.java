package pl.edu.mimuw.ag291541.task2.security.dao.hibernate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.daoUtil.DaoUtilLibrary;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

@Repository
public class HibernateUserDAO extends HibernateDaoSupport implements UserDAO {
	private static final String HQL_GET_GROUP_BY_NAME = "from Group where name = :name";
	private static final String HQL_GET_USER_BY_NAME_AND_SURNAME = "from User where name = :name and surname = :surname";
	@Autowired
	private DaoUtilLibrary util;

	/* Groups */

	@Override
	@Transactional
	public Group createGroup(String name) {
		getHibernateTemplate().persist(new Group(name));
		return getGroup(name);
	}

	@Override
	@Transactional
	public Group getGroup(Long id) {
		return getHibernateTemplate().get(Group.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public Group getGroup(String name) {
		return util.unique(getHibernateTemplate().findByNamedParam(
				HQL_GET_GROUP_BY_NAME, "name", name));
	}

	@Override
	@Transactional
	public void deleteGroup(Group group) {
		getHibernateTemplate().delete(group);
	}

	@Override
	@Transactional
	public List<Group> getAllGroups() {
		return getHibernateTemplate().loadAll(Group.class);
	}

	/* Users */

	@Override
	@Transactional
	public User createUser(String name, String surname) {
		getHibernateTemplate().persist(new User(name, surname));
		return getUser(name, surname);
	}

	@Override
	@Transactional
	public User getUser(Long id) {
		return getHibernateTemplate().get(User.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public User getUser(String name, String surname) {
		return util.unique(getHibernateTemplate().findByNamedParam(
				HQL_GET_USER_BY_NAME_AND_SURNAME,
				new String[] { "name", "surname" },
				new String[] { name, surname }));
	}

	@Override
	@Transactional
	public void deleteUser(User user) {
		getHibernateTemplate().delete(user);
	}

	@Override
	@Transactional
	public List<User> getAllUsers() {
		return getHibernateTemplate().loadAll(User.class);
	}

}
