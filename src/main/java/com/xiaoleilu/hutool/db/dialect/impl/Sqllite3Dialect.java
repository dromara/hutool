package com.xiaoleilu.hutool.db.dialect.impl;

import com.xiaoleilu.hutool.db.sql.Wrapper;

/**
 * SqlLite3方言
 * @author loolly
 *
 */
public class Sqllite3Dialect extends AnsiSqlDialect{
	public Sqllite3Dialect() {
		wrapper = new Wrapper('[', ']');
	}
}
