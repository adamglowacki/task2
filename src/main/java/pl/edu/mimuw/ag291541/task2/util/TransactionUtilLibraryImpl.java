package pl.edu.mimuw.ag291541.task2.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionUtilLibraryImpl implements TransactionUtilLibrary {

	@Override
	public void executeInTransaction(PlatformTransactionManager txManager,
			Executable exec) {
		TransactionDefinition txDefinition = new DefaultTransactionDefinition(
				DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus txStatus = txManager.getTransaction(txDefinition);
		try {
			exec.execute();
		} catch (RuntimeException e) {
			txManager.rollback(txStatus);
			throw e;
		}
		txManager.commit(txStatus);
	}

}
