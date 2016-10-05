package com.github.dexecutor.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ExecutionConfigTest {

	@Test
	public void retryDelayIsOne() {
		ExecutionConfig config = new ExecutionConfig().immediateRetrying(1);
		assertThat(config.getRetryCount(), equalTo(1));
	}
	
	@Test
	public void executionBehaviorIsRetrying() {
		ExecutionConfig config = new ExecutionConfig().immediateRetrying(1);
		assertThat(config.getExecutionBehavior(), equalTo(ExecutionBehavior.IMMEDIATE_RETRY_TERMINATING));
	}
	
	@Test
	public void isNonterminating() {
		ExecutionConfig config = ExecutionConfig.NON_TERMINATING;
		assertThat(config.isTerminating(), equalTo(false));
	}
	
	@Test
	public void isterminating() {
		ExecutionConfig config = ExecutionConfig.TERMINATING;
		assertThat(config.isTerminating(), equalTo(true));
	}

}
