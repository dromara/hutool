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
