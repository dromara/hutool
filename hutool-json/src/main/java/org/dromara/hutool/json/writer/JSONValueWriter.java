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

package org.dromara.hutool.json.writer;

import java.util.function.Predicate;

/**
 * JSON的值自定义写出，通过自定义实现此接口，实现对象自定义写出字符串形式<br>
 * 如自定义的一个CustomBean，我只希望输出id的值，此时自定义此接口。<br>
 * 其中{@link JSONValueWriter#test(Object)}负责判断何种对象使用此规则，{@link JSONValueWriter#write(JSONWriter, Object)}负责写出规则。
 *
 * <p>
 * 注意：使用{@link GlobalValueWriters#add(JSONValueWriter)}加入全局转换规则后，在JSON对象中，自定义对象不会被转换为JSON。
 * 而是原始对象存在，只有在生成JSON字符串时才序列化。
 * </p>
 *
 * @author looly
 * @since 6.0.0
 */
public interface JSONValueWriter extends Predicate<Object> {

	/**
	 * 使用{@link JSONWriter} 写出对象
	 *
	 * @param writer {@link JSONWriter}
	 * @param value  被写出的值
	 */
	void write(JSONWriter writer, Object value);
}
