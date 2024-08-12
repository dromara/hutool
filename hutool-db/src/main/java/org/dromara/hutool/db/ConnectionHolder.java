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

package org.dromara.hutool.db;

import java.sql.Connection;

/**
 * 控制{@link Connection}获取和关闭
 *
 * @author looly
 * @since 6.0.0
 */
public interface ConnectionHolder {
	/**
	 * 获得链接。根据实现不同，可以自定义获取连接的方式
	 *
	 * @return {@link Connection}
	 * @throws DbException 连接获取异常
	 */
	Connection getConnection() throws DbException;

	/**
	 * 关闭连接<br>
	 * 自定义关闭连接有利于自定义回收连接机制，或者不关闭
	 *
	 * @param conn 连接 {@link Connection}
	 */
	void closeConnection(Connection conn);
}
