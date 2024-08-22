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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.SqlBuilder;
import org.dromara.hutool.db.sql.StatementUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

/**
 * 达梦数据库方言
 *
 * @author wb04307201
 */
public class DmDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 3415348435502927423L;

	/**
	 * 构造
	 *
	 * @param dbConfig 数据库配置
	 */
	public DmDialect(final DbConfig dbConfig) {
		super(dbConfig);
		// 默认不使用引号，避免表找不到的问题
		//quoteWrapper = new QuoteWrapper('\"');
	}

	@Override
	public String dialectName() {
		return DialectName.DM.name();
	}

	/**
	 * 构建用于upsert的{@link PreparedStatement}<br>
	 * 达梦使用 MERGE INTO 语法可合并 UPDATE 和 INSERT 语句<br>
	 * 参考文档：https://eco.dameng.com/document/dm/zh-cn/pm/insertion-deletion-modification#5.4%20MERGE%20INTO%20%E8%AF%AD%E5%8F%A5
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   查找字段，某些数据库此字段必须，如H2，某些数据库无需此字段，如MySQL（通过主键）
	 * @return {@link PreparedStatement}
	 */
	@Override
	public PreparedStatement psForUpsert(final Connection conn, final Entity entity, final String... keys) {
		Assert.notEmpty(keys, "Keys must be not empty for DM MERGE SQL.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.of(quoteWrapper);
		final List<String> keyList = Arrays.asList(keys);

		final StringBuilder keyFieldsPart = new StringBuilder();
		final StringBuilder updateFieldsPart = new StringBuilder();
		final StringBuilder insertFieldsPart = new StringBuilder();
		final StringBuilder insertPlaceHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field) && keyList.contains(field)) {
				if (keyFieldsPart.length() > 0) {
					keyFieldsPart.append(" and ");
				}
				keyFieldsPart.append(field).append("= ?");
				builder.addParams(value);
			}
		});

		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field) && !keyList.contains(field)) {
				if (updateFieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					updateFieldsPart.append(", ");
				}
				updateFieldsPart.append(field).append("= ?");
				builder.addParams(value);
			}
		});

		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				if (insertFieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					insertFieldsPart.append(", ");
					insertPlaceHolder.append(", ");
				}
				insertFieldsPart.append((null != quoteWrapper) ? quoteWrapper.wrap(field) : field);
				insertPlaceHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.quoteWrapper) {
			tableName = this.quoteWrapper.wrap(tableName);
		}

		builder.append("MERGE INTO ").append(tableName).append(" USING DUAL ON ").append(keyFieldsPart).append(" WHEN MATCHED THEN UPDATE SET ").append(updateFieldsPart).append(" WHEN NOT MATCHED THEN INSERT (").append(insertFieldsPart).append(") VALUES (").append(insertPlaceHolder).append(")");

		return StatementUtil.prepareStatement(false, dbConfig, conn, builder.build(), builder.getParamValueArray());
	}
}
