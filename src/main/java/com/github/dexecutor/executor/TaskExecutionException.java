package com.github.dexecutor.executor;

public class TaskExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TaskExecutionException() {

	}

	public TaskExecutionException(String msg) {
		super(msg);
	}

	public TaskExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskExecutionException(Throwable cause) {
        super(cause);
    }
}
