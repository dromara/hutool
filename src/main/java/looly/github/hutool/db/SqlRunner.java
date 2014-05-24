package looly.github.hutool.db;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import looly.github.hutool.Log;
import looly.github.hutool.exceptions.UtilException;

import org.slf4j.Logger;

/**
 * SQL执行类
 * 
 * @author Luxiaolei
 * 
 */
public class SqlRunner {
	private static Logger log = Log.get();

	private DataSource ds;

	public SqlRunner(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * 查询
	 * 
	 * @param sql 查询语句
	 * @param rsh 结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T query(String sql, RsHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return this.query(conn, sql, rsh, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 查询<br/>
	 * 发查询语句包括 插入、更新、删除<br/>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param sql 查询语句
	 * @param rsh 结果集处理对象
	 * @param params 参数
	 * @return 结果对象
	 * @throws SQLException
	 */
	public <T> T query(Connection conn, String sql, RsHandler<T> rsh, Object... params) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			this.fillParams(ps, params);
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}

	/**
	 * 执行非查询语句<br/>
	 * 发查询语句包括 插入、更新、删除
	 * 
	 * @param sql SQL
	 * @param params 参数
	 * @return 主键
	 * @throws SQLException
	 */
	public Long execute(String sql, Object... params) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return this.execute(conn, sql, params);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 执行非查询语句<br/>
	 * 发查询语句包括 插入、更新、删除<br/>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL
	 * @param params 参数
	 * @return 主键
	 * @throws SQLException
	 */
	public Long execute(Connection conn, String sql, Object... params) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.fillParams(ps, params);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys(); 
			if(rs != null) {
				return rs.getLong(1);
			}
			return null;
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}

	/**
	 * 批量执行非查询语句
	 * 
	 * @param sql SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws SQLException
	 */
	public int[] executeBatch(String sql, Object[]... paramsBatch) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return this.executeBatch(conn, sql, paramsBatch);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 批量执行非查询语句<br/>
	 * 发查询语句包括 插入、更新、删除<br/>
	 * 此方法不会关闭Connection
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL
	 * @param paramsBatch 批量的参数
	 * @return 每个SQL执行影响的行数
	 * @throws SQLException
	 */
	public int[] executeBatch(Connection conn, String sql, Object[]... paramsBatch) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (Object[] params : paramsBatch) {
				this.fillParams(ps, params);
				ps.addBatch();
			}
			return ps.executeBatch();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 插入数据
	 * @param conn 数据库连接
	 * @param tableName 表名
	 * @param isReplace 是否替换（会调用SQL的replace into方法）
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public Long insert(Connection conn, String tableName,  boolean isReplace, Entity record) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.psForInsert(conn, tableName, isReplace, record);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys(); 
			Long primaryKey = null;
			if(rs != null && rs.next()) {
				primaryKey = rs.getLong(1);
			}
			return primaryKey;
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs, ps);
		}
	}

	/**
	 * 插入数据
	 * @param tableName 表名
	 * @param isReplace 是否替换（会调用SQL的replace into方法）
	 * @param record 记录
	 * @return 主键
	 * @throws SQLException
	 */
	public Long insert(String tableName, boolean isReplace, Entity record) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return insert(conn, tableName, isReplace, record);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	
	/**
	 * 批量插入数据
	 * @param conn 数据库连接
	 * @param tableName 表名
	 * @param isReplace 是否替换（会调用SQL的replace into方法）
	 * @param records 记录
	 * @return 行数
	 * @throws SQLException
	 */
	public int[] insertBatch(Connection conn, String tableName, boolean isReplace, Entity... records) throws SQLException {
		if(records == null || records.length == 0) {
			return null;
		}
		
		if(records.length == 1) {
			insert(conn, tableName, isReplace, records[0]);
			return new int[]{1};
		}
		
		PreparedStatement ps = null;
		try {
			ps = this.psForInsert(conn, tableName, isReplace, records);
			return ps.executeBatch();
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(ps);
		}
	}
	
	/**
	 * 批量插入数据
	 * @param tableName 表名
	 * @param isReplace 是否替换（会调用SQL的replace into方法）
	 * @param records 记录
	 * @return 行数
	 * @throws SQLException
	 */
	public int[] insertBatch(String tableName, boolean isReplace, Entity... records) throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return insertBatch(conn, tableName, isReplace, records);
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(conn);
		}
	}
	//---------------------------------------------------------------------------- Private method start
	/**
	 * 填充SQL的参数。
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @throws SQLException
	 */
	private void fillParams(PreparedStatement ps, Object... params) throws SQLException {
		if (params == null) {
			return;
		}
		ParameterMetaData pmd = ps.getParameterMetaData();
		for (int i = 0; i < params.length; i++) {
			int paramIndex = i + 1;
			if (params[i] != null) {
				ps.setObject(paramIndex, params[i]);
			} else {
				int sqlType = Types.VARCHAR;
				try {
					sqlType = pmd.getParameterType(paramIndex);
				} catch (SQLException e) {
					log.warn("Param get type fail, by: " + e.getMessage());
				}
				ps.setNull(paramIndex, sqlType);
			}
		}
	}
	
	/**
	 * 构建Insert的PreparedStatement
	 * @param conn Connection
	 * @param tableName 表名
	 * @param isReplace 是否使用Replace方式插入
	 * @param records 插入的记录
	 * @return PreparedStatement
	 * @throws SQLException 
	 */
	private PreparedStatement psForInsert(Connection conn, String tableName, boolean isReplace, Entity... records) throws SQLException {
		if(records == null || records.length == 0) {
			throw new UtilException("Records can not be null!");
		}
		
		Map<String, Object> record = records[0];
		final List<Object> firstItemParams = new ArrayList<Object>(record.size());
		final StringBuilder sql = new StringBuilder();
		
		sql.append(isReplace ? "replace" : "insert");
		sql.append(" into `");
		sql.append(tableName.trim()).append("`(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values(");
		
		for (Entry<String, Object> entry: record.entrySet()) {
			if (firstItemParams.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append("`").append(entry.getKey()).append("`");
			temp.append("?");
			firstItemParams.add(entry.getValue());
		}
		sql.append(temp.toString()).append(")");
		
		final PreparedStatement ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		if(records.length > 1) {
			for (Entity map : records) {
				Collection<Object> params = map.values();
				if(params.size() != firstItemParams.size()) {
					insert(conn, tableName, isReplace, map);
					continue;
				}
				fillParams(ps, params.toArray(new Object[record.size()]));
				//只有多于一条才是批量模式
				ps.addBatch();
			}
		}else {
			fillParams(ps, firstItemParams.toArray(new Object[firstItemParams.size()]));
		}
		return ps;
	}
	//---------------------------------------------------------------------------- Private method start
}