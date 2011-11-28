package pl.edu.mimuw.ag291541.task2;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class DbFixture {
	public static final String kunegundaName = "Kunegunda";
	public static final String kunegundaSurname = "Jelonek";
	public static final String jerzyName = "Jerzy";
	public static final String jerzySurname = "Jeż";
	public static final int usersNumber = 2;
	public static final String administratorzyName = "Administratorzy";
	public static final String czytaczeName = "Czytacze";
	public static final int groupsNumber = 2;

	private UserDAO userDao;
	private HibernateTemplate template;

	public DbFixture(HibernateTemplate template, UserDAO userDao) {
		this.template = template;
		this.userDao = userDao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void loadData() {
		User kunegunda = userDao.createUser(kunegundaName, kunegundaSurname);
		User jerzy = userDao.createUser(jerzyName, jerzySurname);
		Group administratorzy = userDao.createGroup(administratorzyName);
		Group czytacze = userDao.createGroup(czytaczeName);
		czytacze.getMembers().add(jerzy);
		administratorzy.getMembers().add(kunegunda);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeData() {
		deleteAll(Group.class);
		deleteAll(User.class);
	}

	private <T> void deleteAll(Class<T> clazz) {
		template.deleteAll(template.loadAll(clazz));
	}
}
