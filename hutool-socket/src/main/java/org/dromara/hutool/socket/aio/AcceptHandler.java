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

package org.dromara.hutool.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.dromara.hutool.log.LogUtil;

/**
 * 接入完成回调，单例使用
 *
 * @author looly
 *
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

	@Override
	public void completed(final AsynchronousSocketChannel socketChannel, final AioServer aioServer) {
		// 继续等待接入（异步）
		aioServer.accept();

		final IoAction<ByteBuffer> ioAction = aioServer.ioAction;
		// 创建Session会话
		final AioSession session = new AioSession(socketChannel, ioAction, aioServer.config);
		// 处理请求接入（同步）
		ioAction.accept(session);

		// 处理读（异步）
		session.read();
	}

	@Override
	public void failed(final Throwable exc, final AioServer aioServer) {
		LogUtil.error(exc);
	}

}
