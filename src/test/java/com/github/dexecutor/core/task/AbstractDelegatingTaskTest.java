package com.github.dexecutor.core.task;

import org.junit.Before;
import org.junit.Test;

import com.github.dexecutor.core.DependentTasksExecutor.ExecutionBehavior;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
	public void testErrored() {
		delegatingTask.setErrored();
		assertThat(this.task.getStatus(), equalTo(ExecutionStatus.ERRORED));
	}
	
	@Test
	public void testSkipped() {
		delegatingTask.setSkipped();
		assertThat(this.task.getStatus(), equalTo(ExecutionStatus.SKIPPED));
	}
	
	@Test
	public void testExecutionBehavior() {
		delegatingTask.setExecutionBehavior(ExecutionBehavior.NON_TERMINATING);
		assertThat(this.task.getExecutionBehavior(), equalTo(ExecutionBehavior.NON_TERMINATING));
	}
	
	@Test
	public void testConsiderExecutionError() {
		delegatingTask.setConsiderExecutionError(true);
		assertThat(this.task.shouldConsiderExecutionError(), equalTo(true));
	}
	
	@Test
	public void testSuccess() {
		assertThat(this.task.getStatus(), equalTo(ExecutionStatus.SUCCESS));
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
