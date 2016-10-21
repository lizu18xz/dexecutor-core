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
/**
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public class StringTraversarAction<T extends Comparable<T>, R> implements TraversarAction <T, R> {

	private final StringBuilder builder;
	
	public StringTraversarAction(final StringBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void onNode(Node<T, R> node) {
		builder.append(node).append(node.getInComingNodes()).append(" ");				
	}

	@Override
	public void onNewPath(int pathNumber) {
		builder.append("Path #").append(pathNumber);				
	}
	
	@Override
	public void onNewLevel(int levelNumber) {
		builder.append("\n");				
	}
}
