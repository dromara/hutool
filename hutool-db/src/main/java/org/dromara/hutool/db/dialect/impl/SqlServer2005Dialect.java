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

package org.dromara.hutool.db.dialect.impl;

import org.dromara.hutool.core.text.StrPool;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.QuoteWrapper;
import org.dromara.hutool.db.sql.SqlBuilder;

/**
 * SQLServer2005-2008方言实现<br>
 * 参考：jdbc-plus
 *
 * @author niliwei, looly
 */
public class SqlServer2005Dialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_TABLE_ALIAS = "table_alias_";
	private static final String DEFAULT_ROWNUM_ALIAS = "rownum_";

	@Override
	public String dialectName() {
		return DialectName.SQLSERVER.name();
	}

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public SqlServer2005Dialect(final DbConfig dbConfig) {
		super(dbConfig);
		//双引号和中括号适用，双引号更广泛
		quoteWrapper = new QuoteWrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		String querySql = find.toString();

		String tableAlias = DEFAULT_TABLE_ALIAS;
		while (querySql.contains(tableAlias)) {
			tableAlias += StrPool.UNDERLINE;
		}
		String rownumAlias = DEFAULT_ROWNUM_ALIAS;
		while (querySql.contains(rownumAlias)) {
			rownumAlias += StrPool.UNDERLINE;
		}

		final String orderBy = getOrderByPart(querySql);
		final String distinctStr;
		if (StrUtil.startWithIgnoreCase(querySql, "select distinct")) {
			querySql = querySql.substring(15);
			distinctStr = "DISTINCT ";
		} else {
			querySql = querySql.substring(6);
			distinctStr = StrUtil.EMPTY;
		}

		return SqlBuilder.of("WITH " + tableAlias + " AS (SELECT " + distinctStr + "TOP 100 PERCENT "
			+ " ROW_NUMBER() OVER (" + orderBy + ") as " + rownumAlias + ", "
			+ querySql + ") SELECT * FROM " + tableAlias + " WHERE " + rownumAlias + " BETWEEN " +
			(page.getBeginIndex() + 1) + " AND " + page.getEndIndex() + " ORDER BY " + rownumAlias);
	}

	private static String getOrderByPart(final String sql) {
		final int orderByIndex = StrUtil.indexOfIgnoreCase(sql, "order by");
		return orderByIndex > -1 ? sql.substring(orderByIndex) : "ORDER BY CURRENT_TIMESTAMP";
	}
}
