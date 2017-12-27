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
public interface TraversarAction<T, R> {
	/**
	 * 
	 * @param pathNumber The path number in graph traversing
	 */
	void onNewPath(int pathNumber);
	/**
	 * 
	 * @param levelNumber The level number in graph traversing
	 */
	void onNewLevel(int levelNumber);
	/**
	 * 
	 * @param node The node in graph traversing
	 */
	void onNode(final Node<T, R> node);
}
