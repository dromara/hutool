/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.engine.jdk.JdkClientEngine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue444Test {
	@Test
	@Disabled
	void getTest() {
		final String s = Request.of("http://localhost:8888").send(new JdkClientEngine()).bodyStr();
		Console.log(s);
	}
}
