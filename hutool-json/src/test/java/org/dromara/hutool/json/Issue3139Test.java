/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import lombok.Data;
import org.dromara.hutool.core.xml.XmlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue3139Test {
	@Test
	public void toBeanTest() {
		final String xml = "<r>\n" +
			"  <c>\n" +
			"     <s>1</s>\n" +
			"     <p>str</p>\n" +
			"  </c>\n" +
			"</r>";

		final JSONObject jsonObject = XmlUtil.xmlToBean(XmlUtil.parseXml(xml).getDocumentElement(), JSONObject.class);
		final R bean = jsonObject.toBean(R.class);
		Assertions.assertNotNull(bean);

		final List<C> c = bean.getC();
		Assertions.assertEquals(1, c.size());
		Assertions.assertEquals("1", c.get(0).getS());
		Assertions.assertEquals("str", c.get(0).getP());
	}

	@Data
	static class C {
		String s;
		String p;
	}

	@Data
	static class R {
		List<C> c;
	}
}
