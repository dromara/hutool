/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
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

/**
 * Boolean类型的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class BooleanValueWriter implements JSONValueWriter<Boolean> {
	/**
	 * 单例对象
	 */
	public static final BooleanValueWriter INSTANCE = new BooleanValueWriter();

	@Override
	public void write(final JSONWriter writer, final Boolean bool) {
		writer.writeRaw(bool.toString());
	}
}
