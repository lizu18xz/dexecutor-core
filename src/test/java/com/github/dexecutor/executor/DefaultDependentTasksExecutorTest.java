package com.github.dexecutor.executor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.github.dexecutor.executor.DependentTasksExecutor.ExecutionBehavior;
import com.github.dexecutor.executor.graph.Graph;
import com.github.dexecutor.executor.graph.Graph.Node;
import com.github.dexecutor.executor.support.ThreadPoolUtil;

import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

/**
 * 
 * @author Nadeem Mohammad
 *
 */
@RunWith(JMockit.class)
public class DefaultDependentTasksExecutorTest {
	
	@Test
	public void testAddAsDependencyToAllInitialNodes() {
		new MockedCompletionService();
		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(false);
		executor.addAsDependencyToAllInitialNodes(1);
		Graph<Integer> graph = Deencapsulation.getField(executor, "graph");
		assertThat(graph.size(), equalTo(1));
		executor.addDependency(1, 2);
		executor.addAsDependencyToAllInitialNodes(1);
		assertThat(graph.size(), equalTo(2));
	}
	
	@Test
	public void testAddAsDependentOnAllLeafNodes() {
		new MockedCompletionService();
		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(false);
		executor.addAsDependentOnAllLeafNodes(1);
		Graph<Integer> graph = Deencapsulation.getField(executor, "graph");
		assertThat(graph.size(), equalTo(1));
		executor.addDependency(1, 2);
		executor.addAsDependentOnAllLeafNodes(1);
		assertThat(graph.size(), equalTo(2));
	}
	
	@Test
	public void testPrint() {
		new MockedCompletionService();
		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(false);
		executor.addDependency(1, 2);
		StringWriter writer = new StringWriter();
		executor.print(writer);
		assertThat(writer.toString(), equalTo("Path #0\n1[] \n2[1] \n\n"));
	}
	
	public void testTerminatingTask() {
		new MockedCompletionService();
		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(false);
		executor.addDependency(1, 2);
		executor.execute(ExecutionBehavior.TERMINATING);
	}
	
	public void testNonTerminatingTask() {
		new MockedCompletionService();
		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(false);
		executor.addDependency(1, 2);
		executor.execute(ExecutionBehavior.NON_TERMINATING);
	}
	
	@Test
	public void testDependentTaskExecutionOrder() {

		new MockedCompletionService();

		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(false);

		addDependencies(executor);

		executor.execute(ExecutionBehavior.RETRY_ONCE_TERMINATING);

		Collection<Node<Integer>> processedNodesOrder = Deencapsulation.getField(executor, "processedNodes");

		assertThat(processedNodesOrder, equalTo(executionOrderExpectedResult()));
	}

	
	@Test
	public void testDependentTaskExecutionOrderWithException() {

		new MockedCompletionService();

		DefaultDependentTasksExecutor<Integer> executor = newTaskExecutor(true);

		addDependencies(executor);

		executor.execute(ExecutionBehavior.RETRY_ONCE_TERMINATING);

		Collection<Node<Integer>> processedNodesOrder = Deencapsulation.getField(executor, "processedNodes");

		assertThat(processedNodesOrder, equalTo((Collection<Node<Integer>>)Arrays.asList(new Node<Integer>(1), new Node<Integer>(11), new Node<Integer>(12))));
	}


	private void addDependencies(DefaultDependentTasksExecutor<Integer> executor) {
		executor.addDependency(1, 2);
		executor.addDependency(1, 2);
		executor.addDependency(1, 3);
		executor.addDependency(3, 4);
		executor.addDependency(3, 5);
		executor.addDependency(3, 6);
		executor.addDependency(2, 7);
		executor.addDependency(2, 9);
		executor.addDependency(2, 8);
		executor.addDependency(9, 10);
		executor.addDependency(12, 13);
		executor.addDependency(13, 4);
		executor.addDependency(13, 14);
		executor.addIndependent(11);
	}

	private Collection<Node<Integer>> executionOrderExpectedResult() {
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

	private DefaultDependentTasksExecutor<Integer> newTaskExecutor(boolean throwEx) {
		return new DefaultDependentTasksExecutor<Integer>(newExecutor(), new DummyTaskProvider<Integer>(throwEx));
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class DummyTaskProvider<T extends Comparable<T>> implements TaskProvider<T> {
		private boolean throwEx;
		
		public DummyTaskProvider(boolean throwEx) {
			this.throwEx = throwEx;
		}

		public Task provid(final T id) {

			return new Task() {

				public void execute() {
					shouldConsiderExecutionError();
					doExecute(id);
				}

				private void doExecute(final T id) {
					if (throwEx) {
						if (id ==  Integer.valueOf(2)) {
							throw new RuntimeException();
						}
					}
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

				public boolean cancel(boolean mayInterruptIfRunning) {
					return false;
				}

				public Node<Integer> get(long timeout, TimeUnit unit)
						throws InterruptedException, ExecutionException, TimeoutException {
					return doGet();
				}

				public Node<Integer> get() throws InterruptedException, ExecutionException {
					return doGet();
				}
				
				private Node<Integer> doGet() {
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
