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
import java.time.LocalDateTime;

/**
 * Holds execution result of a node identified by id
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 *            Type of Node/Task ID
 * @param <R>
 *            Type of Node/Task result
 */
public final class ExecutionResult<T, R> implements Serializable {

	private static final String EMPTY = "";
	private static final long serialVersionUID = 1L;
	private T id;
	private R result;
	private ExecutionStatus status = ExecutionStatus.SUCCESS;
	private String message;
	
	/**
	 * start time for the task
	 */
	private LocalDateTime startTime;
	/**
	 * End time for the task
	 */
	private LocalDateTime endTime;

	public ExecutionResult(final T id, final R result, final ExecutionStatus status) {
		this(id, result, status, EMPTY);
	}

	private ExecutionResult(final T id, final R result, final ExecutionStatus status, final String msg) {
		this.id = id;
		this.result = result;
		this.status = status;
		this.message = msg;
	}

	public static <T, R> ExecutionResult<T, R> success(final T id, final R result) {
		return new ExecutionResult<T, R>(id, result, ExecutionStatus.SUCCESS, EMPTY);
	}

	public static <T, R> ExecutionResult<T, R> errored(final T id, final R result, final String msg) {
		return new ExecutionResult<T, R>(id, result, ExecutionStatus.ERRORED, msg);
	}
	
	public static <T, R> ExecutionResult<T, R> cancelled(final T id, final String msg) {
		return new ExecutionResult<T, R>(id, null, ExecutionStatus.CANCELLED, msg);
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

	/**
	 * @return the status of the execution
	 */
	public ExecutionStatus getStatus() {
		return status;
	}

	/**
	 * Marks the execution result as errored
	 */
	public void errored() {
		this.status = ExecutionStatus.ERRORED;
	}

	/**
	 * Marks the execution result as skipped
	 */
	public void skipped() {
		this.status = ExecutionStatus.SKIPPED;
	}

	/**
	 * 
	 * @return {@code true} if the result is success {@code false} if the result
	 *         is not success
	 */
	public boolean isSuccess() {
		return ExecutionStatus.SUCCESS.equals(this.status);
	}

	/**
	 * 
	 * @return {@code true} if the result is error {@code false} if the result
	 *         is not error
	 */
	public boolean isErrored() {
		return ExecutionStatus.ERRORED.equals(this.status);
	}
	
	public boolean isCancelled() {
		return ExecutionStatus.CANCELLED.equals(this.status);
	}

	/**
	 * 
	 * @return {@code true} if the result is skipped {@code false} if the result
	 *         is not skipped
	 */
	public boolean isSkipped() {
		return ExecutionStatus.SKIPPED.equals(this.status);
	}
	
	/**
	 * 
	 * @return the execution message
	 */
	public String getMessage() {
		return message;
	}

	public void setTimes(LocalDateTime startTime, LocalDateTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		ExecutionResult<T, R> other = (ExecutionResult<T, R>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExecutionResult [id=" + id + ", result=" + result + ", status=" + status + ", message=" + message
				+ ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
}
