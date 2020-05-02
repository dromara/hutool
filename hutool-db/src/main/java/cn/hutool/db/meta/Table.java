package cn.hutool.db.meta;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 数据库表信息
 * 
 * @author loolly
 *
 */
public class Table implements Serializable, Cloneable {
	private static final long serialVersionUID = -810699625961392983L;

	/** 表名 */
	private String tableName;
	/** 注释 */
	private String comment;
	/** 主键字段名列表 */
	private Set<String> pkNames = new LinkedHashSet<>();
	private final Map<String, Column> columns = new LinkedHashMap<>();

	public static Table create(String tableName) {
		return new Table(tableName);
	}

	// ----------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param tableName 表名
	 */
	public Table(String tableName) {
		this.setTableName(tableName);
	}
	// ----------------------------------------------------- Constructor end

	// ----------------------------------------------------- Getters and Setters start
	/**
	 * 获取表名
	 * 
	 * @return 表名
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置表名
	 * 
	 * @param tableName 表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取注释
	 * 
	 * @return 注释
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 设置注释
	 * 
	 * @param comment 注释
	 * @return this
	 */
	public Table setComment(String comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * 获取主键列表
	 * 
	 * @return 主键列表
	 */
	public Set<String> getPkNames() {
		return pkNames;
	}

	/**
	 * 设置主键列表
	 * 
	 * @param pkNames 主键列表
	 */
	public void setPkNames(Set<String> pkNames) {
		this.pkNames = pkNames;
	}
	// ----------------------------------------------------- Getters and Setters end

	/**
	 * 设置列对象
	 * 
	 * @param column 列对象
	 * @return 自己
	 */
	public Table setColumn(Column column) {
		this.columns.put(column.getName(), column);
		return this;
	}

	/**
	 * 获取某列信息
	 * 
	 * @param name 列名
	 * @return 列对象
	 * @since 4.2.2
	 */
	public Column getColumn(String name) {
		return this.columns.get(name);
	}

	/**
	 * 获取所有字段元信息
	 * 
	 * @return 字段元信息集合
	 * @since 4.5.8
	 */
	public Collection<Column> getColumns() {
		return this.columns.values();
	}

	/**
	 * 添加主键
	 * 
	 * @param pkColumnName 主键的列名
	 * @return 自己
	 */
	public Table addPk(String pkColumnName) {
		this.pkNames.add(pkColumnName);
		return this;
	}
}
