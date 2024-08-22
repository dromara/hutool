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

		IoUtil.writeStrs(shellChannel.getInvertedIn(), charset, false, cmd);
		return IoUtil.read(shellChannel.getInvertedOut(), charset);
	}
}
