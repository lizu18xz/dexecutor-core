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

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dexecutor.core.graph.LevelOrderTraversar;
import com.github.dexecutor.core.graph.MergedLevelOrderTraversar;
import com.github.dexecutor.core.graph.Traversar;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskExecutionException;
import com.github.dexecutor.core.task.TaskProvider;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

/**
 * 
 * @author Nadeem Mohammad
 *
 */
@RunWith(JMockit.class)
public class DexecutorTest {

	@Test
	public void testPrint() {
		DefaultDexecutor<Integer, Integer> executor = newTaskExecutor(new LevelOrderTraversar<Integer, Integer>());
		executor.addDependency(1, 2);
		StringWriter writer = new StringWriter();
		executor.print(new LevelOrderTraversar<Integer, Integer>(), writer);
		assertThat(writer.toString(), equalTo("Path #0\n1[] \n2[1] \n\n"));
		StringWriter wr = new StringWriter();
		executor.print(new LevelOrderTraversar<Integer, Integer>(), wr);
		assertThat(wr.toString(), equalTo("Path #0\n1[] \n2[1] \n\n"));
	}
	
	@Test
	public void testMergedPrint() {
		DefaultDexecutor<Integer, Integer> executor = newTaskExecutor(new MergedLevelOrderTraversar<Integer, Integer>());
		executor.addDependency(1, 2);
		StringWriter writer = new StringWriter();
		executor.print(new MergedLevelOrderTraversar<Integer, Integer>(), writer);
		assertThat(writer.toString(), equalTo("1[] \n2[1] \n"));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExectionRunningTeminatedExecutor() {

		final DefaultDexecutor<Integer, Integer> executor = newTaskExecutor(false);		

		executor.execute(new ExecutionConfig().scheduledRetrying(3, new Duration(1, TimeUnit.SECONDS)));
		executor.execute(ExecutionConfig.TERMINATING);
	}

	@Test
	public void shouldThrowExectionAwaitingTermination() {
		new MockedExecutionService();
		
		final DefaultDexecutor<Integer, Integer> executor = newTaskExecutor(false);		

		executor.execute(ExecutionConfig.TERMINATING);
	}
	
	
	private DefaultDexecutor<Integer, Integer> newTaskExecutor(Traversar<Integer, Integer> traversar) {
		DexecutorConfig<Integer, Integer> config = new DexecutorConfig<>(new DefaultExecutionEngine<Integer, Integer>(newExecutor()), new DummyTaskProvider(false));
		//config.setTraversar(traversar);
		return new DefaultDexecutor<Integer, Integer>(config);
	}

	private DefaultDexecutor<Integer, Integer> newTaskExecutor(boolean throwEx) {
		ExecutionEngine<Integer, Integer> engine = new DefaultExecutionEngine<Integer, Integer>(newExecutor());
		return new DefaultDexecutor<Integer, Integer>(engine, new DummyTaskProvider(throwEx));
	}

	private ExecutorService newExecutor() {
		return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
	}

	private static class DummyTaskProvider implements TaskProvider<Integer, Integer> {
		private boolean throwEx;

		public DummyTaskProvider(boolean throwEx) {
			this.throwEx = throwEx;
		}

		public Task<Integer, Integer> provideTask(final Integer id) {

			return new Task<Integer, Integer>() {

				private static final long serialVersionUID = 1L;

				public Integer execute() {
					shouldConsiderExecutionError();
					doExecute(id);
					return id;
				}

				private void doExecute(final Integer id) {
					if (throwEx) {
						if (id == 2) {
							throw new TaskExecutionException("Error Executing task " + id);
						}
					}
				}
				
			};
		}
	}

	private static class MockedExecutionService extends MockUp<ThreadPoolExecutor> {

		/*@Mock
		public void $init(int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue) {
		}*/

		@Mock
		public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException  {
			throw new InterruptedException();
		}
	}
}
