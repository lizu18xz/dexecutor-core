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
 * A Task Provider provides Tasks to be executed, when it comes to execution
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
public interface TaskProvider <T extends Comparable<T>, R> {
	/**
	 * Given the node id, returns the task to be executed, while building graph only the node ids are required, when it comes to execution Task objects would be constructed
	 * 
	 * @param id task id
	 * @return task the task which should be executed
	 */
	Task<T, R> provideTask(final T id);	
}
