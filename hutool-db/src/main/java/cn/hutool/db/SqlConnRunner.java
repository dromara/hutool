package cn.hutool.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.handler.HandleHelper;
import cn.hutool.db.handler.NumberHandler;
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
 * 此对象存在的意义在于，可以由使用者自定义数据库连接对象，并执行多个方法，方便事务的统一控制或减少连接对象的创建关闭
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
		super(dialect);
	}

	/**
	 * 构造
	 *
	 * @param driverClassName 驱动类名，，用于识别方言
	 */
	public SqlConnRunner(String driverClassName) {
		super(driverClassName);
	}
	//------------------------------------------------------- Constructor end

	//---------------------------------------------------------------------------- CRUD start

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
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 *
	 * @param conn   数据库连接
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int insert(Connection conn, Entity record) throws SQLException {
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
	public List<Object> insertForGeneratedKeys(Connection conn, Entity record) throws SQLException {
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
	public Long insertForGeneratedKey(Connection conn, Entity record) throws SQLException {
		return insert(conn, record, (rs) -> {
			Long generatedKey = null;
			if (rs != null && rs.next()) {
				try {
					generatedKey = rs.getLong(1);
				} catch (SQLException e) {
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
	public <T> T find(Connection conn, Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
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
	 * 获取查询结果总数，生成类似于 SELECT count(1) from (sql) as _count
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，包括SQL和参数
	 * @return 结果数
	 * @throws SQLException SQL异常
	 * @since 5.7.0
	 */
	public long count(Connection conn, SqlBuilder sqlBuilder) throws SQLException {
		return count(conn, sqlBuilder.build(), sqlBuilder.getParamValueArray());
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
	public long count(Connection conn, CharSequence selectSql, Object... params) throws SQLException {
		Assert.notBlank(selectSql, "Select SQL must be not blank!");
		final int orderByIndex = StrUtil.indexOfIgnoreCase(selectSql, " order by");
		if (orderByIndex > 0) {
			selectSql = StrUtil.subPre(selectSql, orderByIndex);
		}

		SqlBuilder sqlBuilder = SqlBuilder.of(selectSql)
				.insertPreFragment("SELECT count(1) from(")
				// issue#I3IJ8X@Gitee，在子查询时需设置单独别名，此处为了防止和用户的表名冲突，使用自定义的较长别名
				.append(") as _hutool_alias_count_")
				// 添加参数
				.addParams(params);
		return page(conn, sqlBuilder, null, new NumberHandler()).intValue();
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
	public PageResult<Entity> page(Connection conn, SqlBuilder sqlBuilder, Page page) throws SQLException {
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
	public PageResult<Entity> page(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
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
	public PageResult<Entity> page(Connection conn, Entity where, Page page) throws SQLException {
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
	public PageResult<Entity> page(Connection conn, Collection<String> fields, Entity where, Page page) throws SQLException {
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
	public <T> T page(Connection conn, Collection<String> fields, Entity where, Page page, RsHandler<T> handler) throws SQLException {
		return this.page(conn, Query.of(where).setFields(fields).setPage(page), handler);
	}
	//---------------------------------------------------------------------------- CRUD end
}
