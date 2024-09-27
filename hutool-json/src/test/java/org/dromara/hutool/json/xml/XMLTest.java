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

package org.dromara.hutool.json.xml;

import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XMLTest {

	@Test
	public void toXmlTest(){
		final JSONObject put = JSONUtil.ofObj()
				.putObj("aaa", "你好")
				.putObj("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		Assertions.assertEquals("<aaa>你好</aaa><键2>test</键2>", s);
	}

	@Test
	public void escapeTest(){
		final String xml = "<a>•</a>";
		final JSONObject jsonObject = JSONXMLUtil.toJSONObject(xml);

		Assertions.assertEquals("{\"a\":\"•\"}", jsonObject.toString());

		final String xml2 = JSONXMLUtil.toXml(jsonObject);
		Assertions.assertEquals(xml, xml2);
	}

	@Test
	public void xmlContentTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().putObj("content","123456");

		String xml = JSONXMLUtil.toXml(jsonObject);
		Assertions.assertEquals("123456", xml);

		xml = JSONXMLUtil.toXml(jsonObject, null, new String[0]);
		Assertions.assertEquals("<content>123456</content>", xml);
	}

	@Test
	public void xmlContentTest2(){
		final JSONObject jsonObject = JSONUtil.ofObj().putObj("content","123456");
		final String xml = JSONXMLUtil.toXml(jsonObject, null, new String[0]);
		Assertions.assertEquals("<content>123456</content>", xml);
	}
}
