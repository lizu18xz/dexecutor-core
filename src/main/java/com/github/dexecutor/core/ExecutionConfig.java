package com.github.dexecutor.core;

public class ExecutionConfig {

	private ExecutionBehavior executionBehavior;
	private int retryCount = 0;
	private long retryDelayInMillis = 0;

	public static final ExecutionConfig TERMINATING = new ExecutionConfig().terminating();
	public static final ExecutionConfig NON_TERMINATING = new ExecutionConfig().nonTerminating();

	public ExecutionConfig nonTerminating() {
		this.executionBehavior = ExecutionBehavior.NON_TERMINATING;
		return this;
	}

	public ExecutionConfig terminating() {
		this.executionBehavior = ExecutionBehavior.TERMINATING;
		return this;
	}

	public ExecutionConfig immediateRetrying(int count) {
		this.executionBehavior = ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING;
		this.retryCount = count;
		return this;
	}

	public ExecutionConfig scheduledRetrying(int count, long retryDelayInMillis) {
		this.executionBehavior = ExecutionBehavior.SCHEDULED_RETRY_TERMINATING;
		this.retryCount = count;
		this.retryDelayInMillis = retryDelayInMillis;
		return this;
	}

	public ExecutionBehavior getExecutionBehavior() {
		return executionBehavior;
	}

	public int getRetryCount() {
		return retryCount;
	}
	
	public long getRetryDelayInMillis() {
		return retryDelayInMillis;
	}

	public boolean isTerminating() {
		return ExecutionBehavior.TERMINATING.equals(this.executionBehavior);
	}
	
	public boolean isNonTerminating() {
		return ExecutionBehavior.NON_TERMINATING.equals(this.executionBehavior);
	}

	public boolean isImmediatelyRetrying() {
		return ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING.equals(this.executionBehavior);
	}

	public boolean isScheduledRetrying() {
		return ExecutionBehavior.SCHEDULED_RETRY_TERMINATING.equals(this.executionBehavior);
	}

	public boolean shouldRetry(int currentCount) {
		return this.retryCount != 0 && this.retryCount > currentCount;
	}
}
