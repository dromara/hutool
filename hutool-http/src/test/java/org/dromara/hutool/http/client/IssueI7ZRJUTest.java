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

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.engine.jdk.JdkClientEngine;
import org.dromara.hutool.http.ssl.SSLInfo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI7ZRJUTest {

	@SuppressWarnings({"resource"})
	@Test
	@Disabled
	void getBadSSlTest() {
		final Response response = Request.of("https://expired.badssl.com/")
			.send(new JdkClientEngine().init(ClientConfig.of().setSSLInfo(SSLInfo.DEFAULT)));

		Console.log(response.body());
	}
}
