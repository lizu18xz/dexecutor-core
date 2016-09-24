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

package com.github.dexecutor.core;

import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dexecutor.core.graph.Dag;
import com.github.dexecutor.core.graph.DefaultDag;
import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.Traversar;
import com.github.dexecutor.core.graph.Validator;
import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.ExecutionResults;
import com.github.dexecutor.core.task.ExecutionStatus;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskFactory;
import com.github.dexecutor.core.task.TaskProvider;

/**
 * Default implementation of @DependentTasksExecutor
 * 
 * @author Nadeem Mohammad
 * 
 * @since 0.0.1
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public final class DefaultDependentTasksExecutor <T extends Comparable<T>, R> implements DependentTasksExecutor<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDependentTasksExecutor.class);
	
	private TaskProvider<T, R> taskProvider;
	private ExecutionEngine<T, R> executionEngine;
	private Validator<T, R> validator;
	private Traversar<T, R> traversar;
	private Dag<T, R> graph;

	private Collection<Node<T, R>> processedNodes = new CopyOnWriteArrayList<Node<T, R>>();
	private AtomicInteger nodesCount = new AtomicInteger(0);

	public DefaultDependentTasksExecutor(ExecutionEngine<T, R> executionEngine, TaskProvider<T, R> taskProvider) {
		this(new DependentTasksExecutorConfig<>(executionEngine, taskProvider));
	}

	/**
	 * Creates the Executor with Config
	 * @param config
	 */
	public DefaultDependentTasksExecutor(final DependentTasksExecutorConfig<T, R> config) {
		config.validate();
		this.executionEngine = config.getExecutorEngine();
		this.validator = config.getValidator();
		this.traversar = config.getTraversar();
		this.graph = new DefaultDag<T, R>();
		this.taskProvider = config.getTaskProvider();
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
			for (Node<T, R> node : this.graph.getLeafNodes()) {
				addDependency(node.getValue(), nodeValue);
			}
		}
	}

	@Override
	public void addAsDependencyToAllInitialNodes(final T nodeValue) {
		if (this.graph.size() == 0) {
			addIndependent(nodeValue);
		} else {
			for (Node<T, R> node : this.graph.getInitialNodes()) {
				addDependency(nodeValue, node.getValue());
			}
		}		
	}

	public void execute(final ExecutionConfig config) {
		validate();

		Set<Node<T, R>> initialNodes = this.graph.getInitialNodes();

		long start = new Date().getTime();

		doProcessNodes(config, initialNodes);

		long end = new Date().getTime();

		logger.debug("Total Time taken to process {} jobs is {} ms.", graph.size(), end - start);
		logger.debug("Processed Nodes Ordering {}", this.processedNodes);
	}

	private void validate() {
		this.validator.validate(this.graph);
	}

	private void doProcessNodes(final ExecutionConfig config, final Set<Node<T, R>> nodes) {
		doExecute(nodes, config);
		doWaitForExecution(config);	
	}

	private void doExecute(final Collection<Node<T, R>> nodes, final ExecutionConfig config) {
		for (Node<T, R> node : nodes) {
			if (shouldProcess(node) ) {
				nodesCount.incrementAndGet();
				Task<T, R> task = newTask(config, node);
				if (shouldExecute(node, task)) {
					logger.debug("Going to schedule {} node", node.getValue());
					this.executionEngine.submit(task);
				} else {
					node.setSkipped();
					task.setSkipped();
					logger.debug("Execution Skipped for node # {} ", node.getValue());
					this.processedNodes.add(node);
					doExecute(node.getOutGoingNodes(), config);
				}
			} else {
				logger.debug("node {} depends on {}", node.getValue(), node.getInComingNodes());
			}
		}
	}

	private Task<T, R> newTask(final ExecutionConfig config, Node<T, R> node) {
		Task<T, R> task = this.taskProvider.provideTask(node.getValue());
		task.setId(node.getValue());
		task.setExecutionConfig(config);
		return TaskFactory.newWorker(task);
	}

	private boolean shouldExecute(Node<T, R> node, Task<T, R> task) {
		if (task.shouldExecute(parentResults(node))) {
			return true;
		}
		return false;
	}

	private boolean shouldProcess(final Node<T, R> node) {
		return !isAlreadyProcessed(node) && allIncomingNodesProcessed(node);
	}

	private boolean allIncomingNodesProcessed(final Node<T, R> node) {
		if (node.getInComingNodes().isEmpty() || areAlreadyProcessed(node.getInComingNodes())) {
			return true;
		}
		return false;
	}

	private boolean isAlreadyProcessed(final Node<T, R> node) {
		return this.processedNodes.contains(node);
	}

	private boolean areAlreadyProcessed(final Set<Node<T, R>> nodes) {
        return this.processedNodes.containsAll(nodes);
    }

	private ExecutionResults<T, R> parentResults(final Node<T, R> node) {
		ExecutionResults<T, R> parentResult = new ExecutionResults<T, R>();
		for (Node<T, R> pNode : node.getInComingNodes()) {
			parentResult.add(new ExecutionResult<T, R>(pNode.getValue(), pNode.getResult(), status(pNode)));
		}
		return parentResult;
	}

	private ExecutionStatus status(final Node<T, R> node) {
		ExecutionStatus status = ExecutionStatus.SUCCESS;
		if (node.isErrored()) {
			status = ExecutionStatus.ERRORED;
		} else if (node.isSkipped()) {
			status = ExecutionStatus.SKIPPED;
		}
		return status;
	}

	private void doWaitForExecution(final ExecutionConfig config) {
		int cuurentCount = 0;
		while (cuurentCount != nodesCount.get()) {
			try {
				ExecutionResult<T, R> executionResult = this.executionEngine.processResult();
				logger.debug("Processing of node {} done", executionResult.getId());
				cuurentCount++;
				Node<T, R> processedNode = this.graph.get(executionResult.getId());
				updateNode(executionResult, processedNode);
				this.processedNodes.add(processedNode);
				doExecute(processedNode.getOutGoingNodes(), config);
			} catch (Exception e) {
				cuurentCount++;
				logger.error("Task interrupted", e);
			}
		}
	}

	private void updateNode(final ExecutionResult<T, R> executionResult, final Node<T, R> processedNode) {
		processedNode.setResult(executionResult.getResult());
		if(executionResult.isErrored()) {
			processedNode.setErrored();
		} else if(executionResult.isSkipped()) {
			processedNode.setSkipped();
		} else {
			processedNode.setSuccess();
		}
	}
}
