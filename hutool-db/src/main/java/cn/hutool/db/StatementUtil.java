package cn.hutool.db;

import java.math.BigDecimal;
import java.math.BigInteger;
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

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlLog;
import cn.hutool.db.sql.SqlUtil;

/**
 * Statement和PreparedStatement工具类
 * 
 * @author looly
 * @since 4.0.10
 */
public class StatementUtil {
	/**
	 * 填充SQL的参数。
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillParams(PreparedStatement ps, Collection<Object> params) throws SQLException {
		return fillParams(ps, params.toArray(new Object[params.size()]));
	}

	/**
	 * 填充SQL的参数。<br>
	 * 对于日期对象特殊处理：传入java.util.Date默认按照Timestamp处理
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillParams(PreparedStatement ps, Object... params) throws SQLException {
		if (ArrayUtil.isEmpty(params)) {
			return ps;// 无参数
		}
		Object param;
		for (int i = 0; i < params.length; i++) {
			int paramIndex = i + 1;
			param = params[i];
			if (null != param) {
				if (param instanceof java.util.Date) {
					// 日期特殊处理
					if (param instanceof java.sql.Date) {
						ps.setDate(paramIndex, (java.sql.Date) param);
					} else if (param instanceof java.sql.Time) {
						ps.setTime(paramIndex, (java.sql.Time) param);
					} else {
						ps.setTimestamp(paramIndex, SqlUtil.toSqlTimestamp((java.util.Date) param));
					}
				} else if (param instanceof Number) {
					// 针对大数字类型的特殊处理
					if (param instanceof BigInteger) {
						// BigInteger转为Long
						ps.setLong(paramIndex, ((BigInteger) param).longValue());
					} else if (param instanceof BigDecimal) {
						// BigDecimal的转换交给JDBC驱动处理
						ps.setBigDecimal(paramIndex, (BigDecimal) param);
					} else {
						// 普通数字类型按照默认传入
						ps.setObject(paramIndex, param);
					}
				} else {
					ps.setObject(paramIndex, param);
				}
			} else {
				final ParameterMetaData pmd = ps.getParameterMetaData();
				int sqlType = Types.VARCHAR;
				try {
					sqlType = pmd.getParameterType(paramIndex);
				} catch (SQLException e) {
					// ignore
					// log.warn("Null param of index [{}] type get failed, by: {}", paramIndex, e.getMessage());
				}
				ps.setNull(paramIndex, sqlType);
			}
		}
		return ps;
	}

	/**
	 * 创建{@link PreparedStatement}
	 * 
	 * @param conn 数据库连接
	 * @param sqlBuilder {@link SqlBuilder}包括SQL语句和参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 4.1.3
	 */
	public static PreparedStatement prepareStatement(Connection conn, SqlBuilder sqlBuilder) throws SQLException {
		return prepareStatement(conn, sqlBuilder.build(), sqlBuilder.getParamValueArray());
	}

	/**
	 * 创建{@link PreparedStatement}
	 * 
	 * @param conn 数据库连接
	 * @param sql SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	public static PreparedStatement prepareStatement(Connection conn, String sql, Collection<Object> params) throws SQLException {
		return prepareStatement(conn, sql, params.toArray(new Object[params.size()]));
	}

	/**
	 * 创建{@link PreparedStatement}
	 * 
	 * @param conn 数据库连接
	 * @param sql SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	public static PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTASNCE.log(sql, params);
		PreparedStatement ps;
		if (StrUtil.startWithIgnoreCase(sql, "insert")) {
			// 插入默认返回主键
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} else {
			ps = conn.prepareStatement(sql);
		}
		return fillParams(ps, params);
	}

	/**
	 * 获得自增键的值<br>
	 * 此方法对于Oracle无效
	 * 
	 * @param ps PreparedStatement
	 * @return 自增键的值
	 * @throws SQLException SQL执行异常
	 */
	public static Long getGeneratedKeyOfLong(PreparedStatement ps) throws SQLException {
		ResultSet rs = null;
		try {
			rs = ps.getGeneratedKeys();
			Long generatedKey = null;
			if (rs != null && rs.next()) {
				try {
					generatedKey = rs.getLong(1);
				} catch (SQLException e) {
					// 自增主键不为数字或者为Oracle的rowid，跳过
				}
			}
			return generatedKey;
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs);
		}
	}

	/**
	 * 获得所有主键<br>
	 * 
	 * @param ps PreparedStatement
	 * @return 所有主键
	 * @throws SQLException SQL执行异常
	 */
	public static List<Object> getGeneratedKeys(PreparedStatement ps) throws SQLException {
		List<Object> keys = new ArrayList<Object>();
		ResultSet rs = null;
		int i = 1;
		try {
			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				keys.add(rs.getObject(i++));
			}
			return keys;
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtil.close(rs);
		}
	}
}
