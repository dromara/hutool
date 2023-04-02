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

package org.dromara.hutool;

import org.dromara.hutool.io.IORuntimeException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;

/**
 * Socket相关工具类
 *
 * @author looly
 * @since 4.5.0
 */
public class SocketUtil {

	/**
	 * 获取远程端的地址信息，包括host和端口<br>
	 * null表示channel为null或者远程主机未连接
	 *
	 * @param channel {@link AsynchronousSocketChannel}
	 * @return 远程端的地址信息，包括host和端口，null表示channel为null或者远程主机未连接
	 * @throws IORuntimeException IO异常
	 */
	public static SocketAddress getRemoteAddress(final AsynchronousSocketChannel channel) throws IORuntimeException {
		try {
			return (null == channel) ? null : channel.getRemoteAddress();
		} catch (final ClosedChannelException e) {
			// Channel未打开或已关闭，返回null表示未连接
			return null;
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 远程主机是否处于连接状态<br>
	 * 通过判断远程地址获取成功与否判断
	 *
	 * @param channel {@link AsynchronousSocketChannel}
	 * @return 远程主机是否处于连接状态
	 * @throws IORuntimeException IO异常
	 */
	public static boolean isConnected(final AsynchronousSocketChannel channel) throws IORuntimeException{
		return null != getRemoteAddress(channel);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param hostname 地址
	 * @param port     端口
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static Socket connect(final String hostname, final int port) throws IORuntimeException {
		return connect(hostname, port, -1);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param hostname          地址
	 * @param port              端口
	 * @param connectionTimeout 连接超时
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static Socket connect(final String hostname, final int port, final int connectionTimeout) throws IORuntimeException {
		return connect(new InetSocketAddress(hostname, port), connectionTimeout);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param address           地址
	 * @param connectionTimeout 连接超时
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static Socket connect(final InetSocketAddress address, final int connectionTimeout) throws IORuntimeException {
		final Socket socket = new Socket();
		try {
			if (connectionTimeout <= 0) {
				socket.connect(address);
			} else {
				socket.connect(address, connectionTimeout);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return socket;
	}
}
