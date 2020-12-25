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

import com.github.dexecutor.core.graph.NodeProvider;

/**
 * Class to delegate responsibilities to underlying @Task
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
abstract class AbstractDelegatingTask<T, R> extends Task<T, R> {

	private static final long serialVersionUID = 1L;

	private Task<T, R> task;
	
	public AbstractDelegatingTask(final Task<T, R> task) {
		this.task = task;
	}
	
	protected Task<T, R> getTargetTask() {
		return this.task;
	}
	@Override
	public void setId(final T id) {
		this.task.setId(id);
	}
	@Override
	public T getId() {
		return this.task.getId();
	}
	@Override
	public boolean shouldConsiderExecutionError() {
		return this.task.shouldConsiderExecutionError();
	}
	@Override
	public void setConsiderExecutionError(boolean considerExecutionError) {
		this.task.setConsiderExecutionError(considerExecutionError);
	}
	@Override
	public boolean shouldExecute(final ExecutionResults<T, R> parentResults) {
		return this.task.shouldExecute(parentResults);
	}
	
	@Override
	public void setParentResults(ExecutionResults<T, R> parentResults) {
		this.task.setParentResults(parentResults);
	}
	
	@Override
	public ExecutionResults<T, R> getParentResults() {
		return this.task.getParentResults();
	}
	
	@Override
	public void setNodeProvider(NodeProvider<T, R> nodeProvider) {
		this.task.setNodeProvider(nodeProvider);
	}
}
