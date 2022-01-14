package cn.hutool.db.dialect.impl;

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

	/**
	 * 构建用于upsert的PreparedStatement
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   查找字段 必须是有唯一索引的列且不能为空
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		if (null==keys || keys.length==0){
			throw new SQLException("keys不能为空");
		}
		final SqlBuilder upsert = SqlBuilder.create(wrapper).upsert(entity, this.dialectName(),keys);
		return StatementUtil.prepareStatement(conn, upsert);
	}
}
