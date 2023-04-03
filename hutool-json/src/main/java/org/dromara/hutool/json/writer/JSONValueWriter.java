/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.writer;

/**
 * JSON的值自定义写出
 *
 * @param <T> 写出的对象类型
 * @author looly
 * @since 6.0.0
 */
public interface JSONValueWriter<T> {

	/**
	 * 使用{@link JSONWriter} 写出对象
	 *
	 * @param writer {@link JSONWriter}
	 * @param value  被写出的值
	 */
	void write(JSONWriter writer, T value);
}
