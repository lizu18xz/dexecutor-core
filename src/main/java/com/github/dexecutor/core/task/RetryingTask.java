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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A dexecutor task which terminates the graph execution after one retry 
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Node Id Type
 * @param <R> Result type
 */
class RetryingTask<T extends Comparable<T>, R> extends  AbstractDelegatingTask<T, R>  {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(RetryingTask.class);

	public RetryingTask(final Task<T, R> task) {
		super(task);
	}

	@Override
	public R execute() {
		getTargetTask().setConsiderExecutionError(false);
		
		int count = 1;
		while(count <= getTargetTask().getRetryCount()) {
			try {
				return getTargetTask().execute();
			} catch(Exception ex) {
				logger.error("Exception caught, executing node # " + getId() + " Retry would happen", ex);
			}
			count++;
		}
		getTargetTask().setConsiderExecutionError(true);
		throw new TaskExecutionException("Retried Task " + getId() + " " +  getTargetTask().getRetryCount() + " times");
	}
}
