package com.nadeem.executor.graph;

public interface Validator<T> {
	void validate(Graph<T> graph);
}
