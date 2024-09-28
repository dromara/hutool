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

package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.*;

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
	 * @param out 写出到的{@link OutputStream}
	 */
	void serialize(Object bean, OutputStream out);

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
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		serialize(bean, out);
		return out.toString(CharsetUtil.UTF_8);
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
