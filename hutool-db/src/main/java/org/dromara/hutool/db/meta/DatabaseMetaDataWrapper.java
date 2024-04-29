/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.meta;

import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.db.DbException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用于封装DatabaseMetaData对象，并提供特定数据库的元数据访问。
 * 这个类提供了一种方便的方式来访问和操作数据库元数据，是对DatabaseMetaData的简单封装。
 *
 * @author Looly
 * @since 6.0.0
 */
public class DatabaseMetaDataWrapper extends SimpleWrapper<DatabaseMetaData> {

	/**
	 * 创建一个 DatabaseMetaDataWrapper 实例。
	 *
	 * @param conn 数据库连接
	 * @return 返回一个新的 DatabaseMetaDataWrapper 实例。
	 */
	public static DatabaseMetaDataWrapper of(final Connection conn) {
		return of(MetaUtil.getMetaData(conn), MetaUtil.getCatalog(conn), MetaUtil.getSchema(conn));
	}

	/**
	 * 创建一个 DatabaseMetaDataWrapper 实例。
	 *
	 * @param raw     原始的 DatabaseMetaData 对象，这是 Java SQL API 的一部分，用于获取数据库元数据信息。
	 * @param catalog 要使用的数据库目录（schema）的名称。可以为{@code null}，具体行为取决于数据库和实现。
	 * @param schema  要使用的数据库模式（schema）的名称。可以为{@code null}，具体行为取决于数据库和实现。
	 * @return 返回一个新的 DatabaseMetaDataWrapper 实例。
	 */
	public static DatabaseMetaDataWrapper of(final DatabaseMetaData raw, final String catalog, final String schema) {
		return new DatabaseMetaDataWrapper(raw, catalog, schema);
	}

	private final String catalog;
	private final String schema;
	private final boolean isOracle;

	/**
	 * 构造。这个包装类用于封装DatabaseMetaData对象，并提供特定数据库的元数据访问。
	 *
	 * @param raw     原始的DatabaseMetaData对象，这是Java SQL API的一部分，用于获取数据库元数据。
	 * @param catalog 要使用的数据库目录（在某些数据库系统中相当于数据库名称）。
	 * @param schema  要使用的数据库模式（在某些数据库系统中相当于命名空间）。
	 */
	public DatabaseMetaDataWrapper(final DatabaseMetaData raw, final String catalog, final String schema) {
		super(raw); // 调用父类构造函数，将原始DatabaseMetaData对象传递给父类。
		this.catalog = catalog;
		this.schema = schema;
		// 检查是否为Oracle数据库，用于后续提供特定于Oracle的元数据支持
		this.isOracle = MetaUtil.isOracle(raw);
	}

	/**
	 * 是否为Oracle数据库
	 *
	 * @return 是否为Oracle数据库
	 */
	public boolean isOracle() {
		return isOracle;
	}

	/**
	 * 获取数据库类型名称
	 *
	 * @return 数据库类型名称
	 */
	public String getProductName() {
		try {
			return raw.getDatabaseProductName();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 获取数据库驱动名称
	 *
	 * @return 数据库驱动名称
	 */
	public String getDriverName() {
		try {
			return raw.getDriverName();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 获取指定表的备注信息。
	 *
	 * @param tableName 表名称，指定要查询备注信息的表。
	 * @return 表的备注信息。未找到指定的表或查询成功但无结果，则返回null。
	 */
	public String getRemarks(String tableName) {
		final String catalog = this.catalog;
		final String schema = this.schema;

		// issue#I9BANE Oracle中特殊表名需要解包
		tableName = getPureTableName(tableName);

		try (final ResultSet rs = this.raw.getTables(catalog, schema, tableName, new String[]{TableType.TABLE.value()})) {
			if (null != rs) {
				if (rs.next()) {
					return rs.getString("REMARKS");
				}
			}
		} catch (final SQLException e) {
			throw new DbException(e);
		}
		// 未找到指定的表或查询成功但无结果
		return null;
	}

	/**
	 * 获取指定表的主键列名列表。
	 *
	 * @param tableName 表名，指定要查询主键的表。
	 * @return 主键列名的列表。如果表没有主键，则返回空列表。
	 * @throws DbException 如果查询过程中发生SQLException，将抛出DbException。
	 * @since 5.8.28
	 */
	public Set<String> getPrimaryKeys(String tableName) {
		final String catalog = this.catalog;
		final String schema = this.schema;
		// issue#I9BANE Oracle中特殊表名需要解包
		tableName = getPureTableName(tableName);

		// 初始化主键列表
		Set<String> primaryKeys = null;
		try (final ResultSet rs = this.raw.getPrimaryKeys(catalog, schema, tableName)) {
			// 如果结果集不为空，遍历结果集获取主键列名
			if (null != rs) {
				primaryKeys = new LinkedHashSet<>(rs.getFetchSize(), 1);
				while (rs.next()) {
					primaryKeys.add(rs.getString("COLUMN_NAME"));
				}
			}
		} catch (final SQLException e) {
			// 将SQLException转换为自定义的DbException抛出
			throw new DbException(e);
		}
		return primaryKeys;
	}

	/**
	 * 获取指定表的索引信息。
	 *
	 * @param tableName 需要查询索引信息的表名。
	 * @return 返回一个映射，其中包含表的索引信息。键是表名和索引名的组合，值是索引信息对象。
	 * @since 5.8.28
	 */
	public Map<String, IndexInfo> getIndexInfo(final String tableName) {
		final String catalog = this.catalog;
		final String schema = this.schema;
		final Map<String, IndexInfo> indexInfoMap = new LinkedHashMap<>();

		try (final ResultSet rs = this.raw.getIndexInfo(catalog, schema, tableName, false, false)) {
			if (null != rs) {
				while (rs.next()) {
					//排除统计（tableIndexStatistic）类型索引
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
		} catch (final SQLException e) {
			throw new DbException(e);
		}
		return indexInfoMap;
	}

	/**
	 * 从数据库元数据中获取指定表的列信息。
	 *
	 * @param table 表对象，用于存储获取到的列信息。
	 */
	public void fetchColumns(final Table table) {
		final String catalog = this.catalog;
		final String schema = this.schema;

		// issue#I9BANE Oracle中特殊表名需要解包
		final String tableName = getPureTableName(ObjUtil.defaultIfNull(table.getPureTableName(), table::getTableName));

		// 获得列
		try (final ResultSet rs = this.raw.getColumns(catalog, schema, tableName, null)) {
			if (null != rs) {
				while (rs.next()) {
					table.addColumn(Column.of(table, rs));
				}
			}
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 如果是在Oracle数据库中并且表名被双引号包裹，则移除这些引号。
	 *
	 * @param tableName 待处理的表名，可能被双引号包裹。
	 * @return 处理后的表名，如果原表名被双引号包裹且是Oracle数据库，则返回去除了双引号的表名；否则返回原表名。
	 */
	public String getPureTableName(String tableName) {
		final char wrapChar = '"';
		// 判断表名是否被双引号包裹且当前数据库为Oracle，如果是，则移除双引号
		if (StrUtil.isWrap(tableName, wrapChar) && isOracle) {
			tableName = StrUtil.unWrap(tableName, wrapChar);
		}
		return tableName;
	}
}
