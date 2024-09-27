/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.json.writer.JSONWriter;
import org.dromara.hutool.json.writer.NumberWriteMode;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * JSON原始类型数据封装，根据RFC8259规范，JSONPrimitive只包含以下类型：
 * <ul>
 *     <li>Number</li>
 *     <li>boolean</li>
 *     <li>String</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public class JSONPrimitive implements Wrapper<Object>, JSON {
	private static final long serialVersionUID = -2026215279191790345L;

	/**
	 * JS中表示的数字最大值
	 */
	private static final long JS_MAX_NUMBER = 9007199254740992L;

	/**
	 * 判断给定对象是否可以转为JSONPrimitive类型
	 *
	 * @param value 值
	 * @return 是否为JSONPrimitive类型
	 */
	public static boolean isTypeForJSONPrimitive(final Object value) {
		return value instanceof Boolean
			|| value instanceof Number
			|| value instanceof Character
			|| value instanceof String;
	}

	/**
	 * 判断给定类是否可以转为JSONPrimitive类型
	 *
	 * @param type 值
	 * @return 是否为JSONPrimitive类型
	 */
	public static boolean isTypeForJSONPrimitive(final Class<?> type) {
		return ClassUtil.isBasicType(type)
			|| Number.class.isAssignableFrom(type)
			|| String.class == type;
	}

	private final JSONFactory factory;
	private Object value;

	/**
	 * 构造
	 *
	 * @param value  值
	 * @param config 配置项
	 */
	public JSONPrimitive(final Object value, final JSONConfig config) {
		this(value, JSONFactory.of(config, null));
	}

	/**
	 * 构造
	 *
	 * @param value  值
	 * @param factory 配置项
	 */
	public JSONPrimitive(final Object value, final JSONFactory factory) {
		this.value = Assert.notNull(value);
		this.factory = factory;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public Object getValue() {
		return this.value;
	}

	@Override
	public Object getRaw() {
		return this.value;
	}

	/**
	 * 设置值
	 *
	 * @param value 值
	 * @return this
	 */
	public JSONPrimitive setValue(final Object value) {
		this.value = value;
		return this;
	}

	@Override
	public JSONConfig config() {
		return this.factory.getConfig();
	}

	/**
	 * 是否为数字类型
	 *
	 * @return 是否为数字类型
	 */
	public boolean isNumber() {
		return value instanceof Number;
	}

	/**
	 * 是否为布尔类型
	 *
	 * @return 是否为布尔类型
	 */
	public boolean isBoolean() {
		return value instanceof Boolean;
	}

	/**
	 * 是否为字符串类型
	 *
	 * @return 是否为字符串类型
	 */
	public boolean isString() {
		return value instanceof String;
	}

	/**
	 * 自增，仅支持数字类型，包括：
	 * <ul>
	 *     <li>{@link Integer}</li>
	 *     <li>{@link Long}</li>
	 *     <li>{@link Double}</li>
	 *     <li>{@link Float}</li>
	 *     <li>{@link BigInteger}</li>
	 *     <li>{@link BigDecimal}</li>
	 * </ul>
	 *
	 * @throws JSONException 非数字类型
	 */
	public void increment(){
		if (value instanceof BigInteger) {
			value = ((BigInteger) value).add(BigInteger.ONE);
		} else if (value instanceof BigDecimal) {
			value = ((BigDecimal) value).add(BigDecimal.ONE);
		} else if (value instanceof Integer) {
			value = (Integer) value + 1;
		} else if (value instanceof Long) {
			value = (Long) value + 1;
		} else if (value instanceof Double) {
			value = (Double) value + 1;
		} else if (value instanceof Float) {
			value = (Float) value + 1;
		} else {
			throw new JSONException("Unable to increment {} type: {}", value, value.getClass());
		}
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public void write(final JSONWriter writer) throws JSONException {
		if(value instanceof Boolean){
			// Boolean
			writer.writeRaw(value.toString());
		} else if (value instanceof Number){
			// Number
			writeNumber(writer, (Number) value);
		} else{
			// 默认包装字符串
			writer.writeQuoteStrValue(value.toString());
		}
	}

	@Override
	public String toString() {
		return toJSONString(0);
	}

	/**
	 * 写出数字，根据{@link JSONConfig#isStripTrailingZeros()} 配置不同，写出不同数字<br>
	 * 主要针对Double型是否去掉小数点后多余的0<br>
	 * 此方法输出的值不包装引号。
	 *
	 * @param writer {@link JSONWriter}
	 * @param number 数字
	 */
	private void writeNumber(final JSONWriter writer, final Number number) {
		final JSONConfig config = writer.getConfig();
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = (null == config) || config.isStripTrailingZeros();
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
