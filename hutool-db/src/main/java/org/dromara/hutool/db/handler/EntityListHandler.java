/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.handler;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.handler.row.EntityRowHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * 结果集处理类 ，处理出的结果为Entity列表
 *
 * @author loolly
 */
public class EntityListHandler implements RsHandler<List<Entity>> {
	private static final long serialVersionUID = -2846240126316979895L;

	/**
	 * 是否大小写不敏感
	 */
	private final boolean caseInsensitive;

	/**
	 * 创建一个 EntityListHandler对象，默认大小写敏感
	 *
	 * @return EntityListHandler对象
	 */
	public static EntityListHandler of() {
		return new EntityListHandler();
	}

	/**
	 * 构造，默认大小写敏感
	 */
	public EntityListHandler() {
		this(false);
	}

	/**
	 * 构造
	 *
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public EntityListHandler(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public List<Entity> handle(final ResultSet rs) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final EntityRowHandler rowHandler = new EntityRowHandler(meta, caseInsensitive, true);

		final List<Entity> result = ListUtil.of();
		while (rs.next()) {
			result.add(rowHandler.handle(rs));
		}

		return result;
	}
}
