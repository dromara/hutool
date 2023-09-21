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

package org.dromara.hutool.http;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.resource.FileResource;
import org.dromara.hutool.core.io.resource.HttpResource;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.ResourceBody;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue2901Test {

	@Test
	@Disabled
	public void bodyTest() {
		// 自定义请求体，请求体作为资源读取，解决一次性读取到内存的问题
		final Response res = Request.of("http://localhost:8888/restTest")
				.method(Method.POST)
				.body(new ResourceBody(
						new HttpResource(new FileResource("d:/test/test.jpg"), ContentType.OCTET_STREAM.getValue())))
				.send();

		Console.log(res.bodyStr());
		IoUtil.closeQuietly(res);
	}
}
