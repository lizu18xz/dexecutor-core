package com.github.dexecutor.executor;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class TaskExecutionExceptionTest {

	@Test
	public void testMsgShouldBeAsPassed() {
		String msg = "MSG";
		TaskExecutionException ex = new TaskExecutionException(msg);
		assertThat(ex.getMessage(), equalTo(msg));
	}
}
