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

package org.dromara.hutool.http.server;

import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.HttpUtil;

public class BlankServerTest {
	public static void main(final String[] args) {
		HttpUtil.createServer(8888)
				.addAction("/", (req, res)-> res.write("Hello Hutool Server", ContentType.JSON.getValue()))
				.start();
	}
}
