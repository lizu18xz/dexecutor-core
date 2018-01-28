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

import static com.github.dexecutor.core.support.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import com.github.dexecutor.core.graph.CyclicValidator;
import com.github.dexecutor.core.graph.Validator;
import com.github.dexecutor.core.task.TaskProvider;

/**
 * <p>Configuration Object for Dexecutor framework. At a minimum it needs {@code ExecutorService} and {@code TaskProvider}, rest are optional and takes default values</p>
 * <p>This provides way to hook in your own {@code DexecutorState} and {@code Validator} </p>
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class DexecutorConfig<T, R> {
	
	/**
	 * Number of threads that should handle the immediate retry.
	 */
	private int immediateRetryPoolThreadsCount = 1;
	
	/**
	 * Number of threads that should handle the immediate retry.
	 */
	private int scheduledRetryPoolThreadsCount = 1;

	/**
	 * executor is the main platform on which tasks are executed
	 */
	private ExecutionEngine<T, R> executionEngine;

	/**
	 * When it comes to task execution, task provider would be consulted to provide task objects for execution
	 */
	private TaskProvider<T, R> taskProvider;
	/**
	 * Validator for validating the constructed graph, defaults to detecting Cyclic checks
	 */
	private Validator<T, R> validator = new CyclicValidator<T, R>();

	private DexecutorState<T, R> dexecutorState = new DefaultDexecutorState<T, R>();

	/**
	 * Construct the object with mandatory params, rest are optional
	 * @param executorService provided executor service
	 * @param taskProvider provided task provider
	 */
	public DexecutorConfig(final ExecutorService executorService, final TaskProvider<T, R> taskProvider) {
		this(executorService, taskProvider, null);
	}
	
	public DexecutorConfig(final ExecutorService executorService, final TaskProvider<T, R> taskProvider, final ExecutionListener<T, R> listener) {
		checkNotNull(executorService, "Executer Service should not be null");
		checkNotNull(taskProvider, "Task Provider should not be null");
		this.executionEngine = new DefaultExecutionEngine<>(this.dexecutorState, executorService, listener);
		this.taskProvider = taskProvider;
	}

	/**
	 * Construct the object with mandatory params, rest are optional
	 * @param executionEngine  provided execution Engine
	 * @param taskProvider provided task provider
	 */
	public DexecutorConfig(final ExecutionEngine<T, R> executionEngine, final TaskProvider<T, R> taskProvider) {
		this(new DefaultDexecutorState<>(), executionEngine, taskProvider);
	}

	public DexecutorConfig(final DexecutorState<T, R> dexecutorState, final ExecutionEngine<T, R> executionEngine, final TaskProvider<T, R> taskProvider) {
		checkNotNull(executionEngine, "Execution Engine should not be null");
		checkNotNull(taskProvider, "Task Provider should not be null");
		checkNotNull(dexecutorState, "Dexecutor State should not be null");
		this.executionEngine = executionEngine;
		this.taskProvider = taskProvider;
		this.dexecutorState = dexecutorState;
	}

	void validate() {
		checkNotNull(this.executionEngine, "Execution Engine should not be null");
		checkNotNull(this.taskProvider, "Task Provider should not be null");
		checkNotNull(this.validator, "Validator should not be null");
		checkNotNull(this.dexecutorState, "Dexecutor State should not be null");		
	}

	ExecutionEngine<T, R> getExecutorEngine() {
		return this.executionEngine;
	}

	TaskProvider<T, R> getTaskProvider() {
		return this.taskProvider;
	}

	Validator<T, R> getValidator() {
		return this.validator;
	}
	/**
	 * change the validator to that of specified
	 * @param validator the validator
	 */
	public void setValidator(final Validator<T, R> validator) {
		this.validator = validator;
	}

	/**
	 * 
	 * @return the immediate retry thread pool count
	 */
	public int getImmediateRetryPoolThreadsCount() {
		return immediateRetryPoolThreadsCount;
	}
	/**
	 * sets the immediate retry thread pool size to that of specified
	 * @param immediateRetryPoolThreadsCount Number of threads that should process retry immediately 
	 */
	public void setImmediateRetryPoolThreadsCount(int immediateRetryPoolThreadsCount) {
		this.immediateRetryPoolThreadsCount = immediateRetryPoolThreadsCount;
	}
	/**
	 * 
	 * @return the scheduled retry thread pool size
	 */
	public int getScheduledRetryPoolThreadsCount() {
		return scheduledRetryPoolThreadsCount;
	}
	/**
	 * sets the scheduled thread pool size to that of specified
	 * @param scheduledRetryPoolThreadsCount Number of threads that should process retry immediately 
	 */
	public void setScheduledRetryPoolThreadsCount(int scheduledRetryPoolThreadsCount) {
		this.scheduledRetryPoolThreadsCount = scheduledRetryPoolThreadsCount;
	}
	/**
	 * 
	 * @return the dexecutor state
	 */
	public DexecutorState<T, R> getDexecutorState() {
		return this.dexecutorState;
	}
	
	/**
	 * 
	 * @param dexecutorState to set to
	 */
	public void setDexecutorState(final DexecutorState<T, R> dexecutorState) {
		this.dexecutorState = dexecutorState;
	}

	public void setExecutionListener(ExecutionListener<T, R> listener) {
		if (listener != null) {
			this.executionEngine.setExecutionListener(listener);
		}
	}
}
