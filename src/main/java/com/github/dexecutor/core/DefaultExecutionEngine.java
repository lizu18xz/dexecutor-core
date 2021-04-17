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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dexecutor.core.concurrent.ExecutorCompletionService;
import com.github.dexecutor.core.concurrent.IdentifiableRunnableFuture;
import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskExecutionException;
/**
 * Default Executor, which internally operates on @ExecutorService
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public final class DefaultExecutionEngine<T, R> implements ExecutionEngine<T, R> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultExecutionEngine.class);

	private final DexecutorState<T, R> state;

	private ExecutionListener<T, R> executionListener = new QuiteExecutionListener<>();
	private final ExecutorService executorService;
	private final ExecutorCompletionService<T, ExecutionResult<T, R>> completionService;

	private ScheduledExecutorService timeoutExecutor;

	public DefaultExecutionEngine(final DexecutorState<T, R> state, final ExecutorService executorService) {
		this(state, executorService, null);
	}
	/**
	 * Creates the default instance given @ExecutorService, internally it uses @CompletionService
	 * @param state the state
	 * @param executorService Underlying execution service, where in tasks would be scheduled.
	 * @param listener to notify 
	 */
	public DefaultExecutionEngine(final DexecutorState<T, R> state, final ExecutorService executorService, ExecutionListener<T, R> listener) {
		checkNotNull(state, "State should not be null");
		checkNotNull(executorService, "Executer Service should not be null");
		this.state = state;
		this.executorService = executorService;
		this.completionService = new ExecutorCompletionService<T, ExecutionResult<T, R>>(executorService);
		if (listener != null) {
			this.executionListener = listener;
		}
	}

	@Override
	public ExecutionResult<T, R> processResult() {
		T identifier = null;
		try {
			@SuppressWarnings("unchecked")
			IdentifiableRunnableFuture<T, ExecutionResult<T, R>> future = (IdentifiableRunnableFuture<T, ExecutionResult<T, R>>) this.completionService.take();
			identifier = future.getIdentifier();
			if (future.isCancelled()) {
				ExecutionResult<T, R> result = ExecutionResult.cancelled(future.getIdentifier(), "Task cancelled");
				state.removeErrored(result);
				return result;
			} else {
				return future.get();
			}
		} catch (Exception e) {
			throw new TaskExecutionException(identifier + " Task execution ", e);
		}
	}

	@Override
	public void submit(final Task<T, R> task) {
		logger.debug("Received Task {} ", task.getId());
		Future<ExecutionResult<T, R>> future = this.completionService.submit(newCallable(task));

		if (this.timeoutExecutor != null && task.getTimeout() != null) {
			logger.trace("Task # {}, Is Timeout based", task.getId());
			addTimeOut(task, future);
		}
	}

	private void addTimeOut(Task<T, R> task, Future<ExecutionResult<T, R>> future) {

		timeoutExecutor.schedule(new Runnable() {
			public void run() {
				if (task.isCompleted()) {
					logger.trace("Task already completed {}", task);
				} else if (task.isTimedOut()) {					
					boolean result = future.cancel(true);
					logger.trace("Task timed out {}, cancelled it? : {}", task, result);
				} else {
					logger.trace("Task Not timed out {}, adding it back", task);
					addTimeOut(task, future);
				}
			}
		}, task.getTimeout().toNanos(), TimeUnit.NANOSECONDS);
	}

	private Callable<ExecutionResult<T, R>> newCallable(final Task<T, R> task) {
		return new IdentifiableCallable<T, ExecutionResult<T,R>>() {

			@Override
			public ExecutionResult<T, R> call() throws Exception {
				R r = null;
				ExecutionResult<T, R> result = null;
				try {
					task.markStart();
					r = task.execute();
					result = ExecutionResult.success(task.getId(), r);
					state.removeErrored(result);
					executionListener.onSuccess(task);
				} catch (Exception e) {
					result = ExecutionResult.errored(task.getId(), r, e.getMessage());
					state.addErrored(result);
					executionListener.onError(task, e);
					logger.error("Error Execution Task # {}", task.getId(), e);
				} finally {
					task.markEnd();
					result.setTimes(task.getStartTime(), task.getEndTime());
				}
				return result;
			}

			@Override
			public T getIdentifier() {
				return task.getId();
			}
		};
	}

	@Override
	public boolean isDistributed() {
		return false;
	}

	@Override
	public boolean isAnyTaskInError() {
		return this.state.erroredCount() > 0;
	}

	@Override
	public String toString() {
		return this.executorService.toString();
	}

	@Override
	public void setExecutionListener(ExecutionListener<T, R> listener) {
		this.executionListener = listener;		
	}

	@Override
	public void setTimeoutScheduler(ScheduledExecutorService timeoutExecutor) {
		this.timeoutExecutor = timeoutExecutor;		
	}
}
