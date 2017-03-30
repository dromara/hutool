package com.xiaoleilu.hutool.db.meta;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 数据库表信息
 * @author loolly
 *
 */
public class Table extends HashMap<String, Column>{
	private static final long serialVersionUID = -810699625961392983L;
	
	private String tableName;
	private Set<String> pkNames = new LinkedHashSet<String>();
	
	public static Table create(String tableName) {
		return new Table(tableName);
	}
	
	//----------------------------------------------------- Constructor start
	public Table(String tableName) {
		this.setTableName(tableName);
	}
	//----------------------------------------------------- Constructor end

	//----------------------------------------------------- Getters and Setters start
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Set<String> getPkNames() {
		return pkNames;
	}
	public void setPkNames(Set<String> pkNames) {
		this.pkNames = pkNames;
	}
	//----------------------------------------------------- Getters and Setters end
	
	/**
	 * 设置列对象
	 * @param column 列对象
	 * @return 自己
	 */
	public Table setColumn(Column column) {
		put(column.getName(), column);
		return this;
	}
	
	/**
	 * 添加主键
	 * @param pkColumnName 主键的列名
	 * @return 自己
	 */
	public Table addPk(String pkColumnName) {
		this.pkNames.add(pkColumnName);
		return this;
	}
}
