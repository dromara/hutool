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
