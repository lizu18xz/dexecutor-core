package com.nadeem.executor.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DefaultGraph<T> implements Graph<T> {

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
		Node<T> node = new Node<T>();
		node.value = value;
		return node;
	}

	public List<Node<T>> getInitialNodes() {
		List<Node<T>> orphanNodes = new ArrayList<Node<T>>();
		Set<T> keys = nodes.keySet();
		for (T key : keys) {
			Node<T> node = nodes.get(key);
			if (node.getInComingNodes().isEmpty()) {				
				orphanNodes.add(node);
			}
		}
		return orphanNodes;
	}

	public int size() {
		return nodes.size();
	}
}
