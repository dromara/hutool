package cn.hutool.db.dialect.impl;

import cn.hutool.core.util.StrUtil;
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

	/**
	 * 检查字段值是否为Oracle自增字段，自增字段以`.nextval`结尾
	 *
	 * @param value 检查的字段值
	 * @return 是否为Oracle自增字段
	 * @since 5.7.20
	 */
	public static boolean isNextVal(Object value) {
		return (value instanceof CharSequence) && StrUtil.endWithIgnoreCase(value.toString(), ".nextval");
	}

	public OracleDialect() {
		//Oracle所有字段名用双引号包围，防止字段名或表名与系统关键字冲突
		//wrapper = new Wrapper('"');
	}

	@Override
	protected SqlBuilder wrapPageSql(SqlBuilder find, Page page) {
		final int[] startEnd = page.getStartEnd();
		return find
				.insertPreFragment("SELECT * FROM ( SELECT row_.*, rownum rownum_ from ( ")
				.append(" ) row_ where rownum <= ").append(startEnd[1])//
				.append(") table_alias")//
				.append(" where table_alias.rownum_ > ").append(startEnd[0]);//
	}

	@Override
	public String dialectName() {
		return DialectName.ORACLE.name();
	}
}
