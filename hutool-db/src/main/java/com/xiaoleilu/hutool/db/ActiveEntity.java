package com.xiaoleilu.hutool.db;

import java.sql.SQLException;
import java.util.List;

/**
 * 动态实体类<br>
 * 提供了针对自身实体的增删改方法
 * 
 * @author Looly
 *
 */
public class ActiveEntity extends Entity {
	private static final long serialVersionUID = 6112321379601134750L;

	private SqlRunner runner;

	// --------------------------------------------------------------- Static method start
	/**
	 * 创建ActiveEntity
	 * 
	 * @return ActiveEntity
	 */
	public static ActiveEntity create() {
		return new ActiveEntity();
	}

	/**
	 * 创建ActiveEntity
	 * 
	 * @param tableName 表名
	 * @return ActiveEntity
	 */
	public static ActiveEntity create(String tableName) {
		return new ActiveEntity(tableName);
	}

	/**
	 * 将PO对象转为Entity
	 * 
	 * @param <T> Bean对象类型
	 * @param bean Bean对象
	 * @return ActiveEntity
	 */
	public static <T> ActiveEntity parse(T bean) {
		return create(null).parseBean(bean);
	}

	/**
	 * 将PO对象转为ActiveEntity
	 * 
	 * @param <T> Bean对象类型
	 * @param bean Bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @return ActiveEntity
	 */
	public static <T> ActiveEntity parse(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		return create(null).parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 将PO对象转为ActiveEntity,并采用下划线法转换字段
	 * 
	 * @param <T> Bean对象类型
	 * @param bean Bean对象
	 * @return ActiveEntity
	 */
	public static <T> ActiveEntity parseWithUnderlineCase(T bean) {
		return create(null).parseBean(bean, true, true);
	}
	// --------------------------------------------------------------- Static method end

	// -------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public ActiveEntity() {
		this(SqlRunner.create(), (String) null);
	}

	/**
	 * 构造
	 * 
	 * @param tableName 表名
	 */
	public ActiveEntity(String tableName) {
		this(SqlRunner.create(), tableName);
	}

	/**
	 * 构造
	 * 
	 * @param entity 非动态实体
	 */
	public ActiveEntity(Entity entity) {
		this(SqlRunner.create(), entity);
	}

	/**
	 * 构造
	 * 
	 * @param runner {@link SqlRunner}
	 * @param tableName 表名
	 */
	public ActiveEntity(SqlRunner runner, String tableName) {
		super(tableName);
		this.runner = runner;
	}

	/**
	 * 构造
	 * 
	 * @param runner {@link SqlRunner}
	 * @param entity 非动态实体
	 */
	public ActiveEntity(SqlRunner runner, Entity entity) {
		super(entity.getTableName());
		this.putAll(entity);
		this.runner = runner;
	}
	// -------------------------------------------------------------------------- Constructor end
	
	@Override
	public ActiveEntity setTableName(String tableName) {
		return (ActiveEntity) super.setTableName(tableName);
	}
	
	@Override
	public ActiveEntity setFieldNames(List<String> fieldNames) {
		return (ActiveEntity) super.setFieldNames(fieldNames);
	}
	
	@Override
	public ActiveEntity setFieldNames(String... fieldNames) {
		return (ActiveEntity) super.setFieldNames(fieldNames);
	}
	
	@Override
	public ActiveEntity addFieldNames(String... fieldNames) {
		return (ActiveEntity) super.addFieldNames(fieldNames);
	}
	
	@Override
	public <T> ActiveEntity parseBean(T bean) {
		return (ActiveEntity) super.parseBean(bean);
	}
	
	@Override
	public <T> ActiveEntity parseBean(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		return (ActiveEntity) super.parseBean(bean, isToUnderlineCase, ignoreNullValue);
	}
	
	@Override
	public ActiveEntity set(String field, Object value) {
		return (ActiveEntity) super.set(field, value);
	}
	
	@Override
	public ActiveEntity setIgnoreNull(String field, Object value) {
		return (ActiveEntity) super.setIgnoreNull(field, value);
	}
	
	@Override
	public ActiveEntity clone() {
		return (ActiveEntity) super.clone();
	}

	// -------------------------------------------------------------------------- CRUD start
	/**
	 * 根据Entity中现有字段条件从数据库中增加一条数据
	 * 
	 * @return this
	 */
	public ActiveEntity add() {
		try {
			runner.insert(this);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		}
		return this;
	}

	/**
	 * 根据Entity中现有字段条件从数据库中加载一个Entity对象
	 * 
	 * @return this
	 */
	public ActiveEntity load() {
		try {
			runner.get(this);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		}
		return this;
	}

	/**
	 * 根据现有Entity中的条件删除与之匹配的数据库记录
	 * 
	 * @return this
	 */
	public ActiveEntity del() {
		try {
			runner.del(this);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		}
		return this;
	}

	/**
	 * 根据现有Entity中的条件删除与之匹配的数据库记录
	 * 
	 * @param primaryKey 主键名
	 * @return this
	 */
	public ActiveEntity update(String primaryKey) {
		try {
			runner.update(this, Entity.create().set(primaryKey, this.get(primaryKey)));
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		}
		return this;
	}
	// -------------------------------------------------------------------------- CRUD end
}
