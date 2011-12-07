package pl.edu.mimuw.ag291541.task2.security.service;

import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public interface ACLService {
	/**
	 * Tries to find out if somebody can do something.
	 * 
	 * @param who
	 *            The subject of the operation.
	 * @param whatToDo
	 *            The operation itself.
	 * @param onWhat
	 *            An object of the operation.
	 * @return If <code>who</code> has rights to perform <code>whatToDo</code>
	 *         on <code>onWhat</code>.
	 */
	@Transactional(readOnly = true)
	public boolean checkAcl(User who, ACLRights whatToDo, Object onWhat);

	/**
	 * Grants the rights on all the hierarchy rooted in the class.
	 * 
	 * @param onWhat
	 *            The root class of the hierarchy.
	 * @param whatToDo
	 *            The rights to be granted.
	 * @param toWhom
	 *            The user to be granted the rights.
	 */
	@Transactional
	public void addClassAccess(Class<?> onWhat, ACLRights whatToDo, User toWhom);

	/**
	 * Grants the rights on the object only.
	 * 
	 * @param onWhat
	 *            The object of the rights.
	 * @param whatToDo
	 *            The rights to be granted.
	 * @param toWhom
	 *            The user to be granted the rights.
	 */
	@Transactional
	public void addInstanceAccess(Object onWhat, ACLRights whatToDo, User toWhom);

	/**
	 * Revokes specified rights on the object only.
	 * 
	 * @param onWhat
	 *            The object of the rights.
	 * @param whatToDo
	 *            The rights to be revoked.
	 * @param fromWhom
	 *            The user to be granted the rights.
	 */
	@Transactional
	public void revokeInstanceAccess(Object onWhat, ACLRights whatToDo,
			User fromWhom);

	/**
	 * Revokes specified rights on the whole hierarchy rooted in the class.
	 * 
	 * @param onWhat
	 *            The root class of the hierarchy.
	 * @param whatToDo
	 *            The rights to be revoked.
	 * @param fromWhom
	 *            The user to be granted the rights.
	 */
	@Transactional
	public void revokeClassAccess(Class<?> onWhat, ACLRights whatToDo,
			User fromWhom);
}
