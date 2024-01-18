/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.sql.filter;

import org.dromara.hutool.db.sql.BoundSql;
import org.dromara.hutool.db.sql.SqlLog;

import java.sql.Connection;

/**
 * SQL打印拦截器
 *
 * @author Looly
 */
public class SqlLogFilter implements SqlFilter {

	private final SqlLog sqlLog;

	/**
	 * 构造
	 *
	 * @param sqlLog {@link SqlLog}
	 */
	public SqlLogFilter(final SqlLog sqlLog) {
		this.sqlLog = sqlLog;
	}

	@Override
	public void filter(final Connection conn, final BoundSql boundSql, final boolean returnGeneratedKey) {
		sqlLog.log(boundSql.getSql(), boundSql.getParams());
	}
}
