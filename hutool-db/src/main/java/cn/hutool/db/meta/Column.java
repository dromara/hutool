package cn.hutool.db.meta;

import java.io.Serializable;
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
public class Column implements Serializable, Cloneable {
	private static final long serialVersionUID = 577527740359719367L;

	// ----------------------------------------------------- Fields start
	/** 表名 */
	private String tableName;

	/** 列名 */
	private String name;
	/** 类型，对应java.sql.Types中的类型 */
	private int type;
	/** 类型名称 */
	private String typeName;
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
		this.typeName = columnMetaRs.getString("TYPE_NAME");
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
	 * @return this
	 */
	public Column setTableName(String tableName) {
		this.tableName = tableName;
		return this;
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
	 * @return this
	 */
	public Column setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 获取字段类型的枚举
	 * 
	 * @return 阻断类型枚举
	 * @since 4.5.8
	 */
	public JdbcType getTypeEnum() {
		return JdbcType.valueOf(this.type);
	}
	
	/**
	 * 获取类型，对应{@link java.sql.Types}中的类型
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
	 * @return this
	 */
	public Column setType(int type) {
		this.type = type;
		return this;
	}
	
	/**
	 * 获取类型名称
	 * 
	 * @return 类型名称
	 */
	public String getTypeName() {
		return typeName;
	}
	
	/**
	 * 设置类型名称
	 * 
	 * @param typeName 类型名称
	 * @return this
	 */
	public Column setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
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
	 * @return this
	 */
	public Column setSize(int size) {
		this.size = size;
		return this;
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
	 * @return this
	 */
	public Column setNullable(boolean isNullable) {
		this.isNullable = isNullable;
		return this;
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
	public Column setComment(String comment) {
		this.comment = comment;
		return this;
	}
	// ----------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return "Column [tableName=" + tableName + ", name=" + name + ", type=" + type + ", size=" + size + ", isNullable=" + isNullable + "]";
	}
}
