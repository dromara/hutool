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
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.extra.ssh.Connector;

import java.io.IOException;

/**
 * 基于SSHJ（https://github.com/hierynomus/sshj）相关工具类
 *
 * @author looly
 */
public class SshjUtil {

	/**
	 * 打开客户端连接
	 *
	 * @param connector 连接信息
	 * @return {@link SSHClient}
	 */
	public static SSHClient openClient(final Connector connector) {
		final SSHClient ssh = new SSHClient();
		ssh.addHostKeyVerifier(new PromiscuousVerifier());
		ssh.setConnectTimeout((int) connector.getTimeout());
		ssh.setTimeout((int) connector.getTimeout());

		try {
			ssh.connect(connector.getHost(), connector.getPort());
			ssh.authPassword(connector.getUser(), connector.getPassword());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return ssh;
	}
}
