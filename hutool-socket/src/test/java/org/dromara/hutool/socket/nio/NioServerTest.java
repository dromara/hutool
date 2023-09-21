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
