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

package org.dromara.hutool.socket.aio;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.io.buffer.BufferUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.log.LogUtil;

import java.nio.ByteBuffer;

public class AioServerTest {

	public static void main(final String[] args) {

		@SuppressWarnings("resource") final AioServer aioServer = new AioServer(8899);
		aioServer.setIoAction(new SimpleIoAction() {

			@Override
			public void accept(final AioSession session) {
				LogUtil.debug("【客户端】：{} 连接。", session.getRemoteAddress());
				session.write(BufferUtil.ofUtf8("=== Welcome to Hutool socket server. ==="));
			}

			@Override
			public void doAction(final AioSession session, final ByteBuffer data) {
				Console.log(data);

				if(!data.hasRemaining()) {
					final StringBuilder response = StrUtil.builder()//
							.append("HTTP/1.1 200 OK\r\n")//
							.append("Date: ").append(DateUtil.formatHttpDate(DateUtil.now())).append("\r\n")//
							.append("Content-Type: text/html; charset=UTF-8\r\n")//
							.append("\r\n")
							.append("Hello Hutool socket");//
					session.writeAndClose(BufferUtil.ofUtf8(response));
				}else {
					session.read();
				}
			}
		}).start(true);
	}
}
