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

import java.io.Writer;
import java.util.Collection;
import java.util.Set;

import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.Traversar;
import com.github.dexecutor.core.graph.Validator;
/**
 * Represents Dexecutor state at any given moment of time, It basically tracks 
 * <ul>
 * 	<li><code>Phase : </code> Current Phase Dexecutor is in</li>
 *  <li><code>Graph : </code> Exposes API around building graph </li>
 *  <li><code>unprocessed nodes count : </code></li>
 *  <li><code>processed nodes : </code></li>
 *  <li><code>Discontined nodes : </code></li>
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface DexecutorState<T extends Comparable<T>, R> {
	
	/**
	 * Initialize the Dexecutor state, either to new or existing state
	 */
	void initState();
	
	/**
	 * Add a node as independent, it does not require any dependent node
	 * 
	 * @param nodeValue
	 */
	void addIndependent(final T nodeValue);
	/**
	 * <p>Add Two dependent nodes into the graph, creating the nodes if not already present </p>
	 * <p><code>evalFirstValue </code> would be executed first and then <code> evalAfterValue </code> </p>
	 * 
	 * @param evalFirstValue
	 * @param evalAfterValue
	 */
	void addDependency(final T evalFirstValue, final T evalAfterValue);
	/**
	 * Adds the node as dependent on all leaf nodes (at the time of adding), meaning all leaf nodes would be evaluated first and then the given node
	 * 
	 * @param nodeValue
	 */
	void addAsDependentOnAllLeafNodes(final T nodeValue);
	/**
	 * Adds the node as dependency to all initial nodes (at the time of adding), meaning this given node would be evaluated first and then all initial nodes would run in parallel
	 * 
	 * @param nodeValue
	 */
	void addAsDependencyToAllInitialNodes(final T nodeValue);
	/**
	 * Returns the Set of nodes for which there is no incoming dependencies.
	 * @return set of initial nodes
	 */
	Set<Node<T, R>> getInitialNodes();
	/**
	 * Returns the node with the given id
	 * 
	 * @param id
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
	 * Prints the graph into the writer using the Traversar
	 * 
	 * @param traversar
	 * @param writer
	 */
	void print(final Traversar<T, R> traversar, final Writer writer);
	/**
	 * validates the graph using the validator
	 * 
	 * @param validator
	 */
	void validate(final Validator<T, R> validator);


	/**
	 * sets the phase to that of provided
	 * @param currentPhase
	 */
	void setCurrentPhase(final Phase currentPhase);
	/**
	 * 
	 * @return the current phase of execution
	 */
	Phase getCurrentPhase();

	int getUnProcessedNodesCount();
	int incrementUnProcessedNodesCount();
	int decrementUnProcessedNodesCount();

	boolean shouldProcess(final Node<T, R> node);
	void markProcessingDone(Node<T, R> node);

	boolean isDiscontinuedNodesNotEmpty();
	Collection<Node<T, R>> getDiscontinuedNodes();
	void markDiscontinuedNodesProcessed();
	void processAfterNoError(final Collection<Node<T, R>> nodes);	
}
