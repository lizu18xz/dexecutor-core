package com.github.dexecutor.executor;

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

public final class ExecutionResults<T, R> {

	private List<ExecutionResult<T, R>> results = new ArrayList<ExecutionResult<T, R>>();

	/**
	 * adds {@code result} to existing collection of results
	 *
	 * @param result Result to be added to all results
	 */
	public final void add(final ExecutionResult<T, R> result) {
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
	public boolean hasAnyParentResult() {
		if (this.results.isEmpty()) {
			return false;
		} else {
			return true;
		}
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
