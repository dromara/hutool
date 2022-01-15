package cn.hutool.db.dialect.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phoenix数据库方言
 *
 * @author loolly
 * @since 5.7.2
 */
public class PhoenixDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 1L;

	public PhoenixDialect() {
//		wrapper = new Wrapper('"');
	}

	@Override
	public PreparedStatement psForUpdate(Connection conn, Entity entity, Query query) throws SQLException {
		// Phoenix的插入、更新语句是统一的，统一使用upsert into关键字
		// Phoenix只支持通过主键更新操作，因此query无效，自动根据entity中的主键更新
		return super.psForInsert(conn, entity);
	}

	@Override
	public String dialectName() {
		return DialectName.PHOENIX.name();
	}

	@Override
	public PreparedStatement psForUpsert(Connection conn, Entity entity, String... keys) throws SQLException {
		// Phoenix只支持通过主键更新操作，因此query无效，自动根据entity中的主键更新
		return psForInsert(conn, entity);
	}
}
