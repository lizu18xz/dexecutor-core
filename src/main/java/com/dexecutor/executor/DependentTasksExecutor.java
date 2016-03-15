package com.dexecutor.executor;

public interface DependentTasksExecutor<T> {
	void addIndependent(T nodeValue);
	void addDependency(T evalFirstValue, T evalAfterValue);
	void execute();
}
