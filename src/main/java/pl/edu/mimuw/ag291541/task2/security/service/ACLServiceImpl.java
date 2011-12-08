package pl.edu.mimuw.ag291541.task2.security.service;

import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class ACLServiceImpl implements ACLService {

	@Override
	@Transactional
	public boolean checkAcl(User who, ACLRights whatToDo, Object onWhat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional
	public void addClassAccess(Class<?> onWhat, ACLRights whatToDo, User toWhom) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void addInstanceAccess(Object onWhat, ACLRights whatToDo, User toWhom) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void revokeInstanceAccess(Object onWhat, ACLRights whatToDo,
			User fromWhom) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void revokeClassAccess(Class<?> onWhat, ACLRights whatToDo,
			User fromWhom) {
		// TODO Auto-generated method stub

	}

}
