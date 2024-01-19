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

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.sql.StatementUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.QuoteWrapper;
import org.dromara.hutool.db.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * MySQL方言
 *
 * @author loolly
 */
public class MysqlDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = -3734718212043823636L;

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public MysqlDialect(final DbConfig dbConfig) {
		super(dbConfig);
		quoteWrapper = new QuoteWrapper('`');
	}

	@Override
	protected SqlBuilder wrapPageSql(final SqlBuilder find, final Page page) {
		return find.append(" LIMIT ").append(page.getStartPosition()).append(", ").append(page.getPageSize());
	}

	@Override
	public String dialectName() {
		return DialectName.MYSQL.toString();
	}

	/**
	 * 构建用于upsert的{@link PreparedStatement}<br>
	 * MySQL通过主键方式实现Upsert，故keys无效，生成SQL语法为：
	 * <pre>
	 *     INSERT INTO demo(a,b,c) values(?, ?, ?) ON DUPLICATE KEY UPDATE a=values(a), b=values(b), c=values(c);
	 * </pre>
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   此参数无效
	 * @return PreparedStatement
	 * @since 5.7.20
	 */
	@Override
	public PreparedStatement psForUpsert(final Connection conn, final Entity entity, final String... keys) {
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.of(quoteWrapper);

		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();
		final StringBuilder updateHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				if (fieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
					updateHolder.append(", ");
				}

				field = (null != quoteWrapper) ? quoteWrapper.wrap(field) : field;
				fieldsPart.append(field);
				updateHolder.append(field).append("=values(").append(field).append(")");
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
			// 主键冲突后的更新操作
			.append(") ON DUPLICATE KEY UPDATE ").append(updateHolder);

		return StatementUtil.prepareStatement(false, this.dbConfig, conn, builder.build(), builder.getParamValueArray());
	}
}
