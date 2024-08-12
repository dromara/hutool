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

import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.handler.row.EntityRowHandler;

import java.sql.ResultSet;
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
	public static EntityHandler of() {
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
	public EntityHandler(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public Entity handle(final ResultSet rs) throws SQLException {
		if(rs.next()){
			return new EntityRowHandler(rs.getMetaData(), this.caseInsensitive, true).handle(rs);
		}

		return null;
	}
}
