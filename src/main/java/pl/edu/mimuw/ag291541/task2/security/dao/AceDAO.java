package pl.edu.mimuw.ag291541.task2.security.dao;

import java.util.List;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.ClassAce;
import pl.edu.mimuw.ag291541.task2.security.entity.InstanceAce;

public interface AceDAO {
	public ClassAce createClassAce(Long u, ACLRights rights, String sn);

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
