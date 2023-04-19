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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link PreparedStatement}构建器，构建结果为{@link StatementWrapper}
 *
 * @author looly
 * @since 6.0.0
 */
public class StatementBuilder implements Builder<StatementWrapper> {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建构建器
	 *
	 * @return StatementBuilder
	 */
	public static StatementBuilder of() {
		return new StatementBuilder();
	}

	private SqlLog sqlLog;
	private Connection connection;
	private String sql;
	private Object[] params;
	private boolean returnGeneratedKey = true;

	/**
	 * 设置SQL日志
	 * @param sqlLog {@link SqlLog}
	 * @return this
	 */
	public StatementBuilder setSqlLog(final SqlLog sqlLog) {
		this.sqlLog = sqlLog;
		return this;
	}

	/**
	 * 设置连接
	 *
	 * @param connection {@link Connection}
	 * @return this
	 */
	public StatementBuilder setConnection(final Connection connection) {
		this.connection = connection;
		return this;
	}

	/**
	 * 设置执行的SQL语句
	 *
	 * @param sql SQL语句
	 * @return this
	 */
	public StatementBuilder setSql(final String sql) {
		this.sql = StrUtil.trim(sql);
		return this;
	}

	/**
	 * 设置SQL的"?"对应的参数
	 *
	 * @param params 参数数组
	 * @return this
	 */
	public StatementBuilder setParams(final Object... params) {
		this.params = params;
		return this;
	}

	/**
	 * 设置是否返回主键
	 * @param returnGeneratedKey 是否返回主键
	 * @return this
	 */
	public StatementBuilder setReturnGeneratedKey(final boolean returnGeneratedKey) {
		this.returnGeneratedKey = returnGeneratedKey;
		return this;
	}

	@Override
	public StatementWrapper build() {
		try {
			return _build();
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 创建批量操作的{@link StatementWrapper}
	 *
	 * @param paramsBatch "?"对应参数批次列表
	 * @return {@link StatementWrapper}
	 * @throws DbRuntimeException SQL异常
	 */
	public StatementWrapper buildForBatch(final Iterable<Object[]> paramsBatch) throws DbRuntimeException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sqlLog.log(sql, paramsBatch);

		final StatementWrapper ps;
		try {
			ps = StatementWrapper.of(connection.prepareStatement(sql));
			final Map<Integer, Integer> nullTypeMap = new HashMap<>();
			for (final Object[] params : paramsBatch) {
				ps.fillParams(new ArrayIter<>(params), nullTypeMap);
				ps.addBatch();
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
		return ps;
	}

	/**
	 * 创建批量操作的{@link StatementWrapper}
	 *
	 * @param fields   字段列表，用于获取对应值
	 * @param entities "?"对应参数批次列表
	 * @return {@link StatementWrapper}
	 * @throws DbRuntimeException SQL异常
	 */
	public StatementWrapper buildForBatch(final Iterable<String> fields, final Entity... entities) throws DbRuntimeException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sqlLog.logForBatch(sql);

		final StatementWrapper ps;
		try {
			ps = StatementWrapper.of(connection.prepareStatement(sql));
			final Map<Integer, Integer> nullTypeMap = new HashMap<>();
			for (final Entity entity : entities) {
				ps.fillParams(MapUtil.valuesOfKeys(entity, fields), nullTypeMap);
				ps.addBatch();
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
		return ps;
	}

	/**
	 * 构建{@link StatementWrapper}
	 *
	 * @return {@link StatementWrapper}
	 * @throws SQLException SQL异常
	 */
	private StatementWrapper _build() throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		if (ArrayUtil.isNotEmpty(params) && 1 == params.length && params[0] instanceof Map) {
			// 检查参数是否为命名方式的参数
			final NamedSql namedSql = new NamedSql(sql, Convert.toMap(String.class, Object.class, params[0]));
			sql = namedSql.getSql();
			params = namedSql.getParams();
		}

		sqlLog.log(sql, ArrayUtil.isEmpty(params) ? null : params);
		final PreparedStatement ps;
		if (returnGeneratedKey && StrUtil.startWithIgnoreCase(sql, "insert")) {
			// 插入默认返回主键
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} else {
			ps = connection.prepareStatement(sql);
		}

		return StatementWrapper.of(ps).fillArrayParam(params);
	}
}
