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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Traversar which does level order traversal of the given graph
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class LevelOrderTraversar<T extends Comparable<T>, R> extends BaseTraversar<T, R> {

	private static final Logger logger = LoggerFactory.getLogger(LevelOrderTraversar.class);

	public void traverse(final Dag<T, R> graph, final Writer writer) {
		List<List<List<Node<T, R>>>> levelOrderOfGraphs = traverseLevelOrder(graph);
		int i = 0;
		for (List<List<Node<T, R>>> levelOrderOfGraph : levelOrderOfGraphs) {
			try {
				writer.write("Path #" + (i++) + "\n");
				printGraph(levelOrderOfGraph, writer);
				writer.write("\n");
			} catch (IOException e) {
				logger.error("Error Writing ", e);
			}
		}
	}
}
