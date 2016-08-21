package com.github.dexecutor.executor.support;

/**
 * Support class to check for preconditions
 *  
 * @author Nadeem Mohammad
 *
 */
public final class Preconditions {

	private Preconditions() {

	}

	/**
	 * Checks if the reference is null, and if that is the case throws IllegalArgumentException
	 * @param reference
	 * @param msg
	 * @throws IllegalArgumentException if the reference is null
	 */
	public static <T> void checkNotNull(T reference, String msg) {
		if (reference == null) {
			throw new IllegalArgumentException(msg);
		}
	}
}
