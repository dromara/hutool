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

import java.nio.channels.CompletionHandler;

import org.dromara.hutool.socket.SocketRuntimeException;

/**
 * 数据读取完成回调，调用Session中相应方法处理消息，单例使用
 *
 * @author looly
 *
 */
public class ReadHandler implements CompletionHandler<Integer, AioSession> {

	@Override
	public void completed(final Integer result, final AioSession session) {
		session.callbackRead();
	}

	@Override
	public void failed(final Throwable exc, final AioSession session) {
		throw new SocketRuntimeException(exc);
	}

}
