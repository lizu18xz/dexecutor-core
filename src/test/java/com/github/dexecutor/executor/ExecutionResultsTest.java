package com.github.dexecutor.executor;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ExecutionResultsTest {

	@Test
	public void testThereIsParentResult() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS));
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
		results.add(new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS));
		assertNotNull(results.getFirst());
	}
	
	@Test
	public void testGetAllShouldReturnOneObject() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS));
		assertThat(results.getAll().size(), equalTo(1));
	}
	
	@Test
	public void testToStringIsNotNull() {
		ExecutionResults<Integer, Integer> results = new ExecutionResults<Integer, Integer>();
		results.add(new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS));
		assertNotNull(results.toString());
	}

}
