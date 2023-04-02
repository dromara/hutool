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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.XmlUtil;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

/**
 * 字符串转换器，提供各种对象转换为字符串的逻辑封装
 *
 * @author Looly
 */
public class StringConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	private Map<Class<?>, Function<Object, String>> stringer;

	/**
	 * 加入自定义对象类型的toString规则
	 *
	 * @param clazz 类型
	 * @param stringFunction 序列化函数
	 * @return this
	 */
	public StringConverter putStringer(final Class<?> clazz, final Function<Object, String> stringFunction){
		if(null == stringer){
			stringer = new HashMap<>();
		}
		stringer.put(clazz, stringFunction);
		return this;
	}

	@Override
	protected String convertInternal(final Class<?> targetClass, final Object value) {

		// 自定义toString
		if(MapUtil.isNotEmpty(stringer)){
			final Function<Object, String> stringFunction = stringer.get(targetClass);
			if(null != stringFunction){
				return stringFunction.apply(value);
			}
		}

		if (value instanceof TimeZone) {
			return ((TimeZone) value).getID();
		} else if (value instanceof org.w3c.dom.Node) {
			return XmlUtil.toStr((org.w3c.dom.Node) value);
		} else if (value instanceof Clob) {
			return clobToStr((Clob) value);
		} else if (value instanceof Blob) {
			return blobToStr((Blob) value);
		} else if (value instanceof Type) {
			return ((Type) value).getTypeName();
		}

		// 其它情况
		return convertToStr(value);
	}

	/**
	 * Clob字段值转字符串
	 *
	 * @param clob {@link Clob}
	 * @return 字符串
	 * @since 5.4.5
	 */
	private static String clobToStr(final Clob clob) {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			return IoUtil.read(reader);
		} catch (final SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.closeQuietly(reader);
		}
	}

	/**
	 * Blob字段值转字符串
	 *
	 * @param blob    {@link Blob}
	 * @return 字符串
	 * @since 5.4.5
	 */
	private static String blobToStr(final Blob blob) {
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			return IoUtil.read(in, CharsetUtil.UTF_8);
		} catch (final SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.closeQuietly(in);
		}
	}
}
