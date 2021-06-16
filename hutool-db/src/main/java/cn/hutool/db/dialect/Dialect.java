package cn.hutool.db.dialect;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.sql.Order;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.Wrapper;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL方言，不同的数据库由于在某些SQL上有所区别，故为每种数据库配置不同的方言。<br>
 * 由于不同数据库间SQL语句的差异，导致无法统一拼接SQL，<br>
 * Dialect接口旨在根据不同的数据库，使用不同的方言实现类，来拼接对应的SQL，并将SQL和参数放入PreparedStatement中
 *
 * @author loolly
 */
public interface Dialect extends Serializable {

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
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForInsert(Connection conn, Entity entity) throws SQLException;

	/**
	 * 构建用于批量插入的PreparedStatement
	 *
	 * @param conn     数据库连接对象
	 * @param entities 数据实体，实体的结构必须全部一致，否则插入结果将不可预知
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForInsertBatch(Connection conn, Entity... entities) throws SQLException;

	/**
	 * 构建用于删除的PreparedStatement
	 *
	 * @param conn  数据库连接对象
	 * @param query 查找条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForDelete(Connection conn, Query query) throws SQLException;

	/**
	 * 构建用于更新的PreparedStatement
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param query  查找条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForUpdate(Connection conn, Entity entity, Query query) throws SQLException;

	// -------------------------------------------- Query

	/**
	 * 构建用于获取多条记录的PreparedStatement
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForFind(Connection conn, Query query) throws SQLException;

	/**
	 * 构建用于分页查询的PreparedStatement
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	PreparedStatement psForPage(Connection conn, Query query) throws SQLException;

	/**
	 * 构建用于分页查询的PreparedStatement<br>
	 * 可以在此方法中使用{@link SqlBuilder#orderBy(Order...)}方法加入排序信息，
	 * 排序信息通过{@link Page#getOrders()}获取
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 * @since 5.5.3
	 */
	PreparedStatement psForPage(Connection conn, SqlBuilder sqlBuilder, Page page) throws SQLException;

	/**
	 * 构建用于查询行数的PreparedStatement
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 */
	default PreparedStatement psForCount(Connection conn, Query query) throws SQLException{
		query.setFields(ListUtil.toList("count(1)"));
		return psForFind(conn, query);
	}

	/**
	 * 构建用于查询行数的PreparedStatement
	 *
	 * @param conn  数据库连接对象
	 * @param sqlBuilder 查询语句，应该包含分页等信息
	 * @return PreparedStatement
	 * @throws SQLException SQL执行异常
	 * @since 5.7.2
	 */
	default PreparedStatement psForCount(Connection conn, SqlBuilder sqlBuilder) throws SQLException{
		sqlBuilder = sqlBuilder
				.insertPreFragment("SELECT count(1) from(")
				// issue#I3IJ8X@Gitee，在子查询时需设置单独别名，此处为了防止和用户的表名冲突，使用自定义的较长别名
				.append(") hutool_alias_count_");
		return psForPage(conn, sqlBuilder, null);
	}

	/**
	 * 方言名
	 *
	 * @return 方言名
	 * @since 5.5.3
	 */
	String dialectName();
}
