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
package com.github.dexecutor.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.dexecutor.core.graph.Dag;
import com.github.dexecutor.core.graph.DefaultDag;
import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.Traversar;
import com.github.dexecutor.core.graph.TraversarAction;
import com.github.dexecutor.core.graph.Validator;

public class DefaultDexecutorState<T extends Comparable<T>, R> implements DexecutorState <T, R> {

	private Dag<T, R> graph;
	private Phase currentPhase;
	private AtomicInteger nodesCount;
	private Collection<Node<T, R>> processedNodes;
	private Collection<Node<T, R>> discontinuedNodes;

	public DefaultDexecutorState() {
		this.graph =  new DefaultDag<>();
		this.currentPhase = Phase.BUILDING;
		this.nodesCount = new AtomicInteger(0);
		this.processedNodes = new CopyOnWriteArrayList<Node<T, R>>();
		this.discontinuedNodes = new CopyOnWriteArrayList<Node<T, R>>();
	}

	public void addIndependent(final T nodeValue) {
		this.graph.addIndependent(nodeValue);
	}
	
	public void addDependency(final T evalFirstValue, final T evalAfterValue) {
		this.graph.addDependency(evalFirstValue, evalAfterValue);
	}
	
	public void addAsDependentOnAllLeafNodes(final T nodeValue) {
		this.graph.addAsDependentOnAllLeafNodes(nodeValue);
	}
	
	public void addAsDependencyToAllInitialNodes(final T nodeValue) {
		this.graph.addAsDependencyToAllInitialNodes(nodeValue);
	}

	public void setCurrentPhase(final Phase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public Phase getCurrentPhase() {
		return this.currentPhase;
	}
	
	public Set<Node<T, R>> getInitialNodes() {
		return this.graph.getInitialNodes();
	}
	
	public Set<Node<T, R>> getNonProcessedRootNodes() {
		return this.graph.getNonProcessedRootNodes();
	}
	
	public int graphSize() {
		return this.graph.size();
	}
	
	public Node<T, R> getGraphNode(final T id) {
		return this.graph.get(id);
	}

	public void incrementUnProcessedNodesCount() {
		this.nodesCount.incrementAndGet();
	}
	public void decrementUnProcessedNodesCount() {
		this.nodesCount.decrementAndGet();
	}
	
	public int getUnProcessedNodesCount() {
		return this.nodesCount.get();
	}
	
	public boolean shouldProcess(final Node<T, R> node) {
		return !isAlreadyProcessed(node) && allIncomingNodesProcessed(node);
	}

	private boolean isAlreadyProcessed(final Node<T, R> node) {
		return this.processedNodes.contains(node);
	}

	private boolean allIncomingNodesProcessed(final Node<T, R> node) {
		if (node.getInComingNodes().isEmpty() || areAlreadyProcessed(node.getInComingNodes())) {
			return true;
		}
		return false;
	}

	private boolean areAlreadyProcessed(final Set<Node<T, R>> nodes) {
        return this.processedNodes.containsAll(nodes);
    }
	
	public void markProcessingDone(final Node<T, R> node) {
		this.processedNodes.add(node);
	}

	public Collection<Node<T, R>> getProcessedNodes() {
		return new ArrayList<>(this.processedNodes);
	}
	
	public boolean isDiscontinuedNodesNotEmpty() {
		return !this.discontinuedNodes.isEmpty();
	}
	
	public Collection<Node<T, R>> getDiscontinuedNodes() {
		return new ArrayList<Node<T, R>>(this.discontinuedNodes);
	}
	
	public void markDiscontinuedNodesProcessed() {
		this.discontinuedNodes.clear();
	}

	public void processAfterNoError(final Collection<Node<T, R>> nodes) {
		this.discontinuedNodes.addAll(nodes);
	}

	public void print(final Traversar<T, R> traversar, final TraversarAction<T, R> action) {
		traversar.traverse(this.graph, action);
	}

	public void validate(final Validator<T, R> validator) {
		validator.validate(this.graph);		
	}

	@Override
	public void forcedStop() {
			
	}
}