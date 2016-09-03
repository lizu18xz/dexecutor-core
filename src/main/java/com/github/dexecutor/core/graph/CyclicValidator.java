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

import java.util.ArrayList;
import java.util.Collection;

import com.github.dexecutor.core.graph.Graph.Node;

/**
 * A {@code Validator} which does cyclic checks
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class CyclicValidator<T extends Comparable<T>, R> implements Validator<T, R> {

	private Collection<Graph.Node<T, R>> processedNodes = new ArrayList<Graph.Node<T, R>>();
	private Collection<Graph.Node<T, R>> onStackNodes = new ArrayList<Graph.Node<T, R>>();

	public void validate(final Graph<T, R> graph) {
		doProcess(graph.allNodes());
	}

	private void doProcess(final Collection<Graph.Node<T, R>> nodes) {
		for (Graph.Node<T, R> node : nodes) {
			detectCycle(node);
		}
	}

	private void detectCycle(final Node<T, R> node) {
		this.processedNodes.add(node);
		this.onStackNodes.add(node);
		doDepthFirstTraversal(node);
		this.onStackNodes.remove(node);
	}

	private void doDepthFirstTraversal(final Node<T, R> node) {
		for (Node<T, R> adjNode : node.getOutGoingNodes()) {
			if (!isAlreadyProcessed(adjNode)) {
				detectCycle(adjNode);
			} else if (isOnStack(adjNode)) {
				throw new IllegalArgumentException("Cycle Detected " + node + " With " + adjNode);
			}
		}
	}

	private boolean isAlreadyProcessed(final Node<T, R> node) {
		return this.processedNodes.contains(node);
	}

	private boolean isOnStack(final Node<T, R> node) {
		return this.onStackNodes.contains(node);
	}
}
