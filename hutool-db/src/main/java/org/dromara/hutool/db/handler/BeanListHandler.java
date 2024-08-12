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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果集处理类 ，处理出的结果为Bean列表
 *
 * @param <E> 处理对象类型
 * @author loolly
 * @since 3.1.0
 */
public class BeanListHandler<E> implements RsHandler<List<E>> {
	private static final long serialVersionUID = 4510569754766197707L;

	private final Class<E> elementBeanType;

	/**
	 * 创建一个 BeanListHandler对象
	 *
	 * @param <E> 处理对象类型
	 * @param beanType Bean类型
	 * @return BeanListHandler对象
	 */
	public static <E> BeanListHandler<E> of(final Class<E> beanType) {
		return new BeanListHandler<>(beanType);
	}

	/**
	 * 构造
	 * @param beanType Bean类型
	 */
	public BeanListHandler(final Class<E> beanType) {
		this.elementBeanType = beanType;
	}

	@Override
	public List<E> handle(final ResultSet rs) throws SQLException {
		return ResultSetUtil.toBeanList(rs, new ArrayList<>(), elementBeanType);
	}
}
