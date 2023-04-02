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

package org.dromara.hutool.serialize;

import org.dromara.hutool.JSON;

import java.lang.reflect.Type;

/**
 * JSON自定义反序列化接口，实现JSON to Bean，使用方式为：
 * <ul>
 *     <li>定义好反序列化规则，使用{@link GlobalSerializeMapping#putDeserializer(Type, JSONDeserializer)}，关联指定类型与转换器实现反序列化。</li>
 *     <li>使Bean实现此接口，调用{@link #deserialize(JSON)}解析字段，返回this即可。</li>
 * </ul>
 *
 * @param <T> 反序列化后的类型
 * @author Looly
 */
@FunctionalInterface
public interface JSONDeserializer<T> {

	/**
	 * 反序列化，通过实现此方法，自定义实现JSON转换为指定类型的逻辑
	 *
	 * @param json {@link JSON}
	 * @return 目标对象
	 */
	T deserialize(JSON json);
}
