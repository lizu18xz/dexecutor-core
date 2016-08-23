package com.github.dexecutor.executor.graph;

/**
 * This interface provides API to validate the graph before tasks execution
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface Validator<T extends Comparable<T>, R> {
	/**
	 * Called to figure out if a graph is valid or not, exception should be thrown if the graph is invalid
	 * @param graph
	 */
	void validate(Graph<T, R> graph);
}
