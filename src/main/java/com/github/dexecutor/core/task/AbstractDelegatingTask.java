package com.github.dexecutor.core.task;

import com.github.dexecutor.core.DependentTasksExecutor.ExecutionBehavior;

abstract class AbstractDelegatingTask <T extends Comparable<T>, R> extends Task<T, R> {

	private static final long serialVersionUID = 1L;

	private Task<T, R> task;
	
	public AbstractDelegatingTask(final Task<T, R> task) {
		this.task = task;
	}
	
	protected Task<T, R> getTargetTask() {
		return this.task;
	}
	
	
	public void setId(final T id) {
		this.task.setId(id);
	}

	public T getId() {;
		return this.task.getId();
	}

	public void errored() {
		this.task.errored();
	}

	public void skipped() {
		this.task.skipped();
	}

	public ExecutionStatus getStatus() {
		return this.task.getStatus();
	}

	public ExecutionBehavior getExecutionBehavior() {
		return this.task.getExecutionBehavior();
	}

	public void setExecutionBehavior(ExecutionBehavior executionBehavior) {
		this.task.setExecutionBehavior(executionBehavior);
	}

	public  boolean shouldConsiderExecutionError() {
		return this.task.shouldConsiderExecutionError();
	}

	void setConsiderExecutionError(boolean considerExecutionError) {
		this.task.setConsiderExecutionError(considerExecutionError);
	}
	
	public boolean shouldExecute(final ExecutionResults<T, R> parentResults) {
		return this.task.shouldExecute(parentResults);
	}
}
