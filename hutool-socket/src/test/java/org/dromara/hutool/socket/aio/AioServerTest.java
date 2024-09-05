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
