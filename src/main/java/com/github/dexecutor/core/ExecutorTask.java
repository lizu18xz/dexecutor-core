package com.github.dexecutor.core;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dexecutor.core.TaskProvider.Task;
import com.github.dexecutor.core.graph.Node;

class ExecutorTask<T,R> extends Task<T, R> implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(ExecutorTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Task<T, R> task;
	private final Node<T, R> node;
	private int retryCount = 0;

	public ExecutorTask(final Node<T, R> node, final Task<T, R> task) {
		this.task = task;
		this.node = node;
	}

	public R execute() {
		R result = null;
		if (shouldExecute(parentResults())) {
			logger.debug("{} Node # {}", msg(this.retryCount), this.taskId());
			this.retryCount ++;
			result = this.task.execute();
			this.node.setSuccess();
			this.node.setResult(result);
			logger.debug("Node # {}, Execution Done!", this.taskId());
		} else {
			logger.debug("Execution Skipped for node # {} ", this.taskId());
			this.node.setSkipped();
		}			
		return result;
	}

	@Override
	public boolean shouldExecute(final ExecutionResults<T, R> parentResults) {
		return task.shouldExecute(parentResults);
	}
	
	private ExecutionResults<T, R> parentResults() {
		ExecutionResults<T, R> result = new ExecutionResults<T, R>();
		for (Node<T, R> in : this.node.getInComingNodes()) {
			result.add(new ExecutionResult<T, R>(in.getValue(), in.getResult(), status(in)));
		}
		return result;
	}

	private ExecutionStatus status(final Node<T, R> incomingNode) {
		if (incomingNode.isSuccess()) {
			return ExecutionStatus.SUCCESS;
		} else if (incomingNode.isErrored()) {
			return ExecutionStatus.ERRORED;
		}
		return ExecutionStatus.SKIPPED;
	}

	private T taskId() {
		return this.node.getValue();
	}

	private String msg(int retryCount) {
		return retryCount > 0 ? "Retrying(" + retryCount+ ") " : "Executing";
	}

	@Override
	void setConsiderExecutionError(boolean considerExecutionError) {
		this.task.setConsiderExecutionError(considerExecutionError);
	}
}