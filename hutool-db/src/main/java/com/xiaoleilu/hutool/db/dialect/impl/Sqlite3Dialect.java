package com.xiaoleilu.hutool.db.dialect.impl;

import com.xiaoleilu.hutool.db.dialect.DialectName;
import com.xiaoleilu.hutool.db.sql.Wrapper;

/**
 * SqlLite3方言
 * @author loolly
 *
 */
public class Sqlite3Dialect extends AnsiSqlDialect{
	public Sqlite3Dialect() {
		wrapper = new Wrapper('[', ']');
	}
	
	@Override
	public DialectName dialectName() {
		return DialectName.SQLITE3;
	}
}
