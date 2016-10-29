/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dexecutor.core.support;

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
	 * 
	 * Checks if the reference is null, and if that is the case throws
	 * IllegalArgumentException
	 * @param <T> Type of refrence
	 * @param reference the  reference to check
	 * @param msg the message to spit
	 * @throws IllegalArgumentException
	 *             if the reference is null
	 */
	public static <T> void checkNotNull(final T reference, final String msg) {
		if (reference == null) {
			throw new IllegalArgumentException(msg);
		}
	}

	/**
	 * Ensures the truth of an expression involving one or more parameters to
	 * the calling method.
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorMessage
	 *            the exception message to use if the check fails;
	 * @throws IllegalArgumentException
	 *             if {@code expression} is false
	 */
	public static void checkArgument(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}
}
