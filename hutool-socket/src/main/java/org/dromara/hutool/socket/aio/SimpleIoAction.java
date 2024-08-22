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

import org.dromara.hutool.log.LogUtil;

/**
 * 简易IO信息处理类<br>
 * 简单实现了accept和failed事件
 *
 * @author looly
 *
 */
public abstract class SimpleIoAction implements IoAction<ByteBuffer> {

	@Override
	public void accept(final AioSession session) {
	}

	@Override
	public void failed(final Throwable exc, final AioSession session) {
		LogUtil.error(exc);
	}
}
