package com.dexecutor.executor.support;

public final class Preconditions {

	private Preconditions() {

	}

	public static <T> void checkNotNull(T reference, String msg) {
		if (reference == null) {
			throw new IllegalArgumentException(msg);
		}
	}
}
