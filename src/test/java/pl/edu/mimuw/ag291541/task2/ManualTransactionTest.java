package pl.edu.mimuw.ag291541.task2;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;

import pl.edu.mimuw.ag291541.task2.util.Executable;
import pl.edu.mimuw.ag291541.task2.util.TransactionUtilLibrary;

@TransactionConfiguration(defaultRollback = false)
public abstract class ManualTransactionTest extends GenericTest {
	@Autowired
	private TransactionUtilLibrary txUtil;

	protected void login(Long id) {
		announcementService.login(userDao.getUser(id));
	}

	protected void logout() {
		announcementService.logout();
	}

	protected void executeInTransaction(Executable exec) {
		txUtil.executeInTransaction(txManager, exec);
	}

	@Before
	public void loadData() {
		fix = new DbFix(template, txManager, userDao, contentDao, aceDao);
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				fix.loadData();
			}
		});
	}

	@After
	public void removeData() {
		executeInTransaction(new Executable() {
			@Override
			public void execute() {
				fix.removeData();
			}
		});
	}
}