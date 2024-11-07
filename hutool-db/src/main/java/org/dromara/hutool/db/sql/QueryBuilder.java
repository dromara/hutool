/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.lang.builder.Builder;
import org.dromara.hutool.core.text.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查询构建器，用于构建查询条件，例如：
 * <pre>{@code
 *     Query query = QueryBuilder.of()
 *         .select("id", "name")
 *         .from("user")
 *         .where("id", 1)
 *         .and("name", "hutool")
 *         .or("age", 18)
 *         .build();
 * }</pre>
 *
 * @author Looly
 * @since 6.0.0
 */
public class QueryBuilder implements Builder<Query> {
	private static final long serialVersionUID = 1L;

	private final List<String> fields;
	private final List<String> tableNames;
	private final List<Condition> wheres;

	/**
	 * 构造
	 */
	public QueryBuilder() {
		fields = new ArrayList<>();
		tableNames = new ArrayList<>();
		wheres = new ArrayList<>();
	}

	/**
	 * 批量添加字段，即{@code select field1,field2,field3 }
	 *
	 * @param fields 字段名
	 * @return this
	 */
	public QueryBuilder addFields(final String... fields) {
		this.fields.addAll(Arrays.asList(fields));
		return this;
	}

	/**
	 * 添加多个表名，即{@code from table1,table2 }
	 *
	 * @param tableNames 表名
	 * @return this
	 */
	public QueryBuilder addTableNames(final String... tableNames) {
		this.tableNames.addAll(Arrays.asList(tableNames));
		return this;
	}

	/**
	 * 添加相等条件，即{@code field = value }
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder eq(final String field, final Object value) {
		return addCondition(new Condition(field, value));
	}

	/**
	 * 添加不等条件，即{@code  field != value }
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder ne(final String field, final Object value) {
		return addCondition(new Condition(field, "!=", value));
	}

	/**
	 * 添加大于条件，即{@code field > value}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder gt(final String field, final Object value) {
		return addCondition(new Condition(field, ">", value));
	}

	/**
	 * 添加小于条件，即{@code field < value}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder lt(final String field, final Object value) {
		return addCondition(new Condition(field, "<", value));
	}

	/**
	 * 添加大于等于条件，即{@code field >= value}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder ge(final String field, final Object value) {
		return addCondition(new Condition(field, ">=", value));
	}

	/**
	 * 添加小于等于条件，即{@code field <= value}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder le(final String field, final Object value) {
		return addCondition(new Condition(field, "<=", value));
	}

	/**
	 * 添加模糊查询条件，即{@code field like '%value%'}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder like(final String field, final String value) {
		return addCondition(new Condition(field, value, Condition.LikeType.Contains));
	}

	/**
	 * 添加以固定值开头模糊查询条件，即{@code field like 'value%'}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder likeStartWith(final String field, final String value) {
		return addCondition(new Condition(field, value, Condition.LikeType.StartWith));
	}

	/**
	 * 添加以固定值结尾模糊查询条件，即{@code field like '%value'}
	 *
	 * @param field 字段名
	 * @param value 值
	 * @return this
	 */
	public QueryBuilder likeEndWith(final String field, final String value) {
		return addCondition(new Condition(field, value, Condition.LikeType.EndWith));
	}

	/**
	 * 添加IN查询条件，即{@code field IN (value1,value2)}
	 *
	 * @param field  字段名
	 * @param values 值
	 * @return this
	 */
	public QueryBuilder in(final String field, final Object... values) {
		return addCondition(new Condition(field, "IN", values));
	}

	/**
	 * 添加IN查询条件，即{@code field IN (value1,value2)}
	 *
	 * @param field  字段名
	 * @param values 值
	 * @return this
	 */
	public QueryBuilder in(final String field, final Iterable<?> values) {
		return addCondition(new Condition(field, "IN", values));
	}

	/**
	 * 添加非IN查询条件，即{@code field NOT IN (value1,value2)}
	 *
	 * @param field  字段名
	 * @param values 值
	 * @return this
	 */
	public QueryBuilder notIn(final String field, final Object... values) {
		return addCondition(new Condition(field, "NOT IN", values));
	}

	/**
	 * 添加非IN查询条件，即{@code field NOT IN (value1,value2)}
	 *
	 * @param field  字段名
	 * @param values 值
	 * @return this
	 */
	public QueryBuilder notIn(final String field, final Iterable<?> values) {
		return addCondition(new Condition(field, "NOT IN", values));
	}

	/**
	 * 添加BETWEEN查询条件，即{@code field BETWEEN start AND end}
	 *
	 * @param field 字段名
	 * @param start 开始值
	 * @param end   结束值
	 * @return this
	 */
	public QueryBuilder between(final String field, final Object start, final Object end) {
		return addCondition(new Condition(field, StrUtil.format("BETWEEN {} AND {}", start, end)));
	}

	/**
	 * 添加非BETWEEN查询条件，即{@code field NOT BETWEEN start AND end}
	 *
	 * @param field 字段名
	 * @param start 开始值
	 * @param end   结束值
	 * @return this
	 */
	public QueryBuilder notBetween(final String field, final Object start, final Object end) {
		return addCondition(new Condition(field, StrUtil.format("NOT BETWEEN {} AND {}", start, end)));
	}

	/**
	 * 添加自定义条件
	 *
	 * @param condition 条件
	 * @return this
	 */
	public QueryBuilder addCondition(final Condition condition) {
		this.wheres.add(condition);
		return this;
	}


	@Override
	public Query build() {
		return new Query(fields, tableNames.toArray(new String[0]), wheres.toArray(new Condition[0]), null);
	}
}
