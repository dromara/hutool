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
