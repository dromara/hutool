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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.util.ObjUtil;

import java.lang.reflect.Type;

/**
 * 类型转换接口函数，根据给定的值和目标类型，由用户自定义转换规则。
 *
 * @author looly
 * @since 6.0.0
 */
@FunctionalInterface
public interface Converter {

	/**
	 * 转换为指定类型<br>
	 * 如果类型无法确定，将读取默认值的类型做为目标类型
	 *
	 * @param targetType 目标Type，非泛型类使用
	 * @param value      原始值，如果对象实现了此接口，则value为this
	 * @return 转换后的值
	 * @throws ConvertException 转换无法正常完成或转换异常时抛出此异常
	 */
	Object convert(Type targetType, Object value) throws ConvertException;

	/**
	 * 转换值为指定类型，可选是否不抛异常转换<br>
	 * 当转换失败时返回默认值
	 *
	 * @param <T>          目标类型
	 * @param targetType   目标类型
	 * @param value        值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	default <T> T convert(final Type targetType, final Object value, final T defaultValue) {
		return (T) ObjUtil.defaultIfNull(convert(targetType, value), defaultValue);
	}
}
