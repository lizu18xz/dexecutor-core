package com.github.dexecutor.core;

import java.util.concurrent.TimeUnit;

public class Duration {

	private final long duration;
	private final TimeUnit timeUnit;

	public static final Duration MINIMAL_DURATION = new Duration(1, TimeUnit.NANOSECONDS);

	public Duration(long duration, final TimeUnit timeUnit) {
		this.duration = duration;
		this.timeUnit = timeUnit;
	}
	/**
	 * 
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * 
	 * @return {@TimeUnit} of the {@duration}
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
