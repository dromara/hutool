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

package org.dromara.hutool.extra.ssh.engine.ganymed;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.LocalPortForwarder;
import ch.ethz.ssh2.StreamGobbler;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link ch.ethz.ssh2.Session}包装
 *
 * @author looly
 */
public class GanymedSession implements Session {

	private Connection connection;
	private final ch.ethz.ssh2.Session raw;

	private Map<String, LocalPortForwarder> localPortForwarderMap;

	/**
	 * 构造
	 *
	 * @param connector {@link Connector}，保存连接和验证信息等
	 */
	public GanymedSession(final Connector connector) {
		this(GanymedUtil.openConnection(connector));
	}

	/**
	 * 构造
	 *
	 * @param connection {@link Connection}，连接对象
	 */
	public GanymedSession(final Connection connection) {
		this(GanymedUtil.openSession(connection));
		this.connection = connection;
	}

	/**
	 * 构造
	 *
	 * @param raw {@link ch.ethz.ssh2.Session}
	 */
	private GanymedSession(final ch.ethz.ssh2.Session raw) {
		this.raw = raw;
	}

	@Override
	public ch.ethz.ssh2.Session getRaw() {
		return raw;
	}

	@Override
	public boolean isConnected() {
		// 未找到合适的方法判断是否在线
		return true;
	}

	@Override
	public void close() throws IOException {
		if (raw != null) {
			raw.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public void bindLocalPort(final InetSocketAddress localAddress, final InetSocketAddress remoteAddress) throws IORuntimeException {
		final LocalPortForwarder localPortForwarder;
		try {
			localPortForwarder = this.connection.createLocalPortForwarder(localAddress, remoteAddress.getHostName(), remoteAddress.getPort());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == this.localPortForwarderMap) {
			this.localPortForwarderMap = new HashMap<>();
		}

		//加入记录
		this.localPortForwarderMap.put(localAddress.toString(), localPortForwarder);
	}

	@Override
	public void unBindLocalPort(final InetSocketAddress localAddress) throws IORuntimeException {
		if (MapUtil.isEmpty(this.localPortForwarderMap)) {
			return;
		}

		final LocalPortForwarder localPortForwarder = this.localPortForwarderMap.remove(localAddress.toString());
		if (null != localPortForwarder) {
			try {
				localPortForwarder.close();
			} catch (final IOException e) {
				// ignore
			}
		}
	}

	@Override
	public void bindRemotePort(final InetSocketAddress remoteAddress, final InetSocketAddress localAddress) throws IORuntimeException {
		try {
			this.connection.requestRemotePortForwarding(remoteAddress.getHostName(), remoteAddress.getPort(),
				localAddress.getHostName(), localAddress.getPort());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void unBindRemotePort(final InetSocketAddress remoteAddress) throws IORuntimeException {
		try {
			this.connection.cancelRemotePortForwarding(remoteAddress.getPort());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 执行Shell命令（使用EXEC方式）
	 * <p>
	 * 此方法单次发送一个命令到服务端，不读取环境变量，不会产生阻塞。
	 * </p>
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行返回结果
	 */
	public String exec(final String cmd, Charset charset, final OutputStream errStream) {
		if (null == charset) {
			charset = CharsetUtil.UTF_8;
		}

		// 发送命令
		try {
			this.raw.execCommand(cmd, charset.name());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// 错误输出
		if (null != errStream) {
			IoUtil.copy(new StreamGobbler(this.raw.getStderr()), errStream);
		}

		// 结果输出
		return IoUtil.read(new StreamGobbler(this.raw.getStdout()), charset);
	}

	/**
	 * 执行Shell命令
	 * <p>
	 * 此方法单次发送一个命令到服务端，自动读取环境变量，可能产生阻塞。
	 * </p>
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行返回结果
	 */
	public String execByShell(final String cmd, Charset charset, final OutputStream errStream) {
		if (null == charset) {
			charset = CharsetUtil.UTF_8;
		}

		try {
			this.raw.requestDumbPTY();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// 发送命令
		IoUtil.writeStrs(this.raw.getStdin(), charset, true, cmd);

		// 错误输出
		if (null != errStream) {
			IoUtil.copy(new StreamGobbler(this.raw.getStderr()), errStream);
		}

		// 结果输出
		return IoUtil.read(new StreamGobbler(this.raw.getStdout()), charset);
	}
}
