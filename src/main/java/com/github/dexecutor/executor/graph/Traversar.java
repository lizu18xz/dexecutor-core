package com.github.dexecutor.executor.graph;

import java.io.Writer;

public interface Traversar <T extends Comparable<T>> {

	void traverse(final Graph<T> graph, final Writer writer);
}
