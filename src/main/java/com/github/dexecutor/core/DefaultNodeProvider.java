package com.github.dexecutor.core;

import com.github.dexecutor.core.graph.Node;
import com.github.dexecutor.core.graph.NodeProvider;

public class DefaultNodeProvider<T, R> implements NodeProvider<T, R> {
	
	private DexecutorState<T, R> dexecutorState;
	
	public DefaultNodeProvider(DexecutorState<T, R> dexecutorState) {
		this.dexecutorState = dexecutorState;
	}

	@Override
	public Node<T, R> getGraphNode(T id) {
		return dexecutorState.getGraphNode(id);
	}
}
