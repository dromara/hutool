/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.writer;

/**
 * {@link Class}类型的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class ClassValueWriter implements JSONValueWriter {
	/**
	 * 单例对象
	 */
	public static final ClassValueWriter INSTANCE = new ClassValueWriter();

	@Override
	public boolean test(final Object value) {
		return value instanceof Class;
	}

	@Override
	public void write(final JSONWriter writer, final Object value) {
		writer.writeQuoteStrValue(((Class<?>) value).getName());
	}
}
