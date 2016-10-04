package com.github.dexecutor.core;

public class ExecutionConfig {

	private ExecutionBehavior executionBehavior;
	private int retryCount = 0;
	private long retryDelayInMillis = 0;

	public static final ExecutionConfig TERMINATING = new ExecutionConfig().terminating();
	public static final ExecutionConfig NON_TERMINATING = new ExecutionConfig().nonTerminating();
	
	/**
	 * 
	 * @return {@ ExecutionConfig} representing non-terminating execution behaivor
	 */
	public ExecutionConfig nonTerminating() {
		this.executionBehavior = ExecutionBehavior.NON_TERMINATING;
		return this;
	}
	
	/**
	 * 
	 * @return {@ ExecutionConfig} representing terminating execution behaivor
	 */
	public ExecutionConfig terminating() {
		this.executionBehavior = ExecutionBehavior.TERMINATING;
		return this;
	}
	
	/**
	 * 
	 * @return {@ ExecutionConfig} representing immediate retry execution behaivor
	 */
	public ExecutionConfig immediateRetrying(int count) {
		this.executionBehavior = ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING;
		this.retryCount = count;
		return this;
	}
	
	/**
	 * 
	 * @return {@ ExecutionConfig} representing scheduled retry terminating execution behaivor
	 */
	public ExecutionConfig scheduledRetrying(int count, long retryDelayInMillis) {
		this.executionBehavior = ExecutionBehavior.SCHEDULED_RETRY_TERMINATING;
		this.retryCount = count;
		this.retryDelayInMillis = retryDelayInMillis;
		return this;
	}
	/**
	 * 
	 * @return the execution behavior
	 */
	public ExecutionBehavior getExecutionBehavior() {
		return executionBehavior;
	}
	/**
	 * 
	 * @return the retry count
	 */
	public int getRetryCount() {
		return retryCount;
	}
	/**
	 * 
	 * @return retry delay in millis
	 */
	public long getRetryDelayInMillis() {
		return retryDelayInMillis;
	}
	/**
	 * 
	 * @return {@true} if the {@ExecutionBehavior} is TERMINATING
	 * 			{@false} otherwise
	 */
	public boolean isTerminating() {
		return ExecutionBehavior.TERMINATING.equals(this.executionBehavior);
	}
	/**
	 * 
	 * @return {@true} if the {@ExecutionBehavior} is NON_TERMINATING
	 * 			{@false} otherwise
	 */
	public boolean isNonTerminating() {
		return ExecutionBehavior.NON_TERMINATING.equals(this.executionBehavior);
	}
	/**
	 * 
	 * @return {@true} if the {@ExecutionBehavior} is IMMEDIATE_RETRY_TERMINATING
	 * 			{@false} otherwise
	 */
	public boolean isImmediatelyRetrying() {
		return ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING.equals(this.executionBehavior);
	}
	
	/**
	 * 
	 * @return {@true} if the {@ExecutionBehavior} is SCHEDULED_RETRY_TERMINATING
	 * 			{@false} otherwise
	 */
	public boolean isScheduledRetrying() {
		return ExecutionBehavior.SCHEDULED_RETRY_TERMINATING.equals(this.executionBehavior);
	}
	/**
	 * 
	 * @param currentCount
	 * @return {@true} if a retry should be attempted, based on current retries already happened.
	 * 			{@false} otherwise
	 */
	public boolean shouldRetry(int currentCount) {
		return this.retryCount != 0 && this.retryCount > currentCount;
	}
}
