package com.github.dexecutor.executor.support;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ThreadPoolUtilTest {

	@Test
	public void testIoIntesivePoolSize() {
		double blockingCoefficient = 0.9;
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		int expectedPoolSize = (int) (numberOfCores / (1 - blockingCoefficient));
		
		assertThat(ThreadPoolUtil.ioIntesivePoolSize(), equalTo(expectedPoolSize));
	}

}
