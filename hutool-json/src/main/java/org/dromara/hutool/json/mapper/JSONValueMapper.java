/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.mapper;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.*;
import org.dromara.hutool.json.serialize.JSONStringer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 对象和JSON值映射器，用于转换对象为JSON对象中的值<br>
 * 有效的JSON值包括：
 * <ul>
 *     <li>JSONObject</li>
 *     <li>JSONArray</li>
 *     <li>String</li>
 *     <li>数字（int、long等）</li>
 *     <li>Boolean值，如true或false</li>
 *     <li>{@code null}</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONValueMapper {

	/**
	 * 创建ObjectMapper
	 *
	 * @param jsonConfig    来源对象
	 * @return ObjectMapper
	 */
	public static JSONValueMapper of(final JSONConfig jsonConfig) {
		return new JSONValueMapper(jsonConfig);
	}

	/**
	 * 尝试转换字符串为number, boolean, or null，无法转换返回String<br>
	 * 此方法用于解析JSON字符串时，将字符串中的值转换为JSON值对象
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	public static Object toJsonValue(final String string) {
		// null处理
		if (StrUtil.isEmpty(string) || StrUtil.NULL.equalsIgnoreCase(string)) {
			return null;
		}

		// boolean处理
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}

		// Number处理
		final char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '-') {
			try {
				if (StrUtil.containsAnyIgnoreCase(string, ".", "e")) {
					// pr#192@Gitee，Double会出现小数精度丢失问题，此处使用BigDecimal
					return new BigDecimal(string);
				} else {
					final long myLong = Long.parseLong(string);
					if (string.equals(Long.toString(myLong))) {
						if (myLong == (int) myLong) {
							return (int) myLong;
						} else {
							return myLong;
						}
					}
				}
			} catch (final Exception ignore) {
			}
		}

		// 其它情况返回原String值下
		return string;
	}

	private final JSONConfig jsonConfig;

	/**
	 * 构造
	 *
	 * @param jsonConfig    JSON配置
	 */
	public JSONValueMapper(final JSONConfig jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

	/**
	 * 在需要的时候转换映射对象<br>
	 * 包装包括：
	 * <ul>
	 * <li>array or collection =》 JSONArray</li>
	 * <li>map =》 JSONObject</li>
	 * <li>standard property (Double, String, et al) =》 原对象</li>
	 * <li>来自于java包 =》 字符串</li>
	 * <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param object     被映射的对象
	 * @return 映射后的值，null表示此值需被忽略
	 */
	public Object map(final Object object) {
		// null、JSON、字符串和自定义对象原样存储
		if (null == object
			|| null != InternalJSONUtil.getValueWriter(object)
			|| object instanceof JSON //
			|| object instanceof JSONStringer //
			|| object instanceof CharSequence //
			|| ObjUtil.isBasicType(object) //
		) {
			return object;
		}

		// 合法数字原样存储
		if(object instanceof Number){
			if(!NumberUtil.isValidNumber((Number) object)){
				throw new JSONException("JSON does not allow non-finite numbers.");
			}
			return object;
		}

		// 日期类型做包装，以便自定义输出格式
		if (object instanceof Date
			|| object instanceof Calendar
			|| object instanceof TemporalAccessor
		) {
			// 日期类型保存原始类型，用于在writer时自定义转字符串
			return object;
		}

		// 特定对象转换
		try {
			// fix issue#1399@Github
			if (object instanceof SQLException
			) {
				return object.toString();
			}

			// JSONArray
			if (object instanceof Iterable || ArrayUtil.isArray(object)) {
				return new JSONArray(object, jsonConfig);
			}
			// JSONObject
			if (object instanceof Map || object instanceof Map.Entry) {
				return new JSONObject(object, jsonConfig);
			}

			// pr#3507
			// Class类型保存类名
			if (object instanceof Class) {
				return ((Class<?>) object).getName();
			}

			// 枚举类保存其字符串形式（4.0.2新增）
			if (object instanceof Enum
				// Java内部类不做转换
				|| ClassUtil.isJdkClass(object.getClass())
			) {
				return object.toString();
			}

			// 默认按照JSONObject对待
			return new JSONObject(object, jsonConfig);
		} catch (final Exception exception) {
			return null;
		}
	}
}
