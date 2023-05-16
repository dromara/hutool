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
