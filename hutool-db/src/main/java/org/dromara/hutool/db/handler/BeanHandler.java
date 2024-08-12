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

import org.dromara.hutool.db.handler.row.BeanRowHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Bean对象处理器，只处理第一条数据
 *
 * @param <E> 处理对象类型
 * @author loolly
 *@since 3.1.0
 */
public class BeanHandler<E> implements RsHandler<E>{
	private static final long serialVersionUID = -5491214744966544475L;

	private final Class<E> elementBeanType;

	/**
	 * 创建一个 BeanHandler对象
	 *
	 * @param <E> 处理对象类型
	 * @param beanType Bean类型
	 * @return BeanHandler对象
	 */
	public static <E> BeanHandler<E> of(final Class<E> beanType) {
		return new BeanHandler<>(beanType);
	}

	/**
	 * 构造
	 * @param beanType Bean类型
	 */
	public BeanHandler(final Class<E> beanType) {
		this.elementBeanType = beanType;
	}

	@Override
	public E handle(final ResultSet rs) throws SQLException {
		if(rs.next()){
			return new BeanRowHandler<>(rs.getMetaData(), elementBeanType, true).handle(rs);
		}
		return null;
	}
}
