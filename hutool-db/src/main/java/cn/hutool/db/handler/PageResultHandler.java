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

package cn.hutool.db.handler;

import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 分页结果集处理类 ，处理出的结果为PageResult
 *
 * @author loolly
 */
public class PageResultHandler implements RsHandler<PageResult<Entity>> {
	private static final long serialVersionUID = -1474161855834070108L;

	private final PageResult<Entity> pageResult;
	/**
	 * 是否大小写不敏感
	 */
	private final boolean caseInsensitive;

	/**
	 * 创建一个 EntityHandler对象<br>
	 * 结果集根据给定的分页对象查询数据库，填充结果
	 *
	 * @param pageResult 分页结果集空对象
	 * @return EntityHandler对象
	 */
	public static PageResultHandler of(final PageResult<Entity> pageResult) {
		return new PageResultHandler(pageResult);
	}

	/**
	 * 构造<br>
	 * 结果集根据给定的分页对象查询数据库，填充结果
	 *
	 * @param pageResult 分页结果集空对象
	 */
	public PageResultHandler(final PageResult<Entity> pageResult) {
		this(pageResult, false);
	}

	/**
	 * 构造<br>
	 * 结果集根据给定的分页对象查询数据库，填充结果
	 *
	 * @param pageResult      分页结果集空对象
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public PageResultHandler(final PageResult<Entity> pageResult, final boolean caseInsensitive) {
		this.pageResult = pageResult;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public PageResult<Entity> handle(final ResultSet rs) throws SQLException {
		return ResultSetUtil.toEntityList(rs, pageResult, this.caseInsensitive);
	}
}
