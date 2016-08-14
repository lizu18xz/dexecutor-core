package com.dexecutor.executor;

public interface DependentTasksExecutor<T extends Comparable<T>> {
	void addIndependent(final T nodeValue);
	void addDependency(final T evalFirstValue, final T evalAfterValue);
	void addAsDependencyToAllLeafNodes(final T nodeValue);
	void execute(boolean stopOnError);
}
