package com.dexecutor.executor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.dexecutor.executor.graph.Graph.Node;

import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class DefaultDependentTasksExecutorTest {

	@Test
	public void testDependentTaskExecutionOrder() {

		new MockedCompletionService();

		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor();

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

		executor.execute(true);

		Collection<Node<Integer>> processedNodesOrder = Deencapsulation.getField(executor, "processedNodes");

		assertThat(processedNodesOrder, equalTo(result()));
	}

	private Collection<Node<Integer>> result() {
		List<Node<Integer>> result = new ArrayList<Node<Integer>>();
		result.add(new Node<Integer>(1));
		result.add(new Node<Integer>(11));
		result.add(new Node<Integer>(12));
		result.add(new Node<Integer>(2));
		result.add(new Node<Integer>(3));
		result.add(new Node<Integer>(13));
		result.add(new Node<Integer>(7));
		result.add(new Node<Integer>(9));
		result.add(new Node<Integer>(8));
		result.add(new Node<Integer>(5));
		result.add(new Node<Integer>(6));
		result.add(new Node<Integer>(4));
		result.add(new Node<Integer>(14));
		result.add(new Node<Integer>(10));
		return result;
	}

	private DefaultDependentTasksExecutor<Integer> newTaskExecutor() {
		return new DefaultDependentTasksExecutor<Integer>(newExecutor(), new DummyTaskProvider<Integer>());
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(PoolUtil.ioIntesivePoolSize());
	}

	private static class DummyTaskProvider<T> implements TaskProvider<T> {

		public Task provid(T id) {

			return new Task() {

				public void execute() {

				}
			};
		}
	}

	private static class MockedCompletionService extends MockUp<ExecutorCompletionService<Node<Integer>>> {
		List<Callable<Node<Integer>>> nodes = new ArrayList<Callable<Node<Integer>>>();
		int index = 0;

		@Mock
		public void $init(Executor executor) {

		}

		@Mock
		public Future<Node<Integer>> submit(Callable<Node<Integer>> task) {
			nodes.add(task);
			return null;
		}

		@Mock
		public Future<Node<Integer>> take() throws InterruptedException {
			return new Future<Node<Integer>>() {

				public boolean isDone() {
					return false;
				}

				public boolean isCancelled() {
					return false;
				}

				public Node<Integer> get(long timeout, TimeUnit unit)
						throws InterruptedException, ExecutionException, TimeoutException {
					return null;
				}

				public boolean cancel(boolean mayInterruptIfRunning) {
					return false;
				}

				public Node<Integer> get() throws InterruptedException, ExecutionException {
					try {
						Node<Integer> call = nodes.get(index).call();
						index++;
						return call;
					} catch (Exception e) {

					}
					return null;
				}
			};
		}
	}
}
