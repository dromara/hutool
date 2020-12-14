package cn.hutool.db.handler;

import cn.hutool.db.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

/**
 * 结果集处理类 ，处理出的结果为Entity列表，结果不能重复（按照Entity对象去重）
 * @author loolly
 *
 */
public class EntitySetHandler implements RsHandler<LinkedHashSet<Entity>>{
	private static final long serialVersionUID = 8191723216703506736L;

	/** 是否大小写不敏感 */
	private final boolean caseInsensitive;

	/**
	 * 创建一个 EntityHandler对象
	 * @return EntityHandler对象
	 */
	public static EntitySetHandler create() {
		return new EntitySetHandler();
	}

	/**
	 * 构造
	 */
	public EntitySetHandler() {
		this(false);
	}

	/**
	 * 构造
	 *
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public EntitySetHandler(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public LinkedHashSet<Entity> handle(ResultSet rs) throws SQLException {
		return HandleHelper.handleRs(rs, new LinkedHashSet<>(), this.caseInsensitive);
	}
}
