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

package org.dromara.hutool.dialect.impl;

import org.dromara.hutool.Page;
import org.dromara.hutool.dialect.DialectName;
import org.dromara.hutool.sql.QuoteWrapper;
import org.dromara.hutool.sql.SqlBuilder;
import org.dromara.hutool.text.StrUtil;

/**
 * SQLServer2012 方言
 *
 * @author loolly
 *
 */
public class SqlServer2012Dialect extends AnsiSqlDialect {
	private static final long serialVersionUID = -37598166015777797L;

	public SqlServer2012Dialect() {
		//双引号和中括号适用，双引号更广泛
		 quoteWrapper = new QuoteWrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		if (false == StrUtil.containsIgnoreCase(find.toString(), "order by")) {
			//offset 分页必须要跟在order by后面，没有情况下补充默认排序
			find.append(" order by current_timestamp");
		}
		return find.append(" offset ")
				.append(page.getStartPosition())//
				.append(" row fetch next ")//row和rows同义词
				.append(page.getPageSize())//
				.append(" row only");//
	}

	@Override
	public String dialectName() {
		return DialectName.SQLSERVER2012.name();
	}
}
