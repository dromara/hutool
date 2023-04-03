/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.dialect.impl;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.SqlBuilder;

/**
 * Oracle 方言
 *
 * @author loolly
 */
public class OracleDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 6122761762247483015L;

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

	public OracleDialect() {
		//Oracle所有字段名用双引号包围，防止字段名或表名与系统关键字冲突
		//wrapper = new Wrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		final int[] startEnd = page.getStartEnd();
		return find
				.insertPreFragment("SELECT * FROM ( SELECT row_.*, rownum rownum_ from ( ")
				.append(" ) row_ where rownum <= ").append(startEnd[1])//
				.append(") table_alias_")//
				.append(" where table_alias_.rownum_ > ").append(startEnd[0]);//
	}

	@Override
	public String dialectName() {
		return DialectName.ORACLE.name();
	}
}
