package com.github.dexecutor.executor;

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Ignore;
import org.junit.Test;

import com.github.dexecutor.executor.DependentTasksExecutor.ExecutionBehavior;
import com.github.dexecutor.executor.support.ThreadPoolUtil;

/**
 * 
 * @author Nadeem Mohammad
 *
 */
@Ignore
public class DefaultDependentTasksExecutorIntegrationTest {

	@Test
	public void testDependentTaskExecution() {

		DefaultDependentTasksExecutor<Integer, Integer> executor = newTaskExecutor();

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

		executor.execute(ExecutionBehavior.RETRY_ONCE_TERMINATING);
        System.out.println("*** Done ***");
	}

	private DefaultDependentTasksExecutor<Integer, Integer> newTaskExecutor() {
		return new DefaultDependentTasksExecutor<Integer, Integer>(newExecutor(), new SleepyTaskProvider());
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {

		public Task<Integer, Integer> provid(final Integer id) {

			return new Task<Integer, Integer>() {

				public Integer execute() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return id;
				}
			};			
		}		
	}
}
