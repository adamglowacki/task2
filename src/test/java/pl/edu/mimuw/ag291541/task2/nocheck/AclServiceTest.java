package pl.edu.mimuw.ag291541.task2.nocheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.DeclarativeTransactionTest;
import pl.edu.mimuw.ag291541.task2.entity.Content;
import pl.edu.mimuw.ag291541.task2.exampletype.A;
import pl.edu.mimuw.ag291541.task2.exampletype.B;
import pl.edu.mimuw.ag291541.task2.exampletype.C;
import pl.edu.mimuw.ag291541.task2.security.ACLRights;
import pl.edu.mimuw.ag291541.task2.security.entity.ClassAce;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class AclServiceTest extends DeclarativeTransactionTest {
	private Logger log = LoggerFactory.getLogger(AclServiceTest.class);

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
		log.info("Adding class ACE is ok.");
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
		log.info("Checking object ACL is ok.");
	}

	@Test
	@Transactional
	public void checkCreationAcl() {
		User kunegunda = userDao.getUser(fix.kunegundaId);
		User jerzy = userDao.getUser(fix.jerzyId);
		User ernest = userDao.getUser(fix.ernestId);
		assertTrue(aclService.checkCreationAcl(kunegunda, A.class));
		assertTrue(aclService.checkCreationAcl(kunegunda, B.class));
		assertTrue(aclService.checkCreationAcl(kunegunda, C.class));
		assertFalse(aclService.checkCreationAcl(jerzy, C.class));
		assertTrue(aclService.checkCreationAcl(ernest, A.class));
	}
}
