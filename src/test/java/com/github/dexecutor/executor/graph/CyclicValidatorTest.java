package com.github.dexecutor.executor.graph;

import org.junit.Test;

public class CyclicValidatorTest {

	@Test(expected= IllegalArgumentException.class)
	public void vlidateExceptionThrown() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		graph.addDependency(1, 2);
		graph.addDependency(2, 3);
		graph.addDependency(1, 4);
		graph.addDependency(2, 1);
		
		Validator<Integer> validator = new CyclicValidator<Integer>();
		validator.validate(graph);
	}

	@Test
	public void vlidateNoExceptionThrown() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		graph.addDependency(1, 2);
		graph.addDependency(2, 3);
		graph.addDependency(1, 4);
		
		Validator<Integer> validator = new CyclicValidator<Integer>();
		validator.validate(graph);
	}

}
