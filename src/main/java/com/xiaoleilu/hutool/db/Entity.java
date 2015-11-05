package com.xiaoleilu.hutool.db;

import java.util.Arrays;
import java.util.List;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.StrUtil;

/**
 * 数据实体对象<br>
 * 数据实体类充当两个角色：<br>
 * 1. 数据的载体，一个Entity对应数据库中的一个row<br>
 * 2. SQL条件，Entity中的每一个字段对应一个条件，字段值对应条件的值
 * @author loolly
 *
 */
public class Entity extends Dict{
	private static final long serialVersionUID = -1951012511464327448L;
	
	//--------------------------------------------------------------- Static method start
	/**
	 * 创建Entity
	 * @param tableName 表名
	 * @return Entity
	 */
	public static Entity create() {
		return new Entity();
	}
	
	/**
	 * 创建Entity
	 * @param tableName 表名
	 * @return Entity
	 */
	public static Entity create(String tableName) {
		return new Entity(tableName);
	}
	
	/**
	 * 将PO对象转为Entity
	 * @param <T>
	 * @param vo 值对象
	 * @return Entity
	 */
	public static <T> Entity parse(T vo) {
		return create(null).fromVo(vo);
	}
	//--------------------------------------------------------------- Static method end
	
	/*表名*/
	private String tableName;
	/*字段名列表，用于限制加入的字段的值*/
	private List<String> fieldNames;
	
	//--------------------------------------------------------------- Constructor start
	public Entity() {
	}
	
	/**
	 * 构造
	 * @param tableName 数据表名
	 */
	
	public Entity(String tableName) {
		this.tableName = tableName;
	}
	//--------------------------------------------------------------- Constructor end
	
	//--------------------------------------------------------------- Getters and Setters start
	/**
	 * @return 获得表名
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * 设置表名
	 * @param tableName 表名
	 * @return 本身
	 */
	public Entity setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	/**
	 * 
	 * @return 字段列表
	 */
	public List<String> getFieldNames() {
		return fieldNames;
	}
	/**
	 * 设置字段列表
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
		return this;
	}
	
	/**
	 * 设置字段列表
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(String... fieldNames) {
		this.fieldNames = Arrays.asList(fieldNames);
		return this;
	}
	
	/**
	 * 添加字段列表
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity addFieldNames(String... fieldNames) {
		final List<String> fieldList = Arrays.asList(fieldNames);
		if(null == this.fieldNames){
			this.fieldNames = fieldList;
		}else{
			this.fieldNames.addAll(fieldList);
		}
		return this;
	}
	
	//--------------------------------------------------------------- Getters and Setters end
	/**
	 * 将值对象转换为Entity<br>
	 * 类名会被当作表名，小写第一个字母
	 * @param <T>
	 * @param vo 值对象
	 * @return 自己
	 */
	@Override
	public <T> Entity fromVo(T vo) {
		String tableName = vo.getClass().getSimpleName();
		tableName = StrUtil.lowerFirst(tableName);
		this.setTableName(tableName);
		
		return (Entity) super.fromVo(vo);
	}
	
	//-------------------------------------------------------------------- 特定类型值
	@Override
	public Object put(String key, Object value) {
		if(CollectionUtil.isEmpty(fieldNames) || fieldNames.contains(key)){
			super.put(key, value);
		}
		return super.put(key, value);
	}
	
	@Override
	public Entity set(String attr, Object value) {
		return (Entity) super.set(attr, value);
	}
	
	@Override
	public Entity setIgnoreNull(String attr, Object value) {
		return (Entity) super.setIgnoreNull(attr, value);
	}
	//-------------------------------------------------------------------- 特定类型值
	@Override
	public Entity clone() {
		return (Entity) super.clone();
	}
	//-------------------------------------------------------------------- 特定类型值
	
	@Override
	public String toString() {
		return "Entity {tableName=" + tableName + ", fieldNames=" + fieldNames + ", fields=" + super.toString() + "}";
	}
}
