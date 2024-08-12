/*
 * Copyright (c) 2013-2024 Hutool Team.
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
			return AsynchronousChannelGroup.withFixedThreadPool(//
					poolSize, // 默认线程池大小
					ThreadFactoryBuilder.of().setNamePrefix("Huool-socket-").build()//
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
