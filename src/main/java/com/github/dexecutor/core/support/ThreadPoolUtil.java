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
 * Support class for thread pool size
 * 
 * @author Nadeem Mohammad
 *
 */
public final class ThreadPoolUtil {
	
	private ThreadPoolUtil() {
		
	}
	/**
	 * Each tasks blocks 90% of the time, and works only 10% of its
	 *	lifetime. That is, I/O intensive pool
	 * @return io intesive Thread pool size
	 */
	public static int ioIntesivePoolSize() {
		
		double blockingCoefficient = 0.9;
		return poolSize(blockingCoefficient);
	}

	/**
	 * 
	 * Number of threads = Number of Available Cores / (1 - Blocking
	 * Coefficient) where the blocking coefficient is between 0 and 1.
	 * 
	 * A computation-intensive task has a blocking coefficient of 0, whereas an
	 * IO-intensive task has a value close to 1,
	 * so we don't have to worry about the value reaching 1.
	 *  @param blockingCoefficient the coefficient
	 *  @return Thread pool size
	 */
	public static int poolSize(double blockingCoefficient) {
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		int poolSize = (int) (numberOfCores / (1 - blockingCoefficient));
		return poolSize;
	}
}
