package com.github.dexecutor.core.graph;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * A node representation in this graph, every node may have set of incoming edges and outgoing edges, a node is represented by unique value
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public final class Node<T, R> {

	/**
	 * Unique id of the node
	 */
	private T value;
	/**
	 * Execution result of this node
	 */
	private R result;
	/**
	 * Execution status of this node
	 */
	private NodeStatus status;
	/**
	 * incoming dependencies for this node
	 */
    private Set<Node<T, R>> inComingEdges = new LinkedHashSet<Node<T, R>>();
    /**
     * outgoing dependencies for this node
     */
    private Set<Node<T, R>> outGoingEdges = new LinkedHashSet<Node<T, R>>();
    /**
     * Constructs the node with the given node Id
     * @param val
     */
    public Node(final T val) {
		this.value = val;
	}
    /**
     * Add the given node, to the set of incoming nodes
     * @param node
     */
    public void addInComingNode(final Node<T, R> node) {	        
        this.inComingEdges.add(node);
    }
    /**
     * add the given to the set of out going nodes
     * @param node
     */
    public void addOutGoingNode(final Node<T, R> node) {	        
        this.outGoingEdges.add(node);
    }
    /**
     * 
     * @return the set of incoming nodes
     */
    public Set<Node<T, R>> getInComingNodes() {
        return this.inComingEdges;
    }
    /**
     * 
     * @return set of out going nodes
     */
    public Set<Node<T, R>> getOutGoingNodes() {
        return this.outGoingEdges;
    }
    /**
     * 
     * @return the node's value
     */
	public T getValue() {
		return this.value;
	}

	public R getResult() {
		return result;
	}

	public void setResult(final R result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return NodeStatus.SUCCESS.equals(this.status);
	}

	public boolean isErrored() {
		return NodeStatus.ERRORED.equals(this.status);
	}

	public boolean isSkipped() {
		return NodeStatus.SKIPPED.equals(this.status);
	}

	public void setSuccess() {
		this.status = NodeStatus.SUCCESS;
	}
	
	public void setErrored() {
		this.status = NodeStatus.ERRORED;
	}

	public void setSkipped() {
		this.status = NodeStatus.SKIPPED;
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
		Node<T, R> other = (Node<T, R>) obj;

		return this.value.equals(other.value);
	}

    @Override
    public String toString() {
    	return String.valueOf(this.value);
    }
    /**
     * Represents node's execution status
     * 
     * @author Nadeem Mohammad
     *
     */
    enum NodeStatus {
    	ERRORED,SKIPPED,SUCCESS;
    }
}
