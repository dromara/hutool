package cn.hutool.db.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.hutool.db.Entity;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.Wrapper;

/**
 * SQL方言，不同的数据库由于在某些SQL上有所区别，故为每种数据库配置不同的方言。<br>
 * 由于不同数据库间SQL语句的差异，导致无法统一拼接SQL，<br>
 * Dialect接口旨在根据不同的数据库，使用不同的方言实现类，来拼接对应的SQL，并将SQL和参数放入PreparedStatement中
 * 
 * @author loolly
 *
 */
public interface Dialect {

	/**
	 * @return 包装器
	 */
	Wrapper getWrapper();

	/**
	 * 设置包装器
	 * 
	 * @param wrapper 包装器
	 */
	void setWrapper(Wrapper wrapper);

	// -------------------------------------------- Execute
	/**
	 * 构建用于插入的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForInsert(Connection conn, Entity entity) throws SQLException;
	
	/**
	 * 构建用于批量插入的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param entities 数据实体，实体的结构必须全部一致，否则插入结果将不可预知
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForInsertBatch(Connection conn, Entity... entities) throws SQLException;
	
	/**
	 * 构建用于删除的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param query 查找条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForDelete(Connection conn, Query query) throws SQLException;

	/**
	 * 构建用于更新的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param query 查找条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForUpdate(Connection conn, Entity entity, Query query) throws SQLException;

	// -------------------------------------------- Query
	/**
	 * 构建用于获取多条记录的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForFind(Connection conn, Query query) throws SQLException;

	/**
	 * 构建用于分页查询的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForPage(Connection conn, Query query) throws SQLException;

	/**
	 * 构建用于查询行数的PreparedStatement
	 * 
	 * @param conn 数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForCount(Connection conn, Query query) throws SQLException;

	/**
	 * 方言名
	 * 
	 * @return 方言名
	 */
	DialectName dialectName();
}
