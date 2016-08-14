package com.dexecutor.executor;

public interface TaskProvider <T extends Comparable<T>> {

	Task provid(final T id);

	public static interface Task {
		void execute();
	}
}
