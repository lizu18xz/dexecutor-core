package com.github.dexecutor.core.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LevelOrderTraversarTest {

	@Test
	public void testUPrint() {

		Dag<Integer, Integer> dag = new DefaultDag<>();

		dag.addDependency(1, 2);
		dag.addDependency(1, 3);
		dag.addDependency(2, 4);
		dag.addDependency(3, 5);
		dag.addDependency(4, 5);

		Traversar<Integer, Integer> traversar = new LevelOrderTraversar<>();

		final StringBuilder builder = new StringBuilder();
		traversar.traverse(dag, new StringTraversarAction<Integer, Integer>(builder));
		assertThat(builder.toString(), equalTo("Path #0\n1[] \n2[1] 3[1] \n4[2] \n5[3, 4] "));
	}

	@Test
	public void testTringlePrint() {
		
		Dag<Integer, Integer> dag = new DefaultDag<>();

		dag.addDependency(1, 2);
		dag.addDependency(1, 3);
		dag.addDependency(2, 3);

		
		Traversar<Integer, Integer> traversar = new LevelOrderTraversar<>();
		final StringBuilder builder = new StringBuilder();
		traversar.traverse(dag, new StringTraversarAction<Integer, Integer>(builder));
		assertThat(builder.toString(), equalTo("Path #0\n1[] \n2[1] \n3[1, 2] "));
	}	
}
