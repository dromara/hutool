/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
