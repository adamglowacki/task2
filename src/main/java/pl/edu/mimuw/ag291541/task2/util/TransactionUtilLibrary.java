package pl.edu.mimuw.ag291541.task2.util;

import org.springframework.transaction.PlatformTransactionManager;

public interface TransactionUtilLibrary {
	/**
	 * Creates a new transaction and commits it after execution of
	 * <code>exec</code>. In case of any <code>RuntimeException</code> being
	 * thrown the transaction is rolled back.
	 * 
	 * @param txManager
	 *            The transaction manager to be used to obtain a transaction.
	 * @param exec
	 *            What is to be executed.
	 */
	public void executeInTransaction(PlatformTransactionManager txManager,
			Executable exec);
}
