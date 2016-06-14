package com.xiaoleilu.hutool.db;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.lang.Dict;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;

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
	 * @param bean Bean对象
	 * @return Entity
	 */
	public static <T> Entity parse(T bean) {
		return create(null).parseBean(bean);
	}
	//--------------------------------------------------------------- Static method end
	
	/*表名*/
	private String tableName;
	/*字段名列表，用于限制加入的字段的值*/
	private Set<String> fieldNames;
	
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
	 * @return 字段集合
	 */
	public Set<String> getFieldNames() {
		return this.fieldNames;
	}
	/**
	 * 设置字段列表
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(List<String> fieldNames) {
		if(CollectionUtil.isNotEmpty(fieldNames)){
			this.fieldNames = new HashSet<String>(fieldNames);
		}
		return this;
	}
	
	/**
	 * 设置字段列表
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity setFieldNames(String... fieldNames) {
		if(CollectionUtil.isNotEmpty(fieldNames)){
			this.fieldNames = CollectionUtil.newHashSet(fieldNames);
		}
		return this;
	}
	
	/**
	 * 添加字段列表
	 * @param fieldNames 字段列表
	 * @return 自身
	 */
	public Entity addFieldNames(String... fieldNames) {
		if(CollectionUtil.isNotEmpty(fieldNames)){
			if(null == this.fieldNames){
				return setFieldNames(fieldNames);
			}else{
				for (String fieldName : fieldNames) {
					this.fieldNames.add(fieldName);
				}
			}
		}
		return this;
	}
	
	//--------------------------------------------------------------- Getters and Setters end
	/**
	 * 将值对象转换为Entity<br>
	 * 类名会被当作表名，小写第一个字母
	 * @param <T>
	 * @param bean Bean对象
	 * @return 自己
	 */
	@Override
	public <T> Entity parseBean(T bean) {
		String tableName = bean.getClass().getSimpleName();
		tableName = StrUtil.lowerFirst(tableName);
		this.setTableName(tableName);
		
		return (Entity) super.parseBean(bean);
	}
	
	//-------------------------------------------------------------------- Put and Set start
	/**
	 * PUT方法做了过滤限制，如果此实体限制了属性名，则忽略限制名列表外的字段名
	 * @param key 名
	 * @param value 值
	 */
	@Override
	public Object put(String key, Object value) {
		if(CollectionUtil.isEmpty(fieldNames) || fieldNames.contains(key)){
			super.put(key, value);
		}
		return null;
	}
	
	@Override
	public Entity set(String attr, Object value) {
		return (Entity) super.set(attr, value);
	}
	
	@Override
	public Entity setIgnoreNull(String attr, Object value) {
		return (Entity) super.setIgnoreNull(attr, value);
	}
	//-------------------------------------------------------------------- Put and Set end
	
	//-------------------------------------------------------------------- Get start
	
	/**
	 * 获得Clob类型结果
	 * @param attr 参数
	 * @return Clob
	 */
	public Clob getClob(String attr){
		return get(attr, null);
	}
	
	@Override
	public String getStr(String attr) {
		final Object obj = get(attr);
		if(obj instanceof Clob){
			Clob clob = (Clob)obj;
			Reader reader = null;
			try {
				reader = clob.getCharacterStream();
				return IoUtil.read(reader);
			} catch (SQLException | IOException e) {
				throw new DbRuntimeException(e);
			}finally{
				IoUtil.close(reader);
			}
		}else if(obj instanceof RowId){
			final RowId rowId = (RowId)obj;
			return StrUtil.str(rowId.getBytes(), CharsetUtil.UTF_8);
		}
		return super.getStr(attr);
	}
	
	/**
	 * 获得rowid
	 * @return RowId
	 */
	public RowId getRowId(){
		return getRowId("ROWID");
	}
	
	/**
	 * 获得rowid
	 * @param attr rowid属性名
	 * @return RowId
	 */
	public RowId getRowId(String attr){
		Object obj = this.get(attr);
		if(null == obj){
			return null;
		}
		if(obj instanceof RowId){
			return (RowId)obj;
		}
		throw new DbRuntimeException("Value with name [{}] is not a rowid!", attr);
	}
	
	//-------------------------------------------------------------------- Get end
	
	//-------------------------------------------------------------------- 特殊方法 start
	@Override
	public Entity clone() {
		return (Entity) super.clone();
	}
	//-------------------------------------------------------------------- 特殊方法 end
	
	@Override
	public String toString() {
		return "Entity {tableName=" + tableName + ", fieldNames=" + fieldNames + ", fields=" + super.toString() + "}";
	}
}
