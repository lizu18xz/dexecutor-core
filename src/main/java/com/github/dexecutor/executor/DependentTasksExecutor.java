package com.github.dexecutor.executor;

import java.io.Writer;

public interface DependentTasksExecutor<T extends Comparable<T>> {

	void addIndependent(final T nodeValue);
	void addDependency(final T evalFirstValue, final T evalAfterValue);
	void addAsDependencyToAllLeafNodes(final T nodeValue);

	void execute(final ExecutionBehavior behavior);
	void print(final Writer writer);

	enum ExecutionBehavior {
		TERMINATING, NON_TERMINATING, RETRY_ONCE_TERMINATING;
	}
}
