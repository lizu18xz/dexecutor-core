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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.TaskExecutionException;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class DefaultExecutionEngineTest {

	private ExecutionEngine<Integer, Integer> executionEngine;

	@Before
	public void doBeforeEachTestCase() {
		this.executionEngine = new DefaultExecutionEngine<Integer, Integer>(Executors.newCachedThreadPool());
	}

	@Test
	public void itIsNotDistributed() {
		assertThat(this.executionEngine.isDistributed(), equalTo(false));
	}

	@Test
	public void toStringIsNotNull() {
		assertNotNull(this.executionEngine.toString());
	}

	@Test(expected = TaskExecutionException.class)
	public void processResult() throws InterruptedException {
		new MockedCompletionService();

		assertNotNull(this.executionEngine.processResult());
	}

	private static class MockedCompletionService
			extends MockUp<ExecutorCompletionService<ExecutionResult<Integer, Integer>>> {
	
		@Mock
		public void $init(Executor executor) {

		}
	
		@Mock
		public Future<ExecutionResult<Integer, Integer>> take() throws InterruptedException {
			throw new InterruptedException();
		}
	}
}
