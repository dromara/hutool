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
