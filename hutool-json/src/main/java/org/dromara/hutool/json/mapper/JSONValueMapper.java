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
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.serialize.JSONStringer;
import org.dromara.hutool.json.writer.GlobalValueWriters;

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
			// 当用户自定义了对象的字符串表示形式，则保留这个对象
			|| null != GlobalValueWriters.get(object)
			|| object instanceof JSON //
			|| object instanceof JSONStringer //
			|| object instanceof CharSequence //
			|| ObjUtil.isBasicType(object) //
		) {
			return object;
		}

		// 特定对象转换
		try {
			// JSONArray
			if (object instanceof Iterable || ArrayUtil.isArray(object)) {
				return new JSONArray(object, jsonConfig);
			}

			// 默认按照JSONObject对待
			return new JSONObject(object, jsonConfig);
		} catch (final Exception exception) {
			return null;
		}
	}
}
