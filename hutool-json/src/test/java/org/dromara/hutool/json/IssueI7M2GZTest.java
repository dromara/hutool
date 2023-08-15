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
		//Console.log(json);
		final MyEntity<JSONBeanParserImpl> result = JSONUtil.toBean(json, new TypeReference<MyEntity<JSONBeanParserImpl>>() {});
		Assertions.assertEquals("new Object", result.getList().get(0).getName());
		Assertions.assertNotNull(result.getList().get(0).getParsed());
		Assertions.assertEquals(Integer.valueOf(12), result.getList().get(0).getParsed());
	}
}
