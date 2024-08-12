/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
