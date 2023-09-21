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
import ch.ethz.ssh2.StreamGobbler;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * {@link ch.ethz.ssh2.Session}包装
 *
 * @author looly
 */
public class GanymedSession implements Session {

	private final ch.ethz.ssh2.Session raw;

	/**
	 * 构造
	 *
	 * @param connector {@link Connector}，保存连接和验证信息等
	 */
	public GanymedSession(final Connector connector) {
		this(openSession(connector));
	}

	/**
	 * 构造
	 *
	 * @param raw {@link ch.ethz.ssh2.Session}
	 */
	public GanymedSession(final ch.ethz.ssh2.Session raw) {
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
	}

	/**
	 * 执行Shell命令（使用EXEC方式）
	 * <p>
	 * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭Session，不会产生阻塞。
	 * </p>
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行返回结果
	 */
	public String exec(final String cmd, final Charset charset, final OutputStream errStream) {
		final String result;
		try {
			this.raw.execCommand(cmd, charset.name());
			result = IoUtil.read(new StreamGobbler(this.raw.getStdout()), charset);

			// 错误输出
			IoUtil.copy(new StreamGobbler(this.raw.getStderr()), errStream);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return result;
	}

	/**
	 * 执行Shell命令
	 * <p>
	 * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭Session，可能产生阻塞。
	 * </p>
	 *
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行返回结果
	 */
	public String execByShell(final String cmd, final Charset charset, final OutputStream errStream) {
		final String result;
		try {
			this.raw.requestDumbPTY();
			IoUtil.write(this.raw.getStdin(), charset, true, cmd);

			result = IoUtil.read(new StreamGobbler(this.raw.getStdout()), charset);
			if(null != errStream){
				// 错误输出
				IoUtil.copy(new StreamGobbler(this.raw.getStderr()), errStream);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return result;
	}

	/**
	 * 初始化并打开新的Session
	 *
	 * @param connector {@link Connector}，保存连接和验证信息等
	 * @return {@link ch.ethz.ssh2.Session}
	 */
	private static ch.ethz.ssh2.Session openSession(final Connector connector) {

		// 建立连接
		final Connection conn = new Connection(connector.getHost(), connector.getPort());
		try {
			conn.connect();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// 打开会话
		try {
			conn.authenticateWithPassword(connector.getUser(), connector.getPassword());
			return conn.openSession();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
