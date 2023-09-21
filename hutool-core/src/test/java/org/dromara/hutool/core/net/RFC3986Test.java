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

package org.dromara.hutool.core.net;

import org.dromara.hutool.core.net.url.RFC3986;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RFC3986Test {

	@Test
	public void pacharTest(){
		final String encode = RFC3986.PCHAR.encode("=", CharsetUtil.UTF_8);
		Assertions.assertEquals("=", encode);
	}

	@Test
	public void encodeQueryTest(){
		String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a=b", encode);

		encode = RFC3986.QUERY_PARAM_VALUE.encode("a+1=b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a+1=b", encode);
	}

	@Test
	public void encodeQueryPercentTest(){
		final String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a=%25b", encode);
	}

	@Test
	public void encodeQueryWithSafeTest(){
		final String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%25", CharsetUtil.UTF_8, '%');
		Assertions.assertEquals("a=%25", encode);
	}
}
