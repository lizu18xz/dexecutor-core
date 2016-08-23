package com.github.dexecutor.executor;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ExecutionResultTest {
	
	@Test
	public void testExecutionResultIdShouldBeOne() {
		ExecutionResult<Integer, Integer> result = new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS);
		assertThat(result.getId(), equalTo(1));
	}
	
	@Test
	public void testExecutionResultToBeOne() {
		ExecutionResult<Integer, Integer> result = new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS);
		assertThat(result.getResult(), equalTo(1));
	}

	@Test
	public void testExecutionResultShouldBeSuccess() {
		ExecutionResult<Integer, Integer> result = new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SUCCESS);
		assertThat(result.isSuccess(), equalTo(true));
		assertThat(result.getId(), equalTo(1));
		assertThat(result.getResult(), equalTo(1));
	}
	
	@Test
	public void testExecutionResultShouldBeErrored() {
		ExecutionResult<Integer, Integer> result = new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.ERRORED);
		assertThat(result.isErrored(), equalTo(true));
	}
	
	@Test
	public void testExecutionResultShouldBeSkipped() {
		ExecutionResult<Integer, Integer> result = new ExecutionResult<Integer, Integer>(1, 1, ExecutionStatus.SKIPPED);
		assertThat(result.isSkipped(), equalTo(true));
	}

}
