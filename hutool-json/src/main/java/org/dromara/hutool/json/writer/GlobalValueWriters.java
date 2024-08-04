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

import org.dromara.hutool.core.lang.Assert;

import java.util.*;

/**
 * 全局自定义对象写出<br>
 * 用户通过此全局定义，可针对某些特殊对象
 *
 * @author looly
 * @since 6.0.0
 */
public class GlobalValueWriters {

	private static final List<JSONValueWriter> valueWriterList;

	static {
		valueWriterList = Collections.synchronizedList(new LinkedList<>());
		valueWriterList.add(NumberValueWriter.INSTANCE);
		valueWriterList.add(DateValueWriter.INSTANCE);
		valueWriterList.add(BooleanValueWriter.INSTANCE);
		valueWriterList.add(JSONStringValueWriter.INSTANCE);
		valueWriterList.add(ClassValueWriter.INSTANCE);
		valueWriterList.add(JdkValueWriter.INSTANCE);
	}

	/**
	 * 加入自定义的对象值写出规则，自定义规则总是优先
	 *
	 * @param valueWriter 自定义对象写出实现
	 */
	public static void add(final JSONValueWriter valueWriter) {
		valueWriterList.add(0, Assert.notNull(valueWriter));
	}

	/**
	 * 获取自定义对象值写出规则
	 *
	 * @param value 值，{@code null}表示需要自定义null的输出
	 * @return 自定义的 {@link JSONValueWriter}
	 */
	public static JSONValueWriter get(final Object value) {
		if (value instanceof JSONValueWriter) {
			return (JSONValueWriter) value;
		}

		return valueWriterList.stream().filter(writer -> writer.test(value)).findFirst().orElse(null);
	}
}
