package com.dexecutor.executor.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import com.dexecutor.executor.graph.Graph.Node;

public class DefaultGraphTest {

	@Test
	public void testGraphSize() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		assertThat(graph.size(), equalTo(3));
	}

	@Test
	public void testInitialNodes() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Set<Node<Integer>> initialNodes = graph.getInitialNodes();
		
		assertThat(initialNodes.size(), equalTo(1));		
		assertThat(initialNodes.iterator().next(), equalTo(new Graph.Node<Integer>(1)));
	}
	
	@Test
	public void testAllNodes() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Collection<Node<Integer>> initialNodes = graph.allNodes();
		
		assertThat(initialNodes.size(), equalTo(3));
	}
}
