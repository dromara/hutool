/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.handler;

import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.handler.row.EntityRowHandler;

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
	public static EntitySetHandler of() {
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
	public EntitySetHandler(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public LinkedHashSet<Entity> handle(final ResultSet rs) throws SQLException {
		final EntityRowHandler rowHandler =
			new EntityRowHandler(rs.getMetaData(), caseInsensitive, true);

		final LinkedHashSet<Entity> result = new LinkedHashSet<>();
		while (rs.next()){
			result.add(rowHandler.handle(rs));
		}

		return result;
	}
}
