package pl.edu.mimuw.ag291541.task2;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.entity.Announcement;
import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;
import pl.edu.mimuw.ag291541.task2.entity.Content;
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
	public Long apelDoKunegundyId;
	public Long apelDoJerzegoId;

	private UserDAO userDao;
	private ContentDAO contentDao;
	private HibernateTemplate template;

	public DbFix(HibernateTemplate template, UserDAO userDao,
			ContentDAO contentDao) {
		this.template = template;
		this.userDao = userDao;
		this.contentDao = contentDao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void loadData() {
		User kunegunda = userDao.createUser(kunegundaName, kunegundaSurname);
		kunegundaId = kunegunda.getId();
		User jerzy = userDao.createUser(jerzyName, jerzySurname);
		jerzyId = jerzy.getId();
		Group administratorzy = userDao.createGroup(administratorzyName);
		administratorzyId = administratorzy.getId();
		Group czytacze = userDao.createGroup(czytaczeName);
		czytaczeId = czytacze.getId();
		czytacze.getMembers().add(jerzy);
		administratorzy.getMembers().add(kunegunda);
		Content gazeta = contentDao.createContent(gazetaTitle, gazetaBody);
		gazetaId = gazeta.getId();
		Announcement apel = contentDao.createAnnouncement(apelTitle, apelBody);
		apelId = apel.getId();
		AnnouncementInstance apelDoKunegundy = contentDao
				.createAnnouncementInstance(kunegunda, apel);
		apelDoKunegundyId = apelDoKunegundy.getId();
		AnnouncementInstance apelDoJerzego = contentDao
				.createAnnouncementInstance(jerzy, apel);
		apelDoJerzegoId = apelDoJerzego.getId();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeData() {
		deleteAll(Group.class);
		deleteAll(User.class);
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	private <T> void deleteAll(Class<T> clazz) {
		template.deleteAll(template.loadAll(clazz));
	}
}
