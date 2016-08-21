package com.github.dexecutor.executor.graph;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Dependency would be construced based on this APIs, Dexecutor uses this data structure to represet the dependencies between tasks
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public interface Graph<T extends Comparable<T>> {
	/**
	 * Should add the two nodes in the datastructure in such a way that {@code evalFirstValue} should be evaluated before {@code evalAfterValue}.
	 * Nodes should be created only if it is not already added.
	 * 
	 * @param evalFirstValue
	 * @param evalAfterValue
	 */
	void addDependency(final T evalFirstValue, final T evalAfterValue);
	/**
	 * Adds the given node to the datastructure whith out any dependency
	 * Nodes should be created only if it is not already added.
	 * @param nodeValue
	 */
	void addIndependent(final T nodeValue);
	/**
	 * Returns the Set of nodes for which there is no incoming dependencies.
	 * @return
	 */
	Set<Node<T>> getInitialNodes();
	/**
	 * Retruns the set of nodes for which there is no outgoing dependencies.
	 * @return
	 */
	Set<Node<T>> getLeafNodes();
	/**
	 * Returns all nodes in this graph
	 * @return
	 */
	Collection<Node<T>> allNodes();
	/**
	 * Returns the total number of nodes in this graph
	 * 
	 * @return
	 */
	int size();
	
	/**
	 * A node representation in this graph, every node may have set of incoming edges and outgoing edges, a node is represented by unique value
	 * 
	 * @author Nadeem Mohammad
	 *
	 * @param <T>
	 */
	public final class Node<T> {
		/**
		 * Unique id of the node
		 */
		private T value;
		/**
		 * incoming dependencies for this node
		 */
	    private Set<Node<T>> inComingEdges = new LinkedHashSet<Graph.Node<T>>();
	    /**
	     * outgoing dependencies for this node
	     */
	    private Set<Node<T>> outGoingEdges = new LinkedHashSet<Graph.Node<T>>();
	    
	    public Node(final T val) {
			this.value = val;
		}

	    public void addInComingNode(final Node<T> node) {	        
	        this.inComingEdges.add(node);
	    }

	    public void addOutGoingNode(final Node<T> node) {	        
	        this.outGoingEdges.add(node);
	    }

	    public Set<Node<T>> getInComingNodes() {
	        return this.inComingEdges;
	    }

	    public Set<Node<T>> getOutGoingNodes() {
	        return this.outGoingEdges;
	    }

		public T getValue() {
			return this.value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj == null || obj.getClass() != this.getClass()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Node<T> other = (Node<T>) obj;

			return this.value.equals(other.value);
		}

	    @Override
	    public String toString() {
	    	return String.valueOf(this.value);
	    }
	}
}
