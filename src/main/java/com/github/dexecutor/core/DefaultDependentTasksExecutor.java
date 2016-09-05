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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dexecutor.core.graph.Dag;
import com.github.dexecutor.core.graph.DefaultDag;
import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.Traversar;
import com.github.dexecutor.core.graph.Validator;

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

	private ExecutionEngine<T, R> executionEngine;
	private TaskProvider<T, R> taskProvider;
	private Validator<T, R> validator;
	private Traversar<T, R> traversar;
	private Dag<T, R> graph;

	private Collection<Node<T, R>> processedNodes = new CopyOnWriteArrayList<Node<T, R>>();
	private AtomicInteger nodesCount = new AtomicInteger(0);
	/**
	 * Creates the Executor with bare minimum required params
	 * @param executorService
	 * @param taskProvider
	 */
	public DefaultDependentTasksExecutor(final ExecutorService executorService, final TaskProvider<T, R> taskProvider) {
		this(new DependentTasksExecutorConfig<T, R>(executorService, taskProvider));
	}
	/**
	 * Creates the Executor with Config
	 * @param config
	 */
	public DefaultDependentTasksExecutor(final DependentTasksExecutorConfig<T, R> config) {
		config.validate();
		this.executionEngine = config.getExecutorEngine();
		this.taskProvider = config.getTaskProvider();
		this.validator = config.getValidator();
		this.traversar = config.getTraversar();
		this.graph = new DefaultDag<T, R>();
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

	private boolean isAlreadyProcessed(final Node<T, R> node) {
		return this.processedNodes.contains(node);
	}

	private boolean areAlreadyProcessed(final Set<Node<T, R>> nodes) {
        return this.processedNodes.containsAll(nodes);
    }

	public void execute(final ExecutionBehavior behavior) {
		validate();

		Set<Node<T, R>> initialNodes = this.graph.getInitialNodes();

		long start = new Date().getTime();
		
		doProcessNodes(behavior, initialNodes);

		long end = new Date().getTime();

		logger.debug("Total Time taken to process {} jobs is {} ms.", graph.size(), end - start);
		logger.debug("Processed Nodes Ordering {}", this.processedNodes);
	}

	private void validate() {
		this.validator.validate(this.graph);
	}

	private void doProcessNodes(final ExecutionBehavior behavior, final Set<Node<T, R>> nodes) {
		doExecute(nodes, behavior);
		doWaitForExecution(behavior);	
	}

	private void doExecute(final Collection<Node<T, R>> nodes, final ExecutionBehavior behavior) {
		for (Node<T, R> node : nodes) {
			if (shouldProcess(node) ) {
				nodesCount.incrementAndGet();
				logger.debug("Going to schedule {} node", node.getValue());
				this.executionEngine.submit(WorkerFactory.newWorker(this.taskProvider, node, behavior));
				
			} else {
				logger.debug("node {} depends on {}", node.getValue(), node.getInComingNodes());
			}
		}		
	}

	private boolean shouldProcess(final Node<T, R> node) {
		return !this.executionEngine.isShutdown() && !isAlreadyProcessed(node) && allIncomingNodesProcessed(node);
	}

	private boolean allIncomingNodesProcessed(final Node<T, R> node) {
		if (node.getInComingNodes().isEmpty() || areAlreadyProcessed(node.getInComingNodes())) {
			return true;
		}
		return false;
	}

	private void doWaitForExecution(final ExecutionBehavior behavior) {
		int cuurentCount = 0;
		while (cuurentCount != nodesCount.get()) {
			try {
				Future<Node<T, R>> future = this.executionEngine.take();
				Node<T, R> processedNode = future.get();
				logger.debug("Processing of node {} done", processedNode.getValue());
				cuurentCount++;
				this.processedNodes.add(processedNode);
				doExecute(processedNode.getOutGoingNodes(), behavior);
			} catch (Exception e) {
				cuurentCount++;
				logger.error("Task interrupted", e);
			}
		}
	}
}
