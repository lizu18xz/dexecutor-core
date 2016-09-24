package com.github.dexecutor.core;

public class ExecutionConfig {
	
	private ExecutionBehavior executionBehavior;
	private int retryCount = 0;
	
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

	public ExecutionConfig retrying(int count) {
		this.executionBehavior = ExecutionBehavior.RETRY_IMMEDIATE_TERMINATING;
		this.retryCount = count;
		return this;
	}

	public ExecutionBehavior getExecutionBehavior() {
		return executionBehavior;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public boolean isNonTerminating() {
		return ExecutionBehavior.NON_TERMINATING.equals(this.executionBehavior);
	}

	public boolean isRetrying() {
		return ExecutionBehavior.RETRY_IMMEDIATE_TERMINATING.equals(this.executionBehavior);
	}
}
