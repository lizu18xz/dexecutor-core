package com.github.dexecutor.executor;

import java.util.concurrent.ExecutorService;

import com.github.dexecutor.executor.graph.CyclicValidator;
import com.github.dexecutor.executor.graph.LevelOrderTraversar;
import com.github.dexecutor.executor.graph.Traversar;
import com.github.dexecutor.executor.graph.Validator;
import static com.github.dexecutor.executor.support.Preconditions.*;

public class DependentTasksExecutorConfig<T extends Comparable<T>> {

	private ExecutorService executorService;
	private TaskProvider<T> taskProvider;
	private Validator<T> validator = new CyclicValidator<T>();
	private Traversar<T> traversar = new LevelOrderTraversar<T>();

	public DependentTasksExecutorConfig(final ExecutorService executorService, final TaskProvider<T> taskProvider) {
		this.executorService = executorService;
		this.taskProvider = taskProvider;
	}

	void validate() {
		checkNotNull(this.executorService, "Executer Service should not be null");
		checkNotNull(this.taskProvider, "Task Provider should not be null");
		checkNotNull(this.validator, "Validator should not be null");
		checkNotNull(this.validator, "Traversar should not be null");
	}

	ExecutorService getExecutorService() {
		return executorService;
	}

	TaskProvider<T> getTaskProvider() {
		return taskProvider;
	}

	Validator<T> getValidator() {
		return validator;
	}
	public void setValidator(final Validator<T> validator) {
		this.validator = validator;
	}
	Traversar<T> getTraversar() {
		return traversar;
	}
	public void setTraversar(final Traversar<T> traversar) {
		this.traversar = traversar;
	}
}
