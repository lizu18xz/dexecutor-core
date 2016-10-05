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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Executors;

import org.junit.Test;

import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;

public class DependentTasksExecutorConfigTest {
	
	
	@Test
	public void immediateRetryPoolThreadsCountIsOne() {
		DependentTasksExecutorConfig<String, String> config = new DependentTasksExecutorConfig<String, String>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setImmediateRetryPoolThreadsCount(1);
		assertThat(config.getImmediateRetryPoolThreadsCount(), equalTo(1));
	}
	
	@Test
	public void scheduledRetryPoolThreadsCount() {
		DependentTasksExecutorConfig<String, String> config = new DependentTasksExecutorConfig<String, String>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setScheduledRetryPoolThreadsCount(1);;
		assertThat(config.getScheduledRetryPoolThreadsCount(), equalTo(1));
	}

	@Test(expected= IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenValidatorIsNull() {
		DependentTasksExecutorConfig<String, String> config = new DependentTasksExecutorConfig<String, String>(Executors.newCachedThreadPool(), newTaskProvider());
		config.setValidator(null);
		config.validate();
	}


	@Test(expected= IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenTraversarIsNull() {
		DependentTasksExecutorConfig<String, String> config = new DependentTasksExecutorConfig<String, String>(new DefaultExecutionEngine<String, String>(Executors.newCachedThreadPool()), newTaskProvider());
		config.setTraversar(null);
		config.validate();
	}

	private TaskProvider<String, String> newTaskProvider() {
		return new TaskProvider<String, String>() {
			
			@Override
			public Task<String, String> provideTask(String id) {
				return null;
			}
		};
	}

}
