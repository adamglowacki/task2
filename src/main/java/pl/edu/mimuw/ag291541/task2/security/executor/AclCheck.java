package pl.edu.mimuw.ag291541.task2.security.executor;

import java.lang.annotation.Annotation;

import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreLoadEvent;
import org.hibernate.event.PreLoadEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.annotation.AclGuarded;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.executor.exception.ActionForbiddenException;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.util.UserUtil;

public class AclCheck implements PreUpdateEventListener,
		PreDeleteEventListener, PreInsertEventListener, PreLoadEventListener {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ACLService aclService;
	@Autowired
	UserUtil userUtil;

	@Autowired
	PlatformTransactionManager txManager;

	private Logger log = LoggerFactory.getLogger(AclCheck.class);

	private boolean isGuarded(Object o) {
		Class<?> clazz = o.getClass();
		Annotation annotation = clazz.getAnnotation(AclGuarded.class);
		return annotation != null;
	}

	@Override
	public void onPreLoad(PreLoadEvent event) {
		log.debug("onPreLoad: {}", event.getEntity());
		checkInstanceAccess(event.getEntity(), ACLRights.READ);
	}

	@Override
	public boolean onPreInsert(final PreInsertEvent event) {
		log.debug("onPreInsert: {}", event.getEntity());
		checkAccess(event.getEntity(), new CheckingAccess() {
			@Override
			public void checkAccess(User u) {
				if (!aclService.checkCreationAcl(u, event.getEntity()
						.getClass()))
					throw new ActionForbiddenException();
			}
		});
		return false;
	}

	@Override
	public boolean onPreDelete(PreDeleteEvent event) {
		log.debug("onPreDelete: {}", event.getEntity());
		checkInstanceAccess(event.getEntity(), ACLRights.WRITE);
		return false;
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		log.debug("onPreUpdate: {}", event.getEntity());
		checkInstanceAccess(event.getEntity(), ACLRights.WRITE);
		return false;
	}

	private void checkInstanceAccess(final Object entity,
			final ACLRights whatToDo) {
		checkAccess(entity, new CheckingAccess() {
			@Override
			public void checkAccess(User u) {
				if (!aclService.checkObjectAcl(u, whatToDo, entity))
					throw new ActionForbiddenException();
			}
		});
	}

	private void checkAccess(Object entity, CheckingAccess exec) {
		if (isGuarded(entity)) {
			User loggedUser = userUtil.getUser();
			// TODO: change it so that not logged users are also thrown with an
			// exception
			if (loggedUser != null)
				exec.checkAccess(loggedUser);

		}
	}

	private interface CheckingAccess {
		public void checkAccess(User u);
	}

}
