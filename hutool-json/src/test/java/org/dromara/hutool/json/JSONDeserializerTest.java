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

import org.dromara.hutool.json.serialize.JSONDeserializer;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
	static class TestBean implements JSONDeserializer<Object> {

		private String name;
		private String address;

		@Override
		public Object deserialize(final JSON value) {
			final JSONObject valueObj = (JSONObject) value;
			this.name = valueObj.getStr("customName");
			this.address = valueObj.getStr("customAddress");
			return this;
		}
	}
}
