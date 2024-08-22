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
