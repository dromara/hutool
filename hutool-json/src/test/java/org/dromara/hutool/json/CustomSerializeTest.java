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

import org.dromara.hutool.json.serialize.JSONObjectSerializer;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CustomSerializeTest {

	@BeforeEach
	public void init(){
		JSONUtil.putSerializer(CustomBean.class, (JSONObjectSerializer<CustomBean>) (json, bean) -> json.set("customName", bean.name));
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
		JSONUtil.putDeserializer(CustomBean.class, json -> {
			final CustomBean customBean = new CustomBean();
			customBean.name = ((JSONObject)json).getStr("customName");
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
