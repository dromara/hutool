package cn.hutool.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.handler.PageResultHandler;
import cn.hutool.db.handler.RsHandler;
import cn.hutool.db.sql.Condition.LikeType;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.db.sql.SqlUtil;
import cn.hutool.db.sql.Wrapper;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * SQL执行类<br>
 * 此执行类只接受方言参数，不需要数据源，只有在执行方法时需要数据库连接对象<br>
 * 此对象存在的意义在于，可以由使用者自定义数据库连接对象，并执行多个方法，方便事务的统一控制或减少连接对象的创建关闭
 *
 * @author Luxiaolei
 */
public class SqlConnRunner implements Serializable {
	private static final long serialVersionUID = 1L;

	private Dialect dialect;
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	protected boolean caseInsensitive = DbUtil.caseInsensitiveGlobal;

	/**
	 * 实例化一个新的SQL运行对象
	 *
	 * @param dialect 方言
	 * @return SQL执行类
	 */
	public static SqlConnRunner create(Dialect dialect) {
		return new SqlConnRunner(dialect);
	}

	/**
	 * 实例化一个新的SQL运行对象
	 *
	 * @param ds 数据源
	 * @return SQL执行类
	 */
	public static SqlConnRunner create(DataSource ds) {
		return new SqlConnRunner(DialectFactory.getDialect(ds));
	}

	/**
	 * 实例化一个新的SQL运行对象
	 *
	 * @param driverClassName 驱动类名
	 * @return SQL执行类
	 */
	public static SqlConnRunner create(String driverClassName) {
		return new SqlConnRunner(driverClassName);
	}

	//------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param dialect 方言
	 */
	public SqlConnRunner(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 构造
	 *
	 * @param driverClassName 驱动类名，，用于识别方言
	 */
	public SqlConnRunner(String driverClassName) {
		this(DialectFactory.newDialect(driverClassName));
	}
	//------------------------------------------------------- Constructor end

	//---------------------------------------------------------------------------- CRUD start

	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int insert(Connection conn, Entity record) throws SQLException {
		checkConn(conn);
		if (CollUtil.isEmpty(record)) {
			throw new SQLException("Empty entity provided!");
		}
		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			return ps.executeUpdate();
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 插入或更新数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @param keys   需要检查唯一性的字段
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int insertOrUpdate(Connection conn, Entity record, String... keys) throws SQLException {
		final Entity where = record.filter(keys);
		if (MapUtil.isNotEmpty(where) && count(conn, where) > 0) {
			return update(conn, record, where);
		} else {
			return insert(conn, record);
		}
	}

	/**
	 * 批量插入数据<br>
	 * 需要注意的是，批量插入每一条数据结构必须一致。批量插入数据时会获取第一条数据的字段结构，之后的数据会按照这个格式插入。<br>
	 * 也就是说假如第一条数据只有2个字段，后边数据多于这两个字段的部分将被抛弃。
	 * 此方法不会关闭Connection
	 *
	 * @param conn    数据库连接
	 * @param records 记录列表，记录KV必须严格一致
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int[] insert(Connection conn, Collection<Entity> records) throws SQLException {
		return insert(conn, records.toArray(new Entity[0]));
	}

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

		//单条单独处理
		if (1 == records.length) {
			return new int[]{insert(conn, records[0])};
		}

		PreparedStatement ps = null;
		try {
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
	 * @param conn   数据库连接
	 * @param record 记录
	 * @return 主键列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Object> insertForGeneratedKeys(Connection conn, Entity record) throws SQLException {
		checkConn(conn);
		if (CollUtil.isEmpty(record)) {
			throw new SQLException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			return StatementUtil.getGeneratedKeys(ps);
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @return 自增主键
	 * @throws SQLException SQL执行异常
	 */
	public Long insertForGeneratedKey(Connection conn, Entity record) throws SQLException {
		checkConn(conn);
		if (CollUtil.isEmpty(record)) {
			throw new SQLException("Empty entity provided!");
		}

		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			return StatementUtil.getGeneratedKeyOfLong(ps);
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
		if (CollUtil.isEmpty(where)) {
			//不允许做全表删除
			throw new SQLException("Empty entity provided!");
		}

		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		PreparedStatement ps = null;
		try {
			ps = dialect.psForDelete(conn, query);
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
		if (CollUtil.isEmpty(record)) {
			throw new SQLException("Empty entity provided!");
		}
		if (CollUtil.isEmpty(where)) {
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

		PreparedStatement ps = null;
		try {
			ps = dialect.psForFind(conn, query);
			return SqlExecutor.query(ps, rsh);
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>    结果对象类型
	 * @param conn   数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @param rsh    结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T find(Connection conn, Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		query.setFields(fields);
		return find(conn, query, rsh);
	}

	/**
	 * 查询，返回指定字段列表<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>    结果对象类型
	 * @param conn   数据库连接对象
	 * @param where  条件实体类（包含表名）
	 * @param rsh    结果集处理对象
	 * @param fields 字段列表，可变长参数如果无值表示查询全部字段
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T find(Connection conn, Entity where, RsHandler<T> rsh, String... fields) throws SQLException {
		return find(conn, CollUtil.newArrayList(fields), where, rsh);
	}

	/**
	 * 查询数据列表，返回字段在where参数中定义
	 *
	 * @param conn  数据库连接对象
	 * @param where 条件实体类（包含表名）
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 * @since 3.2.1
	 */
	public List<Entity> find(Connection conn, Entity where) throws SQLException {
		return find(conn, where.getFieldNames(), where, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 查询数据列表，返回所有字段
	 *
	 * @param conn  数据库连接对象
	 * @param where 条件实体类（包含表名）
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findAll(Connection conn, Entity where) throws SQLException {
		return find(conn, where, new EntityListHandler(this.caseInsensitive));
	}

	/**
	 * 查询数据列表，返回所有字段
	 *
	 * @param conn      数据库连接对象
	 * @param tableName 表名
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findAll(Connection conn, String tableName) throws SQLException {
		return findAll(conn, Entity.create(tableName));
	}

	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 *
	 * @param conn      数据库连接对象
	 * @param tableName 表名
	 * @param field     字段名
	 * @param value     字段值
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findBy(Connection conn, String tableName, String field, Object value) throws SQLException {
		return findAll(conn, Entity.create(tableName).set(field, value));
	}

	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 *
	 * @param conn      数据库连接对象
	 * @param tableName 表名
	 * @param field     字段名
	 * @param value     字段值
	 * @param likeType  {@link LikeType}
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findLike(Connection conn, String tableName, String field, String value, LikeType likeType) throws SQLException {
		return findAll(conn, Entity.create(tableName).set(field, SqlUtil.buildLikeValue(value, likeType, true)));
	}

	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 *
	 * @param conn      数据库连接对象
	 * @param tableName 表名
	 * @param field     字段名
	 * @param values    字段值列表
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findIn(Connection conn, String tableName, String field, Object... values) throws SQLException {
		return findAll(conn, Entity.create(tableName).set(field, values));
	}

	/**
	 * 结果的条目数
	 *
	 * @param conn  数据库连接对象
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException SQL执行异常
	 */
	public int count(Connection conn, Entity where) throws SQLException {
		checkConn(conn);

		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		PreparedStatement ps = null;
		try {
			ps = dialect.psForCount(conn, query);
			return SqlExecutor.query(ps, new NumberHandler()).intValue();
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>        结果对象类型
	 * @param conn       数据库连接对象
	 * @param fields     返回的字段列表，null则返回所有字段
	 * @param where      条件实体类（包含表名）
	 * @param pageNumber 页码
	 * @param numPerPage 每页条目数
	 * @param rsh        结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T page(Connection conn, Collection<String> fields, Entity where, int pageNumber, int numPerPage, RsHandler<T> rsh) throws SQLException {
		return page(conn, fields, where, new Page(pageNumber, numPerPage), rsh);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>    结果对象类型
	 * @param conn   数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @param page   分页对象
	 * @param rsh    结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T page(Connection conn, Collection<String> fields, Entity where, Page page, RsHandler<T> rsh) throws SQLException {
		checkConn(conn);
		if (null == page) {
			return this.find(conn, fields, where, rsh);
		}

		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		query.setFields(fields);
		query.setPage(page);
		return SqlExecutor.queryAndClosePs(dialect.psForPage(conn, query), rsh);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn       数据库连接对象
	 * @param fields     返回的字段列表，null则返回所有字段
	 * @param where      条件实体类（包含表名）
	 * @param page       页码
	 * @param numPerPage 每页条目数
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public PageResult<Entity> page(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
		checkConn(conn);

		final int count = count(conn, where);
		final PageResultHandler pageResultHandler = new PageResultHandler(new PageResult<>(page, numPerPage, count), this.caseInsensitive);
		return this.page(conn, fields, where, page, numPerPage, pageResultHandler);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where  条件实体类（包含表名）
	 * @param page   分页对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public PageResult<Entity> page(Connection conn, Collection<String> fields, Entity where, Page page) throws SQLException {
		checkConn(conn);

		//查询全部
		if (null == page) {
			List<Entity> entityList = this.find(conn, fields, where, new EntityListHandler(DbUtil.caseInsensitiveGlobal));
			final PageResult<Entity> pageResult = new PageResult<>(0, entityList.size(), entityList.size());
			pageResult.addAll(entityList);
			return pageResult;
		}

		final int count = count(conn, where);
		PageResultHandler pageResultHandler = new PageResultHandler(new PageResult<>(page.getPageNumber(), page.getPageSize(), count), this.caseInsensitive);
		return this.page(conn, fields, where, page, pageResultHandler);
	}

	/**
	 * 分页全字段查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn  数据库连接对象
	 * @param where 条件实体类（包含表名）
	 * @param page  分页对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public PageResult<Entity> page(Connection conn, Entity where, Page page) throws SQLException {
		return this.page(conn, null, where, page);
	}
	//---------------------------------------------------------------------------- CRUD end

	//---------------------------------------------------------------------------- Getters and Setters end

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
	 * @return this
	 */
	public SqlConnRunner setDialect(Dialect dialect) {
		this.dialect = dialect;
		return this;
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param wrapperChar 包装字符，字符会在SQL生成时位于表名和字段名两边，null时表示取消包装
	 * @return this
	 * @since 4.0.0
	 */
	public SqlConnRunner setWrapper(Character wrapperChar) {
		return setWrapper(new Wrapper(wrapperChar));
	}

	/**
	 * 设置包装器，包装器用于对表名、字段名进行符号包装（例如双引号），防止关键字与这些表名或字段冲突
	 *
	 * @param wrapper 包装器，null表示取消包装
	 * @return this
	 * @since 4.0.0
	 */
	public SqlConnRunner setWrapper(Wrapper wrapper) {
		this.dialect.setWrapper(wrapper);
		return this;
	}
	//---------------------------------------------------------------------------- Getters and Setters end

	//---------------------------------------------------------------------------- Private method start
	private void checkConn(Connection conn) {
		if (null == conn) {
			throw new NullPointerException("Connection object is null!");
		}
	}
	//---------------------------------------------------------------------------- Private method start
}