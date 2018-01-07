package cn.hutool.db.dialect.impl;

import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.Wrapper;

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
