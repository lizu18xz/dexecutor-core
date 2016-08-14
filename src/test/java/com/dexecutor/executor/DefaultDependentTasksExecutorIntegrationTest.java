package com.dexecutor.executor;

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.dexecutor.executor.support.PoolUtil;

public class DefaultDependentTasksExecutorIntegrationTest {

	@Test
	public void testDependentTaskExecution() {

		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor();

        executor.addDependency(1, 2);
        executor.addDependency(1, 2);
        executor.addDependency(1, 3);
        executor.addDependency(3, 4);
        executor.addDependency(3, 5);
        executor.addDependency(3, 6);
        //executor.addDependency(10, 2); // cycle
        executor.addDependency(2, 7);
        executor.addDependency(2, 9);
        executor.addDependency(2, 8);
        executor.addDependency(9, 10);
        executor.addDependency(12, 13);
        executor.addDependency(13, 4);
        executor.addDependency(13, 14);
        executor.addIndependent(11);

        StringWriter writer = new StringWriter();
		executor.print(writer);
		System.out.println(writer);

		boolean stopOnError = true;
		executor.execute(stopOnError);
        System.out.println("*** Done ***");
	}

	private DefaultDependentTasksExecutor<Integer> newTaskExecutor() {
		return new DefaultDependentTasksExecutor<Integer>(newExecutor(), new SleepyTaskProvider<Integer>());
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(PoolUtil.ioIntesivePoolSize());
	}

	private static class SleepyTaskProvider<T extends Comparable<T>> implements TaskProvider<T> {

		public Task provid(T id) {

			return new Task() {

				public void execute() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}		
	}
}
