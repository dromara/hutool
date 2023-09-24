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

package org.dromara.hutool.extra.ssh.engine.jsch;

import com.jcraft.jsch.*;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.Session;
import org.dromara.hutool.extra.ssh.SshException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Jsch Session封装
 */
public class JschSession implements Session {

	private final com.jcraft.jsch.Session raw;

	/**
	 * 构造
	 *
	 * @param connector {@link Connector}，保存连接和验证信息等
	 */
	public JschSession(final Connector connector) {
		this(openSession(connector));
	}

	/**
	 * 构造
	 *
	 * @param raw {@link com.jcraft.jsch.Session}
	 */
	public JschSession(final com.jcraft.jsch.Session raw) {
		this.raw = raw;
	}

	@Override
	public Object getRaw() {
		return raw;
	}

	@Override
	public void close() throws IOException {
		if (raw != null && raw.isConnected()) {
			raw.disconnect();
		}
	}

	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口
	 *
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @param localHost  本地主机
	 * @param localPort  本地端口
	 * @return 成功与否
	 * @throws SshException 端口绑定失败异常
	 */
	public boolean bindPort(final String remoteHost, final int remotePort, final String localHost, final int localPort) throws SshException {
		if (this.raw != null && this.raw.isConnected()) {
			try {
				this.raw.setPortForwardingL(localHost, localPort, remoteHost, remotePort);
			} catch (final JSchException e) {
				throw new SshException(e, "From [{}:{}] mapping to [{}:{}] error！", remoteHost, remotePort, localHost, localPort);
			}
			return true;
		}
		return false;
	}

	/**
	 * 绑定ssh服务端的serverPort端口, 到host主机的port端口上. <br>
	 * 即数据从ssh服务端的serverPort端口, 流经ssh客户端, 达到host:port上.
	 *
	 * @param bindPort ssh服务端上要被绑定的端口
	 * @param host     转发到的host
	 * @param port     host上的端口
	 * @return 成功与否
	 * @throws SshException 端口绑定失败异常
	 */
	public boolean bindRemotePort(final int bindPort, final String host, final int port) throws SshException {
		if (this.raw != null && this.raw.isConnected()) {
			try {
				this.raw.setPortForwardingR(bindPort, host, port);
			} catch (final JSchException e) {
				throw new SshException(e, "From [{}] mapping to [{}] error！", bindPort, port);
			}
			return true;
		}
		return false;
	}

	/**
	 * 解除端口映射
	 *
	 * @param localPort 需要解除的本地端口
	 */
	public void unBindPort(final int localPort) {
		try {
			this.raw.delPortForwardingL(localPort);
		} catch (final JSchException e) {
			throw new SshException(e);
		}
	}

	/**
	 * 创建Channel连接
	 *
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @return {@link Channel}
	 */
	public Channel createChannel(final ChannelType channelType) {
		final Channel channel;
		try {
			if (!this.raw.isConnected()) {
				this.raw.connect();
			}
			channel = this.raw.openChannel(channelType.getValue());
		} catch (final JSchException e) {
			throw new SshException(e);
		}
		return channel;
	}

	/**
	 * 打开Shell连接
	 *
	 * @return {@link ChannelShell}
	 */
	public ChannelShell openShell() {
		return (ChannelShell) openChannel(ChannelType.SHELL);
	}

	/**
	 * 打开Channel连接
	 *
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @return {@link Channel}
	 */
	public Channel openChannel(final ChannelType channelType) {
		return openChannel(channelType, 0);
	}

	/**
	 * 打开Channel连接
	 *
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @param timeout     连接超时时长，单位毫秒
	 * @return {@link Channel}
	 */
	public Channel openChannel(final ChannelType channelType, final int timeout) {
		final Channel channel = createChannel(channelType);
		try {
			channel.connect(Math.max(timeout, 0));
		} catch (final JSchException e) {
			throw new SshException(e);
		}
		return channel;
	}

	/**
	 * 执行Shell命令
	 *
	 * @param cmd     命令
	 * @param charset 发送和读取内容的编码
	 * @return {@link ChannelExec}
	 */
	public String exec(final String cmd, final Charset charset) {
		return exec(cmd, charset, System.err);
	}

	/**
	 * 执行Shell命令（使用EXEC方式）
	 * <p>
	 * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
	 * </p>
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行结果内容
	 * @since 4.3.1
	 */
	public String exec(final String cmd, Charset charset, final OutputStream errStream) {
		if (null == charset) {
			charset = CharsetUtil.UTF_8;
		}
		final ChannelExec channel = (ChannelExec) createChannel(ChannelType.EXEC);
		channel.setCommand(ByteUtil.toBytes(cmd, charset));
		channel.setInputStream(null);

		channel.setErrStream(errStream);
		InputStream in = null;
		try {
			channel.connect();
			in = channel.getInputStream();
			return IoUtil.read(in, charset);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} catch (final JSchException e) {
			throw new SshException(e);
		} finally {
			IoUtil.closeQuietly(in);
			if (channel.isConnected()) {
				channel.disconnect();
			}
		}
	}

	/**
	 * 执行Shell命令
	 * <p>
	 * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
	 * </p>
	 *
	 * @param cmd     命令
	 * @param charset 发送和读取内容的编码
	 * @return {@link ChannelExec}
	 * @since 5.2.5
	 */
	public String execByShell(final String cmd, final Charset charset) {
		final ChannelShell shell = openShell();
		// 开始连接
		shell.setPty(true);
		OutputStream out = null;
		InputStream in = null;
		try {
			out = shell.getOutputStream();
			in = shell.getInputStream();

			out.write(ByteUtil.toBytes(cmd, charset));
			out.flush();

			return IoUtil.read(in, charset);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(out);
			IoUtil.closeQuietly(in);
			if (shell.isConnected()) {
				shell.disconnect();
			}
		}
	}

	/**
	 * 创建{@link com.jcraft.jsch.Session}
	 *
	 * @param connector {@link Connector}，保存连接和验证信息等
	 * @return {@link com.jcraft.jsch.Session}
	 */
	private static com.jcraft.jsch.Session openSession(final Connector connector) {
		final JSch jsch = new JSch();
		final com.jcraft.jsch.Session session;
		try {
			session = jsch.getSession(connector.getUser(), connector.getHost(), connector.getPort());
		} catch (final JSchException e) {
			throw new SshException(e);
		}

		session.setPassword(connector.getPassword());
		// 设置第一次登录的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");

		return session;
	}
}
