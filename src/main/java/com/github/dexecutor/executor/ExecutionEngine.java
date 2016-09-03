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

package com.github.dexecutor.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.github.dexecutor.executor.graph.Graph.Node;
/**
 * An Executor is the main execution engine, where in all the tasks are executed
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 * @param <R>
 */
public interface ExecutionEngine<T, R> {

    /**
     * Submits a value-returning task for execution and returns a Future
     * representing the pending results of the task.  Upon completion,
     * this task may be taken.
     *
     * @param task the task to submit
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if the task is null
     */
    Future<Node<T, R>> submit(Callable<Node<T, R>> task);

    /**
     * Retrieves and removes the Future representing the next
     * completed task, waiting if none are yet present.
     *
     * @return the Future representing the next completed task
     * @throws InterruptedException if interrupted while waiting
     */
    Future<Node<T, R>> take() throws InterruptedException;

    /**
     * Returns <tt>true</tt> if this executor has been shut down.
     *
     * @return <tt>true</tt> if this executor has been shut down
     */
    boolean isShutdown();
}
