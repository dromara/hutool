/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json;

import java.util.Iterator;

/**
 * 此类用于在JSONAray中便于遍历JSONObject而封装的Iterable，可以借助foreach语法遍历
 *
 * @author looly
 * @since 4.0.12
 */
public class JSONObjectIter implements Iterable<JSONObject> {

	Iterator<Object> iterator;

	public JSONObjectIter(final Iterator<Object> iterator) {
		this.iterator = iterator;
	}

	@Override
	public Iterator<JSONObject> iterator() {
		return new Iterator<JSONObject>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public JSONObject next() {
				return (JSONObject) iterator.next();
			}

			@Override
			public void remove() {
				iterator.remove();
			}
		};
	}

}
