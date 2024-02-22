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
 * @author wubo
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
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		// limit A , B 表示：A就是查询的起点位置，B就是你需要多少行。
		return find.append(" limit ").append(page.getStartPosition()).append(" , ").append(page.getPageSize());
	}

	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		Assert.notEmpty(keys, "Keys must be not empty for DM MERGE SQL.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.create(wrapper);
		List<String> keyList = Arrays.asList(keys);

		final StringBuilder keyfieldsPart = new StringBuilder();
		final StringBuilder updatefieldsPart = new StringBuilder();
		final StringBuilder insertfieldsPart = new StringBuilder();
		final StringBuilder insertplaceHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field) && keyList.contains(field)) {
				if (keyfieldsPart.length() > 0) {
					keyfieldsPart.append(" and ");
				}
				keyfieldsPart.append(field + "= ?");
				builder.addParams(value);
			}
		});

		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field) && !keyList.contains(field)) {
				if (updatefieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					updatefieldsPart.append(", ");
				}
				updatefieldsPart.append(field + "= ?");
				builder.addParams(value);
			}
		});

		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				if (insertfieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					insertfieldsPart.append(", ");
					insertplaceHolder.append(", ");
				}
				insertfieldsPart.append((null != wrapper) ? wrapper.wrap(field) : field);
				insertplaceHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.wrapper) {
			tableName = this.wrapper.wrap(tableName);
		}

		builder.append("MERGE INTO ").append(tableName).append(" USING DUAL ON ").append(keyfieldsPart).append(" WHEN MATCHED THEN UPDATE SET ").append(updatefieldsPart).append(" WHEN NOT MATCHED THEN INSERT (").append(insertfieldsPart).append(") VALUES (").append(insertplaceHolder).append(")");

		return StatementUtil.prepareStatement(conn, builder);
	}
}
