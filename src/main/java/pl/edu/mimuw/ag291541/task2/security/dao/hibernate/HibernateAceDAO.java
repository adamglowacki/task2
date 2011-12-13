package pl.edu.mimuw.ag291541.task2.security.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pl.edu.mimuw.ag291541.task2.daoUtil.DaoUtilLibrary;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.entity.ClassAce;
import pl.edu.mimuw.ag291541.task2.security.entity.InstanceAce;
import pl.edu.mimuw.ag291541.task2.security.service.AclUtil;

public class HibernateAceDAO extends HibernateDaoSupport implements AceDAO {
	@Autowired
	DaoUtilLibrary daoUtil;
	@Autowired
	AclUtil aclUtil;

	@Override
	public ClassAce createClassAce(Long u, ACLRights rights, String sn) {
		ClassAce a = new ClassAce(u, rights, sn);
		getHibernateTemplate().persist(a);
		return a;
	}

	@Override
	public InstanceAce createInstanceAce(Long u, ACLRights rights, Object o) {
		InstanceAce a = new InstanceAce(u, rights, aclUtil.getObjectId(o));
		getHibernateTemplate().persist(a);
		return a;
	}

	@Override
	public void deleteClassAce(ClassAce a) {
		getHibernateTemplate().delete(a);
	}

	@Override
	public void deleteInstanceAce(InstanceAce a) {
		getHibernateTemplate().delete(a);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> findByCriteria(Class<T> clazz, Long u, Criterion c) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz)
				.add(Property.forName("user_id").eq(u)).add(c);
		return getHibernateTemplate().findByCriteria(dc);
	}

	@Override
	public InstanceAce getInstanceAce(Long u, Object instance) {
		return daoUtil.uniqueOrNull(findByCriteria(
				InstanceAce.class,
				u,
				Property.forName("objectHashCode").eq(
						aclUtil.getObjectId(instance))));
	}

	@Override
	public ClassAce getClassAce(Long u, String cn) {
		return daoUtil.uniqueOrNull(findByCriteria(ClassAce.class, u, Property
				.forName("canonicalTypeName").eq(cn)));
	}

	@Override
	public InstanceAce getInstanceAce(Long id) {
		return getHibernateTemplate().get(InstanceAce.class, id);
	}

	@Override
	public ClassAce getClassAce(Long id) {
		return getHibernateTemplate().get(ClassAce.class, id);
	}

	@Override
	public List<ClassAce> getAllClassAces() {
		return getHibernateTemplate().loadAll(ClassAce.class);
	}

	@Override
	public List<InstanceAce> getAllInstanceAces() {
		return getHibernateTemplate().loadAll(InstanceAce.class);
	}

}
