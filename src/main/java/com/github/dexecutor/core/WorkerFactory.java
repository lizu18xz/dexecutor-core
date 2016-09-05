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

import java.util.concurrent.Callable;

import com.github.dexecutor.core.DependentTasksExecutor.ExecutionBehavior;
import com.github.dexecutor.core.graph.Node;
/**
 * 
 * @author nmohammad
 *
 */
public class WorkerFactory {

	public static <T extends Comparable<T>, R> Callable<Node<T, R>> newWorker(final TaskProvider<T, R> taskProvider, final Node<T, R> graphNode, final ExecutionBehavior behavior) {
		if (ExecutionBehavior.NON_TERMINATING.equals(behavior)) {
			return new NonTerminatingTask<T, R>(taskProvider, graphNode);
		} else if (ExecutionBehavior.RETRY_ONCE_TERMINATING.equals(behavior)) { 
			return new RetryOnceAndTerminateTask<T,R>(taskProvider, graphNode);
		} else {
			return new TerminatingTask<T,R>(taskProvider, graphNode);
		}
	}
}
