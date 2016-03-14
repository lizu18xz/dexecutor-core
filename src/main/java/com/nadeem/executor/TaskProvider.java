package com.nadeem.executor;

public interface TaskProvider <T> {

	Task provid(T id);

	public static interface Task {
		void execute();
	}
}
