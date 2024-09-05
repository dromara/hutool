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

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.ThreadUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class AioClientTest {
	public static void main(final String[] args) {
		final AioClient client = new AioClient(new InetSocketAddress("localhost", 8899), new SimpleIoAction() {

			@Override
			public void doAction(final AioSession session, final ByteBuffer data) {
				if(data.hasRemaining()) {
					Console.log(StrUtil.utf8Str(data));
					session.read();
				}
				Console.log("OK");
			}
		});
		//线程休息1秒，然后client 和 server 初始化完再连接
		ThreadUtil.sleep(1000);

		client.write(ByteBuffer.wrap("Hello".getBytes()));
		client.read();

		client.close();
	}
}
