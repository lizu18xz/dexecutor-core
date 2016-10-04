/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	 * Arbitray data of this node
	 */
	private Object data;
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
	 /**
     * 
     * @return the node's execution result
     */
	public R getResult() {
		return result;
	}
	/**
     * 
     * sets the node's execution result to a new value
     */
	public void setResult(final R result) {
		this.result = result;
	}
	/**
     * 
     * @return {@true} if the node's execution result us SUCCESS
     * 			{@false} otherwise
     */
	public boolean isSuccess() {
		return NodeStatus.SUCCESS.equals(this.status);
	}
	/**
     * 
     * @return {@true} if the node's execution result us ERRORED
     * 			{@false} otherwise
     */
	public boolean isErrored() {
		return NodeStatus.ERRORED.equals(this.status);
	}
	/**
     * 
     * @return {@true} if the node's execution result us SKIPPED
     * 			{@false} otherwise
     */
	public boolean isSkipped() {
		return NodeStatus.SKIPPED.equals(this.status);
	}
	
	/**
	 * Sets the node's execution result to SUCCESS
	 */
	public void setSuccess() {
		this.status = NodeStatus.SUCCESS;
	}
	/**
	 * Sets the node's execution result to ERRORED
	 */
	public void setErrored() {
		this.status = NodeStatus.ERRORED;
	}
	
	/**
	 * Sets the node's execution result to SKIPPED
	 */
	public void setSkipped() {
		this.status = NodeStatus.SKIPPED;
	}
	
	 /**
     * 
     * @return the node's data
     */
	public Object getData() {
		return data;
	}
	
	/**
	 * Sets the node's data to a new value
	 */
	public void setData(Object data) {
		this.data = data;
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
     * <ul>
     * 		<li> <code> ERRORED:</code> Node's execution was in error</li>
     * 		<li> <code> SKIPPED:</code>Node's  execution was skipped</li>
     * 		<li> <code> SUCCESS:</code>Node's  execution was success</li>
     * </ul>
     * 
     * @author Nadeem Mohammad
     *
     */
    enum NodeStatus {
    	ERRORED,SKIPPED,SUCCESS;
    }
}
