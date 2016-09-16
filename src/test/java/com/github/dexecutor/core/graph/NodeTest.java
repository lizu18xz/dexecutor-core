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

package com.github.dexecutor.core.graph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class NodeTest {

	@Test
	public void testNodeIsErrored() {
		Node<Integer, Integer> node = new  Node<Integer, Integer>(1);
		node.setErrored();
		assertThat(node.isErrored(), equalTo(true));
	}
	
	
	@Test
	public void testNodeIsNotErrored() {
		Node<Integer, Integer> node = new  Node<Integer, Integer>(1);
		node.setSuccess();
		assertThat(node.isErrored(), equalTo(false));
	}
	
	@Test
	public void testNodeIsSkipped() {
		Node<Integer, Integer> node = new  Node<Integer, Integer>(1);
		node.setSkipped();
		assertThat(node.isSkipped(), equalTo(true));
	}
	
	@Test
	public void testNodeIsNotSkipped() {
		Node<Integer, Integer> node = new  Node<Integer, Integer>(1);
		node.setErrored();
		assertThat(node.isSkipped(), equalTo(false));
	}
	
	@Test
	public void testNodeIsSuccess() {
		Node<Integer, Integer> node = new  Node<Integer, Integer>(1);
		node.setSuccess();
		assertThat(node.isSuccess(), equalTo(true));
	}
	
	@Test
	public void testNodeIsNotSuccess() {
		Node<Integer, Integer> node = new  Node<Integer, Integer>(1);
		node.setErrored();
		assertThat(node.isSuccess(), equalTo(false));
	}

}
