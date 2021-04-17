package com.github.dexecutor.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class IdentifiableRunnableFuture<T, V> extends FutureTask<V> {

	private T identifier;

	public IdentifiableRunnableFuture(T identifier, Callable<V> callable) {
		super(callable);
		this.identifier = identifier;
	}
	
    public IdentifiableRunnableFuture(T identifier, Runnable runnable, V result) {
        super(runnable, result);
        this.identifier = identifier;
    }

	public T getIdentifier() {
		return identifier;
	}
}
