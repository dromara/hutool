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

package org.dromara.hutool.extra.ssh.engine.mina;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.extra.ssh.Connector;

import java.io.IOException;

/**
 * Apache MINA SSHD（https://mina.apache.org/sshd-project/）相关工具类
 *
 * @author looly
 */
public class MinaUtil {

	/**
	 * 打开一个客户端对象
	 * @return 客户端对象
	 */
	public static SshClient openClient(){
		final SshClient sshClient = SshClient.setUpDefaultClient();
		sshClient.start();

		return sshClient;
	}

	/**
	 * 打开一个新的Session
	 *
	 * @param sshClient 客户端
	 * @param connector 连接信息
	 * @return {@link ClientSession}
	 */
	public static ClientSession openSession(final SshClient sshClient, final Connector connector){
		final ClientSession session;
		final boolean success;
		try {
			session = sshClient
				.connect(connector.getUser(), connector.getHost(), connector.getPort())
				.verify()
				.getSession();

			session.addPasswordIdentity(connector.getPassword());
			success = session.auth().verify().isSuccess();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if(!success){
			throw new IORuntimeException("Authentication failed.");
		}

		return session;
	}
}
