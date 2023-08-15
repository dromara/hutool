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

import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.json.JSONConfig;

/**
 * 数字类型的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class NumberValueWriter implements JSONValueWriter<Number> {
	/**
	 * 单例对象
	 */
	public static final NumberValueWriter INSTANCE = new NumberValueWriter();

	/**
	 * 写出数字，根据{@link JSONConfig#isStripTrailingZeros()} 配置不同，写出不同数字<br>
	 * 主要针对Double型是否去掉小数点后多余的0<br>
	 * 此方法输出的值不包装引号。
	 *
	 * @param writer {@link JSONWriter}
	 * @param number 数字
	 */
	@Override
	public void write(final JSONWriter writer, final Number number) {
		final JSONConfig config = writer.getConfig();
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = null == config || config.isStripTrailingZeros();
		writer.writeRaw(NumberUtil.toStr(number, isStripTrailingZeros));
	}
}
