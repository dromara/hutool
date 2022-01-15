package cn.hutool.db.dialect.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * H2数据库方言
 *
 * @author loolly
 */
public class H2Dialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 1490520247974768214L;

	public H2Dialect() {
//		wrapper = new Wrapper('"');
	}

	@Override
	public String dialectName() {
		return DialectName.H2.name();
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		// limit A , B 表示：A就是查询的起点位置，B就是你需要多少行。
		return find.append(" limit ").append(page.getStartPosition()).append(" , ").append(page.getPageSize());
	}

	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		Assert.notEmpty(keys, "Keys must be not empty for H2 MERGE SQL.");
		SqlBuilder.validateEntity(entity);
		final SqlBuilder builder = SqlBuilder.create(wrapper);

		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();

		// 构建字段部分和参数占位符部分
		entity.forEach((field, value)->{
			if (StrUtil.isNotBlank(field)) {
				if (fieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
				}

				fieldsPart.append((null != wrapper) ? wrapper.wrap(field) : field);
				placeHolder.append("?");
				builder.addParams(value);
			}
		});

		String tableName = entity.getTableName();
		if (null != this.wrapper) {
			tableName = this.wrapper.wrap(tableName);
		}
		builder.append("MERGE INTO ").append(tableName)
				// 字段列表
				.append(" (").append(fieldsPart)
				// 更新关键字列表
				.append(") KEY(").append(ArrayUtil.join(keys, ", "))
				// 更新值列表
				.append(") VALUES (").append(placeHolder).append(")");

		return StatementUtil.prepareStatement(conn, builder);
	}
}
