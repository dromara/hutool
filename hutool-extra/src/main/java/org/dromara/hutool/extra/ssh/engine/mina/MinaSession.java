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

package org.dromara.hutool.extra.ssh.engine.mina;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Apache MINA SSHD（https://mina.apache.org/sshd-project/）会话封装
 *
 * @author looly
 */
public class MinaSession implements Session {

	private final SshClient sshClient;
	private final ClientSession raw;

	/**
	 * 构造
	 *
	 * @param connector 连接信息
	 */
	public MinaSession(final Connector connector) {
		this.sshClient = MinaUtil.openClient();
		// https://github.com/apache/mina-sshd/blob/master/docs/port-forwarding.md#standard-port-forwarding
		this.sshClient.setForwardingFilter(new AcceptAllForwardingFilter());
		this.raw = MinaUtil.openSession(this.sshClient, connector);
	}

	@Override
	public Object getRaw() {
		return this.raw;
	}

	@Override
	public boolean isConnected() {
		return null != this.raw && this.raw.isOpen();
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.raw);
		if (null != this.sshClient) {
			this.sshClient.stop();
		}
		IoUtil.closeQuietly(this.sshClient);
	}

	@Override
	public void bindLocalPort(final InetSocketAddress localAddress, final InetSocketAddress remoteAddress) throws IORuntimeException {
		try {
			this.raw.startLocalPortForwarding(new SshdSocketAddress(localAddress), new SshdSocketAddress(remoteAddress));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void unBindLocalPort(final InetSocketAddress localAddress) throws IORuntimeException {
		try {
			this.raw.stopLocalPortForwarding(new SshdSocketAddress(localAddress));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void bindRemotePort(final InetSocketAddress remoteAddress, final InetSocketAddress localAddress) throws IORuntimeException {
		try {
			this.raw.startRemotePortForwarding(new SshdSocketAddress(remoteAddress), new SshdSocketAddress(localAddress));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void unBindRemotePort(final InetSocketAddress remoteAddress) throws IORuntimeException{
		try {
			this.raw.stopRemotePortForwarding(new SshdSocketAddress(remoteAddress));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 执行Shell命令
	 *
	 * @param cmd     命令
	 * @param charset 发送和读取内容的编码
	 * @return 结果
	 */
	public String exec(final String cmd, final Charset charset) {
		return exec(cmd, charset, System.err);
	}

	/**
	 * 执行Shell命令（使用EXEC方式）
	 * <p>
	 * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
	 * </p>
	 * 参考：https://github.com/apache/mina-sshd/blob/master/docs/client-setup.md#running-a-command-or-opening-a-shell
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行结果内容
	 */
	public String exec(final String cmd, final Charset charset, final OutputStream errStream) {
		try {
			return this.raw.executeRemoteCommand(cmd, errStream, charset);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 执行Shell命令
	 * <p>
	 * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
	 * </p>
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 异常输出位置
	 * @return 结果
	 */
	public String execByShell(final String cmd, final Charset charset, final OutputStream errStream) {
		final ChannelShell shellChannel;
		try {
			shellChannel = this.raw.createShellChannel();
			if (null != errStream) {
				shellChannel.setErr(errStream);
			}
			shellChannel.open().verify();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		IoUtil.write(shellChannel.getInvertedIn(), charset, false, cmd);
		return IoUtil.read(shellChannel.getInvertedOut(), charset);
	}
}
