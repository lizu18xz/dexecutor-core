package com.github.dexecutor.executor;

import java.util.concurrent.Executors;

import org.junit.Test;

public class DependentTasksExecutorConfigTest {

	@Test(expected= IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenValidatorIsNull() {
		DependentTasksExecutorConfig<String> config = new DependentTasksExecutorConfig<>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setValidator(null);
		config.validate();
	}

	@Test(expected= IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenTraversarIsNull() {
		DependentTasksExecutorConfig<String> config = new DependentTasksExecutorConfig<>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setTraversar(null);
		config.validate();
	}

	private TaskProvider<String> newTaskProvider() {
		return new TaskProvider<String>() {
			
			@Override
			public Task provid(String id) {
				return null;
			}
		};
	}

}
