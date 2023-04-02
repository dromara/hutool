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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

	public BeanHandler(final Class<E> beanType) {
		this.elementBeanType = beanType;
	}

	@Override
	public E handle(final ResultSet rs) throws SQLException {
		final ResultSetMetaData  meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		return rs.next() ? ResultSetUtil.toBean(columnCount, meta, rs, this.elementBeanType) : null;
	}
}
