package cn.hutool.db.meta;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库元数据信息工具类
 *
 * @author looly
 */
public class MetaUtil {
	/**
	 * 获得所有表名
	 *
	 * @param ds 数据源
	 * @return 表名列表
	 */
	public static List<String> getTables(DataSource ds) {
		return getTables(ds, TableType.TABLE);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds    数据源
	 * @param types 表类型
	 * @return 表名列表
	 */
	public static List<String> getTables(DataSource ds, TableType... types) {
		return getTables(ds, null, null, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds     数据源
	 * @param schema 表数据库名，对于Oracle为用户名
	 * @param types  表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, TableType... types) {
		return getTables(ds, schema, null, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds        数据源
	 * @param schema    表数据库名，对于Oracle为用户名
	 * @param tableName 表名
	 * @param types     表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, String tableName, TableType... types) {
		final List<String> tables = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			String catalog = getCataLog(conn);
			if (null == schema) {
				schema = getSchema(conn);
			}

			final DatabaseMetaData metaData = conn.getMetaData();
			try (ResultSet rs = metaData.getTables(catalog, schema, tableName, Convert.toStrArray(types))) {
				if (null != rs) {
					String table;
					while (rs.next()) {
						table = rs.getString("TABLE_NAME");
						if (StrUtil.isNotBlank(table)) {
							tables.add(table);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new DbRuntimeException("Get tables error!", e);
		} finally {
			DbUtil.close(conn);
		}
		return tables;
	}

	/**
	 * 获得结果集的所有列名
	 *
	 * @param rs 结果集
	 * @return 列名数组
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static String[] getColumnNames(ResultSet rs) throws DbRuntimeException {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] labelNames = new String[columnCount];
			for (int i = 0; i < labelNames.length; i++) {
				labelNames[i] = rsmd.getColumnLabel(i + 1);
			}
			return labelNames;
		} catch (Exception e) {
			throw new DbRuntimeException("Get colunms error!", e);
		}
	}

	/**
	 * 获得表的所有列名
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return 列数组
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static String[] getColumnNames(DataSource ds, String tableName) {
		List<String> columnNames = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			String catalog = getCataLog(conn);
			String schema = getSchema(conn);

			final DatabaseMetaData metaData = conn.getMetaData();
			try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
				if (null != rs) {
					while (rs.next()) {
						columnNames.add(rs.getString("COLUMN_NAME"));
					}
				}
			}
			return columnNames.toArray(new String[0]);
		} catch (Exception e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 创建带有字段限制的Entity对象<br>
	 * 此方法读取数据库中对应表的字段列表，加入到Entity中，当Entity被设置内容时，会忽略对应表字段外的所有KEY
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return Entity对象
	 */
	public static Entity createLimitedEntity(DataSource ds, String tableName) {
		final String[] columnNames = getColumnNames(ds, tableName);
		return Entity.create(tableName).setFieldNames(columnNames);
	}

	/**
	 * 获得表的元信息
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return Table对象
	 */
	public static Table getTableMeta(DataSource ds, String tableName) {
		final Table table = Table.create(tableName);
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			String catalog = getCataLog(conn);
			String schema = getSchema(conn);

			final DatabaseMetaData metaData = conn.getMetaData();

			// 获得表元数据（表注释）
			try (ResultSet rs = metaData.getTables(catalog, schema, tableName, new String[]{TableType.TABLE.value()})) {
				if (null != rs) {
					if (rs.next()) {
						table.setComment(rs.getString("REMARKS"));
					}
				}
			}

			// 获得主键
			try (ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
				if (null != rs) {
					while (rs.next()) {
						table.addPk(rs.getString("COLUMN_NAME"));
					}
				}
			}

			// 获得列
			try (ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
				if (null != rs) {
					while (rs.next()) {
						table.setColumn(Column.create(tableName, rs));
					}
				}
			}
		} catch (SQLException e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			DbUtil.close(conn);
		}

		return table;
	}

	/**
	 * 获取catalog，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return catalog，获取失败返回{@code null}
	 * @since 4.6.0
	 */
	public static String getCataLog(Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getCatalog();
		} catch (SQLException e) {
			// ignore
		}

		return null;
	}

	/**
	 * 获取schema，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return schema，获取失败返回{@code null}
	 * @since 4.6.0
	 */
	public static String getSchema(Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getSchema();
		} catch (SQLException e) {
			// ignore
		}

		return null;
	}
}
