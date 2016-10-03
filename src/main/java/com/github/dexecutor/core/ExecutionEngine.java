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

import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskExecutionException;
/**
 * An Executor is the main execution engine, where in all the tasks are executed
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface ExecutionEngine<T extends Comparable<T>, R> {
	/**
	 * Submits the task for execution, the method is expected to return immediately.
	 * 
	 * @param @Task to be submitted for execution 
	 */
    void submit(final Task<T, R> task);
    
    /**
     * This method is expected to block, if there are no execution result, otherwise return the result immediately.
     * 
     * @return @ExecutionResult returns the execution result.     * 
     * @throws TaskExecutionException
     */    
    ExecutionResult<T, R> processResult() throws TaskExecutionException;
    /**
	 * Hints dexecutor if the execution engine is distributed or not
	 * @return {@code true} If this execution engine is a distributed
	 * {@code false} it it is non distributed
	 */
    boolean isDistributed();
    
    boolean isAnyTaskInError();
}
