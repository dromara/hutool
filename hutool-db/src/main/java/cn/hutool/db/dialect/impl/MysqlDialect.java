package cn.hutool.db.dialect.impl;

import cn.hutool.db.Page;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

/**
 * MySQL方言
 *
 * @author loolly
 */
public class MysqlDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = -3734718212043823636L;

	public MysqlDialect() {
		wrapper = new Wrapper('`');
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		int[] startEnd = page.getStartEnd();
		return find
				.append(" LIMIT ?, ?")
				.addParams(startEnd[0], startEnd[1]);
	}

	@Override
	public String dialectName() {
		return DialectName.MYSQL.toString();
	}
}
