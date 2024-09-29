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

package org.dromara.hutool.json.issues;

import lombok.Data;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.dromara.hutool.json.serializer.JSONSerializer;
import org.dromara.hutool.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3504Test {
	@Test
	public void test3504() {
		// 考虑到安全性，Class默认不支持序列化和反序列化，需要自行注册
		TypeAdapterManager.getInstance().register(Class.class,
			(JSONSerializer<Class<?>>) (bean, context) -> new JSONPrimitive(bean.getName(), ObjUtil.apply(context, JSONContext::config)));
		TypeAdapterManager.getInstance().register(Class.class,
			(JSONDeserializer<Class<?>>) (json, deserializeType) -> ClassUtil.forName((String)json.asJSONPrimitive().getValue(), true, null));

		final JsonBean jsonBean = new JsonBean();
		jsonBean.setName("test");
		jsonBean.setClasses(new Class[]{String.class});

		final String jsonStr = JSONUtil.toJsonStr(jsonBean);
		final JsonBean bean = JSONUtil.toBean(jsonStr, JsonBean.class);
		Assertions.assertNotNull(bean);
		Assertions.assertEquals("test", bean.getName());
	}

	@Data
	public static class JsonBean {
		private String name;
		private Class<?>[] classes;
	}
}
