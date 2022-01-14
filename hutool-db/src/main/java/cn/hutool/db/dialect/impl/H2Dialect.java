package cn.hutool.db.dialect.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Function;

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

	/**
	 * 构建用于upsert的PreparedStatement
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   查找字段 如果不提供keys将自动使用主键
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		final SqlBuilder upsert = SqlBuilder.create(wrapper).upsert(entity, this.dialectName(),keys);
		return StatementUtil.prepareStatement(conn, upsert);
	}
}
