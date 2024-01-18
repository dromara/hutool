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

import org.dromara.hutool.core.text.StrPool;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.SqlBuilder;

/**
 * Oracle 方言
 *
 * @author loolly
 */
public class OracleDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 6122761762247483015L;

	private static final String DEFAULT_TABLE_ALIAS = "table_alias_";
	private static final String DEFAULT_ROW_ALIAS = "row_";
	private static final String DEFAULT_ROWNUM_ALIAS = "rownum_";

	/**
	 * 检查字段值是否为Oracle自增字段，自增字段以`.nextval`结尾
	 *
	 * @param value 检查的字段值
	 * @return 是否为Oracle自增字段
	 * @since 5.7.20
	 */
	public static boolean isNextVal(final Object value) {
		return (value instanceof CharSequence) && StrUtil.endWithIgnoreCase(value.toString(), ".nextval");
	}

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public OracleDialect(final DbConfig dbConfig) {
		super(dbConfig);
		//Oracle所有字段名用双引号包围，防止字段名或表名与系统关键字冲突
		//wrapper = new Wrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		final int[] startEnd = page.getStartEnd();

		// 检查别名，避免重名
		final String sql = find.toString();
		String tableAlias = DEFAULT_TABLE_ALIAS;
		while (sql.contains(tableAlias)) {
			tableAlias += StrPool.UNDERLINE;
		}
		String rowAlias = DEFAULT_ROW_ALIAS;
		while (sql.contains(rowAlias)) {
			rowAlias += StrPool.UNDERLINE;
		}
		String rownumAlias = DEFAULT_ROWNUM_ALIAS;
		while (sql.contains(rownumAlias)) {
			rownumAlias += StrPool.UNDERLINE;
		}

		return find
			.insertPreFragment("SELECT * FROM ( SELECT " + rowAlias + ".*, rownum " + rownumAlias + " from ( ")
			.append(" ) row_ where rownum <= ").append(startEnd[1])//
			.append(") ").append(tableAlias)//
			.append(" where ").append(tableAlias).append(".rownum_ > ").append(startEnd[0]);//
	}

	@Override
	public String dialectName() {
		return DialectName.ORACLE.name();
	}
}
