package com.dexecutor.executor.graph;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.dexecutor.executor.graph.Graph.Node;

public class LevelOrderTraversar<T extends Comparable<T>> implements Traversar<T> {
	
	private List<Graph.Node<T>> processed = new ArrayList<Graph.Node<T>>();
	
	public void traverse(final Graph<T> graph, final Writer writer) {
		List<List<List<Node<T>>>> levelOrderOfGraphs = traverseLevelOrder(graph);
		int i = 0;
		for (List<List<Node<T>>> levelOrderOfGraph : levelOrderOfGraphs) {
			try {
				writer.write("Path #" + (i++) + "\n");
				printGraph(levelOrderOfGraph, writer);
				writer.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<List<List<Node<T>>>> traverseLevelOrder(final Graph<T> graph) {
		List<List<List<Node<T>>>> result = new ArrayList<List<List<Node<T>>>>();
		Set<Node<T>> initialNodes = graph.getInitialNodes();
		for (Node<T> iNode : initialNodes) {
			List<List<Node<T>>> iresult = new ArrayList<List<Node<T>>>();
			doTraverse(iresult, iNode);
			result.add(iresult);
		}
		return result;
	}

	private void doTraverse(final List<List<Node<T>>> result, final Node<T> iNode) {
		Queue<Node<T>> queue = new LinkedList<Node<T>>();
		queue.offer(iNode);
		while (!queue.isEmpty()) {
			List<Node<T>> level = new ArrayList<Node<T>>();
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				Node<T> node = queue.poll();
				if (!this.processed.contains(node)) {
					if (!level.contains(node)) {					
						level.add(node);
					}
					this.processed.add(node);
					for (Node<T> ogn : node.getOutGoingNodes()) {
						if (ogn != null && !this.processed.contains(ogn)) {
							queue.offer(ogn);
						}
					}
				}
				
			}
			result.add(level);
		}
	}

	private void printGraph(final List<List<Node<T>>> list, final Writer writer) {
		for (List<Node<T>> nodes : list) {
			try {
				for (Node<T> node : nodes) {
					writer.write(node + "" + node.getInComingNodes() + " ");					
				}
				writer.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
