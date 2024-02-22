package cn.hutool.db.dialect.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 达梦数据库方言
 *
 * @author wb04307201
 */
public class DmDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 3415348435502927423L;

	public DmDialect() {
	}

	@Override
	public String dialectName() {
		return DialectName.DM.name();
	}

	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		Assert.notEmpty(keys, "Keys must be not empty for DM MERGE SQL.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.create(wrapper);
		List<String> keyList = Arrays.asList(keys);

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
				keyFieldsPart.append(field + "= ?");
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
				insertFieldsPart.append((null != wrapper) ? wrapper.wrap(field) : field);
				insertPlaceHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.wrapper) {
			tableName = this.wrapper.wrap(tableName);
		}

		builder.append("MERGE INTO ").append(tableName).append(" USING DUAL ON ").append(keyFieldsPart).append(" WHEN MATCHED THEN UPDATE SET ").append(updateFieldsPart).append(" WHEN NOT MATCHED THEN INSERT (").append(insertFieldsPart).append(") VALUES (").append(insertPlaceHolder).append(")");

		return StatementUtil.prepareStatement(conn, builder);
	}
}
