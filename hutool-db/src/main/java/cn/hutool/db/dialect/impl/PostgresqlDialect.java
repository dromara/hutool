package cn.hutool.db.dialect.impl;

import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.Wrapper;


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
	public DialectName dialectName() {
		return DialectName.POSTGREESQL;
	}
}
