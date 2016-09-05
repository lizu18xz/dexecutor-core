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

import java.util.Collection;
import java.util.Set;

/**
 * Dependency would be constructed based on this APIs, Dexecutor uses this data structure to represent the dependencies between tasks
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface Dag<T extends Comparable<T>, R> {
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
	 * @return set of initial nodes
	 */
	Set<Node<T, R>> getInitialNodes();
	/**
	 * Retruns the set of nodes for which there is no outgoing dependencies.
	 * @return set of leaf nodes
	 */
	Set<Node<T, R>> getLeafNodes();
	/**
	 * Returns all nodes in this graph
	 * @return all nodes in this graph
	 */
	Collection<Node<T, R>> allNodes();
	/**
	 * Returns the total number of nodes in this graph
	 * 
	 * @return total number of nodes in this graph
	 */
	int size();
}
