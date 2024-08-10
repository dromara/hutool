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
