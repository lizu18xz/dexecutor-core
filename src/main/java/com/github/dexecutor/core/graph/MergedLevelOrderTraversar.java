package com.github.dexecutor.core.graph;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * A Traversar which does level order traversal of the given graph and merges each level
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class MergedLevelOrderTraversar<T extends Comparable<T>, R> extends BaseTraversar<T, R> {

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
}
