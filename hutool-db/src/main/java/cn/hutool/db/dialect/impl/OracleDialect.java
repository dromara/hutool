package cn.hutool.db.dialect.impl;

import cn.hutool.db.Page;
import cn.hutool.db.dialect.DialectName;
import cn.hutool.db.sql.SqlBuilder;

/**
 * Oracle 方言
 *
 * @author loolly
 */
public class OracleDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 6122761762247483015L;

	public OracleDialect() {
		//Oracle所有字段名用双引号包围，防止字段名或表名与系统关键字冲突
		//wrapper = new Wrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		final int[] startEnd = page.getStartEnd();
		if (startEnd[0] > 0) {
			return find
					.insertPreFragment("SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM ( ")
					.append(" ) row_ WHERE ROWNUM <= ?")//
					.append(") table_alias")//
					.append(" WHERE table_alias.rownum_ > ?")//
					.addParams(startEnd[1], startEnd[0]);//
		} else {
			// start为0时，可以不用拼接该查询条件，降低SQL复杂度，部分情况下可以提升第一页查询的性能。
			return find
					.insertPreFragment("SELECT row_.*, ROWNUM rownum_ FROM ( ")
					.append(" ) row_ WHERE ROWNUM <= ?")//
					.addParams(startEnd[1]);//
		}
	}

	@Override
	public String dialectName() {
		return DialectName.ORACLE.name();
	}
}
