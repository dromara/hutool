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

package cn.hutool.json;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.XmlUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

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
		final List<C> c = bean.getC();
		Assert.assertEquals(1, c.size());
		Assert.assertEquals("1", c.get(0).getS());
		Assert.assertEquals("str", c.get(0).getP());
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
