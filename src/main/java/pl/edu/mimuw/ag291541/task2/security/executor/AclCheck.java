package pl.edu.mimuw.ag291541.task2.security.executor;

import org.hibernate.event.EventListeners;
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
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class AclCheck implements PreUpdateEventListener,
		PreDeleteEventListener, PreInsertEventListener, PreLoadEventListener {
	private static final long serialVersionUID = 1L;

	@Autowired
	private AnnotationSessionFactoryBean factory;

	private Logger log = LoggerFactory.getLogger(AclCheck.class);

	@Override
	public void onPreLoad(PreLoadEvent event) {
		log.debug("onPreLoad: {}", event.getEntity());
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		EventListeners el = factory.getConfiguration().getEventListeners();
		for (PreUpdateEventListener puel : el.getPreUpdateEventListeners())
			log.debug("factory pre-update {}", puel);
		for (PreDeleteEventListener pdel : el.getPreDeleteEventListeners())
			log.debug("factory pre-delete {}", pdel);
		log.debug("onPreInsert: {}", event.getEntity());
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
