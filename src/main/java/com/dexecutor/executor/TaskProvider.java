package com.dexecutor.executor;

public interface TaskProvider <T extends Comparable<T>> {

	Task provid(final T id);

	public abstract class Task {
		abstract void execute();
		private boolean considerExecutionError = true;

		public boolean shouldConsiderExecutionError() {
			return this.considerExecutionError;
		}
		void setConsiderExecutionError(boolean considerExecutionError) {
			this.considerExecutionError = considerExecutionError;
		}
	}
}
