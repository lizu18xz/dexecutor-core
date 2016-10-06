package com.github.dexecutor.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;

import mockit.Deencapsulation;

public class DefaultDependentTasksExecutorScheduledRetryingTest {

	Condition<Node<Integer, Integer>> nodeTwo = new Condition<Node<Integer, Integer>>() {
		@Override
		public boolean matches(Node<Integer, Integer> value) {
			return value.getValue() == 2;
		}
	};
	
	@Test
	public void testDependentTaskExecution() {

		ExecutorService executorService = newExecutor();
		ExecutionEngine<Integer, Integer> executionEngine = new DefaultExecutionEngine<>(executorService);

		try {
			DefaultDependentTasksExecutor<Integer, Integer> executor = new DefaultDependentTasksExecutor<Integer, Integer>(
					executionEngine, new SleepyTaskProvider());

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

			executor.execute(new ExecutionConfig().scheduledRetrying(4, new Duration(1, TimeUnit.NANOSECONDS)));

			Collection<Node<Integer, Integer>> processedNodesOrder = Deencapsulation.getField(executor, "processedNodes");
			assertThat(processedNodesOrder).containsAll(executionOrderExpectedResult());
			assertThat(processedNodesOrder).size().isEqualTo(16);
			assertThat(processedNodesOrder).areExactly(3, nodeTwo);
			
		} finally {
			try {
				executorService.shutdownNow();
				executorService.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {

			}
		}
	}
	
	private Collection<Node<Integer, Integer>> executionOrderExpectedResult() {
		List<Node<Integer, Integer>> result = new ArrayList<Node<Integer, Integer>>();
		result.add(new Node<Integer, Integer>(1));
		result.add(new Node<Integer, Integer>(2));
		result.add(new Node<Integer, Integer>(7));
		result.add(new Node<Integer, Integer>(9));
		result.add(new Node<Integer, Integer>(10));
		result.add(new Node<Integer, Integer>(8));
		result.add(new Node<Integer, Integer>(11));
		result.add(new Node<Integer, Integer>(12));
		result.add(new Node<Integer, Integer>(3));
		result.add(new Node<Integer, Integer>(13));
		result.add(new Node<Integer, Integer>(5));
		result.add(new Node<Integer, Integer>(6));
		result.add(new Node<Integer, Integer>(4));
		result.add(new Node<Integer, Integer>(14));
		return result;
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {
		
		private AtomicInteger count = new AtomicInteger(0);

		public Task<Integer, Integer> provideTask(final Integer id) {

			return new Task<Integer, Integer>() {

				private static final long serialVersionUID = 1L;

				public Integer execute() {
					if (id == 2) {
						count.incrementAndGet();
						if (count.get() < 3) {							
							throw new IllegalArgumentException("Invalid task");
						}						
					}
					return id;
				}
			};
		}
	}
}
