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

import com.github.dexecutor.core.graph.Traversar;

/**
 * Main Interface for Dexecutor framework, It provides api to build the graph and and to kick off the execution.
 * 
 * @author Nadeem Mohammad
 * 
 * @see {@link DefaultDependentTasksExecutor}
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface Dexecutor<T extends Comparable<T>, R> {
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
	 * Kicks off the execution of the nodes based on the dependency graph constructed, using {@code addDepen***} apis
	 * 
	 * @param ExecutionConfig
	 */
	void execute(final ExecutionConfig config);

	/**
	 * After a dexecutor crash, create a new instance of dexecutor and call this method for recovery
	 * @param config
	 */
	void recoverExecution(final ExecutionConfig config);
	/**
	 * Prints the graph into the writer, using the traversar
	 * 
	 * @param traversar
	 * @param writer
	 */
	void print(final Traversar<T, R> traversar, final Writer writer);
}
