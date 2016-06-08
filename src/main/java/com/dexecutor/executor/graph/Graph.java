package com.dexecutor.executor.graph;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public interface Graph<T> {

	void addDependency(T evalFirstValue, T evalAfterValue);
	Set<Node<T>> getInitialNodes();
	void addIndependent(T nodeValue);
	int size();
	Collection<Node<T>> allNodes();

	public final class Node<T> {
		private T value;
	    private Set<Node<T>> inComingEdges = new LinkedHashSet<Graph.Node<T>>();
	    private Set<Node<T>> outGoingEdges = new LinkedHashSet<Graph.Node<T>>();
	    
	    public Node(T val) {
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
			return value;
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
