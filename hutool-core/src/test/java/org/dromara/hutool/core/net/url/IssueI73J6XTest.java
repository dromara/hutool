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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI73J6XTest {

	@Test
	void decodeQueryTest() {
		final UrlBuilder builder = UrlBuilder.of("/v1/log/query");
		final UrlQuery query = builder.getQuery();
		Assertions.assertEquals(StrUtil.EMPTY, query.toString());
	}

	@Test
	void decodeQueryTest2() {
		final UrlBuilder builder = UrlBuilder.of("/v1/log/query?");
		final UrlQuery query = builder.getQuery();
		Assertions.assertEquals(StrUtil.EMPTY, query.toString());
	}
}
