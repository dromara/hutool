package cn.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果集处理类 ，处理出的结果为List列表
 * @author loolly
 *
 */
public class ValueListHandler implements RsHandler<List<List<Object>>>{
	private static final long serialVersionUID = 1L;

	/**
	 * 创建一个 EntityListHandler对象
	 * @return EntityListHandler对象
	 */
	public static ValueListHandler create() {
		return new ValueListHandler();
	}

	@Override
	public List<List<Object>> handle(ResultSet rs) throws SQLException {
		final ArrayList<List<Object>> result = new ArrayList<>();
		while (rs.next()) {
			result.add(HandleHelper.handleRowToList(rs));
		}
		return result;
	}
}
