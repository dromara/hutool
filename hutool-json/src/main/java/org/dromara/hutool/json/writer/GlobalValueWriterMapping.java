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

import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.reflect.NullType;
import org.dromara.hutool.core.util.ObjUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 全局自定义对象写出<br>
 * 用户通过此全局定义，可针对某些特殊对象
 *
 * @author looly
 * @since 6.0.0
 */
public class GlobalValueWriterMapping {

	private static final Map<Type, JSONValueWriter<?>> writerMap;

	static {
		writerMap = new SafeConcurrentHashMap<>();
	}

	/**
	 * 加入自定义的对象值写出规则
	 *
	 * @param type   对象类型
	 * @param writer 自定义对象写出实现
	 */
	public static void put(final Type type, final JSONValueWriter<?> writer) {
		writerMap.put(ObjUtil.defaultIfNull(type, NullType.INSTANCE), writer);
	}

	/**
	 * 获取自定义对象值写出规则
	 *
	 * @param type 对象类型
	 * @return 自定义的 {@link JSONValueWriter}
	 */
	public static JSONValueWriter<?> get(final Type type) {
		return writerMap.get(ObjUtil.defaultIfNull(type, NullType.INSTANCE));
	}
}
