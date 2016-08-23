package com.github.dexecutor.executor.graph;

import org.junit.Test;

/**
 * 
 * @author Nadeem Mohammad
 *
 */
public class CyclicValidatorTest {

	@Test(expected= IllegalArgumentException.class)
	public void vlidateExceptionThrown() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		graph.addDependency(1, 2);
		graph.addDependency(2, 3);
		graph.addDependency(1, 4);
		graph.addDependency(2, 1);
		
		Validator<Integer, Integer> validator = new CyclicValidator<Integer, Integer>();
		validator.validate(graph);
	}

	@Test
	public void vlidateNoExceptionThrown() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		graph.addDependency(1, 2);
		graph.addDependency(2, 3);
		graph.addDependency(1, 4);
		
		Validator<Integer, Integer> validator = new CyclicValidator<Integer, Integer>();
		validator.validate(graph);
	}

}
