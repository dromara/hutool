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

import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONException;

/**
 * 数字类型的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class NumberValueWriter implements JSONValueWriter {

	/**
	 * JS中表示的数字最大值
	 */
	private static final long JS_MAX_NUMBER = 9007199254740992L;

	/**
	 * 单例对象
	 */
	public static final NumberValueWriter INSTANCE = new NumberValueWriter();

	@Override
	public boolean test(final Object value) {
		// 合法数字原样存储
		if(value instanceof Number){
			if(!NumberUtil.isValidNumber((Number) value)){
				throw new JSONException("JSON does not allow non-finite numbers.");
			}
			return true;
		}
		return false;
	}

	/**
	 * 写出数字，根据{@link JSONConfig#isStripTrailingZeros()} 配置不同，写出不同数字<br>
	 * 主要针对Double型是否去掉小数点后多余的0<br>
	 * 此方法输出的值不包装引号。
	 *
	 * @param writer {@link JSONWriter}
	 * @param value 数字
	 */
	@Override
	public void write(final JSONWriter writer, final Object value) {
		final JSONConfig config = writer.getConfig();
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = (null == config) || config.isStripTrailingZeros();
		final Number number = (Number) value;
		final String numberStr = NumberUtil.toStr(number, isStripTrailingZeros);

		final NumberWriteMode numberWriteMode = (null == config) ? NumberWriteMode.NORMAL : config.getNumberWriteMode();
		switch (numberWriteMode){
			case JS:
				if(number.longValue() > JS_MAX_NUMBER){
					writer.writeQuoteStrValue(numberStr);
				} else{
					writer.writeRaw(numberStr);
				}
				break;
			case STRING:
				writer.writeQuoteStrValue(numberStr);
				break;
			default:
				writer.writeRaw(numberStr);
				break;
		}
	}
}
