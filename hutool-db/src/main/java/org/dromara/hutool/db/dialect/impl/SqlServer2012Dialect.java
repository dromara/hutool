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

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.SqlBuilder;
import org.dromara.hutool.db.sql.QuoteWrapper;

/**
 * SQLServer2012 方言
 *
 * @author Looly
 */
public class SqlServer2012Dialect extends AnsiSqlDialect {
	private static final long serialVersionUID = -37598166015777797L;

	/**
	 * 构造
	 * @param dbConfig 数据库配置
	 */
	public SqlServer2012Dialect(final DbConfig dbConfig) {
		super(dbConfig);
		//双引号和中括号适用，双引号更广泛
		quoteWrapper = new QuoteWrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.SQLSERVER2012.name();
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		if (!StrUtil.containsIgnoreCase(find.toString(), "order by")) {
			//offset 分页必须要跟在order by后面，没有情况下补充默认排序
			find.append(" order by current_timestamp");
		}
		return find.append(" offset ")
			.append(page.getBeginIndex())//
			.append(" row fetch next ")//row和rows同义词
			.append(page.getPageSize())//
			.append(" row only");//
	}
}
