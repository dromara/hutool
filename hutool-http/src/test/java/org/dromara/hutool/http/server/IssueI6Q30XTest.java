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

package org.dromara.hutool.http.server;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.net.multipart.MultipartFormData;
import org.dromara.hutool.http.HttpUtil;

import java.util.concurrent.TimeUnit;

/**
 * 测试上传超时情况<br>
 * https://gitee.com/dromara/hutool/issues/I6Q30X<br>
 *
 * post http://localhost:8888/file
 * form-data: file: file-data
 */
public class IssueI6Q30XTest {
	public static void main(String[] args) {
		final SimpleServer server = HttpUtil.createServer(8888);
		server.addAction("/file", (request, response) -> {
			Console.log(request.getHeaders().entrySet());

			final StopWatch stopWatch = DateUtil.createStopWatch();
			stopWatch.start();
			// graalvm编译后可能变慢
			final MultipartFormData multipart = request.getMultipart();
			stopWatch.stop();
			Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

			multipart.getFile("file").write("d:/test/uploadFile");

			response.write("OK");
		});
		server.start();
	}
}
