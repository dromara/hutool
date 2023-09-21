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

package org.dromara.hutool.core.net;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * 代理Socket工厂，用于创建代理Socket<br>
 * 来自commons-net的DefaultSocketFactory
 *
 * @author commons-net, looly
 * @since 5.8.23
 */
public class ProxySocketFactory extends SocketFactory {

	/**
	 * 创建代理SocketFactory
	 * @param proxy 代理对象
	 * @return {@code ProxySocketFactory}
	 */
	public static ProxySocketFactory of(final Proxy proxy) {
		return new ProxySocketFactory(proxy);
	}

	private final Proxy proxy;

	/**
	 * 构造
	 *
	 * @param proxy Socket代理
	 */
	public ProxySocketFactory(final Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public Socket createSocket() {
		if (proxy != null) {
			return new Socket(proxy);
		}
		return new Socket();
	}

	@Override
	public Socket createSocket(final InetAddress address, final int port) throws IOException {
		if (proxy != null) {
			final Socket s = new Socket(proxy);
			s.connect(new InetSocketAddress(address, port));
			return s;
		}
		return new Socket(address, port);
	}

	@Override
	public Socket createSocket(final InetAddress address, final int port, final InetAddress localAddr, final int localPort) throws IOException {
		if (proxy != null) {
			final Socket s = new Socket(proxy);
			s.bind(new InetSocketAddress(localAddr, localPort));
			s.connect(new InetSocketAddress(address, port));
			return s;
		}
		return new Socket(address, port, localAddr, localPort);
	}

	@Override
	public Socket createSocket(final String host, final int port) throws IOException {
		if (proxy != null) {
			final Socket s = new Socket(proxy);
			s.connect(new InetSocketAddress(host, port));
			return s;
		}
		return new Socket(host, port);
	}

	@Override
	public Socket createSocket(final String host, final int port, final InetAddress localAddr, final int localPort) throws IOException {
		if (proxy != null) {
			final Socket s = new Socket(proxy);
			s.bind(new InetSocketAddress(localAddr, localPort));
			s.connect(new InetSocketAddress(host, port));
			return s;
		}
		return new Socket(host, port, localAddr, localPort);
	}
}
