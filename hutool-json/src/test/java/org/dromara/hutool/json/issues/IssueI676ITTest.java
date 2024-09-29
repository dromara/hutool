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

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.xml.XPathUtil;
import org.dromara.hutool.core.xml.XmlUtil;
import org.dromara.hutool.json.xml.JSONXMLSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.xpath.XPathConstants;

public class IssueI676ITTest {
	@Test
	public void parseXMLTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issueI676IT.json"));
		final String xmlStr = JSONXMLSerializer.toXml(jsonObject, null, (String) null);
		final String content = String.valueOf(XPathUtil.getByXPath("/page/orderItems[1]/content", XmlUtil.readXml(xmlStr), XPathConstants.STRING));
		Assertions.assertEquals(content, "bar1");
	}
}
