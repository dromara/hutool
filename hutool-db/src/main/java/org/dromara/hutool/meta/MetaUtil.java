/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.meta;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.DbRuntimeException;
import org.dromara.hutool.Entity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库元数据信息工具类
 *
 * <p>
 * 需要注意的是，此工具类在某些数据库（比如Oracle）下无效，此时需要手动在数据库配置中增加：
 * <pre>
 *  remarks = true
 *  useInformationSchema = true
 * </pre>
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
	public static List<String> getTables(final DataSource ds) {
		return getTables(ds, TableType.TABLE);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds    数据源
	 * @param types 表类型
	 * @return 表名列表
	 */
	public static List<String> getTables(final DataSource ds, final TableType... types) {
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
	public static List<String> getTables(final DataSource ds, final String schema, final TableType... types) {
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
	public static List<String> getTables(final DataSource ds, String schema, final String tableName, final TableType... types) {
		final List<String> tables = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			final String catalog = getCatalog(conn);
			if (null == schema) {
				schema = getSchema(conn);
			}

			final DatabaseMetaData metaData = conn.getMetaData();
			try (final ResultSet rs = metaData.getTables(catalog, schema, tableName, Convert.toStrArray(types))) {
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
		} catch (final Exception e) {
			throw new DbRuntimeException("Get tables error!", e);
		} finally {
			IoUtil.closeQuietly(conn);
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
	public static String[] getColumnNames(final ResultSet rs) throws DbRuntimeException {
		try {
			final ResultSetMetaData rsmd = rs.getMetaData();
			final int columnCount = rsmd.getColumnCount();
			final String[] labelNames = new String[columnCount];
			for (int i = 0; i < labelNames.length; i++) {
				labelNames[i] = rsmd.getColumnLabel(i + 1);
			}
			return labelNames;
		} catch (final Exception e) {
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
	public static String[] getColumnNames(final DataSource ds, final String tableName) {
		final List<String> columnNames = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			final String catalog = getCatalog(conn);
			final String schema = getSchema(conn);

			final DatabaseMetaData metaData = conn.getMetaData();
			try (final ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
				if (null != rs) {
					while (rs.next()) {
						columnNames.add(rs.getString("COLUMN_NAME"));
					}
				}
			}
			return columnNames.toArray(new String[0]);
		} catch (final Exception e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			IoUtil.closeQuietly(conn);
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
	public static Entity createLimitedEntity(final DataSource ds, final String tableName) {
		final String[] columnNames = getColumnNames(ds, tableName);
		return Entity.of(tableName).setFieldNames(columnNames);
	}

	/**
	 * 获得表的元信息<br>
	 * 注意如果需要获取注释，某些数据库如MySQL，需要在配置中添加:
	 * <pre>
	 *     remarks = true
	 *     useInformationSchema = true
	 * </pre>
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return Table对象
	 */
	public static Table getTableMeta(final DataSource ds, final String tableName) {
		return getTableMeta(ds, null, null, tableName);
	}

	/**
	 * 获得表的元信息<br>
	 * 注意如果需要获取注释，某些数据库如MySQL，需要在配置中添加:
	 * <pre>
	 *     remarks = true
	 *     useInformationSchema = true
	 * </pre>
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @param catalog   catalog name，{@code null}表示自动获取，见：{@link #getCatalog(Connection)}
	 * @param schema    a schema name pattern，{@code null}表示自动获取，见：{@link #getSchema(Connection)}
	 * @return Table对象
	 * @since 5.7.22
	 */
	public static Table getTableMeta(final DataSource ds, String catalog, String schema, final String tableName) {
		final Table table = Table.of(tableName);
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			if (null == catalog) {
				catalog = getCatalog(conn);
			}
			table.setCatalog(catalog);
			if (null == schema) {
				schema = getSchema(conn);
			}
			table.setSchema(schema);

			final DatabaseMetaData metaData = conn.getMetaData();

			// 获得表元数据（表注释）
			try (final ResultSet rs = metaData.getTables(catalog, schema, tableName, new String[]{TableType.TABLE.value()})) {
				if (null != rs) {
					if (rs.next()) {
						table.setComment(rs.getString("REMARKS"));
					}
				}
			}

			// 获得主键
			try (final ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
				if (null != rs) {
					while (rs.next()) {
						table.addPk(rs.getString("COLUMN_NAME"));
					}
				}
			}

			// 获得列
			try (final ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
				if (null != rs) {
					while (rs.next()) {
						table.setColumn(Column.of(table, rs));
					}
				}
			}

			// 获得索引信息(since 5.7.23)
			try (final ResultSet rs = metaData.getIndexInfo(catalog, schema, tableName, false, false)) {
				final Map<String, IndexInfo> indexInfoMap = new LinkedHashMap<>();
				if (null != rs) {
					while (rs.next()) {
						//排除tableIndexStatistic类型索引
						if (0 == rs.getShort("TYPE")) {
							continue;
						}

						final String indexName = rs.getString("INDEX_NAME");
						final String key = StrUtil.join("&", tableName, indexName);
						// 联合索引情况下一个索引会有多个列，此处须组合索引列到一个索引信息对象下
						IndexInfo indexInfo = indexInfoMap.get(key);
						if (null == indexInfo) {
							indexInfo = new IndexInfo(rs.getBoolean("NON_UNIQUE"), indexName, tableName, schema, catalog);
							indexInfoMap.put(key, indexInfo);
						}
						indexInfo.getColumnIndexInfoList().add(ColumnIndexInfo.of(rs));
					}
				}
				table.setIndexInfoList(ListUtil.of(indexInfoMap.values()));
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			IoUtil.closeQuietly(conn);
		}

		return table;
	}

	/**
	 * 获取catalog，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return catalog，获取失败返回{@code null}
	 * @since 5.7.23
	 */
	public static String getCatalog(final Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getCatalog();
		} catch (final SQLException e) {
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
	public static String getSchema(final Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getSchema();
		} catch (final SQLException e) {
			// ignore
		}

		return null;
	}
}
