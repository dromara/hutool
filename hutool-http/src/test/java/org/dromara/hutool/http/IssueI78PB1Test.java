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
