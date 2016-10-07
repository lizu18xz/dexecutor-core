package com.github.dexecutor.core.task;

import org.junit.Test;

import mockit.Deencapsulation;

public class TaskFactoryTest {

	@Test
	public void createInstance() {
		Deencapsulation.newInstance(TaskFactory.class);
	}
}
