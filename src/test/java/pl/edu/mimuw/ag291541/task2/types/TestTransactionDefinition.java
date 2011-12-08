package pl.edu.mimuw.ag291541.task2.types;

import org.springframework.transaction.TransactionDefinition;

public class TestTransactionDefinition implements TransactionDefinition {

	@Override
	public int getPropagationBehavior() {
		return TransactionDefinition.PROPAGATION_REQUIRES_NEW;
	}

	@Override
	public int getIsolationLevel() {
		return TransactionDefinition.ISOLATION_DEFAULT;
	}

	@Override
	public int getTimeout() {
		return TransactionDefinition.TIMEOUT_DEFAULT;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public String getName() {
		return "Transaction definition for tests";
	}

}
