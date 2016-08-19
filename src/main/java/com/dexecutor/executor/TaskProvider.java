package com.dexecutor.executor;

public interface TaskProvider <T extends Comparable<T>> {

	Task provid(final T id);

	abstract class Task {
		abstract void execute();
		private boolean considerExecutionError = true;

		public final boolean shouldConsiderExecutionError() {
			return this.considerExecutionError;
		}

		void setConsiderExecutionError(boolean considerExecutionError) {
			this.considerExecutionError = considerExecutionError;
		}
	}
}
