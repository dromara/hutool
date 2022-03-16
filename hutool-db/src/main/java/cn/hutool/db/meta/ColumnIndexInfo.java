package cn.hutool.db.meta;

import java.io.Serializable;

/**
 * 索引中的列信息
 *
 * @author huzhongying
 */
public class ColumnIndexInfo implements Serializable, Cloneable{

	/**
	 * 列名
	 */
	private String columnName;

	/**
	 * 列排序顺序，“A”: 升序，“D” : 降序，如果不支持排序顺序，可能为空
	 */
	private String ascOrDesc;

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
}
