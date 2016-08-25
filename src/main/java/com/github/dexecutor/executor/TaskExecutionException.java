package com.github.dexecutor.executor;

/**
 * Tasks can throw this exception to report any issues encountered during task execution
 * 
 * @author Nadeem Mohammad
 *
 */

public class TaskExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new TaskExecutionException.
	 * @param msg the detail message
	 */
	public TaskExecutionException(final String msg) {
		super(msg);
	}

	/**
	 * Create a new TaskExecutionException.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public TaskExecutionException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
