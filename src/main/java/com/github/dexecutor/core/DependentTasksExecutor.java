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
public interface DependentTasksExecutor<T extends Comparable<T>> {
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
	 * @param behavior
	 */
	void execute(final ExecutionBehavior behavior);
	
	/**
	 * Prints the graph into the writer
	 * 
	 * @param writer
	 */
	void print(final Writer writer);
	
	/**
	 * Defines the execution behavior of the tasks
	 *   <ul>
	 * 	   <li>
	 * 			<code>TERMINATING </code> : Whole tasks execution would come to an end after the execution is thrown
	 * 	   </li>
	 * 		<li>
	 * 			<code>NON_TERMINATING</code> : Tasks execution wont come to halt after an exception is thrown out of task
	 * 		</li>
	 * 		<li>
	 * 			<code>RETRY_ONCE_TERMINATING</code> : A retry would be attempted after an exception is thrown, and then if the execption is thrown again, the tasks execution would stop
	 * 		</li>
	 * 
	 * </ul>
	 * 
	 * @author Nadeem Mohammad
	 *
	 */
	public enum ExecutionBehavior {
		TERMINATING, NON_TERMINATING, RETRY_ONCE_TERMINATING;
	}
}
