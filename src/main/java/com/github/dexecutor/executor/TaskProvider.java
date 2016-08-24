package com.github.dexecutor.executor;
/**
 * A Task Provider provides Tasks to be executed, when it comes to execution
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface TaskProvider <T extends Comparable<T>, R> {
	/**
	 * Given the node id, returns the task to be executed, while building graph only the node ids are required, when it comes to execution Task objects would be constructed
	 * 
	 * @param id
	 * @return @Task
	 */
	public Task<T, R> provid(final T id);
	
	/**
	 * Represent a unit of execution in Dexecutor framework
	 * 
	 * @author Nadeem Mohammad
	 *
	 * @param <T> Type of Node/Task ID
	 * @param <R> Type of Node/Task result
	 */
	public abstract class Task<T, R> {
		/**
		 * Framework would call this method, when it comes for tasks to be executed.
		 * @return the result of task execution
		 */
		public abstract R execute();
		/**
		 * When using retry behavior, execution error should not be considered until the last retry, this would define when execution error should be considered
		 */
		private boolean considerExecutionError = true;
		/**
		 * 
		 * @return whether execution error should be considered or not
		 */
		public final boolean shouldConsiderExecutionError() {
			return this.considerExecutionError;
		}
		/**
		 * 
		 * @param considerExecutionError
		 */
		void setConsiderExecutionError(boolean considerExecutionError) {
			this.considerExecutionError = considerExecutionError;
		}
		/**
		 * Defines whether or not this task should be executed
		 * 
		 * @param parentResults
		 * 
		 * @return {@code true} If this task should be executed
		 * {@code false} If the task should be skipped
		 */
		public boolean shouldExecute(final ExecutionResults<T, R> parentResults) {
			return true;
		}
	}
}
