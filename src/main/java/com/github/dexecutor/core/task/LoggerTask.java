package com.github.dexecutor.core.task;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggerTask<T extends Comparable<T>, R> extends AbstractDelegatingTask<T, R> implements Serializable {

	private static final long serialVersionUID = 1L;	
	private static final Logger logger = LoggerFactory.getLogger(LoggerTask.class);

	private int retryCount = 0;

	public LoggerTask(final Task<T, R> task) {
		super(task);
	}

	@Override
	public R execute() {
		R result = null;
		logger.debug("{} Node # {}", msg(this.retryCount), this.getId());
		this.retryCount ++;
		result = this.getTargetTask().execute();
		logger.debug("Node # {}, Execution Done!", this.getId());
		return result;
	}
	
	private String msg(int retryCount) {
		return retryCount > 0 ? "Retrying(" + retryCount+ ") " : "Executing";
	}

}
