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

package org.dromara.hutool.json.xml;

import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XMLTest {

	@Test
	public void toXmlTest(){
		final JSONObject put = JSONUtil.ofObj()
				.set("aaa", "你好")
				.set("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		Assertions.assertEquals("<aaa>你好</aaa><键2>test</键2>", s);
	}

	@Test
	public void escapeTest(){
		final String xml = "<a>•</a>";
		final JSONObject jsonObject = JSONXMLUtil.toJSONObject(xml);

		Assertions.assertEquals("{\"a\":\"•\"}", jsonObject.toString());

		final String xml2 = JSONXMLUtil.toXml(JSONUtil.parseObj(jsonObject));
		Assertions.assertEquals(xml, xml2);
	}

	@Test
	public void xmlContentTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().set("content","123456");

		String xml = JSONXMLUtil.toXml(jsonObject);
		Assertions.assertEquals("123456", xml);

		xml = JSONXMLUtil.toXml(jsonObject, null, new String[0]);
		Assertions.assertEquals("<content>123456</content>", xml);
	}
}
