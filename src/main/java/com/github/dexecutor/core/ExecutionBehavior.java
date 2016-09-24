package com.github.dexecutor.core;

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
	TERMINATING, NON_TERMINATING, RETRY_IMMEDIATE_TERMINATING;
}
