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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.reflect.ClassUtil;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * JDK内置类型的值写出器
 *
 * <p>
 * 枚举类和JDK内部类直接使用toString输出，不做转换。
 * </p>
 *
 * <p>
 * {@link SQLException}实现了Iterable导致被识别为列表，可能造成死循环，此处按照字符串处理，见：
 * https://github.com/dromara/hutool/issues/1399
 * </p>
 *
 * @author looly
 * @since 6.0.0
 */
public class JdkValueWriter implements JSONValueWriter {
	/**
	 * 单例对象
	 */
	public static final JdkValueWriter INSTANCE = new JdkValueWriter();

	@Override
	public boolean test(final Object value) {
		if (null == value) {
			return false;
		}
		if (value instanceof Enum || value instanceof SQLException) {
			return true;
		}

		// 可转换为JSONObject和JSONArray的对象
		if (value instanceof Map
			|| value instanceof Map.Entry
			|| value instanceof Iterable
			|| ArrayUtil.isArray(value)
			|| value instanceof Optional
		) {
			return false;
		}

		// Java内部类不做转换
		return ClassUtil.isJdkClass(value.getClass());
	}

	@Override
	public void write(final JSONWriter writer, final Object value) {
		writer.writeQuoteStrValue(value.toString());
	}
}
