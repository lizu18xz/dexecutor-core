/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dexecutor.core.task;

import java.io.Serializable;

import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.NodeProvider;

/**
 * Represent a unit of execution in Dexecutor framework
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public abstract class Task<T, R> implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * parent results of the task being executed
	 */
	private ExecutionResults<T, R> parentResults;
	/**
	 * Node Provider
	 */
	private NodeProvider<T, R> nodeProvider;
	/**
	 * id of the task, this would be same as that of {@code Node} id
	 */
	private T id;

	/**
	 * 
	 * @param nodeProvider
	 */
	public void setNodeProvider(NodeProvider<T, R> nodeProvider) {
		this.nodeProvider = nodeProvider;
	}

	protected ExecutionResult<T, R> getResult(T id) {
		if (this.nodeProvider == null) {
			return null;
		}
		Node<T, R> node = this.nodeProvider.getGraphNode(id);
		if (node != null ) {
			return new ExecutionResult<T, R>(node.getValue(), node.getResult(), status(node));
		}
		return null;
	}

	public ExecutionStatus status(final Node<T, R> node) {
		ExecutionStatus status = ExecutionStatus.SUCCESS;
		if (node.isErrored()) {
			status = ExecutionStatus.ERRORED;
		} else if (node.isSkipped()) {
			status = ExecutionStatus.SKIPPED;
		}
		return status;
	}

	/**
	 * Sets the new id
	 * @param id the task id
	 */
	public void setId(final T id) {
		this.id = id;
	}
	/**
	 * 
	 * @return id of the task
	 */
	public T getId() {
		return this.id;
	}

	/**
	 * 	
	 * @return ExecutionResults
	 */
	public ExecutionResults<T, R> getParentResults() {
		return parentResults;
	}
	/**
	 * sets the parent results
	 * @param parentResults
	 */
	public void setParentResults(ExecutionResults<T, R> parentResults) {
		this.parentResults = parentResults;
	}
	/**
	 * Framework would call this method, when it comes for tasks to be executed.
	 * @return the result of task execution
	 */
	public abstract R execute();
	/**
	 * When using retry behavior, execution error should not be considered until the last retry, this would define when execution error should be considered
	 */
	private boolean considerExecutionError = true;
	/**
	 * 
	 * @return whether execution error should be considered or not
	 */
	public boolean shouldConsiderExecutionError() {
		return this.considerExecutionError;
	}
	/**
	 * sets whether execution errors should be considered or not?
	 * 
	 * @param considerExecutionError the new value
	 */
	public void setConsiderExecutionError(boolean considerExecutionError) {
		this.considerExecutionError = considerExecutionError;
	}
	/**
	 * Defines whether or not this task should be executed
	 * 
	 * @param parentResults parent execution results
	 * 
	 * @return {@code true} If this task should be executed
	 * {@code false} If the task should be skipped
	 */
	public boolean shouldExecute(final ExecutionResults<T, R> parentResults) {
		return true;
	}
}