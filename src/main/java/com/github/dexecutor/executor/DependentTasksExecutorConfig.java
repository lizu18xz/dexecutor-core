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

package com.github.dexecutor.executor;

import static com.github.dexecutor.executor.support.Preconditions.checkNotNull;

import java.util.concurrent.ExecutorService;

import com.github.dexecutor.executor.graph.CyclicValidator;
import com.github.dexecutor.executor.graph.LevelOrderTraversar;
import com.github.dexecutor.executor.graph.Traversar;
import com.github.dexecutor.executor.graph.Validator;

/**
 * <p>Configuration Object for Dexecutor framework. At a minimum it needs {@code ExecutorService} and {@code TaskProvider}, rest are optional and takes default values</p>
 * <p>This provides way to hook in your own {@code Validator} and {@code Traversar}</p>
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class DependentTasksExecutorConfig<T extends Comparable<T>, R> {
	/**
	 * executor is the main platform on which tasks are executed
	 */
	private ExecutionEngine<T, R> executionEngine;

	/**
	 * When it comes to task execution, task provider would be consulted to provide task objects for execution
	 */
	private TaskProvider<T, R> taskProvider;
	/**
	 * Validator for validating the consturcted graph, defaults to detecting Cyclic checks
	 */
	private Validator<T, R> validator = new CyclicValidator<T, R>();
	/**
	 * Traversar used to traverse the graph while printing it on a Writer
	 */
	private Traversar<T, R> traversar = new LevelOrderTraversar<T, R>();
	/**
	 * Construct the object with mandatory params, rest are optional
	 * @param executorService @ExecutorService
	 * @param taskProvider @TaskProvider
	 */
	public DependentTasksExecutorConfig(final ExecutorService executorService, final TaskProvider<T, R> taskProvider) {
		checkNotNull(executorService, "Executer Service should not be null");
		checkNotNull(taskProvider, "Task Provider should not be null");
		this.executionEngine = new DefaultExecutionEngine<>(executorService);
		this.taskProvider = taskProvider;
	}
	
	/**
	 * Construct the object with mandatory params, rest are optional
	 * @param executor  @ExecutionEngine
	 * @param taskProvider @TaskProvider
	 */
	public DependentTasksExecutorConfig(final ExecutionEngine<T, R> executor, final TaskProvider<T, R> taskProvider) {
		this.executionEngine = executor;
		this.taskProvider = taskProvider;
	}

	void validate() {
		checkNotNull(this.executionEngine, "Execution Engine should not be null");
		checkNotNull(this.taskProvider, "Task Provider should not be null");
		checkNotNull(this.validator, "Validator should not be null");
		checkNotNull(this.traversar, "Traversar should not be null");
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
	 * @param validator
	 */
	public void setValidator(final Validator<T, R> validator) {
		this.validator = validator;
	}
	Traversar<T, R> getTraversar() {
		return this.traversar;
	}
	/**
	 * Change the traversar to that of specified
	 * @param traversar
	 */
	public void setTraversar(final Traversar<T, R> traversar) {
		this.traversar = traversar;
	}
}
