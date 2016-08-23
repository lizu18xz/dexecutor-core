package com.github.dexecutor.executor.graph;

import java.io.Writer;

/**
 * Provides an API to Traverse a given graph
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface Traversar <T extends Comparable<T>, R> {
	/**
	 * Traverse the given graph and print it on the Writer
	 * @param graph
	 * @param writer
	 */
	void traverse(final Graph<T, R> graph, final Writer writer);
}
