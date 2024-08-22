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
