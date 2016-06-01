package com.dexecutor.executor;

public final class PoolUtil {
	
	private PoolUtil() {
		
	}

	public static int poolSize() {
		// Each tasks blocks 90% of the time, and works only 10% of its
		// lifetime. That is, I/O intensive pool
		double blockingCoefficient = 0.9;
		return poolSize(blockingCoefficient);
	}

	/**
	 * 
	 * Number of threads = Number of Available Cores / (1 - Blocking
	 * Coefficient) where the blocking coefficient is between 0 and 1.
	 * 
	 * A computation-intensive task has a blocking coefficient of 0, whereas an
	 * IO-intensive task has a value close to 1—a fully blocked task is doomed,
	 * so we don’t have to worry about the value reaching 1.
	 */
	public static int poolSize(double blockingCoefficient) {
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
		return poolSize;
	}
}
