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
import pl.edu.mimuw.ag291541.task2.security.executor.exception.UserNotLoggedIn;
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
		if (!isGuarded(event.getEntity()))
			return;
		User u = userUtil.getUser();
		// TODO: change it into throwing an exception
		if (u == null)
			return;
		if (u == null)
			throw new UserNotLoggedIn();
		else if (!aclService.checkObjectAcl(u, ACLRights.READ,
				event.getEntity())) {
			log.debug("Forbidding access.");
			throw new ActionForbiddenException();
		} else
			log.debug("Access allowed.");
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		return false;
	}

	@Override
	public boolean onPreDelete(PreDeleteEvent event) {
		log.debug("onPreDelete: {}", event.getEntity());
		return false;
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		log.debug("onPreUpdate: {}", event.getEntity());
		return false;
	}

}
