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
