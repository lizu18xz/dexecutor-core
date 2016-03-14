package com.nadeem.executor;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nadeem.executor.TaskProvider.Task;
import com.nadeem.executor.graph.CyclicValidator;
import com.nadeem.executor.graph.DefaultGraph;
import com.nadeem.executor.graph.Graph;
import com.nadeem.executor.graph.Graph.Node;
import com.nadeem.executor.graph.Validator;

public final class DefaultDependentTasksExecutor <T> implements DependentTasksExecutor<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDependentTasksExecutor.class);

	private ExecutorService executorService;
	private TaskProvider<T> taskProvider;
	private Graph<T> graph;

	private Validator<T> validator = new CyclicValidator<T>();
	private Collection<Node<T>> processedNodes = new CopyOnWriteArrayList<Node<T>>();

	public DefaultDependentTasksExecutor(final ExecutorService executor, final TaskProvider<T> taskProvider) {
		this.executorService = executor;
		this.taskProvider = taskProvider;
		this.graph = new DefaultGraph<T>();
	}

	public void addIndependent(final T nodeValue) {
		this.graph.addIndependent(nodeValue);
	}

	public void addDependency(final T evalFirstValue, final T evalAfterValue) {
		this.graph.addDependency(evalFirstValue, evalAfterValue);
	}

	private boolean isAlreadyProcessed(final Node<T> node) {
		return this.processedNodes.contains(node);
	}

	private boolean areAlreadyProcessed(final List<Node<T>> nodes) {
        return this.processedNodes.containsAll(nodes);
    }

	public void execute() {
		validate();
		List<Node<T>> initialNodes = this.graph.getInitialNodes();
		CompletionService<Node<T>> completionService = new ExecutorCompletionService<Node<T>>(executorService);

		long start = new Date().getTime();

		doExecute(initialNodes, completionService);
		doWaitForExecution(completionService, graph.size());

		long end = new Date().getTime();

		logger.info("Total Time taken to process {} jobs each taking 500 ms is {} ms instead of {} ms", graph.size(), end - start, 500 * this.graph.size());
		logger.info(this.processedNodes.toString());
	}

	private void validate() {
		this.validator.validate(this.graph);
	}

	private void doExecute(final Collection<Node<T>> nodes, final CompletionService<Node<T>> completionService) {
		for (Node<T> node : nodes) {
			if (shouldProcess(node) ) {
				logger.debug("Going to schedule {} node", node.value);
				completionService.submit(newTask(node));
			} else {
				logger.debug("node {} depends on {}", node.value, node.getInComingNodes());
			}
		}		
	}

	private boolean shouldProcess(final Node<T> node) {
		return !isAlreadyProcessed(node) && allIncomingNodesProcessed(node);
	}

	private boolean allIncomingNodesProcessed(final Node<T> node) {
		if (node.getInComingNodes().isEmpty() || areAlreadyProcessed(node.getInComingNodes())) {
			return true;
		}
		return false;
	}

	private void doWaitForExecution(final CompletionService<Node<T>> completionService, int totalNodes) {
		int cuurentCount = 0;
		while (cuurentCount != totalNodes) {
			try {
				Future<Node<T>> future = completionService.take();
				Node<T> processedNode = future.get();
				logger.debug("Processing of node {} done", processedNode.value);
				cuurentCount++;
				this.processedNodes.add(processedNode);				
				//System.out.println(this.executorService);
				doExecute(processedNode.getOutGoingNodes(), completionService);
			} catch (InterruptedException e) {
				logger.error("Task interrupted", e);
			} catch (ExecutionException e) {
				logger.error("Task aborted", e);
			}
		}
	}

	private Callable<Node<T>> newTask(final Node<T> graphNode) {
		return new Callable<Node<T>>() {
			public Node<T> call() throws Exception {
				Task task = taskProvider.provid(graphNode.value);
				task.execute();
				return graphNode;
			}
		};
	}
}
