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
