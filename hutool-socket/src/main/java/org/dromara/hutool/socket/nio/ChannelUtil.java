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

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;

/**
 * NIO工具类
 *
 * @since 5.4.0
 */
public class ChannelUtil {

	/**
	 * 注册通道的指定操作到指定Selector上
	 *
	 * @param selector Selector
	 * @param channel 通道
	 * @param ops 注册的通道监听（操作）类型
	 */
	public static void registerChannel(final Selector selector, final SelectableChannel channel, final Operation ops) {
		if (channel == null) {
			return;
		}

		try {
			channel.configureBlocking(false);
			// 注册通道
			//noinspection MagicConstant
			channel.register(selector, ops.getValue());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
