package com.github.dexecutor.executor.graph;

/**
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public interface Validator<T extends Comparable<T>> {
	void validate(Graph<T> graph);
}
