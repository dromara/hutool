package cn.hutool.db.dialect.impl;

import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.Wrapper;

/**
 * SqlLite3方言
 * @author loolly
 *
 */
public class Sqlite3Dialect extends AnsiSqlDialect{
	private static final long serialVersionUID = -3527642408849291634L;

	public Sqlite3Dialect() {
		wrapper = new Wrapper('[', ']');
	}
	
	@Override
	public String dialectName() {
		return DialectName.SQLITE3.name();
	}
}
