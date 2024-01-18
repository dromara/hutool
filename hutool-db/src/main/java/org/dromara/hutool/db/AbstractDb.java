/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db;

import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.Dialect;
import org.dromara.hutool.db.ds.DSWrapper;
import org.dromara.hutool.db.handler.*;
import org.dromara.hutool.db.sql.*;
import org.dromara.hutool.db.sql.Condition.LikeType;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 抽象数据库操作类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作<br>
 * 提供抽象方法getConnection和closeConnection，用于自定义数据库连接的打开和关闭
 *
 * @param <R> return this类型
 * @author looly
 */
public abstract class AbstractDb<R extends AbstractDb<R>> extends DefaultConnectionHolder implements Serializable {
	private static final long serialVersionUID = 3858951941916349062L;

	/**
	 * 是否支持事务
	 */
	protected Boolean isSupportTransaction = null;
	protected DialectRunner runner;
	protected DbConfig dbConfig;
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected boolean caseInsensitive = true;

	// ------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param ds      数据源
	 * @param dialect 数据库方言
	 */
	public AbstractDb(final DataSource ds, final Dialect dialect) {
		super(ds);
		if(ds instanceof DSWrapper){
			this.dbConfig = ((DSWrapper) ds).getDbConfig();
			this.caseInsensitive = this.dbConfig.isCaseInsensitive();
		}
		this.runner = new DialectRunner(this.dbConfig, dialect);
	}
	// ------------------------------------------------------- Constructor end

	// region ----- query

	/**
	 * 查询
	 *
	 * @param sql    查询语句
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 3.1.1
	 */
	public List<Entity> query(final String sql, final Map<String, Object> params) throws DbException {
		return query(sql, new EntityListHandler(this.caseInsensitive), params);
	}

	/**
	 * 查询
	 *
	 * @param sql    查询语句
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 3.1.1
	 */
	public List<Entity> query(final String sql, final Object... params) throws DbException {
		return query(sql, new EntityListHandler(this.caseInsensitive), params);
	}

	/**
	 * 查询
	 *
	 * @param <T>       结果集需要处理的对象类型
	 * @param sql       查询语句
	 * @param beanClass 元素Bean类型
	 * @param params    参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 3.2.2
	 */
	public <T> List<T> query(final String sql, final Class<T> beanClass, final Object... params) throws DbException {
		return query(sql, new BeanListHandler<>(beanClass), params);
	}

	/**
	 * 查询单条记录
	 *
	 * @param sql    查询语句
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public Entity queryOne(final String sql, final Object... params) throws DbException {
		return query(sql, new EntityHandler(this.caseInsensitive), params);
	}

	/**
	 * 查询单条单个字段记录,并将其转换为Number
	 *
	 * @param sql    查询语句
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public Number queryNumber(final String sql, final Object... params) throws DbException {
		return query(sql, NumberHandler.INSTANCE, params);
	}

	/**
	 * 查询单条单个字段记录,并将其转换为String
	 *
	 * @param sql    查询语句
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public String queryString(final String sql, final Object... params) throws DbException {
		return query(sql, new StringHandler(), params);
	}

	/**
	 * 查询
	 *
	 * @param <T>    结果集需要处理的对象类型
	 * @param sql    查询语句
	 * @param rsh    结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public <T> T query(final String sql, final RsHandler<T> rsh, final Object... params) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).query(sql, rsh, params);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 支持占位符的查询，例如：select * from table where field1=:name1
	 *
	 * @param <T>      结果集需要处理的对象类型
	 * @param sql      查询语句，使用参数名占位符，例如:name
	 * @param rsh      结果集处理对象
	 * @param paramMap 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 5.2.2
	 */
	public <T> T query(final String sql, final RsHandler<T> rsh, final Map<String, Object> paramMap) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).query(sql, rsh, paramMap);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 执行自定义的{@link PreparedStatement}，结果使用{@link RsHandler}处理<br>
	 * 此方法主要用于自定义场景，如游标查询等
	 *
	 * @param <T>           结果集需要处理的对象类型
	 * @param statementFunc 自定义{@link PreparedStatement}创建函数
	 * @param rsh           结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 5.7.17
	 */
	public <T> T query(final SerFunction<Connection, PreparedStatement> statementFunc, final RsHandler<T> rsh) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).query(statementFunc, rsh);
		} finally {
			this.closeConnection(conn);
		}
	}
	// endregion

	// region ----- execute

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除
	 *
	 * @param sql    SQL
	 * @param params 参数
	 * @return 影响行数
	 * @throws DbException SQL执行异常
	 */
	public int execute(final String sql, final Object... params) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).execute(sql, params);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除
	 *
	 * @param sql    SQL
	 * @param params 参数
	 * @return 主键
	 * @throws DbException SQL执行异常
	 */
	public Long executeForGeneratedKey(final String sql, final Object... params) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).executeForGeneratedKey(sql, params);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 批量执行非查询语句
	 *
	 * @param sql         SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws DbException SQL执行异常
	 * @since 5.4.2
	 */
	public int[] executeBatch(final String sql, final Iterable<Object[]> paramsBatch) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).executeBatch(sql, paramsBatch);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 批量执行非查询语句
	 *
	 * @param sqls SQL列表
	 * @return 每个SQL执行影响的行数
	 * @throws DbException SQL执行异常
	 * @since 4.5.6
	 */
	public int[] executeBatch(final String... sqls) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).executeBatch(sqls);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 批量执行非查询语句
	 *
	 * @param sqls SQL列表
	 * @return 每个SQL执行影响的行数
	 * @throws DbException SQL执行异常
	 * @since 5.4.2
	 */
	public int[] executeBatch(final Iterable<String> sqls) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.of(this.dbConfig, conn).executeBatch(sqls);
		} finally {
			this.closeConnection(conn);
		}
	}
	// endregion

	// ---------------------------------------------------------------------------- CRUD start

	// region ----- insert

	/**
	 * 插入数据
	 *
	 * @param record 记录
	 * @return 插入行数
	 * @throws DbException SQL执行异常
	 */
	public int insert(final Entity record) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insert(conn, record)[0];
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 插入或更新数据<br>
	 * 根据给定的字段名查询数据，如果存在则更新这些数据，否则执行插入
	 *
	 * @param record 记录
	 * @param keys   需要检查唯一性的字段
	 * @return 插入行数
	 * @throws DbException SQL执行异常
	 * @since 4.0.10
	 */
	public int insertOrUpdate(final Entity record, final String... keys) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insertOrUpdate(conn, record, keys);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 使用upsert语义插入或更新数据<br>
	 * 根据给定的字段名查询数据，如果存在则更新这些数据，否则执行插入
	 * 如果方言未实现本方法，内部会自动调用insertOrUpdate来实现功能，由于upsert和insert使用有区别，为了兼容性保留原有insertOrUpdate不做变动
	 *
	 * @param record 记录
	 * @param keys   需要检查唯一性的字段
	 * @return 插入行数
	 * @throws DbException SQL执行异常
	 * @since 5.7.21
	 */
	public int upsert(final Entity record, final String... keys) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.upsert(conn, record, keys);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 批量插入数据<br>
	 * 需要注意的是，批量插入每一条数据结构必须一致。批量插入数据时会获取第一条数据的字段结构，之后的数据会按照这个格式插入。<br>
	 * 也就是说假如第一条数据只有2个字段，后边数据多于这两个字段的部分将被抛弃。
	 *
	 * @param records 记录列表
	 * @return 插入行数
	 * @throws DbException SQL执行异常
	 */
	public int[] insert(final Collection<Entity> records) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insert(conn, records.toArray(new Entity[0]));
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 插入数据
	 *
	 * @param record 记录
	 * @return 主键列表
	 * @throws DbException SQL执行异常
	 */
	public List<Object> insertForGeneratedKeys(final Entity record) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insert(conn, record, ResultSetUtil::handleRowToList);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 插入数据
	 *
	 * @param record 记录
	 * @return 主键
	 * @throws DbException SQL执行异常
	 */
	public Long insertForGeneratedKey(final Entity record) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insert(conn, record, ResultSetUtil::toLong);
		} finally {
			this.closeConnection(conn);
		}
	}
	// endregion

	// region ----- del

	/**
	 * 删除数据
	 *
	 * @param tableName 表名
	 * @param field     字段名，最好是主键
	 * @param value     值，值可以是列表或数组，被当作IN查询处理
	 * @return 删除行数
	 * @throws DbException SQL执行异常
	 */
	public int del(final String tableName, final String field, final Object value) throws DbException {
		return del(Entity.of(tableName).set(field, value));
	}

	/**
	 * 删除数据
	 *
	 * @param where 条件
	 * @return 影响行数
	 * @throws DbException SQL执行异常
	 */
	public int del(final Entity where) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.del(conn, where);
		} finally {
			this.closeConnection(conn);
		}
	}
	// endregion

	// region ----- update

	/**
	 * 更新数据<br>
	 * 更新条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param record 记录
	 * @param where  条件
	 * @return 影响行数
	 * @throws DbException SQL执行异常
	 */
	public int update(final Entity record, final Entity where) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.update(conn, record, where);
		} finally {
			this.closeConnection(conn);
		}
	}
	// endregion

	// region ----- get

	/**
	 * 根据某个字段（最好是唯一字段）查询单个记录<br>
	 * 当有多条返回时，只显示查询到的第一条
	 *
	 * @param <T>       字段值类型
	 * @param tableName 表名
	 * @param field     字段名
	 * @param value     字段值
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public <T> Entity get(final String tableName, final String field, final T value) throws DbException {
		return this.get(Entity.of(tableName).set(field, value));
	}

	/**
	 * 根据条件实体查询单个记录，当有多条返回时，只显示查询到的第一条
	 *
	 * @param where 条件
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public Entity get(final Entity where) throws DbException {
		return find(where.getFieldNames(), where, new EntityHandler(this.caseInsensitive));

	}
	// endregion

	// region ----- find

	/**
	 * 查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param <T>    需要处理成的结果对象类型
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @param rsh    结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public <T> T find(final Collection<String> fields, final Entity where, final RsHandler<T> rsh) throws DbException {
		return find(Query.of(where).setFields(fields), rsh);
	}

	/**
	 * 查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @return 结果Entity列表
	 * @throws DbException SQL执行异常
	 * @since 4.5.16
	 */
	public List<Entity> find(final Collection<String> fields, final Entity where) throws DbException {
		return find(fields, where, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 查询<br>
	 * Query为查询所需数据的一个实体类，此对象中可以定义返回字段、查询条件，查询的表、分页等信息
	 *
	 * @param <T>   需要处理成的结果对象类型
	 * @param query {@link Query}对象，此对象中可以定义返回字段、查询条件，查询的表、分页等信息
	 * @param rsh   结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 4.0.0
	 */
	public <T> T find(final Query query, final RsHandler<T> rsh) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.find(conn, query, rsh);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 查询，返回所有字段<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param <T>    需要处理成的结果对象类型
	 * @param where  条件实体类（包含表名）
	 * @param rsh    结果集处理对象
	 * @param fields 字段列表，可变长参数如果无值表示查询全部字段
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public <T> T find(final Entity where, final RsHandler<T> rsh, final String... fields) throws DbException {
		return find(Arrays.asList(fields), where, rsh);
	}

	/**
	 * 查询数据列表，返回字段由where参数指定<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param where 条件实体类（包含表名）
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 * @since 3.2.1
	 */
	public List<Entity> find(final Entity where) throws DbException {
		return find(where.getFieldNames(), where, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 查询数据列表，返回字段由where参数指定<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param <T>       Bean类型
	 * @param where     条件实体类（包含表名）
	 * @param beanClass Bean类
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 * @since 3.2.2
	 */
	public <T> List<T> find(final Entity where, final Class<T> beanClass) throws DbException {
		return find(where.getFieldNames(), where, BeanListHandler.of(beanClass));
	}

	/**
	 * 查询数据列表，返回所有字段<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param where 条件实体类（包含表名）
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> findAll(final Entity where) throws DbException {
		return find(where, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 查询数据列表，返回所有字段<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param <T>       Bean类型
	 * @param where     条件实体类（包含表名）
	 * @param beanClass 返回的对象类型
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 * @since 3.2.2
	 */
	public <T> List<T> findAll(final Entity where, final Class<T> beanClass) throws DbException {
		return find(where, BeanListHandler.of(beanClass));
	}

	/**
	 * 查询数据列表，返回所有字段
	 *
	 * @param tableName 表名
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> findAll(final String tableName) throws DbException {
		return findAll(Entity.of(tableName));
	}

	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 *
	 * @param tableName 表名
	 * @param field     字段名
	 * @param value     字段值
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> findBy(final String tableName, final String field, final Object value) throws DbException {
		return findAll(Entity.of(tableName).set(field, value));
	}

	/**
	 * 根据多个条件查询数据列表，返回所有字段
	 *
	 * @param tableName 表名
	 * @param wheres    条件，多个条件的连接逻辑使用{@link Condition#setLinkOperator(LogicalOperator)} 定义
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 * @since 4.0.0
	 */
	public List<Entity> findBy(final String tableName, final Condition... wheres) throws DbException {
		final Query query = new Query(wheres, tableName);
		return find(query, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 *
	 * @param tableName 表名
	 * @param field     字段名
	 * @param value     字段值
	 * @param likeType  {@link LikeType}
	 * @return 数据对象列表
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> findLike(final String tableName, final String field, final String value, final LikeType likeType) throws DbException {
		return findAll(Entity.of(tableName).set(field, SqlUtil.buildLikeValue(value, likeType, true)));
	}
	// endregion

	// region ----- count

	/**
	 * 结果的条目数
	 *
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws DbException SQL执行异常
	 */
	public long count(final Entity where) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.count(conn, Query.of(where));
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 结果的条目数
	 *
	 * @param sql sql构造器
	 * @return 复合条件的结果数
	 * @throws DbException SQL执行异常
	 */
	public long count(final SqlBuilder sql) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.count(conn, sql);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 结果的条目数
	 *
	 * @param selectSql 查询SQL语句
	 * @param params    查询参数
	 * @return 复合条件的结果数
	 * @throws DbException SQL执行异常
	 * @since 5.6.6
	 */
	public long count(final CharSequence selectSql, final Object... params) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.count(conn, SqlBuilder.of(selectSql).addParams(params));
		} finally {
			this.closeConnection(conn);
		}
	}
	// endregion

	// region ----- page

	/**
	 * 分页查询，结果为Bean列表，不计算总数<br>
	 *
	 * @param <T>             Bean类型
	 * @param sql             SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page            分页对象
	 * @param elementBeanType 结果集处理对象
	 * @param params          参数
	 * @return 结果对象
	 */
	public <T> PageResult<T> pageForBeanResult(final CharSequence sql, final Page page,
									   final Class<T> elementBeanType, final Object... params) {
		final PageResult<T> result = new PageResult<>(page, (int) count(sql, params));
		return page(sql, page, PageResultHandler.of(elementBeanType, result), params);
	}

	/**
	 * 分页查询，结果为Bean列表，不计算总数<br>
	 *
	 * @param <T>             Bean类型
	 * @param sql             SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page            分页对象
	 * @param elementBeanType 结果集处理对象
	 * @param params          参数
	 * @return 结果对象
	 */
	public <T> List<T> pageForBeanList(final CharSequence sql, final Page page,
									   final Class<T> elementBeanType, final Object... params) {
		return page(sql, page, BeanListHandler.of(elementBeanType), params);
	}

	/**
	 * 分页查询，结果为Entity列表，不计算总数<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param where 条件实体类（包含表名）
	 * @param page  分页对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 3.2.2
	 */
	public List<Entity> pageForEntityList(final Entity where, final Page page) throws DbException {
		return page(where, page, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 分页查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param <T>   结果对象类型
	 * @param where 条件实体类（包含表名）
	 * @param page  分页对象
	 * @param rsh   结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 3.2.2
	 */
	public <T> T page(final Entity where, final Page page, final RsHandler<T> rsh) throws DbException {
		return page(where.getFieldNames(), where, page, rsh);
	}

	/**
	 * 分页查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param <T>    结果对象类型
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @param page   分页对象
	 * @param rsh    结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public <T> T page(final Collection<String> fields, final Entity where, final Page page, final RsHandler<T> rsh) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, Query.of(where).setFields(fields).setPage(page), rsh);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 分页查询<br>
	 *
	 * @param <T>    结果对象类型
	 * @param sql    SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page   分页对象
	 * @param rsh    结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 5.6.6
	 */
	public <T> T page(final CharSequence sql, final Page page, final RsHandler<T> rsh, final Object... params) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, SqlBuilder.of(sql).addParams(params), page, rsh);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 分页查询
	 *
	 * @param <T>  处理结果类型，可以将ResultSet转换为给定类型
	 * @param sql  SQL构建器
	 * @param page 分页对象
	 * @param rsh  结果集处理对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public <T> T page(final SqlBuilder sql, final Page page, final RsHandler<T> rsh) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, sql, page, rsh);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 分页查询
	 *
	 * @param sql    SQL语句字符串
	 * @param page   分页对象
	 * @param params 参数列表
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 5.5.3
	 */
	public PageResult<Entity> page(final CharSequence sql, final Page page, final Object... params) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, SqlBuilder.of(sql).addParams(params), page);
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 分页查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param fields     返回的字段列表，null则返回所有字段
	 * @param where      条件实体类（包含表名）
	 * @param pageNumber 页码
	 * @param pageSize   每页结果数
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Collection<String> fields, final Entity where, final int pageNumber, final int pageSize) throws DbException {
		return page(fields, where, new Page(pageNumber, pageSize));
	}

	/**
	 * 分页查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @param page   分页对象
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Collection<String> fields, final Entity where, final Page page) throws DbException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, Query.of(where).setFields(fields).setPage(page));
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 分页查询<br>
	 * 查询条件为多个key value对表示，默认key = value，如果使用其它条件可以使用：where.put("key", " &gt; 1")，value也可以传Condition对象，key被忽略
	 *
	 * @param where 条件实体类（包含表名）
	 * @param page  分页对象
	 * @return 分页结果集
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Entity where, final Page page) throws DbException {
		return this.page(where.getFieldNames(), where, page);
	}
	// endregion

	// ---------------------------------------------------------------------------- Getters and Setters start

	/**
	 * 设置是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param caseInsensitive 否在结果中忽略大小写
	 * @return this
	 * @since 5.2.4
	 */
	@SuppressWarnings("unchecked")
	public R setCaseInsensitive(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
		return (R) this;
	}

	/**
	 * 获取{@link DialectRunner}
	 *
	 * @return {@link DialectRunner}
	 */
	public DialectRunner getRunner() {
		return runner;
	}

	/**
	 * 设置 {@link DialectRunner}
	 *
	 * @param runner {@link DialectRunner}
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public R setRunner(final DialectRunner runner) {
		this.runner = runner;
		return (R) this;
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param wrapperChar 包装字符，字符会在SQL生成时位于表名和字段名两边，null时表示取消包装
	 * @return this
	 * @since 4.0.0
	 */
	public R setWrapper(final Character wrapperChar) {
		return setWrapper(new QuoteWrapper(wrapperChar));
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param quoteWrapper 包装器，null表示取消包装
	 * @return this
	 * @since 4.0.0
	 */
	@SuppressWarnings("unchecked")
	public R setWrapper(final QuoteWrapper quoteWrapper) {
		this.runner.setWrapper(quoteWrapper);
		return (R) this;
	}

	/**
	 * 取消包装器<br>
	 * 取消自动添加到字段名、表名上的包装符（例如双引号）
	 *
	 * @return this
	 * @since 4.5.7
	 */
	public R disableWrapper() {
		return setWrapper((QuoteWrapper) null);
	}
	// ---------------------------------------------------------------------------- Getters and Setters end

	// ---------------------------------------------------------------------------- protected method start

	/**
	 * 检查数据库是否支持事务，此项检查同一个数据源只检查一次，如果不支持抛出DbRuntimeException异常
	 *
	 * @param conn Connection
	 * @throws DbException 获取元数据信息失败
	 * @throws DbException 不支持事务
	 */
	protected void checkTransactionSupported(final Connection conn) throws DbException {
		if (null == isSupportTransaction) {
			try {
				isSupportTransaction = conn.getMetaData().supportsTransactions();
			} catch (final SQLException e) {
				throw new DbException(e);
			}
		}
		if (!isSupportTransaction) {
			throw new DbException("Transaction not supported for current database!");
		}
	}
	// ---------------------------------------------------------------------------- protected method end
}
