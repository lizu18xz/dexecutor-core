package com.github.dexecutor.executor;

import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dexecutor.executor.TaskProvider.Task;
import com.github.dexecutor.executor.graph.DefaultGraph;
import com.github.dexecutor.executor.graph.Graph;
import com.github.dexecutor.executor.graph.Graph.Node;
import com.github.dexecutor.executor.graph.Traversar;
import com.github.dexecutor.executor.graph.Validator;

/**
 * Default implementation of @DependentTasksExecutor
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public final class DefaultDependentTasksExecutor <T extends Comparable<T>> implements DependentTasksExecutor<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDependentTasksExecutor.class);

	private ExecutorService executorService;
	private TaskProvider<T> taskProvider;
	private Validator<T> validator;
	private Traversar<T> traversar;
	private Graph<T> graph;

	private Collection<Node<T>> processedNodes = new CopyOnWriteArrayList<Node<T>>();
	private AtomicInteger nodesCount = new AtomicInteger(0);
	/**
	 * Creates the Executor with bare minimum required params
	 * @param executorService
	 * @param taskProvider
	 */
	public DefaultDependentTasksExecutor(final ExecutorService executorService, final TaskProvider<T> taskProvider) {
		this(new DependentTasksExecutorConfig<T>(executorService, taskProvider));
	}
	/**
	 * Creates the Executor with Config
	 * @param config
	 */
	public DefaultDependentTasksExecutor(final DependentTasksExecutorConfig<T> config) {
		config.validate();
		this.executorService = config.getExecutorService();
		this.taskProvider = config.getTaskProvider();
		this.validator = config.getValidator();
		this.traversar = config.getTraversar();
		this.graph = new DefaultGraph<T>();
	}

	public void print(final Writer writer) {
		this.traversar.traverse(this.graph, writer);
	}

	public void addIndependent(final T nodeValue) {
		this.graph.addIndependent(nodeValue);
	}

	public void addDependency(final T evalFirstNode, final T evalLaterNode) {
		this.graph.addDependency(evalFirstNode, evalLaterNode);
	}

	public void addAsDependentOnAllLeafNodes(final T nodeValue) {
		if (this.graph.size() == 0) {
			addIndependent(nodeValue);
		} else {
			for (Node<T> node : this.graph.getLeafNodes()) {
				addDependency(node.getValue(), nodeValue);
			}
		}
	}

	@Override
	public void addAsDependencyToAllInitialNodes(final T nodeValue) {
		if (this.graph.size() == 0) {
			addIndependent(nodeValue);
		} else {
			for (Node<T> node : this.graph.getInitialNodes()) {
				addDependency(nodeValue, node.getValue());
			}
		}		
	}

	private boolean isAlreadyProcessed(final Node<T> node) {
		return this.processedNodes.contains(node);
	}

	private boolean areAlreadyProcessed(final Set<Node<T>> nodes) {
        return this.processedNodes.containsAll(nodes);
    }

	public void execute(final ExecutionBehavior behavior) {
		validate();

		Set<Node<T>> initialNodes = this.graph.getInitialNodes();
		CompletionService<Node<T>> completionService = new ExecutorCompletionService<Node<T>>(executorService);

		long start = new Date().getTime();
		
		doProcessNodes(behavior, initialNodes, completionService);

		long end = new Date().getTime();

		logger.debug("Total Time taken to process {} jobs is {} ms.", graph.size(), end - start);
		logger.debug("Processed Nodes Ordering {}", this.processedNodes);
	}

	private void doProcessNodes(final ExecutionBehavior behavior, final Set<Node<T>> nodes, final CompletionService<Node<T>> completionService) {
		doExecute(nodes, completionService, behavior);
		doWaitForExecution(completionService, behavior);	
	}

	private void validate() {
		this.validator.validate(this.graph);
	}

	private void doExecute(final Collection<Node<T>> nodes, final CompletionService<Node<T>> completionService, final ExecutionBehavior behavior) {
		for (Node<T> node : nodes) {
			if (shouldProcess(node) ) {
				nodesCount.incrementAndGet();
				logger.debug("Going to schedule {} node", node.getValue());
				completionService.submit(newWorker(node, behavior));
			} else {
				logger.debug("node {} depends on {}", node.getValue(), node.getInComingNodes());
			}
		}		
	}

	private boolean shouldProcess(final Node<T> node) {
		return !this.executorService.isShutdown() && !isAlreadyProcessed(node) && allIncomingNodesProcessed(node);
	}

	private boolean allIncomingNodesProcessed(final Node<T> node) {
		if (node.getInComingNodes().isEmpty() || areAlreadyProcessed(node.getInComingNodes())) {
			return true;
		}
		return false;
	}

	private void doWaitForExecution(final CompletionService<Node<T>> completionService, final ExecutionBehavior behavior) {
		int cuurentCount = 0;
		while (cuurentCount != nodesCount.get()) {
			try {
				Future<Node<T>> future = completionService.take();
				Node<T> processedNode = future.get();
				logger.debug("Processing of node {} done", processedNode.getValue());
				cuurentCount++;
				this.processedNodes.add(processedNode);				
				//logger.debug(this.executorService.toString());
				doExecute(processedNode.getOutGoingNodes(), completionService, behavior);
			} catch (Exception e) {
				cuurentCount++;
				logger.error("Task interrupted", e);
			}
		}
	}

	private Callable<Node<T>> newWorker(final Node<T> graphNode, final ExecutionBehavior behavior) {
		if (ExecutionBehavior.NON_TERMINATING.equals(behavior)) {
			return new NonTerminatingTask(graphNode);
		} else if (ExecutionBehavior.RETRY_ONCE_TERMINATING.equals(behavior)) { 
			return new RetryOnceAndTerminateTask(graphNode);
		} else {
			return new TerminatingTask(graphNode);
		}
	}

	private class TerminatingTask implements Callable<Node<T>> {
		private Node<T> node;

		public TerminatingTask(final Node<T> graphNode) {
			this.node = graphNode;
		}

		public Node<T> call() throws Exception {
			Task task = newLoggingTask(this.node.getValue());
			task.setConsiderExecutionError(true);
			task.execute();
			return this.node;
		}		
	}

	private class NonTerminatingTask implements Callable<Node<T>> {
		private Node<T> node;

		public NonTerminatingTask(final Node<T> graphNode) {
			this.node = graphNode;
		}

		public Node<T> call() throws Exception {
			try {
				Task task = newLoggingTask(this.node.getValue());
				task.setConsiderExecutionError(false);
				task.execute();
			} catch(Exception ex) {
				logger.error("Exception caught, executing node # " + this.node.getValue(), ex);
			}
			return this.node;
		}
	}

	private class RetryOnceAndTerminateTask implements Callable<Node<T>> {
		private Node<T> node;

		public RetryOnceAndTerminateTask(final Node<T> graphNode) {
			this.node = graphNode;
		}

		public Node<T> call() throws Exception {
			Task task = newLoggingTask(this.node.getValue());
			boolean retry = shouldRetry(this.node.getValue());
			task.setConsiderExecutionError(!retry);
			try {
				task.execute();
			} catch(Exception ex) {
				logger.error("Exception caught, executing node # " + this.node.getValue() + " Retry would happen : " + getYesNo(retry), ex);
				if (retry) {
					task.setConsiderExecutionError(true);
					task.execute();
				}
			}
			return this.node;
		}

		private String getYesNo(boolean retry) {
			return retry ? "Yes" : "No";
		}	
	}

	protected boolean shouldRetry(final T node) {
		return true;
	}

	private Task newLoggingTask(final T taskId) {
		return new LoggingTask(taskId, this.taskProvider.provid(taskId));
	}

	private class LoggingTask extends Task {
		private final Task task;
		private final T taskId;
		private int retryCount = 0;
		
		public LoggingTask(final T taskId, final Task task) {
			this.task = task;
			this.taskId = taskId;
		}

		public void execute() {
			logger.debug("{} Node # {}", msg(this.retryCount), this.taskId);
			this.retryCount ++;
			this.task.execute();
			logger.debug("Node # {}, Execution Done!", this.taskId);
		}

		private String msg(int retryCount) {
			return retryCount > 0 ? "Retrying(" + retryCount+ ") " : "Executing";
		}

		@Override
		void setConsiderExecutionError(boolean considerExecutionError) {
			this.task.setConsiderExecutionError(considerExecutionError);
		}
	}
}
