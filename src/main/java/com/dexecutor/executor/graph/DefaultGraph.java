package com.dexecutor.executor.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class DefaultGraph<T extends Comparable<T>> implements Graph<T> {

	private Map<T, Node<T>> nodes = new HashMap<T, Node<T>>();

	public void addIndependent(T nodeValue) {
		doAdd(nodeValue);
	}

	public void addDependency(final T evalFirstNode, final T evalLaterNode) {
		Node<T> firstNode = doAdd(evalFirstNode);
		Node<T> afterNode = doAdd(evalLaterNode);

		firstNode.addOutGoingNode(afterNode);
		afterNode.addInComingNode(firstNode);
	}

	private Node<T> doAdd(final T nodeValue) {
		Node<T> graphNode = null;
		if (nodes.containsKey(nodeValue)) {
			graphNode = nodes.get(nodeValue);
		} else {
			graphNode = createNode(nodeValue);
			nodes.put(nodeValue, graphNode);
		}
		return graphNode;
	}

	private Node<T> createNode(final T value) {
		Node<T> node = new Node<T>(value);
		return node;
	}

	public Set<Node<T>> getInitialNodes() {
		Set<Node<T>> initialNodes = new LinkedHashSet<Node<T>>();
		Set<T> keys = nodes.keySet();
		for (T key : keys) {
			Node<T> node = nodes.get(key);
			if (node.getInComingNodes().isEmpty()) {				
				initialNodes.add(node);
			}
		}
		return initialNodes;
	}

	public int size() {
		return nodes.size();
	}

	public Collection<Graph.Node<T>> allNodes() {
		return new ArrayList<Graph.Node<T>>(this.nodes.values());
	}

	public Set<Graph.Node<T>> getLeafNodes() {
		Set<Node<T>> leafNodes = new LinkedHashSet<Node<T>>();
		Set<T> keys = nodes.keySet();
		for (T key : keys) {
			Node<T> node = nodes.get(key);
			if (node.getOutGoingNodes().isEmpty()) {				
				leafNodes.add(node);
			}
		}
		return leafNodes;
	}
}
