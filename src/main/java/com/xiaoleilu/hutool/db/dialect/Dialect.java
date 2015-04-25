package com.xiaoleilu.hutool.db.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.SqlBuilder.Order;

/**
 * SQL方言，不同的数据库由于在某些SQL上有所区别，故为每种数据库配置不同的方言。
 * @author loolly
 *
 */
public interface Dialect {
	//-------------------------------------------- Execute
	/**
	 * 构建用于插入的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForInsert(Connection conn, Entity entity) throws SQLException;
	
	/**
	 * 构建用于删除的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForDelete(Connection conn, Entity entity) throws SQLException;
	
	/**
	 * 构建用于更新的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param where 条件数据类（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForUpdate(Connection conn, Entity entity, Entity where) throws SQLException;
	
	//-------------------------------------------- Query
	/**
	 * 构建用于获取多条记录的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForFind(Connection conn, Collection<String> fields, Entity where) throws SQLException;
	
	/**
	 * 构建用于分页查询的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException;
	
	/**
	 * 构建用于分页查询的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @param orderFields 排序字段
	 * @param order 排序方式
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage, Collection<String> orderFields, Order order) throws SQLException;
	
	/**
	 * 构建用于查询行数的PreparedStatement
	 * @param conn 数据库连接对象
	 * @param where 条件实体类
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement psForCount(Connection conn, Entity where) throws SQLException;
}
