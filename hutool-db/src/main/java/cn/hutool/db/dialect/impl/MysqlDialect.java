package cn.hutool.db.dialect.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.StatementUtil;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL方言
 * @author loolly
 *
 */
public class MysqlDialect extends AnsiSqlDialect{
	private static final long serialVersionUID = -3734718212043823636L;

	public MysqlDialect() {
		wrapper = new Wrapper('`');
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		return find.append(" LIMIT ").append(page.getStartPosition()).append(", ").append(page.getPageSize());
	}

	@Override
	public String dialectName() {
		return DialectName.MYSQL.toString();
	}

	/**
	 * 构建用于upsert的PreparedStatement
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   查找字段
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		final SqlBuilder upsert = SqlBuilder.create(wrapper).upsert(entity, this.dialectName(),keys);
		return StatementUtil.prepareStatement(conn, upsert);
	}
}
