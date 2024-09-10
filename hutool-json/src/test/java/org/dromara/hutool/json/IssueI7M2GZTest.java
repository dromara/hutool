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

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.serialize.JSONDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/dromara/hutool/issues/I7M2GZ
 */
public class IssueI7M2GZTest {

	@Data
	@AllArgsConstructor
	public static class JSONBeanParserImpl implements JSONDeserializer<JSONBeanParserImpl> {
		private String name;
		private Integer parsed;

		@Override
		public JSONBeanParserImpl deserialize(final JSON json) {
			setName("new Object");
			setParsed(12);
			return this;
		}
	}

	@Data
	public static class MyEntity<T> {
		private List<T> list;
	}

	@Test
	public void toListTest() {
		final List<JSONBeanParserImpl> list = new ArrayList<>();
		list.add(new JSONBeanParserImpl("Object1", 1));

		final MyEntity<JSONBeanParserImpl> entity = new MyEntity<>();
		entity.setList(list);

		final String json = JSONUtil.toJsonStr(entity);
		final MyEntity<JSONBeanParserImpl> result = JSONUtil.toBean(json, new TypeReference<MyEntity<JSONBeanParserImpl>>() {});

		Assertions.assertEquals("new Object", result.getList().get(0).getName());
		Assertions.assertNotNull(result.getList().get(0).getParsed());
		Assertions.assertEquals(Integer.valueOf(12), result.getList().get(0).getParsed());
	}
}
