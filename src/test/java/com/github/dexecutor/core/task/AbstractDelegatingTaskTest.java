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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AbstractDelegatingTaskTest {

	private Task<Integer, Integer> task;
	private AbstractDelegatingTask<Integer, Integer> delegatingTask;

	@Before
	public void doBeforeEachTestCase() {
		task = new DummyTask();
		delegatingTask = new DummyDelegatingTask(task);
	}
	
	@Test
	public void testId() {
		delegatingTask.setId(1);
		assertThat(this.task.getId(), equalTo(delegatingTask.getId()));
	}

	@Test
	public void testExecutionBehavior() {
		//delegatingTask.setExecutionBehavior(ExecutionBehavior.NON_TERMINATING);
		//assertThat(this.task.getExecutionBehavior(), equalTo(ExecutionBehavior.NON_TERMINATING));
	}
	
	@Test
	public void testConsiderExecutionError() {
		delegatingTask.setConsiderExecutionError(true);
		assertThat(this.task.shouldConsiderExecutionError(), equalTo(true));
	}
	
	
	@Test
	public void testShouldConsiderExecutionError() {
		assertThat(this.delegatingTask.shouldConsiderExecutionError(), equalTo(this.delegatingTask.getTargetTask().shouldConsiderExecutionError()));
	}
	
	@Test
	public void testShouldExecute() {
		assertThat(this.delegatingTask.shouldExecute(null), equalTo(this.delegatingTask.getTargetTask().shouldExecute(null)));
	}

	private static class DummyDelegatingTask extends AbstractDelegatingTask<Integer, Integer> {

		private static final long serialVersionUID = 1L;

		public DummyDelegatingTask(Task<Integer, Integer> task) {
			super(task);
		}

		@Override
		public Integer execute() {
			return null;
		}
		
	}
	
	private static class DummyTask extends Task<Integer, Integer> {

		private static final long serialVersionUID = 1L;

		@Override
		public Integer execute() {
			return null;
		}
		
	}
}
