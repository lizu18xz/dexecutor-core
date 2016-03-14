package com.nadeem.executor.graph;

import java.util.LinkedList;
import java.util.List;

public interface Graph<T> {

	void addDependency(T evalFirstValue, T evalAfterValue);
	List<Node<T>> getInitialNodes();
	void addIndependent(T nodeValue);
	int size();

	public final class Node<T> {
	    public T value;
	    private List<Node<T>> inComingEdges = new LinkedList<Node<T>>();
	    private List<Node<T>> outGoingEdges = new LinkedList<Node<T>>();

	    public void addInComingNode(final Node<T> node) {	        
	        this.inComingEdges.add(node);
	    }

	    public void addOutGoingNode(final Node<T> node) {	        
	        this.outGoingEdges.add(node);
	    }

	    public List<Node<T>> getInComingNodes() {
	        return this.inComingEdges;
	    }

	    public List<Node<T>> getOutGoingNodes() {
	        return this.outGoingEdges;
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
