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
 */
public class Table implements Serializable, Cloneable {
	private static final long serialVersionUID = -810699625961392983L;

	/**
	 * table所在的schema
	 */
	private String schema;
	/**
	 * tables所在的catalog
	 */
	private String catalog;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 注释
	 */
	private String comment;
	/**
	 * 主键字段名列表
	 */
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
	 * 获取 schema
	 *
	 * @return schema
	 * @since 5.4.3
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * 设置schema
	 *
	 * @param schema schema
	 * @return this
	 * @since 5.4.3
	 */
	public Table setSchema(String schema) {
		this.schema = schema;
		return this;
	}

	/**
	 * 获取catalog
	 *
	 * @return catalog
	 * @since 5.4.3
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * 设置catalog
	 *
	 * @param catalog catalog
	 * @return this
	 * @since 5.4.3
	 */
	public Table setCatalog(String catalog) {
		this.catalog = catalog;
		return this;
	}

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
	 * 给定列名是否为主键
	 *
	 * @param columnName 列名
	 * @return 是否为主键
	 * @since 5.4.3
	 */
	public boolean isPk(String columnName){
		return getPkNames().contains(columnName);
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
