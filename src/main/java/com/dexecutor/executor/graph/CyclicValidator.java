package com.dexecutor.executor.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dexecutor.executor.graph.Graph.Node;

public class CyclicValidator<T> implements Validator<T> {

	private Collection<Graph.Node<T>> processedNodes = new ArrayList<Graph.Node<T>>();
	private Collection<Graph.Node<T>> onStackNodes = new ArrayList<Graph.Node<T>>();

	public void validate(final Graph<T> graph) {
		doProcess(graph.getInitialNodes());
	}

	private void doProcess(final List<Graph.Node<T>> nodes) {
		for (Graph.Node<T> node : nodes) {
			detectCycle(node);
		}
	}

	private void detectCycle(final Node<T> node) {
		this.processedNodes.add(node);
		this.onStackNodes.add(node);
		doDepthFirstTraversal(node);
		this.onStackNodes.remove(node);
	}

	private void doDepthFirstTraversal(final Node<T> node) {
		for (Node<T> adjNode : node.getOutGoingNodes()) {
			if (!isAlreadyProcessed(adjNode)) {
				detectCycle(adjNode);
			} else if (isOnStack(adjNode)) {
				throw new IllegalArgumentException("Cycle Detected " + node + " With " + adjNode);
			}
		}
	}

	private boolean isAlreadyProcessed(final Node<T> node) {
		return this.processedNodes.contains(node);
	}

	private boolean isOnStack(final Node<T> node) {
		return this.onStackNodes.contains(node);
	}
}
