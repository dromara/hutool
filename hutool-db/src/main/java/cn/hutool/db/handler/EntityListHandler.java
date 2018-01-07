package cn.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.db.Entity;

/**
 * 结果集处理类 ，处理出的结果为Entity列表
 * @author loolly
 *
 */
public class EntityListHandler implements RsHandler<List<Entity>>{
	
	/**
	 * 创建一个 EntityListHandler对象
	 * @return EntityListHandler对象
	 */
	public static EntityListHandler create() {
		return new EntityListHandler();
	}

	@Override
	public List<Entity> handle(ResultSet rs) throws SQLException {
		return HandleHelper.handleRs(rs, new ArrayList<Entity>());
	}
}
