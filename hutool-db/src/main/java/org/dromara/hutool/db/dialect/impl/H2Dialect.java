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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.sql.StatementUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * H2数据库方言
 *
 * @author loolly
 */
public class H2Dialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 1490520247974768214L;

	/**
	 * 构造
	 *
	 * @param config 数据库配置
	 */
	public H2Dialect(final DbConfig config) {
		super(config);
//		wrapper = new Wrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.H2.name();
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		// limit A , B 表示：A就是查询的起点位置，B就是你需要多少行。
		return find.append(" limit ").append(page.getBeginIndex()).append(" , ").append(page.getPageSize());
	}

	@Override
	public PreparedStatement psForUpsert(final Connection conn, final Entity entity, String... keys) {
		Assert.notEmpty(keys, "Keys must be not empty for H2 MERGE SQL.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.of(quoteWrapper);

		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				if (fieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
				}

				fieldsPart.append((null != quoteWrapper) ? quoteWrapper.wrap(field) : field);
				placeHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.quoteWrapper) {
			tableName = this.quoteWrapper.wrap(tableName);
			keys = quoteWrapper.wrap(keys);
		}
		builder.append("MERGE INTO ").append(tableName)
			// 字段列表
			.append(" (").append(fieldsPart)
			// 更新关键字列表
			.append(") KEY(").append(ArrayUtil.join(keys, ", "))
			// 更新值列表
			.append(") VALUES (").append(placeHolder).append(")");

		return StatementUtil.prepareStatement(false, this.dbConfig, conn, builder.build(), builder.getParamValueArray());
	}
}
