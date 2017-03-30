package com.xiaoleilu.hutool.db.dialect.impl;

import com.xiaoleilu.hutool.db.dialect.DialectName;
import com.xiaoleilu.hutool.db.sql.Wrapper;


/**
 * Postgree方言
 * @author loolly
 *
 */
public class PostgresqlDialect extends AnsiSqlDialect{
	public PostgresqlDialect() {
		wrapper = new Wrapper('"');
	}

	@Override
	public DialectName dialectName() {
		return DialectName.POSTGREESQL;
	}
}
