package cn.hutool.db.meta;

import cn.hutool.db.DbRuntimeException;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 索引中的列信息
 *
 * @author huzhongying
 * @since 5.7.23
 */
public class ColumnIndexInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 根据DatabaseMetaData#getIndexInfo获取的{@link ResultSet}构建索引列信息
	 *
	 * @param rs 结果集，通过DatabaseMetaData#getIndexInfo获取
	 * @return ColumnIndexInfo
	 */
	public static ColumnIndexInfo create(ResultSet rs) {
		try {
			return new ColumnIndexInfo(
					rs.getString("COLUMN_NAME"),
					rs.getString("ASC_OR_DESC"));
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 列排序顺序，“A”: 升序，“D” : 降序，如果不支持排序顺序，可能为空
	 */
	private String ascOrDesc;

	/**
	 * 构造
	 *
	 * @param columnName 索引列名
	 * @param ascOrDesc  正序或反序，null表示无顺序表示
	 */
	public ColumnIndexInfo(String columnName, String ascOrDesc) {
		this.columnName = columnName;
		this.ascOrDesc = ascOrDesc;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getAscOrDesc() {
		return ascOrDesc;
	}

	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}

	@Override
	public ColumnIndexInfo clone() throws CloneNotSupportedException {
		return (ColumnIndexInfo) super.clone();
	}

	@Override
	public String toString() {
		return "ColumnIndexInfo{" +
				"columnName='" + columnName + '\'' +
				", ascOrDesc='" + ascOrDesc + '\'' +
				'}';
	}
}
