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
public interface Dag<T extends Comparable<T>, R> extends DependencyAware<T> {	
	/**
	 * Returns the total number of nodes in this graph
	 * 
	 * @return total number of nodes in this graph
	 */
	int size();

	/**
	 * Returns the node with the given id
	 * 
	 * @param id unique node id
	 * @return the @Node with the given id
	 */
	Node<T, R> get(final T id);
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
	 * 
	 * @return the root non processed nodes
	 */
	Set<Node<T, R>> getNonProcessedRootNodes();
}
