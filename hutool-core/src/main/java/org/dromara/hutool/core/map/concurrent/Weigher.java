/*
 * Copyright 2010 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.hutool.core.map.concurrent;

/**
 * A class that can determine the selector of a value. The total selector threshold
 * is used to determine when an eviction is required.
 *
 * @param <V> 值类型
 * @author ben.manes@gmail.com (Ben Manes)
 * @see <a href="http://code.google.com/p/concurrentlinkedhashmap/">
 * http://code.google.com/p/concurrentlinkedhashmap/</a>
 */
public interface Weigher<V> {

	/**
	 * Measures an object's selector to determine how many units of capacity that
	 * the value consumes. A value must consume a minimum of one unit.
	 *
	 * @param value the object to weigh
	 * @return the object's selector
	 */
	int weightOf(V value);
}
