package pl.edu.mimuw.ag291541.task2;

import static org.junit.Assert.assertTrue;
import static pl.edu.mimuw.ag291541.task2.DbFix.jerzyName;
import static pl.edu.mimuw.ag291541.task2.DbFix.jerzySurname;
import static pl.edu.mimuw.ag291541.task2.DbFix.kunegundaName;
import static pl.edu.mimuw.ag291541.task2.DbFix.kunegundaSurname;
import static pl.edu.mimuw.ag291541.task2.DbFix.usersNumber;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest {
	private Logger log = LoggerFactory.getLogger(UserDaoTest.class);
	@Autowired
	private UserDAO userDao;
	@Autowired
	private ContentDAO contentDao;
	@Autowired
	private HibernateTemplate template;
	private DbFix fixture;

	@Before
	public void loadData() {
		fixture = new DbFix(template, userDao, contentDao);
		fixture.loadData();
	}

	@After
	public void removeData() {
		fixture.removeData();
	}

	private void log(String msg) {
		log.info(msg);
	}

	@Test
	public void createUser() {
		final String janName = "Jan", janSurname = "Dąb";
		User u = userDao.createUser(janName, janSurname);
		assertTrue(u.equals(userDao.getUser(janName, janSurname)));
		log("User creation is ok.");
	}

	@Test
	public void getUserById() {
		User kunegunda = userDao.getUser(kunegundaName, kunegundaSurname);
		User kunegundaObtained = userDao.getUser(kunegunda.getId());
		assertTrue(kunegunda.equals(kunegundaObtained));
		log("Getting user by id seems to be complement with getting by name and surname.");
	}

	@Test
	public void getUserByName() {
		User kunegunda = userDao.getUser(kunegundaName, kunegundaSurname);
		assertTrue(kunegunda.getName().equals(kunegundaName)
				&& kunegunda.getSurname().equals(kunegundaSurname));
		log("Getting user by their names is ok.");
	}

	@Test
	public void deleteUser() {
		User kunegunda = userDao.getUser(kunegundaName, kunegundaSurname);
		userDao.deleteUser(kunegunda);
		try {
			kunegunda = userDao.getUser(kunegunda.getId());
			log.info("User deleting failed.");
			assertTrue(false);
		} catch (DataRetrievalFailureException e) {
			log.info("User deleting was successful.");
			assertTrue(true);
		}
	}

	@Test
	public void getAllUsers() {
		List<User> all = userDao.getAllUsers();
		assertTrue(all.size() == usersNumber);
		assertTrue(all.contains(userDao
				.getUser(kunegundaName, kunegundaSurname)));
		assertTrue(all.contains(userDao.getUser(jerzyName, jerzySurname)));
		log("Getting all users succeeded.");
	}

	@Test
	public void createGroup() {
		final String name = "Poboczni";
		Group g = userDao.createGroup(name);
		assertTrue(g.equals(userDao.getGroup(name)));
		log("Group creation was successful.");
	}
}
