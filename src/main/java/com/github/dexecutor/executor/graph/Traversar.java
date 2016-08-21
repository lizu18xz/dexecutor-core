package com.github.dexecutor.executor.graph;

import java.io.Writer;

/**
 * Provides an API to Traverse a given graph
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public interface Traversar <T extends Comparable<T>> {
	/**
	 * Traverse the given graph and print it on the Writer
	 * @param graph
	 * @param writer
	 */
	void traverse(final Graph<T> graph, final Writer writer);
}
