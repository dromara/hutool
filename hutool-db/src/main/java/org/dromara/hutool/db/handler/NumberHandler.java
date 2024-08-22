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
 * 处理为数字结果，当查询结果为单个数字时使用此处理器（例如select count(1)）
 * @author loolly
 *
 */
public class NumberHandler implements RsHandler<Number>{
	private static final long serialVersionUID = 4081498054379705596L;

	/**
	 * 单例
	 */
	public static final NumberHandler INSTANCE = new NumberHandler();

	@Override
	public Number handle(final ResultSet rs) throws SQLException {
		return (null != rs && rs.next()) ? rs.getBigDecimal(1) : null;
	}
}
