package cn.hutool.db.handler;

import cn.hutool.db.Entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Entity对象处理器，只处理第一条数据
 * 
 * @author loolly
 *
 */
public class EntityHandler implements RsHandler<Entity>{
	private static final long serialVersionUID = -8742432871908355992L;

	/** 是否大小写不敏感 */
	private final boolean caseInsensitive;

	/**
	 * 创建一个 EntityHandler对象
	 * @return EntityHandler对象
	 */
	public static EntityHandler create() {
		return new EntityHandler();
	}

	/**
	 * 构造
	 */
	public EntityHandler() {
		this(false);
	}

	/**
	 * 构造
	 *
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public EntityHandler(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public Entity handle(ResultSet rs) throws SQLException {
		final ResultSetMetaData  meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		
		return rs.next() ? HandleHelper.handleRow(columnCount, meta, rs, this.caseInsensitive) : null;
	}
}
