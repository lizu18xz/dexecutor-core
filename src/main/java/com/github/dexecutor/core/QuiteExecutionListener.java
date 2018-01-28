package com.github.dexecutor.core;

import com.github.dexecutor.core.task.Task;

public class QuiteExecutionListener<T, R> implements ExecutionListener<T, R> {

	@Override
	public void onSuccess(Task<T, R> task) {

	}

	@Override
	public void onError(Task<T, R> task, Exception exception) {

	}

}
