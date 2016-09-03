/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dexecutor.core.graph;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.github.dexecutor.core.graph.Graph.Node;

/**
 * A Traversar which does level order traversal of the given graph
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class LevelOrderTraversar<T extends Comparable<T>, R> implements Traversar<T, R> {
	
	private List<Graph.Node<T, R>> processed = new ArrayList<Graph.Node<T, R>>();
	
	public void traverse(final Graph<T, R> graph, final Writer writer) {
		List<List<List<Node<T, R>>>> levelOrderOfGraphs = traverseLevelOrder(graph);
		int i = 0;
		for (List<List<Node<T, R>>> levelOrderOfGraph : levelOrderOfGraphs) {
			try {
				writer.write("Path #" + (i++) + "\n");
				printGraph(levelOrderOfGraph, writer);
				writer.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<List<List<Node<T, R>>>> traverseLevelOrder(final Graph<T, R> graph) {
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
				if (!this.processed.contains(node)) {
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

	private void printGraph(final List<List<Node<T, R>>> list, final Writer writer) {
		for (List<Node<T, R>> nodes : list) {
			try {
				for (Node<T, R> node : nodes) {
					writer.write(node + "" + node.getInComingNodes() + " ");
				}
				writer.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
