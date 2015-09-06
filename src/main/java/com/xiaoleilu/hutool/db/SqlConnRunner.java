package com.xiaoleilu.hutool.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.hutool.db.dialect.Dialect;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.db.handler.NumberHandler;
import com.xiaoleilu.hutool.db.handler.RsHandler;

/**
 * SQL执行类<br>
 * 此执行类只接受方言参数，不需要数据源，只有在执行方法时需要数据库连接对象<br>
 * 此对象存在的意义在于，可以由使用者自定义数据库连接对象，并执行多个方法，方便事务的统一控制或减少连接对象的创建关闭
 * @author Luxiaolei
 * 
 */
public class SqlConnRunner{
	private Dialect dialect;
	
	//------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param dialect 方言
	 */
	public SqlConnRunner(Dialect dialect) {
		Log.info("Use Dialect: [{}].", dialect.getClass().getSimpleName());
		
		this.dialect = dialect;
	}
	
	/**
	 * 构造
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
	 * @param conn 数据库连接
	 * @param record 记录
	 * @return 插入行数
	 * @throws SQLException
	 */
	public int insert(Connection conn, Entity record) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param record 记录
	 * @return 主键列表
	 * @throws SQLException
	 */
	public List<Object> insertForGeneratedKeys(Connection conn, Entity record) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			return DbUtil.getGeneratedKeys(ps);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 插入数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param record 记录
	 * @return 自增主键
	 * @throws SQLException
	 */
	public Long insertForGeneratedKey(Connection conn, Entity record) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForInsert(conn, record);
			ps.executeUpdate();
			return DbUtil.getGeneratedKeyOfLong(ps);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 删除数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param where 条件
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int del(Connection conn, Entity where) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForDelete(conn, where);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 更新数据<br>
	 * 此方法不会关闭Connection
	 * @param conn 数据库连接
	 * @param record 记录
	 * @return 影响行数
	 * @throws SQLException
	 */
	public int update(Connection conn, Entity record, Entity where) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = dialect.psForUpdate(conn, record, where);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 查询<br>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T find(Connection conn, Collection<String> fields, Entity where, RsHandler<T> rsh) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dialect.psForFind(conn, fields, where);
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}
	
	/**
	 * 查询，返回所有字段<br>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param where 条件实体类（包含表名）
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T find(Connection conn, Entity where, RsHandler<T> rsh) throws SQLException {
		return find(conn, null, where, rsh);
	}
	
	/**
	 * 分页查询<br>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param fields 返回的字段列表，null则返回所有字段
	 * @param where 条件实体类（包含表名）
	 * @param page 页码
	 * @param numPerPage 每页条目数
	 * @param rsh 结果集处理对象
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T page(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage, RsHandler<T> rsh) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dialect.psForPage(conn, fields, where, page, numPerPage);
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}
	
	/**
	 * 结果的条目数
	 * @param conn 数据库连接对象
	 * @param where 查询条件
	 * @return 复合条件的结果数
	 * @throws SQLException
	 */
	public int count(Connection conn, Entity where) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dialect.psForCount(conn, where);
			rs = ps.executeQuery();
			return new NumberHandler().handle(rs).intValue();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}
	
	//---------------------------------------------------------------------------- CRUD end
	
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method start
}