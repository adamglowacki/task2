package pl.edu.mimuw.ag291541.task2.security.dao;

import java.util.List;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.ClassAce;
import pl.edu.mimuw.ag291541.task2.security.entity.InstanceAce;

public interface AceDAO {
	/**
	 * Create class-scope ACL entry. Be careful, only one ACE for given
	 * <code>u</code> and <code>sn</code> will be accepted in the database.
	 * 
	 * @param u
	 *            <code>id</code> of the user.
	 * @param rights
	 *            The access level that is to be granted.
	 * @param sn
	 *            The canonical name of the class.
	 * @return Newly created entry.
	 */
	public ClassAce createClassAce(Long u, ACLRights rights, String sn);

	/**
	 * Create instance-scope ACL entry. Be careful, only one ACE for given
	 * <code>u</code> and <code>o</code> will be accepted in the database.
	 * 
	 * @param u
	 *            <code>id</code> of the user.
	 * @param rights
	 *            The access level that is to be granted.
	 * @param o
	 *            The object (its <code>field</code> value will be used; if not
	 *            provided then falls back to <code>hashCode()</code>).
	 * @return Newly created entry.
	 */
	public InstanceAce createInstanceAce(Long u, ACLRights rights, Object o);

	public void deleteClassAce(ClassAce a);

	public void deleteInstanceAce(InstanceAce a);

	public InstanceAce getInstanceAce(Long u, Object instance);

	public ClassAce getClassAce(Long u, String cn);

	public InstanceAce getInstanceAce(Long id);

	public ClassAce getClassAce(Long id);

	public List<ClassAce> getAllClassAces();

	public List<InstanceAce> getAllInstanceAces();
}
