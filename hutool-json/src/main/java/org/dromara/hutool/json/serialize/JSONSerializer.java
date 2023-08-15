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

package org.dromara.hutool.json.serialize;

import org.dromara.hutool.json.JSON;

/**
 * 序列化接口，通过实现此接口，实现自定义的对象转换为JSON的操作
 *
 * @param <T> JSON类型，可以是JSONObject或者JSONArray
 * @param <V> 对象类型
 * @author Looly
 */
@FunctionalInterface
public interface JSONSerializer<T extends JSON, V> {

	/**
	 * 序列化实现，通过实现此方法，将指定类型的对象转换为{@link JSON}对象<br>
	 * 转换后的对象可以为JSONObject也可以为JSONArray，首先new一个空的JSON，然后将需要的数据字段put到JSON对象中去即可。
	 *
	 * @param json JSON，可以为JSONObject或者JSONArray
	 * @param bean 指定类型对象
	 */
	void serialize(T json, V bean);
}
