package com.github.dexecutor.executor.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of Graph
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public final class DefaultGraph<T extends Comparable<T>, R> implements Graph<T, R> {

	private Map<T, Node<T, R>> nodes = new HashMap<T, Node<T, R>>();

	public void addIndependent(T nodeValue) {
		doAdd(nodeValue);
	}

	public void addDependency(final T evalFirstNode, final T evalLaterNode) {
		Node<T, R> firstNode = doAdd(evalFirstNode);
		Node<T, R> afterNode = doAdd(evalLaterNode);

		if (!firstNode.equals(afterNode)) {
			firstNode.addOutGoingNode(afterNode);
			afterNode.addInComingNode(firstNode);			
		}
	}

	private Node<T, R> doAdd(final T nodeValue) {
		Node<T, R> graphNode = null;
		if (nodes.containsKey(nodeValue)) {
			graphNode = nodes.get(nodeValue);
		} else {
			graphNode = createNode(nodeValue);
			nodes.put(nodeValue, graphNode);
		}
		return graphNode;
	}

	private Node<T, R> createNode(final T value) {
		Node<T, R> node = new Node<T, R>(value);
		return node;
	}

	public Set<Node<T, R>> getInitialNodes() {
		Set<Node<T, R>> initialNodes = new LinkedHashSet<Node<T, R>>();
		Set<T> keys = nodes.keySet();
		for (T key : keys) {
			Node<T, R> node = nodes.get(key);
			if (node.getInComingNodes().isEmpty()) {				
				initialNodes.add(node);
			}
		}
		return initialNodes;
	}

	public int size() {
		return nodes.size();
	}

	public Collection<Graph.Node<T, R>> allNodes() {
		return new ArrayList<Graph.Node<T, R>>(this.nodes.values());
	}

	public Set<Graph.Node<T, R>> getLeafNodes() {
		Set<Node<T, R>> leafNodes = new LinkedHashSet<Node<T, R>>();
		Set<T> keys = nodes.keySet();
		for (T key : keys) {
			Node<T, R> node = nodes.get(key);
			if (node.getOutGoingNodes().isEmpty()) {				
				leafNodes.add(node);
			}
		}
		return leafNodes;
	}
}
