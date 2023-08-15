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
