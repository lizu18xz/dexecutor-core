package com.github.dexecutor.executor;

import java.util.concurrent.Executors;

import org.junit.Test;

public class DependentTasksExecutorConfigTest {

	@Test(expected= IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenValidatorIsNull() {
		DependentTasksExecutorConfig<String, String> config = new DependentTasksExecutorConfig<>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setValidator(null);
		config.validate();
	}

	@Test(expected= IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenTraversarIsNull() {
		DependentTasksExecutorConfig<String, String> config = new DependentTasksExecutorConfig<>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setTraversar(null);
		config.validate();
	}

	private TaskProvider<String, String> newTaskProvider() {
		return new TaskProvider<String, String>() {
			
			@Override
			public Task<String, String> provid(String id) {
				return null;
			}
		};
	}

}
