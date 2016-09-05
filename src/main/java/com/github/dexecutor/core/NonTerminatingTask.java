package com.github.dexecutor.core;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dexecutor.core.TaskProvider;
import com.github.dexecutor.core.TaskProvider.Task;
import com.github.dexecutor.core.graph.Node;

class NonTerminatingTask <T extends Comparable<T>, R> implements Callable<Node<T, R>> , Serializable {
	private static final Logger logger = LoggerFactory.getLogger(NonTerminatingTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node<T, R> node;
	private TaskProvider<T, R> taskProvider;

	public NonTerminatingTask(final TaskProvider<T, R> taskProvider, final Node<T, R> graphNode) {
		this.taskProvider = taskProvider;
		this.node = graphNode;
	}
	public Node<T, R> call() throws Exception {
		try {
			Task<T, R> task = new ExecutorTask<T, R>(node, this.taskProvider.provid(node.getValue()));
			task.setConsiderExecutionError(false);
			task.execute();
		} catch(Exception ex) {
			logger.error("Exception caught, executing node # " + this.node.getValue(), ex);
			this.node.setErrored();
		}
		return this.node;
	}
}
