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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.ExecutionResults;

public class ExecutionResultsTest {

	@Test
	public void testThereIsParentResult() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1));
		assertThat(results.hasAnyParentResult(), equalTo(true));
	}
	
	@Test
	public void testThereIsNoParentResult() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		assertThat(results.hasAnyParentResult(), equalTo(false));
	}
	
	@Test
	public void testGetFirstIsNull() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();		
		assertNull(results.getFirst());
	}
	
	@Test
	public void testGetFirstIsNotNull() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1));
		assertNotNull(results.getFirst());
	}
	
	@Test
	public void testGetAllShouldReturnOneObject() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1));
		assertThat(results.getAll().size(), equalTo(1));
	}
	
	@Test
	public void testToStringIsNotNull() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1));
		assertNotNull(results.toString());
	}

}
