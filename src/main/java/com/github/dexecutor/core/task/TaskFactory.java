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

package com.github.dexecutor.core.task;

/**
 * A factory to create Worker task for dexecutor based on @ExecutionBehavior
 * 
 * @author Nadeem Mohammad
 *
 */
public class TaskFactory {
	
	/**
	 * 
	 * @param task the task that should be wrapped
	 * @return the new worker task wrapped with {@code LoggerTask}
	 * @param <T> Type of Node/Task ID
     * @param <R> Type of Node/Task result
	 */
	public static <T, R> Task<T, R> newWorker(final Task<T, R> task) {
		return new LoggerTask<T, R>(task);
	}
}
