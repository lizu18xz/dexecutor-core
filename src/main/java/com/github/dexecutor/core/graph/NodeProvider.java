package com.github.dexecutor.core.graph;

public interface NodeProvider<T, R> {
	/**
	 * Returns the node with the given id
	 * 
	 * @param id The Unique node id
	 * @return the @Node with the given id
	 */
	Node<T, R> getGraphNode(final T id);
}
