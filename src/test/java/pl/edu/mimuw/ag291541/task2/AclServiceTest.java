package pl.edu.mimuw.ag291541.task2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.exampletype.C;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.ClassAce;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class AclServiceTest extends DbTest {

	@Test
	@Transactional
	public void addClassAce() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		String cn = Content.class.getCanonicalName();
		ClassAce ace = aceDao.getClassAce(fix.kunegundaId, cn);
		assertNull(ace);
		aclService.addClassAccess(Content.class, ACLRights.READ, kunegunda);
		ace = aceDao.getClassAce(fix.kunegundaId, cn);
		assertTrue(ace.getCanonicalTypeName().equals(cn));
		assertTrue(ace.getRightsType().equals(ACLRights.READ));
		assertTrue(ace.getUser().equals(fix.kunegundaId));
	}

	@Test
	@Transactional
	public void checkObjectAcl() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		User jerzy = userDao.getUser(fix.jerzyId);
		C anotherCObj = new C();
		assertTrue(aclService.checkObjectAcl(kunegunda, ACLRights.WRITE,
				fix.cObj));
		assertTrue(aclService.checkObjectAcl(kunegunda, ACLRights.READ,
				anotherCObj));
		assertTrue(aclService.checkObjectAcl(jerzy, ACLRights.READ, fix.cObj));
		assertFalse(aclService.checkObjectAcl(jerzy, ACLRights.WRITE, fix.cObj));
	}
}
