package com.github.dexecutor.executor.support;

import org.junit.Test;

public class PreconditionsTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentException() {
		Preconditions.checkNotNull(null, "");
	}
	
	@Test
	public void shouldNotThrowIllegalArgumentException() {
		Preconditions.checkNotNull(new Object(), "");
	}

}
