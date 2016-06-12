package com.dexecutor.executor.graph;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(graph.size()).isEqualTo(3);
	}

	@Test
	public void testInitialNodes() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Set<Node<Integer>> initialNodes = graph.getInitialNodes();
		
		assertThat(initialNodes.size()).isEqualTo(1);
		assertThat(initialNodes).containsSequence(new Graph.Node<Integer>(1));
	}

	@Test
	public void testAllNodes() {
		Graph<Integer> graph = new DefaultGraph<Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Collection<Node<Integer>> initialNodes = graph.allNodes();
		
		assertThat(initialNodes.size()).isEqualTo(3);
		assertThat(initialNodes).containsSequence(new Graph.Node<Integer>(1), new Graph.Node<Integer>(2), new Graph.Node<Integer>(3));
	}
}
