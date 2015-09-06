package com.xiaoleilu.hutool.db;

import java.sql.SQLException;
import java.util.List;

import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.db.handler.SingleEntityHandler;

/**
 * 数据访问层模板
 * @author Looly
 *
 */
public class DaoTemplate {
	protected SqlRunner runner;
	
	/** 表名 */
	protected String tableName;
	/** 本表的主键字段，请在子类中覆盖或构造方法中指定，默认为id */
	protected String primaryKeyField = "id";
	
	//--------------------------------------------------------------- Constructor start
	public DaoTemplate(String tableName) {
		this.tableName = tableName;
	}
	
	public DaoTemplate(String tableName, String primaryKeyField) {
		this.tableName = tableName;
		this.primaryKeyField = primaryKeyField;
	}
	
	public DaoTemplate(String tableName, String primaryKeyField, SqlRunner runner) {
		this.tableName = tableName;
		this.primaryKeyField = primaryKeyField;
		this.runner = runner;
	}
	//--------------------------------------------------------------- Constructor end
	
	/**
	 * 添加
	 * @param entity 实体对象
	 * @return 插入行数
	 * @throws SQLException
	 */
	public int add(Entity entity) throws SQLException {
		entity.setTableName(tableName);
		return runner.insert(entity);
	}
	
	/**
	 * 添加
	 * @param entity 实体对象
	 * @return 主键列表
	 * @throws SQLException
	 */
	public List<Object> addForGeneratedKeys(Entity entity) throws SQLException {
		entity.setTableName(tableName);
		return runner.insertForGeneratedKeys(entity);
	}
	
	/**
	 * 添加
	 * @param entity 实体对象
	 * @return 自增主键
	 * @throws SQLException
	 */
	public Long addForGeneratedKey(Entity entity) throws SQLException {
		entity.setTableName(tableName);
		return runner.insertForGeneratedKey(entity);
	}
	
	/**
	 * 删除
	 * @param <T> 主键类型
	 * 
	 * @param pk 主键
	 * @return 删除行数
	 * @throws SQLException
	 */
	public <T> int del(T pk) throws SQLException {
		if (pk == null) {
			return 0;
		}

		return runner.del(Entity.create(tableName).set(primaryKeyField, pk));
	}
	
	
	/**
	 * 更新职位信息
	 * 
	 * @param entity 实体对象，必须包含主键
	 * @return 更新行数
	 * @throws SQLException
	 */
	public int update(Entity entity) throws SQLException {
		entity.setTableName(tableName);
		Object pk = entity.get(primaryKeyField);
		if (null == pk) {
			throw new SQLException(StrUtil.format("Please determine `{}` for update", primaryKeyField));
		}

		Entity where = Entity.create(tableName).set(primaryKeyField, pk);
		Entity record = (Entity) entity.clone();
		record.remove(primaryKeyField);

		return runner.update(record, where);
	}
	
	/**
	 * 增加或者更新实体
	 * @param entity 实体，当包含主键时更新，否则新增
	 * @return 新增或更新条数
	 * @throws SQLException
	 */
	public int addOrUpdate(Entity entity) throws SQLException {
		if(entity.get(primaryKeyField) == null) {
			entity.set(primaryKeyField, add(entity));
			return 1;
		}else {
			 return update(entity);
		}
	}
	
	/**
	 * 获取单个记录
	 * @param <T>
	 * 
	 * @param pk 主键值
	 * @return 记录
	 * @throws SQLException
	 * @throws SQLException
	 */
	public <T> Entity get(T pk) throws SQLException {
		return runner.find(null,
				Entity.create(tableName).set(primaryKeyField, pk),
				new SingleEntityHandler());
	}
}
