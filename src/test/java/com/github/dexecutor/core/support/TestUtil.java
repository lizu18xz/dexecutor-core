package com.github.dexecutor.core.support;

import java.util.Collection;

import com.github.dexecutor.core.DefaultDexecutor;
import com.github.dexecutor.core.DefaultDexecutorState;
import com.github.dexecutor.core.graph.Dag;
import com.github.dexecutor.core.graph.Node;

import mockit.Deencapsulation;

public class TestUtil {

	public static Collection<Node<Integer, Integer>> processedNodesOrder(DefaultDexecutor<Integer, Integer> executor) {
		DefaultDexecutorState<Integer, Integer> state = Deencapsulation.getField(executor, "state");
		return Deencapsulation.getField(state, "processedNodes");
	}
	
	public static Dag<Integer, Integer> getGraph(DefaultDexecutor<Integer, Integer> executor) {
		DefaultDexecutorState<Integer, Integer> state = Deencapsulation.getField(executor, "state");
		return Deencapsulation.getField(state, "graph");
	}
}
