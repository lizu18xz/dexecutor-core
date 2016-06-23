package com.dexecutor.executor;

public interface DependentTasksExecutor<T extends Comparable<T>> {
	void addIndependent(T nodeValue);
	void addDependency(T evalFirstValue, T evalAfterValue);
	void execute(boolean stopOnError);
}
