package com.github.dexecutor.core;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.github.dexecutor.core.TaskProvider;
import com.github.dexecutor.core.TaskProvider.Task;
import com.github.dexecutor.core.graph.Node;

class TerminatingTask<T extends Comparable<T>, R> implements Callable<Node<T, R>> , Serializable {

	private static final long serialVersionUID = 1L;
	private Node<T, R> node;
	private TaskProvider<T, R> taskProvider;

	public TerminatingTask(final TaskProvider<T, R> taskProvider, final Node<T, R> graphNode) {
		this.taskProvider = taskProvider;
		this.node = graphNode;
	}

	public Node<T, R> call() throws Exception {
		Task<T, R> task = new ExecutorTask<T, R>(node, this.taskProvider.provid(node.getValue()));
		task.setConsiderExecutionError(true);
		R result = task.execute();
		this.node.setResult(result);
		return this.node;
	}	

}