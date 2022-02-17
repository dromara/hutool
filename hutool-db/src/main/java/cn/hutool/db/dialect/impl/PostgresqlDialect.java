package cn.hutool.db.dialect.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

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
		wrapper = new Wrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.POSTGREESQL.name();
	}

	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		Assert.notEmpty(keys, "Keys must be not empty for Postgres.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.create(wrapper);

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

				final String wrapedField = (null != wrapper) ? wrapper.wrap(field) : field;
				fieldsPart.append(wrapedField);
				updateHolder.append(wrapedField).append("=EXCLUDED.").append(field);
				placeHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.wrapper) {
			tableName = this.wrapper.wrap(tableName);
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
