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

package org.dromara.hutool.ssl;

import org.dromara.hutool.array.ArrayUtil;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 自定义支持协议类型的SSLSocketFactory
 *
 * @author looly
 */
public class CustomProtocolsSSLFactory extends SSLSocketFactory {

	private final SSLSocketFactory raw;
	private final String[] protocols;

	/**
	 * 构造
	 *
	 * @param factory   {@link SSLSocketFactory}
	 * @param protocols 支持协议列表
	 */
	public CustomProtocolsSSLFactory(final SSLSocketFactory factory, final String... protocols) {
		this.raw = factory;
		this.protocols = protocols;
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return raw.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return raw.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket() throws IOException {
		final SSLSocket sslSocket = (SSLSocket) raw.createSocket();
		resetProtocols(sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket(final Socket s, final String host, final int port, final boolean autoClose) throws IOException {
		final SSLSocket socket = (SSLSocket) raw.createSocket(s, host, port, autoClose);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(final String host, final int port) throws IOException {
		final SSLSocket socket = (SSLSocket) raw.createSocket(host, port);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(final String host, final int port, final InetAddress localHost, final int localPort) throws IOException {
		final SSLSocket socket = (SSLSocket) raw.createSocket(host, port, localHost, localPort);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(final InetAddress host, final int port) throws IOException {
		final SSLSocket socket = (SSLSocket) raw.createSocket(host, port);
		resetProtocols(socket);
		return socket;
	}

	@Override
	public Socket createSocket(final InetAddress address, final int port, final InetAddress localAddress, final int localPort) throws IOException {
		final SSLSocket socket = (SSLSocket) raw.createSocket(address, port, localAddress, localPort);
		resetProtocols(socket);
		return socket;
	}

	/**
	 * 重置可用策略
	 *
	 * @param socket SSLSocket
	 */
	private void resetProtocols(final SSLSocket socket) {
		if (ArrayUtil.isNotEmpty(this.protocols)) {
			socket.setEnabledProtocols(this.protocols);
		}
	}

}
