package com.github.dexecutor.executor;
/**
 * A Task Provider provides Tasks to be executed, when it comes to execution
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public interface TaskProvider <T extends Comparable<T>> {
	/**
	 * Given the node id, returns the task to be executed
	 * 
	 * @param id
	 * @return @Task
	 */
	Task provid(final T id);
	
	/**
	 * Represent a unit of execution in Dexecutor framework
	 * 
	 * @author Nadeem Mohammad
	 *
	 */
	abstract class Task {
		/**
		 * 
		 */
		abstract void execute();
		private boolean considerExecutionError = true;
		/**
		 * 
		 * @return
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
	}
}
