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

/**
 * Holds execution result of a node identified by id 
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class ExecutionResult <T, R> implements Serializable {

	private static final long serialVersionUID = 1L;
	private T id;
	private R result;
	private ExecutionStatus status = ExecutionStatus.SUCCESS;

	public ExecutionResult(final T id) {
		this.id = id;
	}

	public ExecutionResult(final T id, final R result) {
		this(id, result, ExecutionStatus.SUCCESS);
	}

	public ExecutionResult(final T id, final R result, final ExecutionStatus status) {
		this.id = id;
		this.result = result;
		this.status = status;
	}
	
	/**
	 * 
	 * @return the id of the executing node
	 */
	public T getId() {
		return id;
	}
	/**
	 * 
	 * @return result of execution
	 */
	public R getResult() {
		return result;
	}
	
	public void errored() {
		this.status = ExecutionStatus.ERRORED;
	}
	
	public void skipped() {
		this.status = ExecutionStatus.SKIPPED;
	}

	/**
	 * 
	 * @return {@code true} if the result is success
	 * {@code false} if the result is not success
	 */
	public boolean isSuccess() {
		return ExecutionStatus.SUCCESS.equals(this.status);
	}
	/**
	 * 
	 * @return {@code true} if the result is error
	 * {@code false} if the result is not error
	 */
	public boolean isErrored() {
		return ExecutionStatus.ERRORED.equals(this.status);
	}
	/**
	 * 
	 * @return {@code true} if the result is skipped
	 * {@code false} if the result is not skipped
	 */
	public boolean isSkipped() {
		return ExecutionStatus.SKIPPED.equals(this.status);
	}

	@Override
	public String toString() {
		return "ExecutionResult [id=" + id + ", result=" + result + ", status=" + status + "]";
	}	
}
