package pl.edu.mimuw.ag291541.task2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class GlobalFixture {
	public static final String kunegundaName = "Kunegunda";
	public static final String kunegundaSurname = "Jelonek";
	public static final String jerzyName = "Jerzy";
	public static final String jerzySurname = "Jeż";
	public static final int usersNumber = 2;
	public static final String administratorzyName = "Administratorzy";
	public static final String czytaczeName = "Czytacze";
	public static final int groupsNumber = 2;

	private static GlobalFixture instance = new GlobalFixture();

	/*
	 * TODO to make this wiring work to remove templates from parameters of
	 * every method
	 */
	@Autowired
	private HibernateTemplate hibernateTemplate;

	private GlobalFixture() {

	}

	public static GlobalFixture getInstance() {
		return instance;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void loadData(HibernateTemplate ht) {
		User kunegunda = newUser(ht, kunegundaName, kunegundaSurname);
		User jerzy = newUser(ht, jerzyName, jerzySurname);
		Group administratorzy = newGroup(ht, administratorzyName);
		Group czytacze = newGroup(ht, czytaczeName);
		czytacze.getMembers().add(jerzy);
		administratorzy.getMembers().add(kunegunda);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeData(HibernateTemplate ht) {
		deleteAll(ht, User.class);
		deleteAll(ht, Group.class);
	}

	private User newUser(HibernateTemplate ht, String name, String surname) {
		User u = new User(name, surname);
		u.setId((Long) ht.save(u));
		return u;
	}

	private Group newGroup(HibernateTemplate ht, String name) {
		Group g = new Group(name);
		g.setId((Long) ht.save(g));
		return g;
	}

	private <T> void deleteAll(HibernateTemplate ht, Class<T> clazz) {
		ht.deleteAll(ht.loadAll(clazz));
	}
}
