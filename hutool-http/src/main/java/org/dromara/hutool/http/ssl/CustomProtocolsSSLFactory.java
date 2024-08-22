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

package org.dromara.hutool.http.ssl;

import org.dromara.hutool.core.array.ArrayUtil;

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
