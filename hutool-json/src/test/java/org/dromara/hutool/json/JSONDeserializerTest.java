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

import lombok.Data;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

public class JSONDeserializerTest {

	@Test
	public void parseTest(){
		final String jsonStr = "{\"customName\": \"customValue\", \"customAddress\": \"customAddressValue\"}";
		final TestBean testBean = JSONUtil.toBean(jsonStr, TestBean.class);
		Assertions.assertNotNull(testBean);
		Assertions.assertEquals("customValue", testBean.getName());
		Assertions.assertEquals("customAddressValue", testBean.getAddress());
	}

	@Data
	static class TestBean implements JSONDeserializer<TestBean> {

		private String name;
		private String address;

		@Override
		public TestBean deserialize(final JSON json, final Type deserializeType) {
			final JSONObject valueObj = (JSONObject) json;
			this.name = valueObj.getStr("customName");
			this.address = valueObj.getStr("customAddress");
			return this;
		}
	}
}
