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

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.github.dexecutor.core.TaskProvider;
import com.github.dexecutor.core.TaskProvider.Task;
import com.github.dexecutor.core.graph.Node;
/**
 * A dexecutor task which terminates the graph execution after exception 
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Node Id Type
 * @param <R> Result type
 */
class TerminatingTask<T extends Comparable<T>, R> implements Callable<Node<T, R>> , Serializable {

	private static final long serialVersionUID = 1L;
	private Node<T, R> node;
	private TaskProvider<T, R> taskProvider;

	public TerminatingTask(final TaskProvider<T, R> taskProvider, final Node<T, R> graphNode) {
		this.taskProvider = taskProvider;
		this.node = graphNode;
	}

	public Node<T, R> call() throws Exception {
		Task<T, R> task = new ExecutorTask<T, R>(node, this.taskProvider.provid(node.getValue()));
		task.setConsiderExecutionError(true);
		R result = task.execute();
		this.node.setResult(result);
		return this.node;
	}	

}