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

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import com.github.dexecutor.core.DependentTasksExecutor.ExecutionBehavior;
import com.github.dexecutor.core.support.ThreadPoolUtil;

/**
 * 
 * @author Nadeem Mohammad
 *
 */
@Ignore
public class DefaultDependentTasksExecutorIntegrationTest {

	@Test
	public void testDependentTaskExecution() {
		ExecutorService executorService = newExecutor();

		try {
			DefaultDependentTasksExecutor<Integer, Integer> executor = newTaskExecutor(executorService);

			executor.addDependency(1, 2);
			executor.addDependency(1, 2);
			executor.addDependency(1, 3);
			executor.addDependency(3, 4);
			executor.addDependency(3, 5);
			executor.addDependency(3, 6);
			//executor.addDependency(10, 2); // cycle
			executor.addDependency(2, 7);
			executor.addDependency(2, 9);
			executor.addDependency(2, 8);
			executor.addDependency(9, 10);
			executor.addDependency(12, 13);
			executor.addDependency(13, 4);
			executor.addDependency(13, 14);
			executor.addIndependent(11);

			printGraph(executor);

			executor.execute(ExecutionBehavior.RETRY_ONCE_TERMINATING);
			System.out.println("*** Done ***");
		} finally {
			try {
				executorService.shutdownNow();
				executorService.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				
			}
		}
	}

	private void printGraph(DefaultDependentTasksExecutor<Integer, Integer> executor) {
		StringWriter writer = new StringWriter();
		executor.print(writer);
		System.out.println(writer);
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private DefaultDependentTasksExecutor<Integer, Integer> newTaskExecutor(ExecutorService executorService) {
		return new DefaultDependentTasksExecutor<Integer, Integer>(executorService, new SleepyTaskProvider());
	}

	private static class SleepyTaskProvider implements TaskProvider<Integer, Integer> {

		public Task<Integer, Integer> provid(final Integer id) {

			return new Task<Integer, Integer>() {

				public Integer execute() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return id;
				}
			};			
		}		
	}
}
