package com.github.dexecutor.core;

import com.github.dexecutor.core.task.Task;

/**
 * 
 * @author Nadeem Mohammad
 * 
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface ExecutionListener<T, R> {
	/**
	 * Called on successful node execution
	 * 
	 * @param node
	 */
	void onSuccess(Task<T, R> task);
	/**
	 * Called on errored node execution
	 * 
	 * @param node
	 */
	void onError(Task<T, R> task, Exception exception);
}
