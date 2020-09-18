package cn.hutool.db.sql;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.Entity;
import cn.hutool.db.dialect.DialectName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

/**
 * SQL构建器<br>
 * 首先拼接SQL语句，值使用 ? 占位<br>
 * 调用getParamValues()方法获得占位符对应的值
 * 
 * @author Looly
 *
 */
public class SqlBuilder implements Builder<String>{
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------------- Static methods start
	/**
	 * 创建SQL构建器
	 * 
	 * @return SQL构建器
	 */
	public static SqlBuilder create() {
		return new SqlBuilder();
	}

	/**
	 * 创建SQL构建器
	 * 
	 * @param wrapper 包装器
	 * @return SQL构建器
	 */
	public static SqlBuilder create(Wrapper wrapper) {
		return new SqlBuilder(wrapper);
	}

	// --------------------------------------------------------------- Static methods end

	// --------------------------------------------------------------- Enums start
	/**
	 * SQL中多表关联用的关键字
	 * 
	 * @author Looly
	 *
	 */
	public enum Join {
		/** 如果表中有至少一个匹配，则返回行 */
		INNER,
		/** 即使右表中没有匹配，也从左表返回所有的行 */
		LEFT,
		/** 即使左表中没有匹配，也从右表返回所有的行 */
		RIGHT,
		/** 只要其中一个表中存在匹配，就返回行 */
		FULL
	}
	// --------------------------------------------------------------- Enums end

	private final StringBuilder sql = new StringBuilder();
	/** 字段列表（仅用于插入和更新） */
	private final List<String> fields = new ArrayList<>();
	/** 占位符对应的值列表 */
	private final List<Object> paramValues = new ArrayList<>();
	/** 包装器 */
	private Wrapper wrapper;

	// --------------------------------------------------------------- Constructor start
	public SqlBuilder() {
	}

	public SqlBuilder(Wrapper wrapper) {
		this.wrapper = wrapper;
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Builder start

	/**
	 * 插入，使用默认的ANSI方言
	 * 
	 * @param entity 实体
	 * @return 自己
	 */
	public SqlBuilder insert(Entity entity) {
		return this.insert(entity, DialectName.ANSI);
	}

	/**
	 * 插入<br>
	 * 插入会忽略空的字段名及其对应值，但是对于有字段名对应值为{@code null}的情况不忽略
	 * 
	 * @param entity 实体
	 * @param dialectName 方言名，用于对特殊数据库特殊处理
	 * @return 自己
	 */
	public SqlBuilder insert(Entity entity, DialectName dialectName) {
		// 验证
		validateEntity(entity);

		if (null != wrapper) {
			// 包装表名
			// entity = wrapper.wrap(entity);
			entity.setTableName(wrapper.wrap(entity.getTableName()));
		}

		final boolean isOracle = ObjectUtil.equal(dialectName, DialectName.ORACLE);// 对Oracle的特殊处理
		final StringBuilder fieldsPart = new StringBuilder();
		final StringBuilder placeHolder = new StringBuilder();

		boolean isFirst = true;
		String field;
		Object value;
		for (Entry<String, Object> entry : entity.entrySet()) {
			field = entry.getKey();
			value = entry.getValue();
			if (StrUtil.isNotBlank(field) /* && null != value */) {
				if (isFirst) {
					isFirst = false;
				} else {
					// 非第一个参数，追加逗号
					fieldsPart.append(", ");
					placeHolder.append(", ");
				}

				this.fields.add(field);
				fieldsPart.append((null != wrapper) ? wrapper.wrap(field) : field);
				if (isOracle && value instanceof String && StrUtil.endWithIgnoreCase((String) value, ".nextval")) {
					// Oracle的特殊自增键，通过字段名.nextval获得下一个值
					placeHolder.append(value);
				} else {
					placeHolder.append("?");
					this.paramValues.add(value);
				}
			}
		}
		sql.append("INSERT INTO ")//
				.append(entity.getTableName()).append(" (").append(fieldsPart).append(") VALUES (")//
				.append(placeHolder.toString()).append(")");

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
			throw new DbRuntimeException("Table name is blank !");
		}

		if (null != wrapper) {
			// 包装表名
			tableName = wrapper.wrap(tableName);
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
	public SqlBuilder update(Entity entity) {
		// 验证
		validateEntity(entity);

		if (null != wrapper) {
			// 包装表名
			// entity = wrapper.wrap(entity);
			entity.setTableName(wrapper.wrap(entity.getTableName()));
		}

		sql.append("UPDATE ").append(entity.getTableName()).append(" SET ");
		String field;
		for (Entry<String, Object> entry : entity.entrySet()) {
			field = entry.getKey();
			if (StrUtil.isNotBlank(field)) {
				if (paramValues.size() > 0) {
					sql.append(", ");
				}
				this.fields.add(field);
				sql.append((null != wrapper) ? wrapper.wrap(field) : field).append(" = ? ");
				this.paramValues.add(entry.getValue());// 更新不对空做处理，因为存在清空字段的情况
			}
		}

		return this;
	}

	/**
	 * 查询
	 * 
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(boolean isDistinct, String... fields) {
		return select(isDistinct, Arrays.asList(fields));
	}

	/**
	 * 查询
	 * 
	 * @param isDistinct 是否添加DISTINCT关键字（查询唯一结果）
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(boolean isDistinct, Collection<String> fields) {
		sql.append("SELECT ");
		if (isDistinct) {
			sql.append("DISTINCT ");
		}

		if (CollectionUtil.isEmpty(fields)) {
			sql.append("*");
		} else {
			if (null != wrapper) {
				// 包装字段名
				fields = wrapper.wrap(fields);
			}
			sql.append(CollectionUtil.join(fields, StrUtil.COMMA));
		}

		return this;
	}

	/**
	 * 查询（非Distinct）
	 * 
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(String... fields) {
		return select(false, fields);
	}

	/**
	 * 查询（非Distinct）
	 * 
	 * @param fields 查询的字段
	 * @return 自己
	 */
	public SqlBuilder select(Collection<String> fields) {
		return select(false, fields);
	}

	/**
	 * 添加 from语句
	 * 
	 * @param tableNames 表名列表（多个表名用于多表查询）
	 * @return 自己
	 */
	public SqlBuilder from(String... tableNames) {
		if (ArrayUtil.isEmpty(tableNames) || StrUtil.hasBlank(tableNames)) {
			throw new DbRuntimeException("Table name is blank in table names !");
		}

		if (null != wrapper) {
			// 包装表名
			tableNames = wrapper.wrap(tableNames);
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
	public SqlBuilder where(Condition... conditions) {
		if (ArrayUtil.isNotEmpty(conditions)) {
			where(buildCondition(conditions));
		}

		return this;
	}

	/**
	 * 添加Where语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * 
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件，当条件为空时，只添加WHERE关键字
	 * @return 自己
	 * @deprecated logicalOperator放在Condition中了，因此请使用 {@link #where(Condition...)}
	 */
	@Deprecated
	public SqlBuilder where(LogicalOperator logicalOperator, Condition... conditions) {
		return where(conditions);
	}

	/**
	 * 添加Where语句<br>
	 * 
	 * @param where WHERE语句之后跟的条件语句字符串
	 * @return 自己
	 */
	public SqlBuilder where(String where) {
		if (StrUtil.isNotBlank(where)) {
			sql.append(" WHERE ").append(where);
		}
		return this;
	}

	/**
	 * 多值选择
	 * 
	 * @param <T> 值类型
	 * @param field 字段名
	 * @param values 值列表
	 * @return 自身
	 */
	@SuppressWarnings("unchecked")
	public <T> SqlBuilder in(String field, T... values) {
		sql.append(wrapper.wrap(field)).append(" IN ").append("(").append(ArrayUtil.join(values, StrUtil.COMMA)).append(")");
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
			if (null != wrapper) {
				// 包装字段名
				fields = wrapper.wrap(fields);
			}

			sql.append(" GROUP BY ").append(ArrayUtil.join(fields, StrUtil.COMMA));
		}

		return this;
	}

	/**
	 * 添加Having语句
	 * 
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件
	 * @return 自己
	 * @deprecated logicalOperator放在Condition中了，因此请使用 {@link #having(Condition...)}
	 */
	@Deprecated
	public SqlBuilder having(LogicalOperator logicalOperator, Condition... conditions) {
		return having(conditions);
	}

	/**
	 * 添加Having语句，所有逻辑之间关系使用{@link Condition#setLinkOperator(LogicalOperator)} 定义
	 *
	 * @param conditions 条件
	 * @return this
	 * @since 5.4.3
	 */
	public SqlBuilder having(Condition... conditions) {
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
	public SqlBuilder having(String having) {
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
	public SqlBuilder orderBy(Order... orders) {
		if (ArrayUtil.isEmpty(orders)) {
			return this;
		}

		sql.append(" ORDER BY ");
		String field;
		boolean isFirst = true;
		for (Order order : orders) {
			field = order.getField();
			if (null != wrapper) {
				// 包装字段名
				field = wrapper.wrap(field);
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
	 * @param join 内联方式
	 * @return 自己
	 */
	public SqlBuilder join(String tableName, Join join) {
		if (StrUtil.isBlank(tableName)) {
			throw new DbRuntimeException("Table name is blank !");
		}

		if (null != join) {
			sql.append(StrUtil.SPACE).append(join).append(" JOIN ");
			if (null != wrapper) {
				// 包装表名
				tableName = wrapper.wrap(tableName);
			}
			sql.append(tableName);
		}
		return this;
	}

	/**
	 * 配合JOIN的 ON语句，多表关联的条件语句<br>
	 * 只支持单一的逻辑运算符（例如多个条件之间）
	 * 
	 * @param logicalOperator 逻辑运算符
	 * @param conditions 条件
	 * @return 自己
	 * @deprecated logicalOperator放在Condition中了，因此请使用 {@link #on(Condition...)}
	 */
	@Deprecated
	public SqlBuilder on(LogicalOperator logicalOperator, Condition... conditions) {
		return on(conditions);
	}

	/**
	 * 配合JOIN的 ON语句，多表关联的条件语句，所有逻辑之间关系使用{@link Condition#setLinkOperator(LogicalOperator)} 定义
	 *
	 * @param conditions 条件
	 * @return this
	 * @since 5.4.3
	 */
	public SqlBuilder on(Condition... conditions) {
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
	public SqlBuilder on(String on) {
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
	public SqlBuilder insertPreFragment(Object sqlFragment) {
		if (null != sqlFragment) {
			this.sql.insert(0, sqlFragment);
		}
		return this;
	}

	/**
	 * 追加SQL其它部分片段
	 * 
	 * @param sqlFragment SQL其它部分片段
	 * @return this
	 */
	public SqlBuilder append(Object sqlFragment) {
		if (null != sqlFragment) {
			this.sql.append(sqlFragment);
		}
		return this;
	}

	/**
	 * 构建查询SQL
	 * 
	 * @param query {@link Query}
	 * @return this
	 */
	public SqlBuilder query(Query query) {
		return this.select(query.getFields()).from(query.getTableNames()).where(LogicalOperator.AND, query.getWhere());
	}
	// --------------------------------------------------------------- Builder end

	/**
	 * 获得插入或更新的数据库字段列表
	 *
	 * @return 插入或更新的数据库字段列表
	 */
	public String[] getFieldArray() {
		return this.fields.toArray(new String[0]);
	}

	/**
	 * 获得插入或更新的数据库字段列表
	 *
	 * @return 插入或更新的数据库字段列表
	 */
	public List<String> getFields() {
		return this.fields;
	}

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

		if (null != wrapper) {
			// 包装字段名
			conditions = wrapper.wrap(conditions);
		}

		return ConditionBuilder.of(conditions).build(this.paramValues);
	}

	/**
	 * 验证实体类对象的有效性
	 * 
	 * @param entity 实体类对象
	 * @throws DbRuntimeException SQL异常包装，获取元数据信息失败
	 */
	private static void validateEntity(Entity entity) throws DbRuntimeException {
		if (null == entity) {
			throw new DbRuntimeException("Entity is null !");
		}
		if (StrUtil.isBlank(entity.getTableName())) {
			throw new DbRuntimeException("Entity`s table name is null !");
		}
		if (entity.isEmpty()) {
			throw new DbRuntimeException("No filed and value in this entity !");
		}
	}
	// --------------------------------------------------------------- private method end
}
