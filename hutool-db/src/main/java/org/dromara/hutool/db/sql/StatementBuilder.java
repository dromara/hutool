/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.sql.filter.SqlFilter;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private final BoundSql boundSql = new BoundSql();
	private Connection connection;
	private boolean returnGeneratedKey = true;
	private SqlFilter sqlFilter;

	/**
	 * 设置SQL日志
	 *
	 * @param sqlFilter {@link SqlFilter}
	 * @return this
	 */
	public StatementBuilder setSqlFilter(final SqlFilter sqlFilter) {
		this.sqlFilter = sqlFilter;
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
		this.boundSql.setSql(sql);
		return this;
	}

	/**
	 * 设置SQL的"?"对应的参数
	 *
	 * @param params 参数数组
	 * @return this
	 */
	public StatementBuilder setParams(final Object... params) {
		this.boundSql.setParams(ListUtil.of(params));
		return this;
	}

	/**
	 * 设置是否返回主键
	 *
	 * @param returnGeneratedKey 是否返回主键
	 * @return this
	 */
	public StatementBuilder setReturnGeneratedKey(final boolean returnGeneratedKey) {
		this.returnGeneratedKey = returnGeneratedKey;
		return this;
	}

	/**
	 * 构建{@link StatementWrapper}
	 *
	 * @return {@link StatementWrapper}，{@code null}表示不执行
	 */
	@Override
	public StatementWrapper build() {
		try {
			return _build();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 创建批量操作的{@link StatementWrapper}
	 *
	 * @return {@link StatementWrapper}，{@code null}表示不执行
	 * @throws DbException SQL异常
	 */
	public StatementWrapper buildForBatch() throws DbException {
		final String sql = this.boundSql.getSql();
		Assert.notBlank(sql, "Sql String must be not blank!");
		final List<Object> paramsBatch = this.boundSql.getParams();

		sqlFilter.filter(this.connection, this.boundSql, this.returnGeneratedKey);

		final StatementWrapper ps;
		try {
			ps = StatementWrapper.of(connection.prepareStatement(sql));
			final Map<Integer, Integer> nullTypeMap = new HashMap<>();
			Set<String> keys = null;
			for (final Object params : paramsBatch) {
				if (null == params) {
					continue;
				}
				if (ArrayUtil.isArray(params)) {
					ps.fillParams(new ArrayIter<>(params), nullTypeMap);
				} else if (params instanceof Entity) {
					final Entity entity = (Entity) params;
					// 对于多Entity批量插入的情况，为防止数据不对齐，故按照首行提供键值对筛选。
					if(null == keys){
						keys = entity.keySet();
						ps.fillParams(entity.values(), nullTypeMap);
					} else{
						ps.fillParams(MapUtil.valuesOfKeys(entity, keys), nullTypeMap);
					}
				}
				ps.addBatch();
			}
		} catch (final SQLException e) {
			throw new DbException(e);
		}
		return ps;
	}

	/**
	 * 创建存储过程或函数调用的{@link StatementWrapper}
	 *
	 * @return StatementWrapper，{@code null}表示不执行
	 * @since 6.0.0
	 */
	public CallableStatement buildForCall() {
		final String sql = this.boundSql.getSql();
		final Object[] params = this.boundSql.getParamArray();
		Assert.notBlank(sql, "Sql String must be not blank!");

		sqlFilter.filter(this.connection, this.boundSql, this.returnGeneratedKey);

		try {
			return (CallableStatement) StatementWrapper
				.of(connection.prepareCall(sql))
				.fillArrayParam(params)
				.getRaw();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 构建{@link StatementWrapper}
	 *
	 * @return {@link StatementWrapper}，{@code null}表示不执行
	 * @throws SQLException SQL异常
	 */
	private StatementWrapper _build() throws SQLException {
		String sql = this.boundSql.getSql();
		Object[] params = this.boundSql.getParamArray();
		Assert.notBlank(sql, "Sql String must be not blank!");

		if (ArrayUtil.isNotEmpty(params) && 1 == params.length && params[0] instanceof Map) {
			// 检查参数是否为命名方式的参数
			final NamedSql namedSql =  new NamedSql(sql, Convert.toMap(String.class, Object.class, params[0]));
			sql = namedSql.getSql();
			params = namedSql.getParamArray();
		}

		if(null != this.sqlFilter){
			this.sqlFilter.filter(this.connection, this.boundSql, this.returnGeneratedKey);
		}

		final PreparedStatement ps;
		if (this.returnGeneratedKey && StrUtil.startWithIgnoreCase(sql, "insert")) {
			// 插入默认返回主键
			ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} else {
			ps = this.connection.prepareStatement(sql);
		}

		return StatementWrapper.of(ps).fillArrayParam(params);
	}
}
