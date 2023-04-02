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

import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.Entity;
import org.dromara.hutool.StatementUtil;
import org.dromara.hutool.dialect.DialectName;
import org.dromara.hutool.sql.SqlBuilder;
import org.dromara.hutool.sql.QuoteWrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Postgree方言
 * @author loolly
 *
 */
public class PostgresqlDialect extends AnsiSqlDialect{
	private static final long serialVersionUID = 3889210427543389642L;

	public PostgresqlDialect() {
		quoteWrapper = new QuoteWrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.POSTGRESQL.name();
	}

	@Override
	public PreparedStatement psForUpsert(final Connection conn, final Entity entity, final String... keys) throws SQLException {
		Assert.notEmpty(keys, "Keys must be not empty for Postgres.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.of(quoteWrapper);

		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();
		final StringBuilder updateHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value)->{
			if (StrUtil.isNotBlank(field)) {
				if (fieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
					updateHolder.append(", ");
				}

				final String wrapedField = (null != quoteWrapper) ? quoteWrapper.wrap(field) : field;
				fieldsPart.append(wrapedField);
				updateHolder.append(wrapedField).append("=EXCLUDED.").append(field);
				placeHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.quoteWrapper) {
			tableName = this.quoteWrapper.wrap(tableName);
		}
		builder.append("INSERT INTO ").append(tableName)
				// 字段列表
				.append(" (").append(fieldsPart)
				// 更新值列表
				.append(") VALUES (").append(placeHolder)
				// 定义检查冲突的主键或字段
				.append(") ON CONFLICT (").append(ArrayUtil.join(keys,", "))
				// 主键冲突后的更新操作
				.append(") DO UPDATE SET ").append(updateHolder);

		return StatementUtil.prepareStatement(conn, builder);
	}
}
