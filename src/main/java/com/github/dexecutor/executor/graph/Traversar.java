package com.github.dexecutor.executor.graph;

import java.io.Writer;

/**
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public interface Traversar <T extends Comparable<T>> {

	void traverse(final Graph<T> graph, final Writer writer);
}
