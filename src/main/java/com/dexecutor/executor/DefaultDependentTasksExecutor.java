package com.dexecutor.executor;

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

import com.dexecutor.executor.TaskProvider.Task;
import com.dexecutor.executor.graph.DefaultGraph;
import com.dexecutor.executor.graph.Graph;
import com.dexecutor.executor.graph.Graph.Node;
import com.dexecutor.executor.graph.Traversar;
import com.dexecutor.executor.graph.Validator;

public final class DefaultDependentTasksExecutor <T extends Comparable<T>> implements DependentTasksExecutor<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDependentTasksExecutor.class);

	private ExecutorService executorService;
	private TaskProvider<T> taskProvider;
	private Validator<T> validator;
	private Traversar<T> traversar;
	private Graph<T> graph;

	private Collection<Node<T>> processedNodes = new CopyOnWriteArrayList<Node<T>>();
	private AtomicInteger nodesCount = new AtomicInteger(0);

	public DefaultDependentTasksExecutor(final ExecutorService executorService, final TaskProvider<T> taskProvider) {
		this(new DependentTasksExecutorConfig<T>(executorService, taskProvider));
	}

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

	public void addAsDependencyToAllLeafNodes(final T nodeValue) {
		if (this.graph.size() == 0) {
			addIndependent(nodeValue);
		} else {
			for (Node<T> node : this.graph.getLeafNodes()) {
				addDependency(node.getValue(), nodeValue);
			}
		}
	}

	private boolean isAlreadyProcessed(final Node<T> node) {
		return this.processedNodes.contains(node);
	}

	private boolean areAlreadyProcessed(final Set<Node<T>> nodes) {
        return this.processedNodes.containsAll(nodes);
    }

	public void execute(boolean stopOnError) {
		validate();

		Set<Node<T>> initialNodes = this.graph.getInitialNodes();
		CompletionService<Node<T>> completionService = new ExecutorCompletionService<Node<T>>(executorService);

		long start = new Date().getTime();
		
		doProcessNodes(stopOnError, initialNodes, completionService);

		long end = new Date().getTime();

		logger.debug("Total Time taken to process {} jobs is {} ms.", graph.size(), end - start);
		logger.debug("Processed Ndoes Ordering {}", this.processedNodes);
	}

	private void doProcessNodes(boolean stopOnError, Set<Node<T>> nodes, CompletionService<Node<T>> completionService) {
		doExecute(nodes, completionService, stopOnError);
		doWaitForExecution(completionService, stopOnError);	
	}

	private void validate() {
		this.validator.validate(this.graph);
	}

	private void doExecute(final Collection<Node<T>> nodes, final CompletionService<Node<T>> completionService, boolean stopOnError) {
		for (Node<T> node : nodes) {
			if (shouldProcess(node) ) {
				nodesCount.incrementAndGet();
				logger.debug("Going to schedule {} node", node.getValue());
				completionService.submit(newTask(node, stopOnError));
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

	private void doWaitForExecution(final CompletionService<Node<T>> completionService, boolean stopOnError) {
		int cuurentCount = 0;
		while (cuurentCount != nodesCount.get()) {
			try {
				Future<Node<T>> future = completionService.take();
				Node<T> processedNode = future.get();
				logger.debug("Processing of node {} done", processedNode.getValue());
				cuurentCount++;
				this.processedNodes.add(processedNode);				
				//System.out.println(this.executorService);
				doExecute(processedNode.getOutGoingNodes(), completionService, stopOnError);
			} catch (Exception e) {
				cuurentCount++;
				logger.error("Task interrupted", e);
			}
		}
	}

	private Callable<Node<T>> newTask(final Node<T> graphNode, boolean stopOnError) {
		if (stopOnError) {
			return new TerminatingTask(graphNode);
		} else {
			return new NonTerminatingTask(graphNode);
		}
	}

	private class TerminatingTask implements Callable<Node<T>> {
		private Node<T> node;

		public TerminatingTask(final Node<T> graphNode) {
			this.node = graphNode;
		}

		public Node<T> call() throws Exception {
			Task task = taskProvider.provid(this.node.getValue());
			task.execute();
			return this.node;
		}		
	}

	private class NonTerminatingTask implements Callable<Node<T>> {
		private Node<T> node;

		public NonTerminatingTask(Node<T> graphNode) {
			this.node = graphNode;
		}

		public Node<T> call() throws Exception {
			try {
				Task task = taskProvider.provid(this.node.getValue());
				task.execute();
			} catch(Exception ex) {
				logger.error("Exception caught, executing task :" + this.node.getValue(), ex);
			}
			return this.node;
		}
	}
}
