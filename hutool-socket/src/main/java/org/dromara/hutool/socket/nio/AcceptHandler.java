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
