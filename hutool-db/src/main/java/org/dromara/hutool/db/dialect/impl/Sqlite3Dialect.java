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
