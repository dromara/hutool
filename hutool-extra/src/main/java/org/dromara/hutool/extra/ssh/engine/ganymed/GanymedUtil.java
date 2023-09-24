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
import ch.ethz.ssh2.Session;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.extra.ssh.Connector;

import java.io.IOException;

/**
 * Ganymed-ssh2相关工具类
 *
 * @author looly
 */
public class GanymedUtil {

	/**
	 * 打开SSH连接
	 *
	 * @param connector 连接信息
	 * @return {@link Connection}
	 */
	public static Connection openConnection(final Connector connector) {
		// 建立连接
		final Connection conn = new Connection(connector.getHost(), connector.getPort());
		try {
			conn.connect();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		// 验证
		final boolean isAuth;
		try {
			isAuth  = conn.authenticateWithPassword(connector.getUser(), connector.getPassword());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if(!isAuth){
			throw new IORuntimeException("Authentication failed.");
		}

		return conn;
	}

	/**
	 * 打开SSH会话
	 *
	 * @param connection 连接对象
	 * @return {@link Session}
	 */
	public static Session openSession(final Connection connection) {
		// 打开会话
		try {
			return connection.openSession();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
