package com.xiaoleilu.hutool.db.sql;

import java.sql.SQLException;
import java.util.Collection;

import com.xiaoleilu.hutool.db.Page;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 查询对象，用于传递查询所需的字段值
 * @author Looly
 *
 */
public class Query {
	
	/** 查询的字段名列表 */
	Collection<String> fields;
	/** 查询的表名 */
	String[] tableNames;
	/** 查询的条件语句 */
	Condition[] where;
	/** 分页对象 */
	Page page;
	
	//--------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param tableNames 表名
	 */
	public Query(String... tableNames) {
		this(null, tableNames);
		this.tableNames = tableNames;
	}
	
	/**
	 * 构造
	 * @param where 条件语句
	 * @param tableNames 表名
	 */
	public Query(Condition[] where, String... tableNames) {
		this(where, null, tableNames);
	}
	
	/**
	 * 构造
	 * @param tableNames 表名
	 * @param where 条件语句
	 * @param page 分页
	 */
	public Query(Condition[] where, Page page, String... tableNames) {
		this.tableNames = tableNames;
		this.where = where;
		this.page = page;
	}
	
	/**
	 * 构造
	 * @param fields 字段
	 * @param tableNames 表名
	 * @param where 条件
	 * @param page 分页
	 */
	public Query(Collection<String> fields, String[] tableNames, Condition[] where, Page page) {
		super();
		this.fields = fields;
		this.tableNames = tableNames;
		this.where = where;
		this.page = page;
	}
	//--------------------------------------------------------------- Constructor end
	
	//--------------------------------------------------------------- Getters and Setters start
	/**
	 * 获得查询的字段名列表
	 * @return 查询的字段名列表
	 */
	public Collection<String> getFields() {
		return fields;
	}

	/**
	 * 设置查询的字段名列表
	 * @param fields 查询的字段名列表
	 */
	public void setFields(Collection<String> fields) {
		this.fields = fields;
	}
	
	/**
	 * 获得表名数组
	 * @return 表名数组
	 */
	public String[] getTableNames() {
		return tableNames;
	}
	/**
	 * 设置表名
	 * @param tableNames 表名
	 */
	public void setTableNames(String[] tableNames) {
		this.tableNames = tableNames;
	}
	
	/**
	 * 获得条件语句
	 * @return 条件语句
	 */
	public Condition[] getWhere() {
		return where;
	}
	/**
	 * 设置条件语句
	 * @param where 条件语句
	 */
	public void setWhere(Condition[] where) {
		this.where = where;
	}
	
	/**
	 * 获得分页对象
	 * @return 分页对象
	 */
	public Page getPage() {
		return page;
	}
	/**
	 * 设置分页对象
	 * @param page 分页对象
	 */
	public void setPage(Page page) {
		this.page = page;
	}
	//--------------------------------------------------------------- Getters and Setters end
	
	/**
	 * 获得第一个表名
	 */
	public String getFirstTableName() throws SQLException{
		if(CollectionUtil.isEmpty(this.tableNames)){
			throw new SQLException("No tableName!");
		}
		return this.tableNames[0];
	}
}
