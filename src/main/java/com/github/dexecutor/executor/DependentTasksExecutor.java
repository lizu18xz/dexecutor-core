package com.github.dexecutor.executor;

import java.io.Writer;

/**
 * Main Interface for Dexecutor framework, It provides api to build the graph and and to kick off the execution.
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
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
	 * Adds the node as dependent on all leaf nodes (currently), meaning all leaf nodes would be evaluated first and then the given node
	 * 
	 * @param nodeValue
	 */
	void addAsDependencyToAllLeafNodes(final T nodeValue);
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
	 * @author nmohammad
	 *
	 */
	enum ExecutionBehavior {
		TERMINATING, NON_TERMINATING, RETRY_ONCE_TERMINATING;
	}
}
