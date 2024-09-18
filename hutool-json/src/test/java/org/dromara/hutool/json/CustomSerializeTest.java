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

package org.dromara.hutool.json;

import lombok.ToString;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.dromara.hutool.json.serializer.JSONSerializer;
import org.dromara.hutool.json.serializer.SerializerManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CustomSerializeTest {

	@BeforeEach
	public void init() {
		SerializerManager.getInstance().register(CustomBean.class,
			(JSONSerializer<CustomBean>) (bean, context) ->
				((JSONObject)context.getContextJson()).set("customName", bean.name));
	}

	@Test
	public void serializeTest() {
		final CustomBean customBean = new CustomBean();
		customBean.name = "testName";

		final JSONObject obj = JSONUtil.parseObj(customBean);
		Assertions.assertEquals("testName", obj.getStr("customName"));
	}

	@Test
	public void putTest() {
		final CustomBean customBean = new CustomBean();
		customBean.name = "testName";

		final JSONObject obj = JSONUtil.ofObj().set("customBean", customBean);
		Assertions.assertEquals("testName", obj.getJSONObject("customBean").getStr("customName"));
	}

	@Test
	public void deserializeTest() {
		SerializerManager.getInstance().register(CustomBean.class, (JSONDeserializer<CustomBean>) (json, deserializeType) -> {
			final CustomBean customBean = new CustomBean();
			customBean.name = ((JSONObject) json).getStr("customName");
			return customBean;
		});

		final String jsonStr = "{\"customName\":\"testName\"}";
		final CustomBean bean = JSONUtil.parseObj(jsonStr).toBean(CustomBean.class);
		Assertions.assertEquals("testName", bean.name);
	}

	@ToString
	public static class CustomBean {
		public String name;
		public String b;
		public Date date;
	}
}
