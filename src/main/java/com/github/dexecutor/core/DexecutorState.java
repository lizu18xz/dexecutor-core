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
package com.github.dexecutor.core;

import java.util.Collection;
import java.util.Set;

import com.github.dexecutor.core.graph.DependencyAware;
import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.Traversar;
import com.github.dexecutor.core.graph.TraversarAction;
import com.github.dexecutor.core.graph.Validator;
import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.ExecutionResults;
/**
 * Represents Dexecutor state at any given moment of time, It basically tracks 
 * <ul>
 * 	<li><code>Phase : </code> Current Phase Dexecutor is in</li>
 *  <li><code>Graph : </code> Exposes API around building graph </li>
 *  <li><code>unprocessed nodes count : </code>How many nodes are waiting to be processed</li>
 *  <li><code>processed nodes : </code> Nodes which are processed till that point</li>
 *  <li><code>Discontinued nodes : </code>Nodes for which processing should continue after system comes to valid state</li>
 * </ul>
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface DexecutorState<T extends Comparable<T>, R> extends DependencyAware<T> {
	/**
	 * Returns the node with the given id
	 * 
	 * @param id The Unique node id
	 * @return the @Node with the given id
	 */
	Node<T, R> getGraphNode(final T id);
	/**
	 * Returns the total number of nodes in this graph
	 * 
	 * @return total number of nodes in this graph
	 */
	int graphSize();
	
	/**
	 * Returns the Set of nodes for which there is no incoming dependencies.
	 * @return set of initial nodes
	 */
	Set<Node<T, R>> getInitialNodes();

	/**
	 * 
	 * @return the root non processed nodes
	 */
	Set<Node<T, R>> getNonProcessedRootNodes();

	/**
	 * Prints the graph into the writer using the Traversar
	 * 
	 * @param traversar would traverse the graph
	 * @param action callback which would be called based on traverse
	 */
	void print(final Traversar<T, R> traversar, final TraversarAction<T, R> action);
	/**
	 * validates the graph using the validator
	 * 
	 * @param validator based on which validation would happen
	 */
	void validate(final Validator<T, R> validator);

	/**
	 * sets the phase to that of provided
	 * @param currentPhase the phase that should be set to 
	 */
	void setCurrentPhase(final Phase currentPhase);
	/**
	 * 
	 * @return the current phase of execution
	 */
	Phase getCurrentPhase();
	/**
	 * 
	 * @return the total number of unprocessed nodes(Waiting for execution result) at any given moment of time.
	 */
	int getUnProcessedNodesCount();
	/**
	 * increments the count of total number of unprocessed nodes
	 */
	void incrementUnProcessedNodesCount();
	/**
	 * decrements the count of total number of unprocessed nodes
	 */
	void decrementUnProcessedNodesCount();
	
	/**
	 * 
	 * @param node on which test should happen
	 * @return weather the {@code node} should be processed by dexecutor or not
	 */
	boolean shouldProcess(final Node<T, R> node);
	/**
	 * 
	 * @param node Mark the {@code node} as processed.
	 */
	void markProcessingDone(final Node<T, R> node);
	
	/**
	 * 
	 * @return processedNodes
	 */
	Collection<Node<T, R>> getProcessedNodes();	

	/**
	 * 
	 * @return {@code true} if there nodes that should be processed, if some were discontinued due to error.
	 * 			{@code false} otherwise
	 */
	boolean isDiscontinuedNodesNotEmpty();
	/**
	 * 
	 * @return the @nodes that are waiting to be processed, which were discontinued due to error
	 */
	Collection<Node<T, R>> getDiscontinuedNodes();
	/**
	 * clear (or marks) all the discontinued nodes till this point as processed
	 */
	void markDiscontinuedNodesProcessed();
	/**
	 * Add to existing collection of discontinued nodes to be processed later, if system come to valid state.
	 * @param nodes which should be processed after retry success
	 */
	void processAfterNoError(final Collection<Node<T, R>> nodes);
	/**
	 * Add to errored collection
	 * @param task to add to errored collection
	 */
	void addErrored(ExecutionResult<T, R> task);
	/**
	 * Remove errored collection
	 * @param task to remove from errored collection
	 */
	void removeErrored(ExecutionResult<T, R> task);
	/**
	 * @return Number of errors at this instance of time
	 */
	int erroredCount();
	/**
	 * 
	 * @return thre errored result
	 */
	ExecutionResults<T, R> getErrored();
	
	/**
	 * called to force stop
	 */
	void forcedStop();
	/**
	 * called when Dexecutor is finished execution
	 */
	void onTerminate();
	/**
	 * called where Dexecutor is Recovered
	 */
	void onRecover();
}
