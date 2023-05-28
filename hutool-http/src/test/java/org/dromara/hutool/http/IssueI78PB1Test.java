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

package org.dromara.hutool.http;

import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

public class IssueI78PB1Test {

	/**
	 * 参考HttpClient对RFC396规范的理解，query中对于分隔符作为内容时，理应编码。
	 *
	 * @throws URISyntaxException 异常
	 */
	@Test
	void uriBuilderTest() throws URISyntaxException {
		final URIBuilder ub = new URIBuilder("https://hutool.cn");
		ub.setPath("/ /");
		ub.addParameter(":/?#[]@!$&'()*+,;= ", ":/?#[]@!$&'()*+,;= ");
		final String url = ub.toString();
		Assertions.assertEquals("https://hutool.cn/%20/?" +
			"%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2A%2B%2C%3B%3D%20=" +
			"%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2A%2B%2C%3B%3D%20", url);
	}
}
