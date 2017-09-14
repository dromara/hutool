package com.xiaoleilu.hutool.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.xiaoleilu.hutool.db.handler.EntityHandler;
import com.xiaoleilu.hutool.db.handler.EntityListHandler;
import com.xiaoleilu.hutool.db.handler.RsHandler;
import com.xiaoleilu.hutool.db.sql.Condition.LikeType;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.db.sql.SqlExecutor;

/**
 * 抽象SQL执行类<br>
 * 通过给定的数据源执行给定SQL或者给定数据源和方言，执行相应的CRUD操作<br>
 * 提供抽象方法getConnection和closeConnection，用于自定义数据库连接的打开和关闭
 * 
 * @author Luxiaolei
 * 
 */
public abstract class AbstractSqlRunner{
	protected SqlConnRunner runner;
	
	//------------------------------------------------------- Constructor start
	//------------------------------------------------------- Constructor end
	
	/**
	 * 获得链接。根据实现不同，可以自定义获取连接的方式
	 * @return {@link Connection}
	 * @throws SQLException 连接获取异常
	 */
	public abstract Connection getConnection() throws SQLException;
	
	/**
	 * 关闭连接<br>
	 * 自定义关闭连接有利于自定义回收连接机制，或者不关闭
	 * @param conn 连接 {@link Connection}
	 */
	public abstract void closeConnection(Connection conn);
	
	/**
	 * 查询
	 * 
	 * @param <T> 结果集需要处理的对象类型
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 * @since 3.1.1
	 */
	public <T> List<Entity> query(String sql, Object... params) throws SQLException {
		return query(sql, new EntityListHandler(), params);
	}

	/**
	 * 查询
	 * 
	 * @param <T> 结果集需要处理的对象类型
	 * @param sql 查询语句
	 * @param rsh 结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T query(String sql, RsHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.query(conn, sql, rsh, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除
	 * 
	 * @param sql SQL
	 * @param params 参数
	 * @return 影响行数
	 * @throws SQLException SQL执行异常
	 */
	public int execute(String sql, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.execute(conn, sql, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 执行非查询语句<br>
	 * 语句包括 插入、更新、删除
	 * 
	 * @param sql SQL
	 * @param params 参数
	 * @return 主键
	 * @throws SQLException SQL执行异常
	 */
	public Long executeForGeneratedKey(String sql, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.executeForGeneratedKey(conn, sql, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}

	/**
	 * 批量执行非查询语句
	 * 
	 * @param sql SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws SQLException SQL执行异常
	 */
	public int[] executeBatch(String sql, Object[]... paramsBatch) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return SqlExecutor.executeBatch(conn, sql, paramsBatch);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}

	//---------------------------------------------------------------------------- CRUD start
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException SQL执行异常
	 */
	public int insert(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insert(conn, record);
		} catch (SQLException e) {
			throw e;
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
	 * @throws SQLException SQL执行异常
	 */
	public int[] insert(Collection<Entity> records) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insert(conn, records);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 主键列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Object> insertForGeneratedKeys(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insertForGeneratedKeys(conn, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 插入数据
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException SQL执行异常
	 */
	public Long insertForGeneratedKey(Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.insertForGeneratedKey(conn, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 删除数据
	 * 
	 * @param tableName 表名
	 * @param field 字段名，最好是主键
	 * @param value 值，值可以是列表或数组，被当作IN查询处理
	 * @return 删除行数
	 * @throws SQLException SQL执行异常
	 */
	public int del(String tableName, String field, Object value) throws SQLException {
		return del(Entity.create(tableName).set(field, value));
	}
	
	/**
	 * 删除数据
	 * 
	 * @param where 条件
	 * @return 影响行数
	 * @throws SQLException SQL执行异常
	 */
	public int del(Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.del(conn, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 更新数据
	 * 
	 * @param record 记录
	 * @param where 条件
	 * @return 影响行数
	 * @throws SQLException SQL执行异常
	 */
	public int update(Entity record, Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.update(conn, record, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	//------------------------------------------------------------- Get start
	/**
	 * 根据某个字段（最好是唯一字段）查询单个记录<br>
	 * 当有多条返回时，只显示查询到的第一条
	 * 
	 * @param <T> 字段值类型
	 * @param tableName 表名
	 * @param field 字段名
	 * @param value 字段值
	 * @return 记录
	 * @throws SQLException SQL执行异常
	 */
	public <T> Entity get(String tableName, String field, T value) throws SQLException {
		return this.get(Entity.create(tableName).set(field, value));
	}
	
	/**
	 * 根据条件实体查询单个记录，当有多条返回时，只显示查询到的第一条
	 * 
	 * @param where 条件
	 * @return 记录
	 * @throws SQLException SQL执行异常
	 */
	public Entity get(Entity where) throws SQLException {
		return find(null, where, new EntityHandler());
	}
	//------------------------------------------------------------- Get end
	
	/**
	 * 查询
	 * 
	 * @param <T> 需要处理成的结果对象类型
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T find(Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.find(conn, fields, where, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 查询，返回所有字段
	 * 
	 * @param <T> 需要处理成的结果对象类型
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @param fields 字段列表，可变长参数如果无值表示查询全部字段
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T find(Entity where, RsHandler<T> rsh, String... fields) throws SQLException {
		return find(CollectionUtil.newArrayList(fields), where, rsh);
	}
	
	/**
	 * 查询数据列表，返回所有字段
	 * 
	 * @param where 条件实体类（包含表名）
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findAll(Entity where) throws SQLException{
		return find(where, EntityListHandler.create());
	}
	
	/**
	 * 查询数据列表，返回所有字段
	 * 
	 * @param tableName 表名
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findAll(String tableName) throws SQLException{
		return findAll(Entity.create(tableName));
	}
	
	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 * 
	 * @param tableName 表名
	 * @param field 字段名
	 * @param value 字段值
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findBy(String tableName, String field, Object value) throws SQLException{
		return findAll(Entity.create(tableName).set(field, value));
	}
	
	/**
	 * 根据某个字段名条件查询数据列表，返回所有字段
	 * 
	 * @param tableName 表名
	 * @param field 字段名
	 * @param value 字段值
	 * @param likeType {@link LikeType}
	 * @return 数据对象列表
	 * @throws SQLException SQL执行异常
	 */
	public List<Entity> findLike(String tableName, String field, String value, LikeType likeType) throws SQLException{
		return findAll(Entity.create(tableName).set(field, DbUtil.buildLikeValue(value, likeType)));
	}
	
	/**
	 * 结果的条目数
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException SQL执行异常
	 */
	public int count(Entity where) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.count(conn, where);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 分页查询<br>
	 * 
	 * @param <T> 结果对象类型
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T page(Collection<String> fields, Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, fields, where, page, numPerPage, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 分页查询<br>
	 * 
	 * @param <T> 结果对象类型
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 分页对象
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public <T> T page(Collection<String> fields, Entity where, Page page, RsHandler<T> rsh) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, fields, where, page, rsh);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 分页查询<br>
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public PageResult<Entity> page(Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, fields, where, page, numPerPage);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 分页查询<br>
	 * 
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @return 结果对象
	 * @throws SQLException SQL执行异常
	 */
	public PageResult<Entity> page(Collection<String> fields, Entity where, Page page) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return runner.page(conn, fields, where, page);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * 分页查询<br>
	 * 
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @return 分页结果集
	 * @throws SQLException SQL执行异常
	 */
	public PageResult<Entity> page(Entity where, Page page) throws SQLException {
		return this.page(null, where, page);
	}
	//---------------------------------------------------------------------------- CRUD end
	
	//---------------------------------------------------------------------------- Getters and Setters start
	/**
	 * 获取{@link SqlConnRunner}
	 * @return {@link SqlConnRunner}
	 */
	public SqlConnRunner getRunner() {
		return runner;
	}
	
	/**
	 * 设置 {@link SqlConnRunner}
	 * @param runner {@link SqlConnRunner}
	 */
	public void setRunner(SqlConnRunner runner) {
		this.runner = runner;
	}
	//---------------------------------------------------------------------------- Getters and Setters end
	
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method end
}