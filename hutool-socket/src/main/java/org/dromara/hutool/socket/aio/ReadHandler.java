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
