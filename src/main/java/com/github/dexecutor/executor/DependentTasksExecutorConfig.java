package com.github.dexecutor.executor;

import java.util.concurrent.ExecutorService;

import com.github.dexecutor.executor.graph.CyclicValidator;
import com.github.dexecutor.executor.graph.LevelOrderTraversar;
import com.github.dexecutor.executor.graph.Traversar;
import com.github.dexecutor.executor.graph.Validator;
import static com.github.dexecutor.executor.support.Preconditions.*;

/**
 * <p>Configuration Object for Dexecutor framework. At a minimum it needs {@code ExecutorService} and {@code TaskProvider}, rest are optional and takes default values</p>
 * <p>This provides way to hook in your own {@code Validator} and {@code Traversar}</p>
 * 
 * @author Nadeem Mohammad
 *
 * @param <T>
 */
public class DependentTasksExecutorConfig<T extends Comparable<T>> {
	/**
	 * executorService is the main platform on which tasks are executed
	 */
	private ExecutorService executorService;
	/**
	 * When it comes to task execution, task provider would be consulted to provide task objects for execution
	 */
	private TaskProvider<T> taskProvider;
	/**
	 * Validator for validating the consturcted graph, defaults to detecting Cyclic checks
	 */
	private Validator<T> validator = new CyclicValidator<T>();
	/**
	 * Traversar used to traverse the graph while printing it on a Writer
	 */
	private Traversar<T> traversar = new LevelOrderTraversar<T>();
	/**
	 * Construct the object with mandatory params, rest are optional
	 * @param executorService
	 * @param taskProvider
	 */
	public DependentTasksExecutorConfig(final ExecutorService executorService, final TaskProvider<T> taskProvider) {
		this.executorService = executorService;
		this.taskProvider = taskProvider;
	}

	void validate() {
		checkNotNull(this.executorService, "Executer Service should not be null");
		checkNotNull(this.taskProvider, "Task Provider should not be null");
		checkNotNull(this.validator, "Validator should not be null");
		checkNotNull(this.traversar, "Traversar should not be null");
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
	/**
	 * change the validator to that of specified
	 * @param validator
	 */
	public void setValidator(final Validator<T> validator) {
		this.validator = validator;
	}
	Traversar<T> getTraversar() {
		return traversar;
	}
	/**
	 * Change the traversar to that of specified
	 * @param traversar
	 */
	public void setTraversar(final Traversar<T> traversar) {
		this.traversar = traversar;
	}
}
