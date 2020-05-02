package cn.hutool.db.handler;

import cn.hutool.db.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果集处理类 ，处理出的结果为Entity列表
 * @author loolly
 *
 */
public class EntityListHandler implements RsHandler<List<Entity>>{
	private static final long serialVersionUID = -2846240126316979895L;
	
	/** 是否大小写不敏感 */
	private final boolean caseInsensitive;
	
	/**
	 * 创建一个 EntityListHandler对象
	 * @return EntityListHandler对象
	 */
	public static EntityListHandler create() {
		return new EntityListHandler();
	}
	
	/**
	 * 构造
	 */
	public EntityListHandler() {
		this(false);
	}
	
	/**
	 * 构造
	 * 
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public EntityListHandler(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public List<Entity> handle(ResultSet rs) throws SQLException {
		return HandleHelper.handleRs(rs, new ArrayList<>(), this.caseInsensitive);
	}
}
