package cn.hutool.db.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;

/**
 * 数据库表的列信息
 * 
 * @author loolly
 *
 */
public class Column implements Cloneable {

	// ----------------------------------------------------- Fields start
	/** 表名 */
	private String tableName;

	/** 列名 */
	private String name;
	/** 类型，对应java.sql.Types中的类型 */
	private int type;
	/** 大小或数据长度 */
	private int size;
	/** 是否为可空 */
	private boolean isNullable;
	/** 注释 */
	private String comment;
	// ----------------------------------------------------- Fields end

	/**
	 * 创建列对象
	 * 
	 * @param tableName 表名
	 * @param columnMetaRs 列元信息的ResultSet
	 * @return 列对象
	 */
	public static Column create(String tableName, ResultSet columnMetaRs) {
		return new Column(tableName, columnMetaRs);
	}

	// ----------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public Column() {
	}

	/**
	 * 构造
	 * 
	 * @param tableName 表名
	 * @param columnMetaRs Meta信息的ResultSet
	 */
	public Column(String tableName, ResultSet columnMetaRs) {
		try {
			init(tableName, columnMetaRs);
		} catch (SQLException e) {
			throw new DbRuntimeException(StrUtil.format("Get table [{}] meta info error!", tableName));
		}
	}
	// ----------------------------------------------------- Constructor end

	/**
	 * 初始化
	 * 
	 * @param tableName 表名
	 * @param columnMetaRs 列的meta ResultSet
	 * @throws SQLException SQL执行异常
	 */
	public void init(String tableName, ResultSet columnMetaRs) throws SQLException {
		this.tableName = tableName;

		this.name = columnMetaRs.getString("COLUMN_NAME");
		this.type = columnMetaRs.getInt("DATA_TYPE");
		this.size = columnMetaRs.getInt("COLUMN_SIZE");
		this.isNullable = columnMetaRs.getBoolean("NULLABLE");
		this.comment = columnMetaRs.getString("REMARKS");
	}

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
	 * 获取列名
	 * 
	 * @return 列名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置列名
	 * 
	 * @param name 列名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取类型，对应java.sql.Types中的类型
	 * 
	 * @return 类型
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置类型，对应java.sql.Types中的类型
	 * 
	 * @param type 类型
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取大小或数据长度
	 * 
	 * @return 大小或数据长度
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 设置大小或数据长度
	 * 
	 * @param size 大小或数据长度
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 是否为可空
	 * 
	 * @return 是否为可空
	 */
	public boolean isNullable() {
		return isNullable;
	}

	/**
	 * 设置是否为可空
	 * 
	 * @param isNullable 是否为可空
	 */
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
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
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	// ----------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return "Column [tableName=" + tableName + ", name=" + name + ", type=" + type + ", size=" + size + ", isNullable=" + isNullable + "]";
	}
}
