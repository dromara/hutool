package looly.github.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import looly.github.hutool.db.Entity;

/**
 * 数据结果集处理辅助类
 * @author loolly
 *
 */
public class HandleHelper {
	/**
	 * 处理单条数据
	 * @param columnCount 列数
	 * @param meta ResultSetMetaData
	 * @param rs 数据集
	 * @return 每一行的Map
	 * @throws SQLException
	 */
	public static Entity handleRow(int columnCount, ResultSetMetaData  meta, ResultSet rs) throws SQLException {
		final Entity row = new Entity();
		String columnLabel;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			row.put(columnLabel, rs.getObject(columnLabel));
		}
		return row;
	}
}
