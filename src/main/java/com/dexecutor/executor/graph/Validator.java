package com.dexecutor.executor.graph;

public interface Validator<T> {
	void validate(Graph<T> graph);
}
