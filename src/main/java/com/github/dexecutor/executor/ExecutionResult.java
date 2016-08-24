package com.github.dexecutor.executor;
/**
 * Holds execution result of a node identified by id 
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class ExecutionResult <T, R> {

	private T id;
	private R result;
	private ExecutionStatus status;

	public ExecutionResult(final T id, final R result, ExecutionStatus status) {
		this.id = id;
		this.result = result;
		this.status = status;
	}
	/**
	 * 
	 * @return the id of the executing node
	 */
	public T getId() {
		return id;
	}
	/**
	 * 
	 * @return result of execution
	 */
	public R getResult() {
		return result;
	}
	/**
	 * 
	 * @return {@code true} if the result is success
	 * {@code false} if the result is not success
	 */
	public boolean isSuccess() {
		return ExecutionStatus.SUCCESS.equals(this.status);
	}
	/**
	 * 
	 * @return {@code true} if the result is error
	 * {@code false} if the result is not error
	 */
	public boolean isErrored() {
		return ExecutionStatus.ERRORED.equals(this.status);
	}
	/**
	 * 
	 * @return {@code true} if the result is skipped
	 * {@code false} if the result is not skipped
	 */
	public boolean isSkipped() {
		return ExecutionStatus.SKIPPED.equals(this.status);
	}

	@Override
	public String toString() {
		return "ExecutionResult [id=" + id + ", result=" + result + ", status=" + status + "]";
	}	
}
