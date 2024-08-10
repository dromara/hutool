/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.engine;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * JSON引擎实现
 *
 * @author Looly
 * @since 6.0.0
 */
public interface JSONEngine {

	/**
	 * 初始化配置<br>
	 * 在引擎被加载时，如果需要自定义引擎，可以首先调用此方法<br>
	 * 调用此方法前，需要清除已经生成的引擎内容
	 *
	 * @param config 配置
	 * @return this
	 */
	JSONEngine init(JSONEngineConfig config);

	/**
	 * 生成JSON数据（序列化），用于将指定的Bean对象通过Writer写出为JSON字符串
	 *
	 * @param bean   Java Bean（POJO）对象
	 * @param writer 写出到的Writer
	 */
	void serialize(Object bean, Writer writer);

	/**
	 * 解析JSON数据（反序列化），用于从Reader中读取JSON字符串，转换为Bean对象<br>
	 * type此处定义为Object，因为不同引擎对Type的定义不同，尤其是出现泛型定义时，需要传入引擎自身实现的TypeReference
	 *
	 * @param <T>    Java Bean对象类型
	 * @param reader 读取JSON的Reader
	 * @param type   Java Bean（POJO）对象类型，可以为{@code Class<T>}或者TypeReference
	 * @return Java Bean（POJO）对象
	 */
	<T> T deserialize(Reader reader, Object type);

	/**
	 * 将Java Bean（POJO）对象转换为JSON字符串
	 *
	 * @param bean Java Bean（POJO）对象
	 * @return JSON字符串
	 */
	default String toJsonString(final Object bean) {
		final StringWriter stringWriter = new StringWriter();
		serialize(bean, stringWriter);
		return stringWriter.toString();
	}

	/**
	 * 将JSON字符串转换为Java Bean（POJO）对象
	 *
	 * @param <T>     Java Bean对象类型
	 * @param jsonStr JSON字符串
	 * @param type    Java Bean（POJO）对象类型，可以为{@code Class<T>}或者TypeReference
	 * @return Java Bean（POJO）对象
	 */
	default <T> T fromJsonString(final String jsonStr, final Object type) {
		final StringReader stringReader = new StringReader(jsonStr);
		return deserialize(stringReader, type);
	}
}
