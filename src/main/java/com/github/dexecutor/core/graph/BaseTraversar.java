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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTraversar <T extends Comparable<T>, R> implements Traversar<T, R> {

	private static final Logger logger = LoggerFactory.getLogger(BaseTraversar.class);

	private static final String SPACE = " ";
	private static final String EMPTY_STRING = "";

	private List<Node<T, R>> processed = new ArrayList<Node<T, R>>();

	protected List<List<List<Node<T, R>>>> traverseLevelOrder(final Dag<T, R> graph) {
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

	protected void printGraph(final List<List<Node<T, R>>> list, final Writer writer) {
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
