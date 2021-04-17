package com.github.dexecutor.core;

import java.util.concurrent.Callable;

public interface IdentifiableCallable<T, V> extends Callable<V>{
	
	T getIdentifier();
}
