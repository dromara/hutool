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

package org.dromara.hutool.socket;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.thread.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Channel相关封装
 *
 * @author looly
 * @since 5.8.2
 */
public class ChannelUtil {

	/**
	 * 创建{@link AsynchronousChannelGroup}
	 *
	 * @param poolSize 线程池大小
	 * @return {@link AsynchronousChannelGroup}
	 */
	public static AsynchronousChannelGroup createFixedGroup(final int poolSize) {

		try {
			return AsynchronousChannelGroup.withFixedThreadPool(
					poolSize, 
					ThreadFactoryBuilder.of().setNamePrefix("Huool-socket-").build()
			);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 连接到指定地址
	 *
	 * @param group   {@link AsynchronousChannelGroup}
	 * @param address 地址信息，包括地址和端口
	 * @return {@link AsynchronousSocketChannel}
	 */
	public static AsynchronousSocketChannel connect(final AsynchronousChannelGroup group, final InetSocketAddress address) {
		final AsynchronousSocketChannel channel;
		try {
			channel = AsynchronousSocketChannel.open(group);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		try {
			channel.connect(address).get();
		} catch (final InterruptedException | ExecutionException e) {
			IoUtil.closeQuietly(channel);
			throw new SocketRuntimeException(e);
		}
		return channel;
	}
}
