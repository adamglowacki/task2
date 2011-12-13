package pl.edu.mimuw.ag291541.task2.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.ClassAce;
import pl.edu.mimuw.ag291541.task2.security.entity.InstanceAce;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class ACLServiceImpl implements ACLService {
	@Autowired
	AceDAO aceDao;

	private boolean checkOnSuperclasses(User u, ACLRights r, Class<?> c) {
		ClassAce ace = null;
		while (c != null) {
			ace = aceDao.getClassAce(u.getId(), c.getCanonicalName());
			if (ace != null)
				if (ace.getRightsType() == ACLRights.WRITE
						|| r == ACLRights.READ)
					return true;
			c = c.getSuperclass();
		}
		return false;
	}

	@Override
	@Transactional
	public boolean checkObjectAcl(User who, ACLRights whatToDo, Object onWhat) {
		InstanceAce ace = aceDao.getInstanceAce(who.getId(), onWhat);
		if (ace != null
				&& (ace.getRightsType() == ACLRights.WRITE || whatToDo == ACLRights.READ))
			return true;
		else
			return checkOnSuperclasses(who, whatToDo, onWhat.getClass());
	}

	@Override
	public boolean checkCreationAcl(User who, Class<?> clazz) {
		return checkOnSuperclasses(who, ACLRights.WRITE, clazz);
	}

	@Override
	@Transactional
	public void addClassAccess(Class<?> onWhat, ACLRights whatToDo, User toWhom) {
		ClassAce ace = aceDao.getClassAce(toWhom.getId(),
				onWhat.getCanonicalName());
		if (ace != null) {
			/*
			 * If the entry exists then the user is already granted at least
			 * READ so we need only to do something if WRITE is a desired level.
			 */
			if (whatToDo == ACLRights.WRITE)
				ace.setRightsType(ACLRights.WRITE);
		} else
			aceDao.createClassAce(toWhom.getId(), whatToDo,
					onWhat.getCanonicalName());
	}

	@Override
	@Transactional
	public void addInstanceAccess(Object onWhat, ACLRights whatToDo, User toWhom) {
		InstanceAce ace = aceDao.getInstanceAce(toWhom.getId(), onWhat);
		if (ace != null) {
			/*
			 * If the entry exists then the user is already granted at least
			 * READ so we need only to do something if WRITE is a desired level.
			 */
			if (whatToDo == ACLRights.WRITE)
				ace.setRightsType(ACLRights.WRITE);
		} else
			aceDao.createInstanceAce(toWhom.getId(), whatToDo, onWhat);
	}

	@Override
	@Transactional
	public void revokeInstanceAccess(Object onWhat, ACLRights whatToDo,
			User fromWhom) {
		InstanceAce ace = aceDao.getInstanceAce(fromWhom.getId(), onWhat);
		if (ace != null) {
			switch (whatToDo) {
			case READ:
				aceDao.deleteInstanceAce(ace);
				break;
			case WRITE:
				ace.setRightsType(ACLRights.READ);
				break;
			}
		}
	}

	@Override
	@Transactional
	public void revokeClassAccess(Class<?> onWhat, ACLRights whatToDo,
			User fromWhom) {
		ClassAce ace = aceDao.getClassAce(fromWhom.getId(),
				onWhat.getCanonicalName());
		if (ace != null) {
			switch (whatToDo) {
			case READ:
				aceDao.deleteClassAce(ace);
				break;
			case WRITE:
				ace.setRightsType(ACLRights.READ);
				break;
			}
		}
	}

}
