package pl.edu.mimuw.ag291541.task2;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.exampletype.A;
import pl.edu.mimuw.ag291541.task2.exampletype.B;
import pl.edu.mimuw.ag291541.task2.exampletype.C;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.Group;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class DbFix {
	public static final String kunegundaName = "Kunegunda";
	public static final String kunegundaSurname = "Jelonek";
	public Long kunegundaId;
	public static final String jerzyName = "Jerzy";
	public static final String jerzySurname = "Jeż";
	public Long jerzyId;
	public static final String ernestName = "Ernest";
	public static final String ernestSurname = "Głowacki";
	public Long ernestId;
	public static final int usersNumber = 2;
	public static final String administratorzyName = "Administratorzy";
	public Long administratorzyId;
	public static final String czytaczeName = "Czytacze";
	public Long czytaczeId;
	public static final int groupsNumber = 2;
	public Long gazetaId;
	public static final String gazetaTitle = "Rotał24";
	public static final String gazetaBody = "Sprawy najwyższej wagi, w tym Hapsburkowie.";
	public Long apelId;
	public static final String apelTitle = "Cichy apel";
	public static final String apelBody = "Bardzo proszę.";
	private static final String ANNOUNCEMENT_INSTANCE_TABLE = "announcement_instance";
	private static final String CONTENT_TABLE = "content";
	private static final String GROUP_USER_TABLE = "secuser_secgroup";
	private static final String GROUP_TABLE = "secgroup";
	private static final String USER_TABLE = "secuser";
	private static final String CLASS_ACE_TABLE = "classace";
	private static final String INSTANCE_ACE_TABLE = "instanceace";
	public Long apelDoKunegundyId;
	public Long apelDoJerzegoId;
	public Long kunegundaAAceId;
	public Long kunegundaBAceId;
	public C cObj;
	public Long jerzyCObjAceId;
	public Long kunegundaGazetaReadId;
	public Long jerzyGazetaWriteId;
	public Long ernestObjectWriteId;

	private SessionFactory sessionFactory;
	private PlatformTransactionManager txManager;
	private JdbcTemplate template;

	private UserDAO userDao;
	private ContentDAO contentDao;
	private AceDAO aceDao;

	// private Logger log = LoggerFactory.getLogger(DbFix.class);

	public DbFix(JdbcTemplate template, SessionFactory factory,
			PlatformTransactionManager txManager, UserDAO userDao,
			ContentDAO contentDao, AceDAO aceDao) {
		this.template = template;
		this.sessionFactory = factory;
		this.txManager = txManager;
		this.userDao = userDao;
		this.contentDao = contentDao;
		this.aceDao = aceDao;
	}

	@Transactional
	public void loadData() {
		User kunegunda = userDao.createUser(kunegundaName, kunegundaSurname);
		kunegundaId = kunegunda.getId();
		User jerzy = userDao.createUser(jerzyName, jerzySurname);
		jerzyId = jerzy.getId();
		User ernest = userDao.createUser(ernestName, ernestSurname);
		ernestId = ernest.getId();
		Group administratorzy = userDao.createGroup(administratorzyName);
		administratorzyId = administratorzy.getId();
		Group czytacze = userDao.createGroup(czytaczeName);
		czytaczeId = czytacze.getId();
		czytacze.getMembers().add(jerzy);
		czytacze.getMembers().add(kunegunda);
		administratorzy.getMembers().add(kunegunda);
		Content gazeta = contentDao.createContent(gazetaTitle, gazetaBody);
		gazetaId = gazeta.getId();
		Set<User> apelRecipients = new HashSet<User>();
		apelRecipients.add(kunegunda);
		apelRecipients.add(jerzy);
		Announcement apel = contentDao.createAnnouncement(apelTitle, apelBody,
				apelRecipients);
		apelId = apel.getId();
		for (AnnouncementInstance i : apel.getInstances())
			if (i.getReceiver().equals(kunegunda))
				apelDoKunegundyId = i.getId();
			else if (i.getReceiver().equals(jerzy))
				apelDoJerzegoId = i.getId();
			else
				assert false;
		aceDao.createInstanceAce(kunegundaId, ACLRights.READ, apel);
		aceDao.createInstanceAce(jerzyId, ACLRights.READ, apel);
		kunegundaBAceId = aceDao.createClassAce(kunegundaId, ACLRights.READ,
				B.class.getCanonicalName()).getId();
		kunegundaAAceId = aceDao.createClassAce(kunegundaId, ACLRights.WRITE,
				A.class.getCanonicalName()).getId();
		cObj = new C();
		jerzyCObjAceId = aceDao
				.createInstanceAce(jerzyId, ACLRights.READ, cObj).getId();
		kunegundaGazetaReadId = aceDao.createInstanceAce(kunegundaId,
				ACLRights.READ, gazeta).getId();
		jerzyGazetaWriteId = aceDao.createInstanceAce(jerzyId, ACLRights.WRITE,
				gazeta).getId();
		ernestObjectWriteId = aceDao.createClassAce(ernestId, ACLRights.WRITE,
				Object.class.getCanonicalName()).getId();
	}

	@Transactional
	public void removeData() {
		SecurityContextHolder.getContext().setAuthentication(null);
		// Session s = sessionFactory.getCurrentSession();
		// s.flush();
		txManager.getTransaction(null).flush();
		deleteAllRawSql(ANNOUNCEMENT_INSTANCE_TABLE);
		deleteAllRawSql(CONTENT_TABLE);
		deleteAllRawSql(CLASS_ACE_TABLE);
		deleteAllRawSql(INSTANCE_ACE_TABLE);
		deleteAllRawSql(GROUP_USER_TABLE);
		deleteAllRawSql(GROUP_TABLE);
		deleteAllRawSql(USER_TABLE);
	}

	@Transactional
	private <T> void deleteAllRawSql(String table) {
		template.execute("DELETE FROM " + table);
		// Query q = s.createSQLQuery("DELETE FROM " + table);
		// log.debug("Deletion query to be executed: {}", q.getQueryString());
		// q.executeUpdate();
		// factory.getCurrentSession().createSQLQuery("DELETE FROM ?")
		// .setString(0, table);
	}
}
