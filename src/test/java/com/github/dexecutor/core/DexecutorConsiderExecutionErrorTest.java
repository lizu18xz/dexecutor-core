package com.github.dexecutor.core;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;

public class DexecutorConsiderExecutionErrorTest {

	Condition<Boolean> trueCondition = new Condition<Boolean>() {
		@Override
		public boolean matches(Boolean value) {
			return value == Boolean.TRUE;
		}
	};

	@Test
	public void testDependentTaskExecution() {

		ExecutorService executorService = newExecutor();
		
		try {
			SleepyTaskProvider taskProvider = new SleepyTaskProvider();
			DefaultDexecutorState<Integer, Integer> dexecutorState = new DefaultDexecutorState<Integer, Integer>();
			ExecutionEngine<Integer, Integer> executionEngine = new DefaultExecutionEngine<>(dexecutorState, executorService);
			DexecutorConfig<Integer, Integer> config = new DexecutorConfig<>(executionEngine, taskProvider);
			config.setDexecutorState(dexecutorState);
			DefaultDexecutor<Integer, Integer> executor = new DefaultDexecutor<Integer, Integer>(config);

			executor.addDependency(1, 2);
			executor.addDependency(1, 2);
			executor.addDependency(1, 3);
			executor.addDependency(3, 4);
			executor.addDependency(3, 5);
			executor.addDependency(3, 6);
			// executor.addDependency(10, 2); // cycle
			executor.addDependency(2, 7);
			executor.addDependency(2, 9);
			executor.addDependency(2, 8);
			executor.addDependency(9, 10);
			executor.addDependency(12, 13);
			executor.addDependency(13, 4);
			executor.addDependency(13, 14);
			executor.addIndependent(11);

			executor.execute(new ExecutionConfig().immediateRetrying(2));

			assertThat(taskProvider.getShouldConsiderExecutionErrors()).size().isEqualTo(3);
			assertThat(taskProvider.getShouldConsiderExecutionErrors()).areExactly(1, trueCondition);
			
		} finally {
			try {
				executorService.shutdownNow();
				executorService.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {

			}
		}
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {
		
		private List<Boolean> errors = new ArrayList<>();

		public Task<Integer, Integer> provideTask(final Integer id) {

			return new Task<Integer, Integer>() {

				private static final long serialVersionUID = 1L;

				public Integer execute() {
					if (id == 2) {
						errors.add(shouldConsiderExecutionError());
						throw new IllegalArgumentException("Invalid task");
					}
					return id;
				}
			};
		}

		public List<Boolean> getShouldConsiderExecutionErrors() {
			return this.errors;			
		}
	}
}
