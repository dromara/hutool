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

package org.dromara.hutool.extra.ssh;

import org.dromara.hutool.core.func.Wrapper;
import org.dromara.hutool.core.io.IORuntimeException;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * SSH Session抽象
 *
 * @author looly
 */
public interface Session extends Wrapper<Object>, Closeable {

	/**
	 * 是否连接状态
	 *
	 * @return 是否连接状态
	 */
	boolean isConnected();

	// region bindPort
	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口<br>
	 * 当请求localHost:localPort时，通过SSH到服务器，转发请求到remoteHost:remotePort<br>
	 * 此方法用于访问本地无法访问但是服务器可以访问的地址，如内网数据库库等
	 *
	 * @param localPort  本地端口
	 * @param remoteAddress 远程主机和端口
	 * @return 成功与否
	 */
	default boolean bindLocalPort(final int localPort, final InetSocketAddress remoteAddress) {
		return bindLocalPort(new InetSocketAddress(localPort), remoteAddress);
	}

	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口<br>
	 * 当请求localHost:localPort时，通过SSH到服务器，转发请求到remoteHost:remotePort<br>
	 * 此方法用于访问本地无法访问但是服务器可以访问的地址，如内网数据库库等
	 *
	 * @param localAddress  本地主机和端口
	 * @param remoteAddress 远程主机和端口
	 * @return 成功与否
	 */
	boolean bindLocalPort(final InetSocketAddress localAddress, final InetSocketAddress remoteAddress);

	/**
	 * 解除本地端口映射
	 *
	 * @param localPort 需要解除的本地端口
	 * @throws IORuntimeException 端口解绑失败异常
	 */
	default void unBindLocalPort(final int localPort){
		unBindLocalPort(new InetSocketAddress(localPort));
	}

	/**
	 * 解除本地端口映射
	 *
	 * @param localAddress 需要解除的本地地址
	 */
	void unBindLocalPort(final InetSocketAddress localAddress);
	// endregion
}
