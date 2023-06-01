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
