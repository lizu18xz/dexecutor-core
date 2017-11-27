package com.github.dexecutor.core.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MergedLevelOrderTraversarTest {
	
	@Test
	public void testComplexPrint() {
		
		Dag<Integer, Integer> dag = new DefaultDag<>();

		dag.addDependency(1, 2);
		dag.addDependency(1, 3);

		dag.addDependency(3, 4);
		dag.addDependency(3, 5);
		dag.addDependency(3, 6);
		
		dag.addDependency(2, 9);
		dag.addDependency(2, 8);
		dag.addDependency(2, 7);
		
		dag.addDependency(9, 10);
		
		dag.addDependency(12, 13);
		
		dag.addDependency(13, 4);
		dag.addDependency(13, 14);
		
		dag.addIndependent(11);
		
		Traversar<Integer, Integer> traversar = new MergedLevelOrderTraversar<>();
		final StringBuilder builder = new StringBuilder();
		traversar.traverse(dag, new StringTraversarAction<Integer, Integer>(builder));
		assertThat(builder.toString(), equalTo("\nPath #0\n1[] 11[] 12[] \n2[1] 3[1] 13[12] \n9[2] 8[2] 7[2] 5[3] 6[3] 4[3, 13] 14[13] \n10[9] "));
	}
	
	@Test
	public void testComplex2Print() {
		
		Dag<Integer, Integer> dag = new DefaultDag<>();

		dag.addDependency(1, 2);
		dag.addDependency(1, 3);
		
		dag.addDependency(7, 8);		
		dag.addDependency(7, 9);
		
		dag.addDependency(11, 12);
		dag.addDependency(11, 13);
		
		dag.addDependency(2, 4);
		dag.addDependency(4, 5);
		dag.addDependency(3, 6);
		
		dag.addDependency(8, 10);
		
		dag.addDependency(10, 6);
		
		dag.addDependency(12, 10);
		dag.addDependency(12, 14);
		
		dag.addDependency(13, 15);		
		dag.addDependency(6, 15);
		
		dag.addDependency(15, 16);

		Traversar<Integer, Integer> traversar = new MergedLevelOrderTraversar<>();
		final StringBuilder builder = new StringBuilder();
		traversar.traverse(dag, new StringTraversarAction<Integer, Integer>(builder));
		assertThat(builder.toString(), equalTo("\nPath #0\n1[] 7[] 11[] \n2[1] 3[1] 8[7] 9[7] 12[11] 13[11] \n4[2] 10[8, 12] 14[12] \n5[4] 6[3, 10] \n15[13, 6] \n16[15] "));
	}
}
