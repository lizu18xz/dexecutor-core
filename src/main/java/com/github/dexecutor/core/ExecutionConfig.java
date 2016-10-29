package com.github.dexecutor.core;

import static com.github.dexecutor.core.support.Preconditions.*;

public class ExecutionConfig {

	private ExecutionBehavior executionBehavior;
	private int retryCount = 0;
	private Duration retryDelay = Duration.MINIMAL_DURATION; 

	public static final ExecutionConfig TERMINATING = new ExecutionConfig().terminating();
	public static final ExecutionConfig NON_TERMINATING = new ExecutionConfig().nonTerminating();
	
	/**
	 * 
	 * @return {@code ExecutionConfig} representing non-terminating execution behavior
	 */
	public ExecutionConfig nonTerminating() {
		this.executionBehavior = ExecutionBehavior.NON_TERMINATING;
		return this;
	}
	
	/**
	 * 
	 * @return {@code ExecutionConfig} representing terminating execution behaivor
	 */
	public ExecutionConfig terminating() {
		this.executionBehavior = ExecutionBehavior.TERMINATING;
		return this;
	}
	
	/**
	 * @param count number of times retry should happen
	 * @return {@code ExecutionConfig} representing immediate retry execution behaivor
	 */
	public ExecutionConfig immediateRetrying(int count) {
		this.executionBehavior = ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING;
		this.retryCount = count;
		return this;
	}
	
	/**
	 * 
	 * @param count the number of times retry should happen
	 * @param delay the delay
	 * @return {@code ExecutionConfig} representing scheduled retry terminating execution behaivor
	 */
	public ExecutionConfig scheduledRetrying(int count, Duration delay) {
		this.executionBehavior = ExecutionBehavior.SCHEDULED_RETRY_TERMINATING;
		this.retryCount = count;
		this.retryDelay = delay;
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
	 * @return the retry delay
	 */
	public Duration getRetryDelay() {
		return retryDelay;
	}

	/**
	 * 
	 * @return {@code true} if the {@code ExecutionBehavior} is TERMINATING
	 * 			{@code false} otherwise
	 */
	public boolean isTerminating() {
		return ExecutionBehavior.TERMINATING.equals(this.executionBehavior);
	}
	/**
	 * 
	 * @return {@code true} if the {@code ExecutionBehavior} is NON_TERMINATING
	 * 			{@code false} otherwise
	 */
	public boolean isNonTerminating() {
		return ExecutionBehavior.NON_TERMINATING.equals(this.executionBehavior);
	}
	/**
	 * 
	 * @return {@code true} if the {@code ExecutionBehavior} is IMMEDIATE_RETRY_TERMINATING
	 * 			{@code false} otherwise
	 */
	public boolean isImmediatelyRetrying() {
		return ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING.equals(this.executionBehavior);
	}
	
	/**
	 * 
	 * @return {@code true} if the {@code ExecutionBehavior} is SCHEDULED_RETRY_TERMINATING
	 * 			{@code false} otherwise
	 */
	public boolean isScheduledRetrying() {
		return ExecutionBehavior.SCHEDULED_RETRY_TERMINATING.equals(this.executionBehavior);
	}
	/**
	 * 
	 * @param currentCount the current count
	 * @return {@code true} if a retry should be attempted, based on current retries already happened.
	 * 			{@code false} otherwise
	 */
	public boolean shouldRetry(int currentCount) {
		return this.retryCount != 0 && this.retryCount >= currentCount;
	}
	/**
	 * Does basic validation to make sure object is valid
	 */
	public void validate() {
		if (isScheduledRetrying()) {
			checkNotNull(this.retryDelay, "retryDelay should be specified for " + ExecutionBehavior.SCHEDULED_RETRY_TERMINATING);
			checkArgument(this.getRetryDelay().getDuration() > 0, "Retry delay duration should be greater than ZERO");
		}
	}
}
