package com.dexecutor.executor.graph;

public interface Validator<T extends Comparable<T>> {
	void validate(Graph<T> graph);
}
