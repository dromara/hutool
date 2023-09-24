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
