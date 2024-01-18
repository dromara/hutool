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

import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.dialect.impl.OracleDialect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SQL构建器<br>
 * 首先拼接SQL语句，值使用 ? 占位<br>
 * 调用getParamValues()方法获得占位符对应的值
 *
 * @author Looly
 */
public class SqlBuilder implements Builder<String> {
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------------- Static methods start

	/**
	 * 创建SQL构建器
	 *
	 * @return SQL构建器
	 */
	public static SqlBuilder of() {
		return new SqlBuilder();
	}

	/**
	 * 创建SQL构建器
	 *
	 * @param quoteWrapper 包装器
	 * @return SQL构建器
	 */
	public static SqlBuilder of(final QuoteWrapper quoteWrapper) {
		return new SqlBuilder(quoteWrapper);
	}

	/**
	 * 从已有的SQL中构建一个SqlBuilder
	 *
	 * @param sql SQL语句
	 * @return SqlBuilder
	 * @since 5.5.3
	 */
	public static SqlBuilder of(final CharSequence sql) {
		return of().append(sql);
	}

	/**
	 * 验证实体类对象的有效性
	 *
	 * @param entity 实体类对象
	 * @throws DbException SQL异常包装，获取元数据信息失败
	 */
	public static void validateEntity(final Entity entity) throws DbException {
		if (null == entity) {
			throw new DbException("Entity is null !");
		}
		if (StrUtil.isBlank(entity.getTableName())) {
			throw new DbException("Entity`s table name is null !");
		}
		if (entity.isEmpty()) {
			throw new DbException("No filed and value in this entity !");
		}
	}

	// --------------------------------------------------------------- Static methods end

	// --------------------------------------------------------------- Enums start

	/**
	 * SQL中多表关联用的关键字
	 *
	 * @author Looly
	 */
	public enum Join {
		/**
		 * 如果表中有至少一个匹配，则返回行
		 */
		INNER,
		/**
		 * 即使右表中没有匹配，也从左表返回所有的行
		 */
		LEFT,
		/**
		 * 即使左表中没有匹配，也从右表返回所有的行
		 */
		RIGHT,
		/**
		 * 只要其中一个表中存在匹配，就返回行
		 */
		FULL
	}
	// --------------------------------------------------------------- Enums end

	private final StringBuilder sql = new StringBuilder();
	/**
	 * 占位符对应的值列表
	 */
	private final List<Object> paramValues = new ArrayList<>();
	/**
	 * 包装器
	 */
	private QuoteWrapper quoteWrapper;

	// --------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public SqlBuilder() {
	}

	/**
	 * 构造
	 *
	 * @param quoteWrapper 包装器
	 */
	public SqlBuilder(final QuoteWrapper quoteWrapper) {
		this.quoteWrapper = quoteWrapper;
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Builder start

	/**
	 * 插入，使用默认的ANSI方言
	 *
	 * @param entity 实体
	 * @return 自己
	 */
	public SqlBuilder insert(final Entity entity) {
		return this.insert(entity, DialectName.ANSI);
	}

	/**
	 * 插入<br>
	 * 插入会忽略空的字段名及其对应值，但是对于有字段名对应值为{@code null}的情况不忽略
	 *
	 * @param entity      实体
	 * @param dialectName 方言名，用于对特殊数据库特殊处理
	 * @return 自己
	 */
	public SqlBuilder insert(final Entity entity, final DialectName dialectName) {
		return insert(entity, dialectName.name());
	}

	/**
	 * 插入<br>
	 * 插入会忽略空的字段名及其对应值，但是对于有字段名对应值为{@code null}的情况不忽略
	 *
	 * @param entity      实体
	 * @param dialectName 方言名，用于对特殊数据库特殊处理
	 * @return 自己
	 * @since 5.5.3
	 */
	public SqlBuilder insert(final Entity entity, final String dialectName) {
		// 验证
		validateEntity(entity);

		final boolean isOracle = DialectName.ORACLE.match(dialectName);// 对Oracle的特殊处理
		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();

		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				if (fieldsPart.length() > 0) {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
				}

				fieldsPart.append((null != quoteWrapper) ? quoteWrapper.wrap(field) : field);
				if (isOracle && OracleDialect.isNextVal(value)) {
					// Oracle的特殊自增键，通过字段名.nextval获得下一个值
					placeHolder.append(value);
				} else {
					// 普通字段使用占位符
					placeHolder.append("?");
					this.paramValues.add(value);
				}
			}
		});

		// issue#1656@Github Phoenix兼容
		if (DialectName.PHOENIX.match(dialectName)) {
			sql.append("UPSERT INTO ");
		} else {
			sql.append("INSERT INTO ");
		}

		String tableName = entity.getTableName();
		if (null != this.quoteWrapper) {
			// 包装表名 entity = wrapper.wrap(entity);
			tableName = this.quoteWrapper.wrap(tableName);
		}
		sql.append(tableName)
				.append(" (").append(fieldsPart).append(") VALUES (")//
				.append(placeHolder).append(")");

		return this;
	}

	/**
	 * 删除
	 *
	 * @param tableName 表名
	 * @return 自己
	 */
	public SqlBuilder delete(String tableName) {
		if (StrUtil.isBlank(tableName)) {
			throw new DbException("Table name is blank !");
		}

		if (null != quoteWrapper) {
			// 包装表名
			tableName = quoteWrapper.wrap(tableName);
		}

		sql.append("DELETE FROM ").append(tableName);

		return this;
	}

	/**
	 * 更新
	 *
	 * @param entity 要更新的实体
	 * @return 自己
	 */
	public SqlBuilder update(final Entity entity) {
		// 验证
		validateEntity(entity);

		String tableName = entity.getTableName();
		if (null != quoteWrapper) {
			// 包装表名
			tableName = quoteWrapper.wrap(tableName);
		}

		sql.append("UPDATE ").append(tableName).append(" SET ");
		entity.forEach((field, value) -> {
			if (StrUtil.isNotBlank(field)) {
				if (!paramValues.isEmpty()) {
					sql.append(", ");
				}
				sql.append((null != quoteWrapper) ? quoteWrapper.wrap(field) : field).append(" = ? ");
				this.paramValues.add(value);// 更新不对空做处理，因为存在清空字段的情况
			}
		});
		return this;
	}

	/**
	 * 查询
	 *
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields     查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(final boolean isDistinct, final String... fields) {
		return select(isDistinct, Arrays.asList(fields));
	}

	/**
	 * 查询
	 *
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields     查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(final boolean isDistinct, Collection<String> fields) {
		sql.append("SELECT ");
		if (isDistinct) {
			sql.append("DISTINCT ");
		}

		if (CollUtil.isEmpty(fields)) {
			sql.append("*");
		} else {
			if (null != quoteWrapper) {
				// 包装字段名
				fields = quoteWrapper.wrap(fields);
			}
			sql.append(CollUtil.join(fields, StrUtil.COMMA));
		}

		return this;
	}

	/**
	 * 查询（非Distinct）
	 *
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(final String... fields) {
		return select(false, fields);
	}

	/**
	 * 查询（非Distinct）
	 *
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(final Collection<String> fields) {
		return select(false, fields);
	}

	/**
	 * 添加 from语句
	 *
	 * @param tableNames 表名列表（多个表名用于多表查询）
	 * @return 自己
	 */
	public SqlBuilder from(String... tableNames) {
		if (ArrayUtil.isEmpty(tableNames) || ArrayUtil.hasBlank(tableNames)) {
			throw new DbException("Table name is blank in table names !");
		}

		if (null != quoteWrapper) {
			// 包装表名
			tableNames = quoteWrapper.wrap(tableNames);
		}

		sql.append(" FROM ").append(ArrayUtil.join(tableNames, StrUtil.COMMA));

		return this;
	}

	/**
	 * 添加Where语句，所有逻辑之间关系使用{@link Condition#setLinkOperator(LogicalOperator)} 定义
	 *
	 * @param conditions 条件，当条件为空时，只添加WHERE关键字
	 * @return 自己
	 * @since 4.4.4
	 */
	public SqlBuilder where(final Condition... conditions) {
		if (ArrayUtil.isNotEmpty(conditions)) {
			where(buildCondition(conditions));
		}

		return this;
	}

	/**
	 * 添加Where语句<br>
	 *
	 * @param where WHERE语句之后跟的条件语句字符串
	 * @return 自己
	 */
	public SqlBuilder where(final String where) {
		if (StrUtil.isNotBlank(where)) {
			sql.append(" WHERE ").append(where);
		}
		return this;
	}

	/**
	 * 多值选择
	 *
	 * @param <T>    值类型
	 * @param field  字段名
	 * @param values 值列表
	 * @return 自身
	 */
	@SuppressWarnings("unchecked")
	public <T> SqlBuilder in(final String field, final T... values) {
		sql.append(quoteWrapper.wrap(field)).append(" IN ").append("(").append(ArrayUtil.join(values, StrUtil.COMMA)).append(")");
		return this;
	}

	/**
	 * 分组
	 *
	 * @param fields 字段
	 * @return 自己
	 */
	public SqlBuilder groupBy(String... fields) {
		if (ArrayUtil.isNotEmpty(fields)) {
			if (null != quoteWrapper) {
				// 包装字段名
				fields = quoteWrapper.wrap(fields);
			}

			sql.append(" GROUP BY ").append(ArrayUtil.join(fields, StrUtil.COMMA));
		}

		return this;
	}

	/**
	 * 添加Having语句，所有逻辑之间关系使用{@link Condition#setLinkOperator(LogicalOperator)} 定义
	 *
	 * @param conditions 条件
	 * @return this
	 * @since 5.4.3
	 */
	public SqlBuilder having(final Condition... conditions) {
		if (ArrayUtil.isNotEmpty(conditions)) {
			having(buildCondition(conditions));
		}

		return this;
	}

	/**
	 * 添加Having语句
	 *
	 * @param having 条件语句
	 * @return 自己
	 */
	public SqlBuilder having(final String having) {
		if (StrUtil.isNotBlank(having)) {
			sql.append(" HAVING ").append(having);
		}
		return this;
	}

	/**
	 * 排序
	 *
	 * @param orders 排序对象
	 * @return 自己
	 */
	public SqlBuilder orderBy(final Order... orders) {
		if (ArrayUtil.isEmpty(orders)) {
			return this;
		}

		sql.append(" ORDER BY ");
		String field;
		boolean isFirst = true;
		for (final Order order : orders) {
			field = order.getField();
			if (null != quoteWrapper) {
				// 包装字段名
				field = quoteWrapper.wrap(field);
			}
			if (StrUtil.isBlank(field)) {
				continue;
			}

			// 只有在非第一项前添加逗号
			if (isFirst) {
				isFirst = false;
			} else {
				sql.append(StrUtil.COMMA);
			}
			sql.append(field);
			final Direction direction = order.getDirection();
			if (null != direction) {
				sql.append(StrUtil.SPACE).append(direction);
			}
		}
		return this;
	}

	/**
	 * 多表关联
	 *
	 * @param tableName 被关联的表名
	 * @param join      内联方式
	 * @return 自己
	 */
	public SqlBuilder join(String tableName, final Join join) {
		if (StrUtil.isBlank(tableName)) {
			throw new DbException("Table name is blank !");
		}

		if (null != join) {
			sql.append(StrUtil.SPACE).append(join).append(" JOIN ");
			if (null != quoteWrapper) {
				// 包装表名
				tableName = quoteWrapper.wrap(tableName);
			}
			sql.append(tableName);
		}
		return this;
	}

	/**
	 * 配合JOIN的 ON语句，多表关联的条件语句，所有逻辑之间关系使用{@link Condition#setLinkOperator(LogicalOperator)} 定义
	 *
	 * @param conditions 条件
	 * @return this
	 * @since 5.4.3
	 */
	public SqlBuilder on(final Condition... conditions) {
		if (ArrayUtil.isNotEmpty(conditions)) {
			on(buildCondition(conditions));
		}

		return this;
	}

	/**
	 * 配合JOIN的 ON语句，多表关联的条件语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 *
	 * @param on 条件
	 * @return 自己
	 */
	public SqlBuilder on(final String on) {
		if (StrUtil.isNotBlank(on)) {
			this.sql.append(" ON ").append(on);
		}
		return this;
	}

	/**
	 * 在SQL的开头补充SQL片段
	 *
	 * @param sqlFragment SQL片段
	 * @return this
	 * @since 4.1.3
	 */
	public SqlBuilder insertPreFragment(final Object sqlFragment) {
		if (null != sqlFragment) {
			this.sql.insert(0, sqlFragment);
		}
		return this;
	}

	/**
	 * 追加SQL其它部分片段，此方法只是简单的追加SQL字符串，空格需手动加入，例如：
	 *
	 * <pre>
	 *     SqlBuilder builder = SqlBuilder.of("select *");
	 *     builder.append(" from ").append("user");
	 * </pre>
	 * <p>
	 * 如果需要追加带占位符的片段，需调用{@link #addParams(Object...)} 方法加入对应参数值。
	 *
	 * @param sqlFragment SQL其它部分片段
	 * @return this
	 */
	public SqlBuilder append(final Object sqlFragment) {
		if (null != sqlFragment) {
			this.sql.append(sqlFragment);
		}
		return this;
	}

	/**
	 * 手动增加参数，调用此方法前需确认SQL中有对应占位符，主要用于自定义SQL片段中有占位符的情况，例如：
	 *
	 * <pre>
	 *     SqlBuilder builder = SqlBuilder.of("select * from user where id=?");
	 *     builder.append(" and name=?")
	 *     builder.addParams(1, "looly");
	 * </pre>
	 *
	 * @param params 参数列表
	 * @return this
	 * @since 5.5.3
	 */
	public SqlBuilder addParams(final Object... params) {
		if (ArrayUtil.isNotEmpty(params)) {
			Collections.addAll(this.paramValues, params);
		}
		return this;
	}

	/**
	 * 构建查询SQL
	 *
	 * @param query {@link Query}
	 * @return this
	 */
	public SqlBuilder query(final Query query) {
		return this.select(query.getFields()).from(query.getTableNames()).where(query.getWhere());
	}
	// --------------------------------------------------------------- Builder end

	/**
	 * 获得占位符对应的值列表<br>
	 *
	 * @return 占位符对应的值列表
	 */
	public List<Object> getParamValues() {
		return this.paramValues;
	}

	/**
	 * 获得占位符对应的值列表<br>
	 *
	 * @return 占位符对应的值列表
	 */
	public Object[] getParamValueArray() {
		return this.paramValues.toArray(new Object[0]);
	}

	/**
	 * 格式化SQL语句
	 * @return SqlBuilder
	 */
	public SqlBuilder format() {
		this.sql.replace(0, this.sql.length(), SqlFormatter.format(this.sql.toString()));
		return this;
	}

	/**
	 * 构建，默认打印SQL日志
	 *
	 * @return 构建好的SQL语句
	 */
	@Override
	public String build() {
		return this.sql.toString();
	}

	@Override
	public String toString() {
		return this.build();
	}

	// --------------------------------------------------------------- private method start

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @param conditions 条件对象
	 * @return 构建后的SQL语句条件部分
	 */
	private String buildCondition(Condition... conditions) {
		if (ArrayUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}

		if (null != quoteWrapper) {
			// 包装字段名
			conditions = quoteWrapper.wrap(conditions);
		}

		return ConditionBuilder.of(conditions).build(this.paramValues);
	}
	// --------------------------------------------------------------- private method end
}
