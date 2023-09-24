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

package org.dromara.hutool.extra.ssh.engine.ganymed;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.LocalPortForwarder;
import ch.ethz.ssh2.StreamGobbler;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.net.Ipv4Util;
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

	private Map<Integer, LocalPortForwarder> localPortForwarderMap;

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
	public void close() throws IOException {
		if (raw != null) {
			raw.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口
	 *
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @param localPort  本地端口
	 * @return 成功与否
	 * @throws IORuntimeException 端口绑定失败异常
	 */
	public boolean bindLocalPort(final String remoteHost, final int remotePort, final int localPort) throws IORuntimeException {
		return bindLocalPort(remoteHost, remotePort, Ipv4Util.LOCAL_IP, localPort);
	}

	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口
	 *
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @param localHost  本地主机
	 * @param localPort  本地端口
	 * @return 成功与否
	 * @throws IORuntimeException 端口绑定失败异常
	 */
	public boolean bindLocalPort(final String remoteHost, final int remotePort, final String localHost, final int localPort) throws IORuntimeException {
		final LocalPortForwarder localPortForwarder;
		try {
			localPortForwarder = this.connection.createLocalPortForwarder(new InetSocketAddress(localHost, localPort), remoteHost, remotePort);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if(null == this.localPortForwarderMap){
			this.localPortForwarderMap = new HashMap<>();
		}

		//加入记录
		this.localPortForwarderMap.put(localPort, localPortForwarder);

		return true;
	}

	/**
	 * 解除本地端口映射
	 *
	 * @param localPort 需要解除的本地端口
	 * @throws IORuntimeException 端口解绑失败异常
	 */
	public void unBindLocalPort(final int localPort) throws IORuntimeException {
		if(MapUtil.isEmpty(this.localPortForwarderMap)){
			return;
		}

		final LocalPortForwarder localPortForwarder = this.localPortForwarderMap.remove(localPort);
		if(null != localPortForwarder){
			try {
				localPortForwarder.close();
			} catch (final IOException e) {
				// ignore
			}
		}
	}

	/**
	 * 绑定ssh服务端的serverPort端口, 到host主机的port端口上. <br>
	 * 即数据从ssh服务端的serverPort端口, 流经ssh客户端, 达到host:port上.
	 *
	 * @param bindPort ssh服务端上要被绑定的端口
	 * @param host     转发到的host
	 * @param port     host上的端口
	 * @return 成功与否
	 * @throws IORuntimeException 端口绑定失败异常
	 */
	public boolean bindRemotePort(final int bindPort, final String host, final int port) throws IORuntimeException {
		try {
			this.connection.requestRemotePortForwarding(Ipv4Util.LOCAL_IP, bindPort, host, port);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return true;
	}

	/**
	 * 解除远程端口映射
	 *
	 * @param localPort 需要解除的本地端口
	 * @throws IORuntimeException 端口解绑失败异常
	 */
	public void unBindRemotePort(final int localPort) throws IORuntimeException {
		try {
			this.connection.cancelRemotePortForwarding(localPort);
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
		IoUtil.write(this.raw.getStdin(), charset, true, cmd);

		// 错误输出
		if (null != errStream) {
			IoUtil.copy(new StreamGobbler(this.raw.getStderr()), errStream);
		}

		// 结果输出
		return IoUtil.read(new StreamGobbler(this.raw.getStdout()), charset);
	}
}
