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

package org.dromara.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理为字符串结果，当查询结果为单个字符串时使用此处理器
 *
 * @author  weibaohui
 */
public class StringHandler implements RsHandler<String>{
	private static final long serialVersionUID = -5296733366845720383L;

	/**
	 * 创建一个 NumberHandler对象
	 * @return NumberHandler对象
	 */
	public static StringHandler of() {
		return new StringHandler();
	}

	@Override
	public String handle(final ResultSet rs) throws SQLException {
		return rs.next() ? rs.getString(1) : null;
	}
}
