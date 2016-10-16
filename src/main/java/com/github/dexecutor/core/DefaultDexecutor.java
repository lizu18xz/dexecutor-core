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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Default implementation of @Dexecutor
 * 
 * @author Nadeem Mohammad
 * 
 * @since 0.0.1
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public final class DefaultDexecutor <T extends Comparable<T>, R> implements Dexecutor<T> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDexecutor.class);

	private final Validator<T, R> validator;
	private final Traversar<T, R> traversar;
	private final TaskProvider<T, R> taskProvider;
	private final ExecutionEngine<T, R> executionEngine;
	private final ExecutorService immediatelyRetryExecutor;
	private final ScheduledExecutorService scheduledRetryExecutor;

	private final DexecutorState<T, R> state;

	public DefaultDexecutor(final ExecutionEngine<T, R> executionEngine, final TaskProvider<T, R> taskProvider) {
		this(new DexecutorConfig<>(executionEngine, taskProvider));
	}

	/**
	 * Creates the Executor with Config
	 * @param config
	 */
	public DefaultDexecutor(final DexecutorConfig<T, R> config) {
		config.validate();

		this.immediatelyRetryExecutor = Executors.newFixedThreadPool(config.getImmediateRetryPoolThreadsCount());
		this.scheduledRetryExecutor = Executors.newScheduledThreadPool(config.getScheduledRetryPoolThreadsCount());

		this.executionEngine = config.getExecutorEngine();
		this.validator = config.getValidator();
		this.traversar = config.getTraversar();
		this.taskProvider = config.getTaskProvider();
		this.state = config.getDexecutorState();
	}

	public void print(final Writer writer) {
		this.state.print(this.traversar, writer);
	}

	public void addIndependent(final T nodeValue) {
		checkValidPhase();
		this.state.addIndependent(nodeValue);
	}

	public void addDependency(final T evalFirstNode, final T evalLaterNode) {
		checkValidPhase();
		this.state.addDependency(evalFirstNode, evalLaterNode);
	}

	public void addAsDependentOnAllLeafNodes(final T nodeValue) {
		checkValidPhase();
		this.state.addAsDependentOnAllLeafNodes(nodeValue);		
	}

	@Override
	public void addAsDependencyToAllInitialNodes(final T nodeValue) {
		checkValidPhase();
		this.state.addAsDependencyToAllInitialNodes(nodeValue);				
	}

	@Override
	public void recoverExecution(final ExecutionConfig config) {
		logger.debug("Recovering Dexecutor from Phase : {}" , this.state.getCurrentPhase());
		if (Phase.RUNNING.equals(this.state.getCurrentPhase())) {
			doWaitForExecution(config);
			//TODO: Add further logic to recover non processed nodes.
		}		
	}

	public void execute(final ExecutionConfig config) {
		validate(config);

		this.state.setCurrentPhase(Phase.RUNNING);

		Set<Node<T, R>> initialNodes = this.state.getInitialNodes();

		long start = new Date().getTime();

		doProcessNodes(config, initialNodes);
		shutdownExecutors();

		long end = new Date().getTime();

		this.state.setCurrentPhase(Phase.TERMINATED);

		logger.debug("Total Time taken to process {} jobs is {} ms.", this.state.graphSize(), end - start);
		logger.debug("Processed Nodes Ordering {}", this.state.getProcessedNodes());
	}

	private void shutdownExecutors() {
		this.immediatelyRetryExecutor.shutdown();
		this.scheduledRetryExecutor.shutdown();
		try {
			this.immediatelyRetryExecutor.awaitTermination(1, TimeUnit.NANOSECONDS);
			this.scheduledRetryExecutor.awaitTermination(1, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			logger.error("Error Shuting down Executor", e);
		}		
	}

	private void validate(final ExecutionConfig config) {
		checkValidPhase();
		config.validate();
		this.state.validate(this.validator);
	}

	private void checkValidPhase() {
		throwExceptionIfTerminated();
		throwExceptionIfRunning();
	}

	private void throwExceptionIfRunning() {
		if (Phase.RUNNING.equals(this.state.getCurrentPhase())) {
			throw new IllegalStateException("Dexecutor is already running!");
		}
	}

	private void throwExceptionIfTerminated() {
		if (Phase.TERMINATED.equals(this.state.getCurrentPhase())) {
			throw new IllegalStateException("Dexecutor has been terminated!");
		}
	}

	private void doProcessNodes(final ExecutionConfig config, final Set<Node<T, R>> nodes) {
		doExecute(nodes, config);
		doWaitForExecution(config);	
	}

	private void doExecute(final Collection<Node<T, R>> nodes, final ExecutionConfig config) {
		for (Node<T, R> node : nodes) {
			forceStopIfRequired();
			if (this.state.shouldProcess(node)) {				
				Task<T, R> task = newTask(config, node);
				if (shouldExecute(node, task)) {					
					state.incrementUnProcessedNodesCount();
					logger.debug("Going to schedule {} node", node.getValue());
					this.executionEngine.submit(task);
				} else {
					node.setSkipped();
					logger.debug("Execution Skipped for node # {} ", node.getValue());
					this.state.markProcessingDone(node);
					doExecute(node.getOutGoingNodes(), config);
				}
			} else {
				logger.debug("node {} depends on {}", node.getValue(), node.getInComingNodes());
			}
		}
	}

	private boolean shouldExecute(final Node<T, R> node, final Task<T, R> task) {
		if (task.shouldExecute(parentResults(node))) {
			return true;
		}
		return false;
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
		while (state.getUnProcessedNodesCount() > 0) {
			forceStopIfRequired();
			ExecutionResult<T, R> executionResult = this.executionEngine.processResult();
			state.decrementUnProcessedNodesCount();
			logger.debug("Processing of node {} done, with status {}", executionResult.getId(), executionResult.getStatus());

			final Node<T, R> processedNode = this.state.getGraphNode(executionResult.getId());
			updateNode(executionResult, processedNode);
			this.state.markProcessingDone(processedNode);

			if (executionResult.isSuccess() && !this.executionEngine.isAnyTaskInError() && this.state.isDiscontinuedNodesNotEmpty()) {
				Collection<Node<T, R>> recover = new HashSet<>(this.state.getDiscontinuedNodes());	
				this.state.markDiscontinuedNodesProcessed();
				doExecute(recover, config);
			}

			if (config.isNonTerminating() ||  (!this.executionEngine.isAnyTaskInError())) {
				doExecute(processedNode.getOutGoingNodes(), config);				
			} else if (this.executionEngine.isAnyTaskInError() && executionResult.isSuccess()) { 
				this.state.processAfterNoError(processedNode.getOutGoingNodes());
			} else if (shouldDoImmediateRetry(config, executionResult, processedNode)) {
				logger.debug("Submitting for Immediate retry, node {}", executionResult.getId());
				submitForImmediateRetry(config, processedNode);
			} else if (shouldScheduleRetry(config, executionResult, processedNode)) {
				logger.debug("Submitting for Scheduled retry, node {}", executionResult.getId());
				submitForScheduledRetry(config, processedNode);
			}
		}
	}

	private boolean shouldScheduleRetry(final ExecutionConfig config, final ExecutionResult<T, R> executionResult,
			final Node<T, R> processedNode) {
		return executionResult.isErrored() && config.isScheduledRetrying() && config.shouldRetry(getExecutionCount(processedNode));
	}

	private boolean shouldDoImmediateRetry(final ExecutionConfig config, final ExecutionResult<T, R> executionResult,
			final Node<T, R> processedNode) {
		return executionResult.isErrored() && config.isImmediatelyRetrying() && config.shouldRetry(getExecutionCount(processedNode));
	}

	private void submitForImmediateRetry(final ExecutionConfig config, final Node<T, R> node) {
		Task<T, R> task = newTask(config, node);		
		this.immediatelyRetryExecutor.submit(retryingTask(config, task));
	}

	private void submitForScheduledRetry(ExecutionConfig config, Node<T, R> node) {
		Task<T, R> task = newTask(config, node);
		this.scheduledRetryExecutor.schedule(retryingTask(config, task), config.getRetryDelay().getDuration(), config.getRetryDelay().getTimeUnit());
	}

	private Task<T, R> newTask(final ExecutionConfig config, final Node<T, R> node) {
		Task<T, R> task = this.taskProvider.provideTask(node.getValue());
		task.setId(node.getValue());
		updateConsiderExecutionStatus(config, task);
		return TaskFactory.newWorker(task);
	}

	private void updateConsiderExecutionStatus(final ExecutionConfig config, final Task<T, R> task) {
		if (config.isImmediatelyRetrying() || config.isScheduledRetrying()) {
			Node<T, R> node = this.state.getGraphNode(task.getId());
			Integer currentCount = getExecutionCount(node);
			if (currentCount < config.getRetryCount()) {
				task.setConsiderExecutionError(false);
			} else {
				task.setConsiderExecutionError(true);
			}
		}
	}

	private Runnable retryingTask(final ExecutionConfig config, final Task<T, R> task) {
		this.state.incrementUnProcessedNodesCount();
		return new Runnable() {
			@Override
			public void run() {
				executionEngine.submit(task);
			}
		};
	}

	private void updateExecutionCount(final Node<T, R> node) {
		Integer count = getExecutionCount(node);
		count++;
		node.setData(count);
	}

	private Integer getExecutionCount(final Node<T, R> node) {
		Integer count = (Integer) node.getData();
		if (count == null) {
			count = 0;
		}
		return count;
	}

	private void updateNode(final ExecutionResult<T, R> executionResult, final Node<T, R> processedNode) {
		updateExecutionCount(processedNode);
		processedNode.setResult(executionResult.getResult());
		if(executionResult.isErrored()) {
			processedNode.setErrored();
		} else {
			processedNode.setSuccess();
		}
	}

	private void forceStopIfRequired() {
		if (!shouldContinueProcessingNodes()) {
			this.immediatelyRetryExecutor.shutdownNow();
			this.scheduledRetryExecutor.shutdownNow();
			throw new IllegalStateException("Forced to Stop the instance of Dexecutor!");
		}		
	}

	protected boolean shouldContinueProcessingNodes() {
		return true;
	}
}
