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
import java.util.ArrayList;
import java.util.List;
/**
 * Wrapper class around @ExecutionResult
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public final class ExecutionResults<T, R> implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ExecutionResult<T, R>> results = new ArrayList<ExecutionResult<T, R>>();

	/**
	 * adds {@code result} to existing collection of results
	 *
	 * @param result Result to be added to all results
	 */
	public void add(final ExecutionResult<T, R> result) {
		this.results.add(result);
	}
	/**
	 *  
	 * @return the first {@link ExecutionResult in the collection}
	 */
	public ExecutionResult<T, R> getFirst() {
		if (this.results.isEmpty()) {
			return null;
		} else {
			return this.results.iterator().next();
		}
	}
	/**
	 *
	 * @return {@code true} If there is any result
	 * {@code false} if no result
	 */
	public boolean hasAnyResult() {
		if (this.results.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return {@code true} If any parent is skipped
	 * {@code false} if no parent is skipped
	 * 
	 * @return the result
	 */
	public boolean anySkipped() {
		boolean parentSkipped = false;
		for (ExecutionResult<T, R> parent : this.results) {
			if (parent.isSkipped()) {
				parentSkipped = true;
				break;
			}
		}
		
		return parentSkipped;
	}

	public boolean anyCancelled() {
		boolean parentSkipped = false;
		for (ExecutionResult<T, R> parent : this.results) {
			if (parent.isCancelled()) {
				parentSkipped = true;
				break;
			}
		}
		
		return parentSkipped;
	}

	public List<ExecutionResult<T, R>> getSkipped() {
		List<ExecutionResult<T, R>> items = new ArrayList<ExecutionResult<T,R>>();
		for (ExecutionResult<T, R> result : this.results) {
			if (result.isSkipped()) {
				items.add(result);
			}
		}
		return items;
	}

	public List<ExecutionResult<T, R>> getCancelled() {
		List<ExecutionResult<T, R>> items = new ArrayList<ExecutionResult<T,R>>();
		for (ExecutionResult<T, R> result : this.results) {
			if (result.isCancelled()) {
				items.add(result);
			}
		}
		return items;
	}

	public List<ExecutionResult<T, R>> getSuccess() {
		List<ExecutionResult<T, R>> items = new ArrayList<ExecutionResult<T,R>>();
		for (ExecutionResult<T, R> result : this.results) {
			if (result.isSuccess()) {
				items.add(result);
			}
		}
		return items;
	}

	public List<ExecutionResult<T, R>> getErrored() {
		List<ExecutionResult<T, R>> items = new ArrayList<ExecutionResult<T,R>>();
		for (ExecutionResult<T, R> result : this.results) {
			if (result.isErrored()) {
				items.add(result);
			}
		}
		return items;
	}
	/**
	 * 
	 * @return all result in the collection
	 */
	public List<ExecutionResult<T, R>> getAll() {
		return new ArrayList<ExecutionResult<T, R>>(this.results);
	}

	@Override
	public String toString() {
		return this.results.toString();
	}
}
