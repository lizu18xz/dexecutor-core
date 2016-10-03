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
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class DefaultExecutionEngine<T extends Comparable<T>, R> implements ExecutionEngine<T, R> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultExecutionEngine.class);

	private AtomicBoolean errored = new AtomicBoolean(false);

	private final ExecutorService executorService;
	private final CompletionService<ExecutionResult<T, R>> completionService;
	/**
	 * Creates the default instance given @ExecutorService, internally it uses @CompletionService
	 * @param executorService
	 */
	public DefaultExecutionEngine(final ExecutorService executorService) {
		checkNotNull(executorService, "Executer Service should not be null");
		this.executorService = executorService;
		this.completionService = new ExecutorCompletionService<ExecutionResult<T, R>>(executorService);
	}

	@Override
	public ExecutionResult<T, R> processResult() {
		try {
			return this.completionService.take().get();
		} catch (Exception e) {
			throw new TaskExecutionException("Task execution ", e);
		}
	}

	private Callable<ExecutionResult<T, R>> newCallable(final Task<T, R> task) {
		return new Callable<ExecutionResult<T,R>>() {

			@Override
			public ExecutionResult<T, R> call() throws Exception {
				R r = null;
				try {
					r = task.execute();
				} catch (Exception e) {
					task.setErrored();
					errored.set(true);
					logger.error("Error Execution Task # {}", task.getId(), e);
				}
				return new ExecutionResult<T, R>(task.getId(), r, task.getStatus());
			}
		};
	}

	@Override
	public void submit(final Task<T, R> task) {
		this.completionService.submit(newCallable(task));		
	}

	@Override
	public String toString() {
		return this.executorService.toString();
	}

	@Override
	public boolean isDistributed() {
		return false;
	}

	@Override
	public boolean isAnyTaskInError() {
		return this.errored.get();
	}
}
