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

package org.dromara.hutool.extra.ssh.engine.sshj;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Parameters;
import net.schmizz.sshj.connection.channel.forwarded.RemotePortForwarder;
import net.schmizz.sshj.connection.channel.forwarded.SocketForwardingConnectListener;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基于SSHJ（https://github.com/hierynomus/sshj）的Session封装
 *
 * @author looly
 */
public class SshjSession implements Session {

	private SSHClient ssh;
	private final net.schmizz.sshj.connection.channel.direct.Session raw;

	private Map<String, ServerSocket> localPortForwarderMap;

	/**
	 * 构造
	 *
	 * @param connector {@link Connector}，保存连接和验证信息等
	 */
	public SshjSession(final Connector connector) {
		this(SshjUtil.openClient(connector));
	}

	/**
	 * 构造
	 *
	 * @param ssh {@link SSHClient}
	 */
	public SshjSession(final SSHClient ssh) {
		this.ssh = ssh;
		try {
			this.raw = ssh.startSession();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public net.schmizz.sshj.connection.channel.direct.Session getRaw() {
		return raw;
	}

	@Override
	public boolean isConnected() {
		return null != this.raw && (null == this.ssh || this.ssh.isConnected());
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.raw);
		IoUtil.closeQuietly(this.ssh);
	}

	/**
	 * 打开SFTP会话
	 *
	 * @param charset 编码
	 * @return {@link SshjSftp}
	 */
	public SshjSftp openSftp(final Charset charset) {
		return new SshjSftp(this.ssh, charset);
	}

	@Override
	public boolean bindLocalPort(final InetSocketAddress localAddress, final InetSocketAddress remoteAddress) throws IORuntimeException {
		final Parameters params = new Parameters(
			localAddress.getHostName(), localAddress.getPort(),
			remoteAddress.getHostName(), remoteAddress.getPort());
		final ServerSocket ss;
		try {
			ss = new ServerSocket();
			ss.setReuseAddress(true);
			ss.bind(localAddress);
			ssh.newLocalPortForwarder(params, ss).listen();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == this.localPortForwarderMap) {
			this.localPortForwarderMap = new HashMap<>();
		}

		//加入记录
		this.localPortForwarderMap.put(localAddress.toString(), ss);

		return true;
	}

	@Override
	public void unBindLocalPort(final InetSocketAddress localAddress) throws IORuntimeException {
		if (MapUtil.isEmpty(this.localPortForwarderMap)) {
			return;
		}

		IoUtil.closeQuietly(this.localPortForwarderMap.remove(localAddress.toString()));
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
			this.ssh.getRemotePortForwarder().bind(
				new RemotePortForwarder.Forward(bindPort),
				new SocketForwardingConnectListener(new InetSocketAddress(host, port))
			);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return true;
	}

	/**
	 * 解除远程端口映射
	 *
	 * @param localPort 需要解除的本地端口
	 */
	public void unBindRemotePort(final int localPort) {
		final RemotePortForwarder remotePortForwarder = this.ssh.getRemotePortForwarder();
		final Set<RemotePortForwarder.Forward> activeForwards = remotePortForwarder.getActiveForwards();
		for (final RemotePortForwarder.Forward activeForward : activeForwards) {
			if (localPort == activeForward.getPort()) {
				try {
					remotePortForwarder.cancel(activeForward);
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
				return;
			}
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

		final net.schmizz.sshj.connection.channel.direct.Session.Command command;

		// 发送命令
		try {
			command = this.raw.exec(cmd);
			//command.join();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// 错误输出
		if (null != errStream) {
			IoUtil.copy(command.getErrorStream(), errStream);
		}

		// 结果输出
		return IoUtil.read(command.getInputStream(), charset);
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

		final net.schmizz.sshj.connection.channel.direct.Session.Shell shell;
		try {
			shell = this.raw.startShell();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// 发送命令
		IoUtil.write(shell.getOutputStream(), charset, true, cmd);

		// 错误输出
		if (null != errStream) {
			IoUtil.copy(shell.getErrorStream(), errStream);
		}

		// 结果输出
		return IoUtil.read(shell.getInputStream(), charset);
	}
}
