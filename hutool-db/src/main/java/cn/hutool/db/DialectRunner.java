package cn.hutool.db;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.handler.RsHandler;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.db.sql.SqlUtil;
import cn.hutool.db.sql.Wrapper;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 提供基于方言的原始增删改查执行封装
 *
 * @author looly
 * @since 5.5.3
 */
public class DialectRunner implements Serializable {
	private static final long serialVersionUID = 1L;

	private Dialect dialect;
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected boolean caseInsensitive = GlobalDbConfig.caseInsensitive;

	/**
	 * 构造
	 *
	 * @param dialect 方言
	 */
	public DialectRunner(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 构造
	 *
	 * @param driverClassName 驱动类名，用于识别方言
	 */
	public DialectRunner(String driverClassName) {
		this(DialectFactory.newDialect(driverClassName));
	}

	//---------------------------------------------------------------------------- CRUD start

	/**
	 * 批量插入数据<br>
	 * 批量插入必须严格保持Entity的结构一致，不一致会导致插入数据出现不可预知的结果<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn    数据库连接
	 * @param records 记录列表，记录KV必须严格一致
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int[] insert(Connection conn, Entity... records) throws SQLException {
		checkConn(conn);
		if (ArrayUtil.isEmpty(records)) {
			return new int[]{0};
		}

		PreparedStatement ps = null;
		try {
			if (1 == records.length) {
				//单条单独处理
				ps = dialect.psForInsert(conn, records[0]);
				return new int[]{ps.executeUpdate()};
			}

			// 批量
			ps = dialect.psForInsertBatch(conn, records);
			return ps.executeBatch();
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>                  主键类型，可能为数字或对象列表
	 * @param conn                 数据库连接
	 * @param record               记录
	 * @param generatedKeysHandler 自增主键处理器，用于定义返回自增主键的范围和类型
	 * @return 主键列表
	 * @throws SQLException SQL执行异常
	 */
	public <T> T insert(Connection conn, Entity record, RsHandler<T> generatedKeysHandler) throws SQLException {
		checkConn(conn);
		if (MapUtil.isEmpty(record)) {
			throw new SQLException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			if (null == generatedKeysHandler) {
				return null;
			}
			return StatementUtil.getGeneratedKeys(ps, generatedKeysHandler);
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 删除数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn  数据库连接
	 * @param where 条件
	 * @return 影响行数
	 * @throws SQLException SQL执行异常
	 */
	public int del(Connection conn, Entity where) throws SQLException {
		checkConn(conn);
		if (MapUtil.isEmpty(where)) {
			//不允许做全表删除
			throw new SQLException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForDelete(conn, Query.of(where));
			return ps.executeUpdate();
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 更新数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @param where  条件
	 * @return 影响行数
	 * @throws SQLException SQL执行异常
	 */
	public int update(Connection conn, Entity record, Entity where) throws SQLException {
		checkConn(conn);
		if (MapUtil.isEmpty(record)) {
			throw new SQLException("Empty entity provided!");
		}
		if (MapUtil.isEmpty(where)) {
			//不允许做全表更新
			throw new SQLException("Empty where provided!");
		}

		//表名可以从被更新记录的Entity中获得，也可以从Where中获得
		String tableName = record.getTableName();
		if (StrUtil.isBlank(tableName)) {
			tableName = where.getTableName();
			record.setTableName(tableName);
		}

		final Query query = new Query(SqlUtil.buildConditions(where), tableName);
		PreparedStatement ps = null;
		try {
			ps = dialect.psForUpdate(conn, record, query);
			return ps.executeUpdate();
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>   结果对象类型
	 * @param conn  数据库连接对象
	 * @param query {@link Query}
	 * @param rsh   结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T find(Connection conn, Query query, RsHandler<T> rsh) throws SQLException {
		checkConn(conn);
		Assert.notNull(query, "[query] is null !");
		return SqlExecutor.queryAndClosePs(dialect.psForFind(conn, query), rsh);
	}

	/**
	 * 获取结果总数，生成类似于select count(1) from XXX wher XXX=? and YYY=?
	 *
	 * @param conn  数据库连接对象
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException SQL执行异常
	 */
	public long count(Connection conn, Entity where) throws SQLException {
		checkConn(conn);
		return SqlExecutor.queryAndClosePs(dialect.psForCount(conn, Query.of(where)), new NumberHandler()).longValue();
	}

	/**
	 * 获取查询结果总数，生成类似于 SELECT count(1) from (sql) hutool_alias_count_<br>
	 * 此方法会重新构建{@link SqlBuilder}，并去除末尾的order by子句
	 *
	 * @param conn  数据库连接对象
	 * @param sqlBuilder 查询语句
	 * @return 复合条件的结果数
	 * @throws SQLException SQL执行异常
	 * @since 5.7.2
	 */
	public long count(Connection conn, SqlBuilder sqlBuilder) throws SQLException {
		checkConn(conn);

		String selectSql = sqlBuilder.build();
		// 去除order by 子句
		final int orderByIndex = StrUtil.indexOfIgnoreCase(selectSql, " order by");
		if (orderByIndex > 0) {
			selectSql = StrUtil.subPre(selectSql, orderByIndex);
		}
		return SqlExecutor.queryAndClosePs(dialect.psForCount(conn,
				SqlBuilder.of(selectSql).addParams(sqlBuilder.getParamValueArray())),
				new NumberHandler()).longValue();
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>   结果对象类型
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @param rsh   结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T page(Connection conn, Query query, RsHandler<T> rsh) throws SQLException {
		checkConn(conn);
		if (null == query.getPage()) {
			return this.find(conn, query, rsh);
		}

		return SqlExecutor.queryAndClosePs(dialect.psForPage(conn, query), rsh);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>        结果对象类型
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @param rsh        结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 * @since 5.5.3
	 */
	public <T> T page(Connection conn, SqlBuilder sqlBuilder, Page page, RsHandler<T> rsh) throws SQLException {
		checkConn(conn);
		if (null == page) {
			return SqlExecutor.query(conn, sqlBuilder, rsh);
		}

		return SqlExecutor.queryAndClosePs(dialect.psForPage(conn, sqlBuilder, page), rsh);
	}
	//---------------------------------------------------------------------------- CRUD end

	//---------------------------------------------------------------------------- Getters and Setters start

	/**
	 * 设置是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param caseInsensitive 否在结果中忽略大小写
	 * @since 5.2.4
	 */
	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	/**
	 * @return SQL方言
	 */
	public Dialect getDialect() {
		return dialect;
	}

	/**
	 * 设置SQL方言
	 *
	 * @param dialect 方言
	 */
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param wrapperChar 包装字符，字符会在SQL生成时位于表名和字段名两边，null时表示取消包装
	 */
	public void setWrapper(Character wrapperChar) {
		setWrapper(new Wrapper(wrapperChar));
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param wrapper 包装器，null表示取消包装
	 */
	public void setWrapper(Wrapper wrapper) {
		this.dialect.setWrapper(wrapper);
	}
	//---------------------------------------------------------------------------- Getters and Setters end

	//---------------------------------------------------------------------------- Private method start
	private void checkConn(Connection conn) {
		Assert.notNull(conn, "Connection object must be not null!");
	}
	//---------------------------------------------------------------------------- Private method start
}
