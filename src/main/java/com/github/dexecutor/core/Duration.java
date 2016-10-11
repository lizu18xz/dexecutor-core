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
package com.github.dexecutor.core;

import java.util.concurrent.TimeUnit;
/**
 * 
 * @author Nadeem Mohammad
 *
 */
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
