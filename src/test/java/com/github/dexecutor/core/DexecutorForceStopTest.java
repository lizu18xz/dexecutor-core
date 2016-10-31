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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;
/**
 * 
 * @author Nadeem Mohammad
 *
 */
public class DexecutorForceStopTest {

	@Test(expected = IllegalStateException.class)
	public void testDependentTaskExecution() {

		ExecutorService executorService = newExecutor();

		try {
			DexecutorConfig<Integer, Integer> config = new DexecutorConfig<>(executorService, new SleepyTaskProvider());
			DefaultDexecutor<Integer, Integer> executor = new DefaultDexecutor<Integer, Integer>(config) {
				protected boolean shouldContinueProcessingNodes() {
					return false;
				}
			};
			executor.addDependency(1, 2);
			executor.execute(ExecutionConfig.NON_TERMINATING);
					
		} finally {
			try {
				executorService.shutdownNow();
				executorService.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {

			}
		}
	}
	
	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {

		public Task<Integer, Integer> provideTask(final Integer id) {

			return new Task<Integer, Integer>() {

				private static final long serialVersionUID = 1L;

				public Integer execute() {
					return id;
				}
			};
		}
	}

}
