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

package org.dromara.hutool.db.dialect.impl;

import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.QuoteWrapper;

/**
 * SqlLite3方言
 *
 * @author Looly
 */
public class Sqlite3Dialect extends AnsiSqlDialect {
	private static final long serialVersionUID = -3527642408849291634L;

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public Sqlite3Dialect(final DbConfig dbConfig) {
		super(dbConfig);
		quoteWrapper = new QuoteWrapper('[', ']');
	}

	@Override
	public String dialectName() {
		return DialectName.SQLITE3.name();
	}
}
