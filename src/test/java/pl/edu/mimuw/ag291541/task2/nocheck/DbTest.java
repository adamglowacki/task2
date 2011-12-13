package pl.edu.mimuw.ag291541.task2.nocheck;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.mimuw.ag291541.task2.DbFix;
import pl.edu.mimuw.ag291541.task2.GenericTest;

@TransactionConfiguration(defaultRollback = false)
public abstract class DbTest extends GenericTest {
	private Logger log = LoggerFactory.getLogger(DbTest.class);

	@Before
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void loadData() {
		fix = new DbFix(template, factory, txManager, userDao, contentDao,
				aceDao);
		log.debug("Started loading db fixture...");
		fix.loadData();
		log.debug("Db fixture loaded.");
	}

	@After
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeData() {
		log.debug("Removing data after a test...");
		fix.removeData();
		log.debug("Data after a test removed.");
	}
}
