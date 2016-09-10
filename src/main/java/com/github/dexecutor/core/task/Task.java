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

import com.github.dexecutor.core.DependentTasksExecutor.ExecutionBehavior;

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
	private T id;
	private ExecutionStatus status = ExecutionStatus.SUCCESS;
	private ExecutionBehavior executionBehavior = ExecutionBehavior.RETRY_ONCE_TERMINATING;

	public void setId(final T id) {
		this.id = id;
	}

	public T getId() {
		return this.id;
	}

	public void setErrored() {
		this.status = ExecutionStatus.ERRORED;
	}

	public void setSkipped() {
		this.status = ExecutionStatus.SKIPPED;
	}

	public ExecutionStatus getStatus() {
		return status;
	}

	public ExecutionBehavior getExecutionBehavior() {
		return executionBehavior;
	}

	public void setExecutionBehavior(ExecutionBehavior executionBehavior) {
		this.executionBehavior = executionBehavior;
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
	 * 
	 * @param considerExecutionError
	 */
	void setConsiderExecutionError(boolean considerExecutionError) {
		this.considerExecutionError = considerExecutionError;
	}
	/**
	 * Defines whether or not this task should be executed
	 * 
	 * @param parentResults
	 * 
	 * @return {@code true} If this task should be executed
	 * {@code false} If the task should be skipped
	 */
	public boolean shouldExecute(final ExecutionResults<T, R> parentResults) {
		return true;
	}
}