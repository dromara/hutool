package cn.hutool.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.handler.HandleHelper;
import cn.hutool.db.handler.PageResultHandler;
import cn.hutool.db.handler.RsHandler;
import cn.hutool.db.sql.Condition.LikeType;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * SQL执行类<br>
 * 此执行类只接受方言参数，不需要数据源，只有在执行方法时需要数据库连接对象<br>
 * 此对象存在的意义在于，可以由使用者自定义数据库连接对象，并执行多个方法，方便事务的统一控制或减少连接对象的创建关闭<br>
 * 相比{@link DialectRunner}，此类中提供了更多重载方法
 *
 * @author Luxiaolei
 */
public class SqlConnRunner extends DialectRunner {
	private static final long serialVersionUID = 1L;

	/**
	 * 实例化一个新的SQL运行对象
	 *
	 * @param dialect 方言
	 * @return SQL执行类
	 */
	public static SqlConnRunner create(final Dialect dialect) {
		return new SqlConnRunner(dialect);
	}

	/**
	 * 实例化一个新的SQL运行对象
	 *
	 * @param ds 数据源
	 * @return SQL执行类
	 */
	public static SqlConnRunner create(final DataSource ds) {
		return new SqlConnRunner(DialectFactory.getDialect(ds));
	}

	/**
	 * 实例化一个新的SQL运行对象
	 *
	 * @param driverClassName 驱动类名
	 * @return SQL执行类
	 */
	public static SqlConnRunner create(final String driverClassName) {
		return new SqlConnRunner(driverClassName);
	}

	//------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param dialect 方言
	 */
	public SqlConnRunner(final Dialect dialect) {
		super(dialect);
	}

	/**
	 * 构造
	 *
	 * @param driverClassName 驱动类名，用于识别方言
	 */
	public SqlConnRunner(final String driverClassName) {
		super(driverClassName);
	}
	//------------------------------------------------------- Constructor end

	//---------------------------------------------------------------------------- CRUD start

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
	public int[] insert(final Connection conn, final Collection<Entity> records) throws SQLException {
		return insert(conn, records.toArray(new Entity[0]));
	}

	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int insert(final Connection conn, final Entity record) throws SQLException {
		return insert(conn, new Entity[]{record})[0];
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
	public List<Object> insertForGeneratedKeys(final Connection conn, final Entity record) throws SQLException {
		return insert(conn, record, HandleHelper::handleRowToList);
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
	public Long insertForGeneratedKey(final Connection conn, final Entity record) throws SQLException {
		return insert(conn, record, (rs) -> {
			Long generatedKey = null;
			if (rs != null && rs.next()) {
				try {
					generatedKey = rs.getLong(1);
				} catch (final SQLException e) {
					// 自增主键不为数字或者为Oracle的rowid，跳过
				}
			}
			return generatedKey;
		});
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
	public <T> T find(final Connection conn, final Collection<String> fields, final Entity where, final RsHandler<T> rsh) throws SQLException {
		return find(conn, Query.of(where).setFields(fields), rsh);
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
	public <T> T find(final Connection conn, final Entity where, final RsHandler<T> rsh, final String... fields) throws SQLException {
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
	public List<Entity> find(final Connection conn, final Entity where) throws SQLException {
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
	public List<Entity> findAll(final Connection conn, final Entity where) throws SQLException {
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
	public List<Entity> findAll(final Connection conn, final String tableName) throws SQLException {
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
	public List<Entity> findBy(final Connection conn, final String tableName, final String field, final Object value) throws SQLException {
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
	public List<Entity> findLike(final Connection conn, final String tableName, final String field, final String value, final LikeType likeType) throws SQLException {
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
	public List<Entity> findIn(final Connection conn, final String tableName, final String field, final Object... values) throws SQLException {
		return findAll(conn, Entity.create(tableName).set(field, values));
	}

	/**
	 * 获取查询结果总数，生成类似于 SELECT count(1) from (sql) as _count
	 *
	 * @param conn      数据库连接对象
	 * @param selectSql 查询语句
	 * @param params    查询参数
	 * @return 结果数
	 * @throws SQLException SQL异常
	 * @since 5.6.6
	 */
	public long count(final Connection conn, final CharSequence selectSql, final Object... params) throws SQLException {
		return count(conn, SqlBuilder.of(selectSql).addParams(params));
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
	public <T> T page(final Connection conn, final Collection<String> fields, final Entity where, final int pageNumber, final int numPerPage, final RsHandler<T> rsh) throws SQLException {
		return page(conn, Query.of(where).setFields(fields).setPage(new Page(pageNumber, numPerPage)), rsh);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 * @since 5.5.3
	 */
	public PageResult<Entity> page(final Connection conn, final SqlBuilder sqlBuilder, final Page page) throws SQLException {
		final PageResultHandler pageResultHandler = new PageResultHandler(
				new PageResult<>(page.getPageNumber(), page.getPageSize(), (int) count(conn, sqlBuilder)),
				this.caseInsensitive);
		return page(conn, sqlBuilder, page, pageResultHandler);
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
	public PageResult<Entity> page(final Connection conn, final Collection<String> fields, final Entity where, final int page, final int numPerPage) throws SQLException {
		return page(conn, fields, where, new Page(page, numPerPage));
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
	public PageResult<Entity> page(final Connection conn, final Entity where, final Page page) throws SQLException {
		return this.page(conn, null, where, page);
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
	public PageResult<Entity> page(final Connection conn, final Collection<String> fields, final Entity where, final Page page) throws SQLException {
		final PageResultHandler pageResultHandler = new PageResultHandler(
				new PageResult<>(page.getPageNumber(), page.getPageSize(), (int) count(conn, where)),
				this.caseInsensitive);
		return page(conn, fields, where, page, pageResultHandler);
	}

	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 *
	 * @param <T>     结果类型，取决于 {@link RsHandler} 的处理逻辑
	 * @param conn    数据库连接对象
	 * @param fields  返回的字段列表，null则返回所有字段
	 * @param where   条件实体类（包含表名）
	 * @param page    分页对象
	 * @param handler 结果集处理器
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T page(final Connection conn, final Collection<String> fields, final Entity where, final Page page, final RsHandler<T> handler) throws SQLException {
		return this.page(conn, Query.of(where).setFields(fields).setPage(page), handler);
	}
	//---------------------------------------------------------------------------- CRUD end
}
