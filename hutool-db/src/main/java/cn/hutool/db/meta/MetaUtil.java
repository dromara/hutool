package cn.hutool.db.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;

/**
 * 数据库元数据信息工具类
 * 
 * @author looly
 *
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
	 * @param ds 数据源
	 * @param types 表类型
	 * @return 表名列表
	 */
	public static List<String> getTables(DataSource ds, TableType... types) {
		return getTables(ds, null, null, types);
	}

	/**
	 * 获得所有表名
	 * 
	 * @param ds 数据源
	 * @param schema 表数据库名，对于Oracle为用户名
	 * @param types 表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, TableType... types) {
		return getTables(ds, schema, null, types);
	}

	/**
	 * 获得所有表名
	 * 
	 * @param ds 数据源
	 * @param schema 表数据库名，对于Oracle为用户名
	 * @param tableName 表名
	 * @param types 表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, String tableName, TableType... types) {
		final List<String> tables = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getTables(conn.getCatalog(), schema, tableName, Convert.toStrArray(types));
			if (rs == null) {
				return null;
			}
			String table;
			while (rs.next()) {
				table = rs.getString("TABLE_NAME");
				if (StrUtil.isNotBlank(table)) {
					tables.add(table);
				}
			}
		} catch (Exception e) {
			throw new DbRuntimeException("Get tables error!", e);
		} finally {
			DbUtil.close(rs, conn);
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
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return 列数组
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static String[] getColumnNames(DataSource ds, String tableName) {
		List<String> columnNames = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
			while (rs.next()) {
				columnNames.add(rs.getString("COLUMN_NAME"));
			}
			return columnNames.toArray(new String[columnNames.size()]);
		} catch (Exception e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			DbUtil.close(rs, conn);
		}
	}
	
	/**
	 * 创建带有字段限制的Entity对象<br>
	 * 此方法读取数据库中对应表的字段列表，加入到Entity中，当Entity被设置内容时，会忽略对应表字段外的所有KEY
	 * 
	 * @param ds 数据源
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
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return Table对象
	 */
	@SuppressWarnings("resource")
	public static Table getTableMeta(DataSource ds, String tableName) {
		final Table table = Table.create(tableName);
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			// 获得主键
			rs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName);
			while (rs.next()) {
				table.addPk(rs.getString("COLUMN_NAME"));
			}

			// 获得列
			rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
			while (rs.next()) {
				table.setColumn(Column.create(tableName, rs));
			}
		} catch (SQLException e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			DbUtil.close(rs, conn);
		}

		return table;
	}
}
