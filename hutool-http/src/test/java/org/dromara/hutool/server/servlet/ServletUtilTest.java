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

package org.dromara.hutool.server.servlet;

import org.dromara.hutool.util.ByteUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * ServletUtil工具类测试
 *
 * @author dazer
 * @see ServletUtil
 * @see JakartaServletUtil
 */
public class ServletUtilTest {

	@Test
	@Disabled
	public void writeTest() {
		final HttpServletResponse response = null;
		final byte[] bytes = ByteUtil.toUtf8Bytes("地球是我们共同的家园，需要大家珍惜.");

		//下载文件
		// 这里没法直接测试，直接写到这里，方便调用；
		//noinspection ConstantConditions
		if (response != null) {
			final String fileName = "签名文件.pdf";
			final String contentType = "application/pdf";// application/octet-stream、image/jpeg、image/gif
			response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 必须设置否则乱码; 但是 safari乱码
			ServletUtil.write(response, new ByteArrayInputStream(bytes), contentType, fileName);
		}
	}

	@Test
	@Disabled
	public void jakartaWriteTest() {
		final jakarta.servlet.http.HttpServletResponse response = null;
		final byte[] bytes = ByteUtil.toUtf8Bytes("地球是我们共同的家园，需要大家珍惜.");

		//下载文件
		// 这里没法直接测试，直接写到这里，方便调用；
		//noinspection ConstantConditions
		if (response != null) {
			final String fileName = "签名文件.pdf";
			final String contentType = "application/pdf";// application/octet-stream、image/jpeg、image/gif
			response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 必须设置否则乱码; 但是 safari乱码
			JakartaServletUtil.write(response, new ByteArrayInputStream(bytes), contentType, fileName);
		}
	}
}
