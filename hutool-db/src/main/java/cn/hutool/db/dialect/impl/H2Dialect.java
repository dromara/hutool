package cn.hutool.db.dialect.impl;

import cn.hutool.db.Page;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

/**
 * SqlLite3方言
 * @author loolly
 *
 */
public class H2Dialect extends AnsiSqlDialect{
	public H2Dialect() {
		wrapper = new Wrapper('"', '"');
	}
	
	@Override
	public DialectName dialectName() {
		return DialectName.H2;
	}
	
	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		//limit  A , B 表示：A就是查询的起点位置，B就是你需要多少行。
		return find.append(" limit ").append(page.getStartPosition()).append(" , ").append(page.getPageSize());
	}
}
