package com.github.dexecutor.executor.graph;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public interface Graph<T extends Comparable<T>> {

	void addDependency(final T evalFirstValue, final T evalAfterValue);
	void addIndependent(final T nodeValue);
	Set<Node<T>> getInitialNodes();
	Set<Node<T>> getLeafNodes();
	Collection<Node<T>> allNodes();	
	int size();
	
	/**
	 * 
	 * @author Nadeem Mohammad
	 *
	 * @param <T>
	 */
	public final class Node<T> {
		private T value;
	    private Set<Node<T>> inComingEdges = new LinkedHashSet<Graph.Node<T>>();
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
