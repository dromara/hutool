/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
