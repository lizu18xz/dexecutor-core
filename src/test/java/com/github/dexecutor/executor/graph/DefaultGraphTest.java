package com.github.dexecutor.executor.graph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import com.github.dexecutor.executor.graph.Graph.Node;

/**
 * 
 * @author Nadeem Mohammad
 *
 */
public class DefaultGraphTest {
	
	@Test
	public void nodeTest() {
		Node<Integer, Integer> intNode = new Node<Integer, Integer>(1);
		assertThat(intNode.equals(null)).isFalse();
		Node<String, String> strNode = new Node<String, String>("");
		assertThat(intNode.equals(strNode)).isFalse();
	}
	
	@Test
	public void testAddSameDependency() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 1);
		assertThat(graph.size()).isEqualTo(1);
		
		graph.addDependency(1, 3);
		assertThat(graph.size()).isEqualTo(2);
	}

	@Test
	public void testGraphSize() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		assertThat(graph.size()).isEqualTo(3);
	}

	@Test
	public void testInitialNodes() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Set<Node<Integer, Integer>> initialNodes = graph.getInitialNodes();
		
		assertThat(initialNodes.size()).isEqualTo(1);
		assertThat(initialNodes).containsSequence(new Graph.Node<Integer, Integer>(1));
	}

	@Test
	public void testAllNodes() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Collection<Node<Integer, Integer>> initialNodes = graph.allNodes();
		
		assertThat(initialNodes.size()).isEqualTo(3);
		assertThat(initialNodes).containsSequence(new Graph.Node<Integer, Integer>(1), new Graph.Node<Integer, Integer>(2), new Graph.Node<Integer, Integer>(3));
	}

	@Test
	public void testLeafNodes() {
		Graph<Integer, Integer> graph = new DefaultGraph<Integer, Integer>();
		
		graph.addIndependent(1);		
		graph.addDependency(1, 2);		
		graph.addIndependent(3);
		graph.addDependency(1, 3);
		Collection<Node<Integer, Integer>> initialNodes = graph.getLeafNodes();
		
		assertThat(initialNodes.size()).isEqualTo(2);
		assertThat(initialNodes).containsSequence(new Graph.Node<Integer, Integer>(2), new Graph.Node<Integer, Integer>(3));
	}
}
