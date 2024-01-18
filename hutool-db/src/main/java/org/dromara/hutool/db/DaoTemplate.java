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

package org.dromara.hutool.db;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.ds.DSUtil;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * 数据访问层模板<br>
 * 此模板用于简化对指定表的操作，简化的操作如下：<br>
 * <pre>
 * 1、在初始化时指定了表名，CRUD操作时便不需要表名
 * 2、在初始化时指定了主键，某些需要主键的操作便不需要指定主键类型
 * </pre>
 *
 * @author Looly
 */
public class DaoTemplate {

	/**
	 * 表名
	 */
	protected String tableName;
	/**
	 * 本表的主键字段，请在子类中覆盖或构造方法中指定，默认为id
	 */
	protected String primaryKeyField = "id";
	/**
	 * SQL运行器
	 */
	protected Db db;

	// region ----- Constructor

	/**
	 * 构造，此构造需要自定义SqlRunner，主键默认为id
	 *
	 * @param tableName 数据库表名
	 */
	public DaoTemplate(final String tableName) {
		this(tableName, (String) null);
	}

	/**
	 * 构造，使用默认的池化连接池，读取默认配置文件的空分组，适用于只有一个数据库的情况
	 *
	 * @param tableName       数据库表名
	 * @param primaryKeyField 主键字段名
	 */
	public DaoTemplate(final String tableName, final String primaryKeyField) {
		this(tableName, primaryKeyField, DSUtil.getDS());
	}

	/**
	 * 构造
	 *
	 * @param tableName 表
	 * @param ds 数据源
	 */
	public DaoTemplate(final String tableName, final DataSource ds) {
		this(tableName, null, ds);
	}

	/**
	 * 构造
	 *
	 * @param tableName       表名
	 * @param primaryKeyField 主键字段名
	 * @param ds              数据源
	 */
	public DaoTemplate(final String tableName, final String primaryKeyField, final DataSource ds) {
		this(tableName, primaryKeyField, Db.of(ds));
	}

	/**
	 * 构造
	 *
	 * @param tableName       表名
	 * @param primaryKeyField 主键字段名
	 * @param db              Db对象
	 */
	public DaoTemplate(final String tableName, final String primaryKeyField, final Db db) {
		this.tableName = tableName;
		if (StrUtil.isNotBlank(primaryKeyField)) {
			this.primaryKeyField = primaryKeyField;
		}
		this.db = db;
	}
	// endregion

	// region ----- Add

	/**
	 * 添加
	 *
	 * @param entity 实体对象
	 * @return 插入行数
	 * @throws DbException SQL执行异常
	 */
	public int add(final Entity entity) throws DbException {
		return db.insert(fixEntity(entity));
	}

	/**
	 * 添加
	 *
	 * @param entity 实体对象
	 * @return 主键列表
	 * @throws DbException SQL执行异常
	 */
	public List<Object> addForGeneratedKeys(final Entity entity) throws DbException {
		return db.insertForGeneratedKeys(fixEntity(entity));
	}

	/**
	 * 添加
	 *
	 * @param entity 实体对象
	 * @return 自增主键
	 * @throws DbException SQL执行异常
	 */
	public Long addForGeneratedKey(final Entity entity) throws DbException {
		return db.insertForGeneratedKey(fixEntity(entity));
	}
	// endregion

	// region ----- Delete

	/**
	 * 删除
	 *
	 * @param <T> 主键类型
	 * @param pk  主键
	 * @return 删除行数
	 * @throws DbException SQL执行异常
	 */
	public <T> int del(final T pk) throws DbException {
		if (pk == null) {
			return 0;
		}
		return this.del(Entity.of(tableName).set(primaryKeyField, pk));
	}

	/**
	 * 删除
	 *
	 * @param <T>   主键类型
	 * @param field 字段名
	 * @param value 字段值
	 * @return 删除行数
	 * @throws DbException SQL执行异常
	 */
	public <T> int del(final String field, final T value) throws DbException {
		if (StrUtil.isBlank(field)) {
			return 0;
		}

		return this.del(Entity.of(tableName).set(field, value));
	}

	/**
	 * 删除
	 *
	 * @param where 删除条件，当条件为空时，返回0（防止误删全表）
	 * @return 删除行数
	 * @throws DbException SQL执行异常
	 */
	public int del(final Entity where) throws DbException {
		if (MapUtil.isEmpty(where)) {
			return 0;
		}
		return db.del(fixEntity(where));
	}
	// endregion

	// region ----- Update

	/**
	 * 按照条件更新
	 *
	 * @param record 更新的内容
	 * @param where  条件
	 * @return 更新条目数
	 * @throws DbException SQL执行异常
	 */
	public int update(final Entity record, final Entity where) throws DbException {
		if (MapUtil.isEmpty(record)) {
			return 0;
		}
		return db.update(fixEntity(record), where);
	}

	/**
	 * 更新
	 *
	 * @param entity 实体对象，必须包含主键
	 * @return 更新行数
	 * @throws DbException SQL执行异常
	 */
	public int update(Entity entity) throws DbException {
		if (MapUtil.isEmpty(entity)) {
			return 0;
		}
		entity = fixEntity(entity);
		final Object pk = entity.get(primaryKeyField);
		if (null == pk) {
			throw new DbException(StrUtil.format("Please determine `{}` for update", primaryKeyField));
		}

		final Entity where = Entity.of(tableName).set(primaryKeyField, pk);
		final Entity record = entity.clone();
		record.remove(primaryKeyField);

		return db.update(record, where);
	}

	/**
	 * 增加或者更新实体
	 *
	 * @param entity 实体，当包含主键时更新，否则新增
	 * @return 新增或更新条数
	 * @throws DbException SQL执行异常
	 */
	public int addOrUpdate(final Entity entity) throws DbException {
		return null == entity.get(primaryKeyField) ? add(entity) : update(entity);
	}
	// endregion

	//region ----- Get
	/**
	 * 根据主键获取单个记录
	 *
	 * @param <T> 主键类型
	 * @param pk  主键值
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public <T> Entity get(final T pk) throws DbException {
		return this.get(primaryKeyField, pk);
	}

	/**
	 * 根据某个字段（最好是唯一字段）查询单个记录<br>
	 * 当有多条返回时，只显示查询到的第一条
	 *
	 * @param <T>   字段值类型
	 * @param field 字段名
	 * @param value 字段值
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public <T> Entity get(final String field, final T value) throws DbException {
		return this.get(Entity.of(tableName).set(field, value));
	}

	/**
	 * 根据条件实体查询单个记录，当有多条返回时，只显示查询到的第一条
	 *
	 * @param where 条件
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public Entity get(final Entity where) throws DbException {
		return db.get(fixEntity(where));
	}
	// endregion

	// region ----- Find and page

	/**
	 * 根据某个字段值查询结果
	 *
	 * @param <T>   字段值类型
	 * @param field 字段名
	 * @param value 字段值
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public <T> List<Entity> find(final String field, final T value) throws DbException {
		return this.find(Entity.of(tableName).set(field, value));
	}

	/**
	 * 查询当前表的所有记录
	 *
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> findAll() throws DbException {
		return this.find(Entity.of(tableName));
	}

	/**
	 * 根据某个字段值查询结果
	 *
	 * @param where 查询条件
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> find(final Entity where) throws DbException {
		return db.find(null, fixEntity(where));
	}

	/**
	 * 根据SQL语句查询结果<br>
	 * SQL语句可以是非完整SQL语句，可以只提供查询的条件部分（例如WHERE部分）<br>
	 * 此方法会自动补全SELECT * FROM [tableName] 部分，这样就无需关心表名，直接提供条件即可
	 *
	 * @param sql    SQL语句
	 * @param params SQL占位符中对应的参数
	 * @return 记录
	 * @throws DbException SQL执行异常
	 */
	public List<Entity> findBySql(String sql, final Object... params) throws DbException {
		final String selectKeyword = StrUtil.subPre(sql.trim(), 6).toLowerCase();
		if (!"select".equals(selectKeyword)) {
			sql = "SELECT * FROM " + this.tableName + " " + sql;
		}
		return db.query(sql, params);
	}

	/**
	 * 分页
	 *
	 * @param where        条件
	 * @param page         分页对象
	 * @param selectFields 查询的字段列表
	 * @return 分页结果集
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Entity where, final Page page, final String... selectFields) throws DbException {
		return db.page(Arrays.asList(selectFields), fixEntity(where), page);
	}

	/**
	 * 分页
	 *
	 * @param where 条件
	 * @param page  分页对象
	 * @return 分页结果集
	 * @throws DbException SQL执行异常
	 */
	public PageResult<Entity> page(final Entity where, final Page page) throws DbException {
		return db.page(fixEntity(where), page);
	}

	/**
	 * 满足条件的数据条目数量
	 *
	 * @param where 条件
	 * @return 数量
	 * @throws DbException SQL执行异常
	 */
	public long count(final Entity where) throws DbException {
		return db.count(fixEntity(where));
	}

	/**
	 * 指定条件的数据是否存在
	 *
	 * @param where 条件
	 * @return 是否存在
	 * @throws DbException SQL执行异常
	 */
	public boolean exist(final Entity where) throws DbException {
		return this.count(where) > 0;
	}
	// endregion

	/**
	 * 修正Entity对象，避免null和填充表名
	 *
	 * @param entity 实体类
	 * @return 修正后的实体类
	 */
	private Entity fixEntity(Entity entity) {
		if (null == entity) {
			entity = Entity.of(tableName);
		} else if (StrUtil.isBlank(entity.getTableName())) {
			entity.setTableName(tableName);
		}
		return entity;
	}
}
