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
	 * @param task successfull one
	 */
	void onSuccess(Task<T, R> task);
	/**
	 * Called on errored node execution
	 * @param task the errored one
	 * @param exception the exception caught
	 */
	void onError(Task<T, R> task, Exception exception);
}
