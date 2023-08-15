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

import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.PageResult;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 分页结果集处理类 ，处理出的结果为PageResult
 *
 * @param <T> Bean类型
 * @author loolly
 */
public class PageResultHandler<T> implements RsHandler<PageResult<T>> {
	private static final long serialVersionUID = 1L;

	private final Class<T> beanClass;
	private final PageResult<T> pageResult;
	/**
	 * 是否大小写不敏感
	 */
	private boolean caseInsensitive;

	/**
	 * 创建 PageResultHandler
	 *
	 * @param pageResult 分页结果集空对象
	 * @return BeanResultHandler
	 */
	public static PageResultHandler<Entity> of(final PageResult<Entity> pageResult) {
		return of(Entity.class, pageResult);
	}

	/**
	 * 创建 PageResultHandler
	 *
	 * @param <T>        Bean类型
	 * @param beanClass  bean类
	 * @param pageResult 分页结果集空对象
	 * @return PageResultHandler
	 */
	public static <T> PageResultHandler<T> of(final Class<T> beanClass, final PageResult<T> pageResult) {
		return new PageResultHandler<>(beanClass, pageResult);
	}

	/**
	 * 构造<br>
	 * 结果集根据给定的分页对象查询数据库，填充结果
	 *
	 * @param beanClass  bean类
	 * @param pageResult 分页结果集空对象
	 */
	public PageResultHandler(final Class<T> beanClass, final PageResult<T> pageResult) {
		this.beanClass = beanClass;
		this.pageResult = pageResult;
	}

	/**
	 * 设置是否忽略大小写
	 *
	 * @param caseInsensitive 是否忽略大小写
	 * @return this
	 */
	public PageResultHandler<T> setCaseInsensitive(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<T> handle(final ResultSet rs) throws SQLException {
		if (Entity.class == this.beanClass) {
			return (PageResult<T>) ResultSetUtil.toEntityList(
				rs, (PageResult<Entity>) pageResult, this.caseInsensitive);
		}
		return ResultSetUtil.toBeanList(rs, this.pageResult, this.beanClass);
	}
}
