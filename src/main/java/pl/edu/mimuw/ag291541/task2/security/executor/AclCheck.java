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

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.annotation.AclGuarded;
import pl.edu.mimuw.ag291541.task2.security.entity.User;
import pl.edu.mimuw.ag291541.task2.security.executor.exception.AccessForbiddenException;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.util.UserUtilLibrary;

public class AclCheck implements PreUpdateEventListener,
		PreDeleteEventListener, PreInsertEventListener, PreLoadEventListener {
	private static final long serialVersionUID = 581928135979956278L;

	private Logger log = LoggerFactory.getLogger(AclCheck.class);

	@Autowired
	private ACLService aclService;
	@Autowired
	private UserUtilLibrary userUtil;
	@Autowired
	private DuringProcessing duringAccessCheck;

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
		final Object entity = event.getEntity();
		log.debug("onPreInsert: {}", entity);
		checkAccess(entity, new CheckingAccess() {
			@Override
			public void checkAccess(User u) {
				if (!aclService.checkCreationAcl(u, entity.getClass()))
					throw new AccessForbiddenException();
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
					throw new AccessForbiddenException();
			}
		});
	}

	/*
	 * Executes given closure only if user is logged in. It also prevents
	 * recursion when checking ACLs. It happens because checking access needs
	 * session to be flushed and flushing triggers access check again on the
	 * saved objects.
	 */
	private void checkAccess(Object entity, CheckingAccess exec) {
		if (isGuarded(entity)) {
			User loggedUser = userUtil.getUser();
			if (loggedUser == null)
				log.info("User not logged in. Access control will not be executed.");
			else if (!duringAccessCheck.is(entity)) {
				try {
					duringAccessCheck.set(entity);
					exec.checkAccess(loggedUser);
				} finally {
					duringAccessCheck.clear(entity);
				}
			}
		}
	}

	private interface CheckingAccess {
		public void checkAccess(User u);
	}

}
