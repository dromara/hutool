/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.log.LogUtil;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接入完成回调，单例使用
 *
 * @author looly
 */
public class AcceptHandler implements CompletionHandler<ServerSocketChannel, NioServer> {

	@Override
	public void completed(final ServerSocketChannel serverSocketChannel, final NioServer nioServer) {
		final SocketChannel socketChannel;
		try {
			// 获取连接到此服务器的客户端通道
			socketChannel = serverSocketChannel.accept();
			LogUtil.debug("Client [{}] accepted.", socketChannel.getRemoteAddress());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// SocketChannel通道的可读事件注册到Selector中
		ChannelUtil.registerChannel(nioServer.getSelector(), socketChannel, Operation.READ);
	}

	@Override
	public void failed(final Throwable exc, final NioServer nioServer) {
		LogUtil.error(exc);
	}

}
