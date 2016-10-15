package com.github.dexecutor.core.graph;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Traversar which does level order traversal of the given graph and merges each level
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class MergedLevelOrderTraversar<T extends Comparable<T>, R> implements Traversar<T, R> {

	private static final String SPACE = " ";
	private static final String EMPTY_STRING = "";

	private static final Logger logger = LoggerFactory.getLogger(MergedLevelOrderTraversar.class);

	private List<Node<T, R>> processed = new ArrayList<Node<T, R>>();

	public void traverse(final Dag<T, R> graph, final Writer writer) {
		List<List<List<Node<T, R>>>> levelOrderOfGraphs = traverseLevelOrder(graph);

		List<List<Node<T, R>>> merged = merge(levelOrderOfGraphs);
		printGraph(merged, writer);
	}

	private List<List<Node<T, R>>> merge(final List<List<List<Node<T, R>>>> levelOrderOfGraphs) {
		List<List<Node<T, R>>> merged  = new ArrayList<List<Node<T, R>>>();

		for (List<List<Node<T, R>>> levelOrderOfGraph : levelOrderOfGraphs) {

			for (int i = 0; i < levelOrderOfGraph.size(); i++) {
				if (merged.size() <= i) {					
					merged.add(new ArrayList<Node<T,R>>());
				}
				merged.get(i).addAll(levelOrderOfGraph.get(i));
			}
		}
		return merged;
	}

	private List<List<List<Node<T, R>>>> traverseLevelOrder(final Dag<T, R> graph) {
		List<List<List<Node<T, R>>>> result = new ArrayList<List<List<Node<T, R>>>>();
		Set<Node<T, R>> initialNodes = graph.getInitialNodes();
		for (Node<T, R> iNode : initialNodes) {
			List<List<Node<T, R>>> iresult = new ArrayList<List<Node<T, R>>>();
			doTraverse(iresult, iNode);
			result.add(iresult);
		}
		return result;
	}

	private void doTraverse(final List<List<Node<T, R>>> result, final Node<T, R> iNode) {
		Queue<Node<T, R>> queue = new LinkedList<Node<T, R>>();
		queue.offer(iNode);
		while (!queue.isEmpty()) {
			List<Node<T, R>> level = new ArrayList<Node<T, R>>();
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				Node<T, R> node = queue.poll();
				if (!this.processed.contains(node) && allProcessed(node.getInComingNodes())) {
					if (!level.contains(node)) {
						level.add(node);
					}
					this.processed.add(node);
					for (Node<T, R> ogn : node.getOutGoingNodes()) {
						if (ogn != null && !this.processed.contains(ogn)) {
							queue.offer(ogn);
						}
					}
				}				
			}
			result.add(level);
		}
	}

	private boolean allProcessed(final Set<Node<T, R>> inComingNodes) {
		return this.processed.containsAll(inComingNodes);
	}

	private void printGraph(final List<List<Node<T, R>>> list, final Writer writer) {
		for (List<Node<T, R>> nodes : list) {
			try {
				for (Node<T, R> node : nodes) {
					writer.write(node + EMPTY_STRING + node.getInComingNodes() + SPACE);
				}
				writer.write("\n");
			} catch (IOException e) {
				logger.error("Error Writing ", e);
			}
		}
	}
}
