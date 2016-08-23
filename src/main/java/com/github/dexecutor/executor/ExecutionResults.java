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

	public final void add(final ExecutionResult<T, R> result) {
		this.results.add(result);
	}

	public ExecutionResult<T, R> getFirst() {
		if (this.results.isEmpty()) {
			return null;
		} else {
			return this.results.iterator().next();
		}
	}

	public boolean hasAnyParentResult() {
		if (this.results.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public List<ExecutionResult<T, R>> getAll() {
		return new ArrayList<ExecutionResult<T, R>>(this.results);
	}

	@Override
	public String toString() {
		return this.results.toString();
	}
}
