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

package org.dromara.hutool.db.sql.filter;

import org.dromara.hutool.db.sql.BoundSql;

import java.sql.Connection;
import java.util.List;

/**
 * SQL拦截器
 */
public interface SqlFilter {

	/**
	 * 过滤
	 *
	 * @param conn               {@link Connection}
	 * @param boundSql           {@link BoundSql}，包含SQL语句和参数，
	 *                           可通过{@link BoundSql#setSql(String)}和{@link BoundSql#setParams(List)} 自定义SQL和参数
	 * @param returnGeneratedKey 是否自动生成主键
	 */
	void filter(Connection conn, BoundSql boundSql, boolean returnGeneratedKey);
}
