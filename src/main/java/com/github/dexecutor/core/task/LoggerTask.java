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

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * An {@code AbstractDelegatingTask} which logs the execution
 * 
 * @author Nadeem Mohammad
 *
 * @param <T> Type of Node/Task ID
 * @param <R> Type of Node/Task result
 */
class LoggerTask<T, R> extends AbstractDelegatingTask<T, R> implements Serializable {

	private static final long serialVersionUID = 1L;	
	private static final Logger logger = LoggerFactory.getLogger(LoggerTask.class);

	public LoggerTask(final Task<T, R> task) {
		super(task);
	}

	@Override
	public R execute() {
		R result = null;
		logger.debug("Executing Node # {}", this.getId());
		result = this.getTargetTask().execute();
		logger.debug("Node # {}, Execution Done!", this.getId());
		return result;
	}
}
