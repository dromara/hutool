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

package org.dromara.hutool.socket.nio;

import org.dromara.hutool.core.io.buffer.BufferUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioServerTest {

	public static void main(final String[] args) {
		final NioServer server = new NioServer(8080);
		server.setChannelHandler((sc)->{
			final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			try{
				//从channel读数据到缓冲区
				final int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					//Flips this buffer.  The limit is set to the current position and then
					// the position is set to zero，就是表示要从起始位置开始读取数据
					readBuffer.flip();
					//eturns the number of elements between the current position and the  limit.
					// 要读取的字节长度
					final byte[] bytes = new byte[readBuffer.remaining()];
					//将缓冲区的数据读到bytes数组
					readBuffer.get(bytes);
					final String body = StrUtil.utf8Str(bytes);
					Console.log("[{}]: {}", sc.getRemoteAddress(), body);

					doWrite(sc, body);
				} else if (readBytes < 0) {
					IoUtil.closeQuietly(sc);
				}
			} catch (final IOException e){
				throw new IORuntimeException(e);
			}
		});
		server.listen();
	}

	public static void doWrite(final SocketChannel channel, String response) throws IOException {
		response = "收到消息：" + response;
		//将缓冲数据写入渠道，返回给客户端
		channel.write(BufferUtil.ofUtf8(response));
	}
}
