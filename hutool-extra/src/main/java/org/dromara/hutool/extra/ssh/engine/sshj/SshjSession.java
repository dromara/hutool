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
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 基于SSHJ（https://github.com/hierynomus/sshj）的Session封装
 *
 * @author looly
 */
public class SshjSession implements Session {

	private SSHClient ssh;
	private final net.schmizz.sshj.connection.channel.direct.Session raw;

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

	/**
	 * 构造
	 *
	 * @param raw {@link net.schmizz.sshj.connection.channel.direct.Session}
	 */
	public SshjSession(final net.schmizz.sshj.connection.channel.direct.Session raw) {
		this.raw = raw;
	}

	@Override
	public net.schmizz.sshj.connection.channel.direct.Session getRaw() {
		return raw;
	}

	/**
	 * 是否连接状态
	 *
	 * @return 是否连接状态
	 */
	public boolean isConnected() {
		return null != this.raw && (null == this.ssh || this.ssh.isConnected());
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.raw);
		IoUtil.closeQuietly(this.ssh);
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
