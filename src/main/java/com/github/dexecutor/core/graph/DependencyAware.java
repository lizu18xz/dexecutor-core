package com.github.dexecutor.core.graph;
/**
 * Implementors should be aware of the the dependencies between entities
 * 
 * @param <T> Type of Node/Task ID
 *
 * @author Nadeem Mohammad
 *
 */
public interface DependencyAware<T extends Comparable<T>> {
	/**
	 * Adds the given node to the datastructure whith out any dependency
	 * Nodes should be created only if it is not already added.
	 * @param nodeValue Unique node id
	 */
	void addIndependent(final T nodeValue);
	/**
	 * Should add the two nodes in the datastructure in such a way that {@code evalFirstValue} should be evaluated before {@code evalAfterValue}.
	 * Nodes should be created only if it is not already added.
	 * 
	 * @param evalFirstValue Node which should be evaluated first
	 * @param evalAfterValue Node which should be evaluated after {@code evalFirstValue}
	 */
	void addDependency(final T evalFirstValue, final T evalAfterValue);	
	/**
	 * Adds the node as dependent on all leaf nodes (at the time of adding), meaning all leaf nodes would be evaluated first and then the given node
	 * 
	 * @param nodeValue Node which should depend on all leaf nodes
	 */
	void addAsDependentOnAllLeafNodes(final T nodeValue);
	/**
	 * Adds the node as dependency to all initial nodes (at the time of adding), meaning this given node would be evaluated first and then all initial nodes would run in parallel
	 * 
	 * @param nodeValue Node on  which all initial nodes should depends on
	 */
	void addAsDependencyToAllInitialNodes(final T nodeValue);	
}
