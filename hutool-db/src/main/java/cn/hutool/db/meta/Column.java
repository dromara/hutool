package cn.hutool.db.meta;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库表的列信息
 *
 * @author loolly
 */
public class Column implements Serializable, Cloneable {
	private static final long serialVersionUID = 577527740359719367L;

	// ----------------------------------------------------- Fields start
	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 列名
	 */
	private String name;
	/**
	 * 类型，对应java.sql.Types中的类型
	 */
	private int type;
	/**
	 * 类型名称
	 */
	private String typeName;
	/**
	 * 大小或数据长度
	 */
	private int size;
	private Integer digit;
	/**
	 * 是否为可空
	 */
	private boolean isNullable;
	/**
	 * 注释
	 */
	private String comment;
	/**
	 * 是否自增
	 */
	private boolean autoIncrement;
	/**
	 * 是否为主键
	 */
	private boolean isPk;
	// ----------------------------------------------------- Fields end

	/**
	 * 创建列对象
	 *
	 * @param tableName    表名
	 * @param columnMetaRs 列元信息的ResultSet
	 * @return 列对象
	 * @deprecated 请使用 {@link #create(Table, ResultSet)}
	 */
	public static Column create(String tableName, ResultSet columnMetaRs) {
		return new Column(tableName, columnMetaRs);
	}

	/**
	 * 创建列对象
	 *
	 * @param columnMetaRs 列元信息的ResultSet
	 * @param table        表信息
	 * @return 列对象
	 * @since 5.4.3
	 */
	public static Column create(Table table, ResultSet columnMetaRs) {
		return new Column(table, columnMetaRs);
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
	 * @param tableName    表名
	 * @param columnMetaRs Meta信息的ResultSet
	 * @deprecated 请使用 {@link #Column(Table, ResultSet)}
	 */
	@Deprecated
	public Column(String tableName, ResultSet columnMetaRs) {
		try {
			init(tableName, columnMetaRs);
		} catch (SQLException e) {
			throw new DbRuntimeException(StrUtil.format("Get table [{}] meta info error!", tableName));
		}
	}

	/**
	 * 构造
	 *
	 * @param table        表信息
	 * @param columnMetaRs Meta信息的ResultSet
	 * @since 5.4.3
	 */
	public Column(Table table, ResultSet columnMetaRs) {
		try {
			init(table, columnMetaRs);
		} catch (SQLException e) {
			throw new DbRuntimeException(StrUtil.format("Get table [{}] meta info error!", tableName));
		}
	}
	// ----------------------------------------------------- Constructor end

	/**
	 * 初始化
	 *
	 * @param tableName    表名
	 * @param columnMetaRs 列的meta ResultSet
	 * @throws SQLException SQL执行异常
	 * @deprecated 请使用 {@link #init(Table, ResultSet)}
	 */
	@Deprecated
	public void init(String tableName, ResultSet columnMetaRs) throws SQLException {
		init(Table.create(tableName), columnMetaRs);
	}

	/**
	 * 初始化
	 *
	 * @param table        表信息
	 * @param columnMetaRs 列的meta ResultSet
	 * @throws SQLException SQL执行异常
	 */
	public void init(Table table, ResultSet columnMetaRs) throws SQLException {
		this.tableName = table.getTableName();

		this.name = columnMetaRs.getString("COLUMN_NAME");
		this.isPk = table.isPk(this.name);

		this.type = columnMetaRs.getInt("DATA_TYPE");
		this.typeName = columnMetaRs.getString("TYPE_NAME");
		this.size = columnMetaRs.getInt("COLUMN_SIZE");
		this.isNullable = columnMetaRs.getBoolean("NULLABLE");
		this.comment = columnMetaRs.getString("REMARKS");

		// 保留小数位数
		try {
			this.digit = columnMetaRs.getInt("DECIMAL_DIGITS");
		} catch (SQLException ignore) {
			//某些驱动可能不支持，跳过
		}

		// 是否自增
		try {
			String auto = columnMetaRs.getString("IS_AUTOINCREMENT");
			if (BooleanUtil.toBoolean(auto)) {
				this.autoIncrement = true;
			}
		} catch (SQLException ignore) {
			//某些驱动可能不支持，跳过
		}
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
	 * 获取小数位数
	 *
	 * @return 大小或数据长度
	 */
	public int getDigit() {
		return digit;
	}

	/**
	 * 设置小数位数
	 *
	 * @param digit 小数位数
	 * @return this
	 */
	public Column setDigit(int digit) {
		this.digit = digit;
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

	/**
	 * 是否自增
	 *
	 * @return 是否自增
	 * @since 5.4.3
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * 设置是否自增
	 *
	 * @param autoIncrement 是否自增
	 * @return this
	 * @since 5.4.3
	 */
	public Column setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
		return this;
	}

	/**
	 * 是否主键
	 *
	 * @return 是否主键
	 * @since 5.4.3
	 */
	public boolean isPk() {
		return isPk;
	}

	/**
	 * 设置是否主键
	 *
	 * @param isPk 是否主键
	 * @return this
	 * @since 5.4.3
	 */
	public Column setPk(boolean isPk) {
		this.isPk = isPk;
		return this;
	}
	// ----------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return "Column [tableName=" + tableName + ", name=" + name + ", type=" + type + ", size=" + size + ", isNullable=" + isNullable + "]";
	}
}
