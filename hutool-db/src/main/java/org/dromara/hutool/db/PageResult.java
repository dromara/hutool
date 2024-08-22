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

package org.dromara.hutool.db;

import org.dromara.hutool.core.lang.page.PageInfo;

import java.util.ArrayList;

/**
 * 分页数据结果集
 *
 * @param <T> 结果集项的类型
 * @author Looly
 */
public class PageResult<T> extends ArrayList<T> {
	private static final long serialVersionUID = 9056411043515781783L;

	/**
	 * 默认每页结果数
	 */
	public static final int DEFAULT_PAGE_SIZE = Page.DEFAULT_PAGE_SIZE;

	/**
	 * 页码
	 */
	private int page;
	/**
	 * 每页结果数
	 */
	private int pageSize;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 总数
	 */
	private int total;

	//---------------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public PageResult() {
		this(0, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param page     页码，0表示第一页
	 * @param pageSize 每页结果数
	 */
	public PageResult(final int page, final int pageSize) {
		super(pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize);

		this.page = Math.max(page, 0);
		this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	/**
	 * 构造
	 *
	 * @param page  分页对象
	 * @param total 结果总数
	 * @since 6.0.0
	 */
	public PageResult(final Page page, final int total) {
		this(page.getPageNumber(), page.getPageSize(), total);
	}

	/**
	 * 构造
	 *
	 * @param page     页码，0表示第一页
	 * @param pageSize 每页结果数
	 * @param total    结果总数
	 */
	public PageResult(final int page, final int pageSize, final int total) {
		this(page, pageSize);

		this.total = total;
		this.totalPage = PageInfo.of(total, pageSize).getPageCount();
	}
	//---------------------------------------------------------- Constructor end

	//---------------------------------------------------------- Getters and Setters start

	/**
	 * 页码，0表示第一页
	 *
	 * @return 页码，0表示第一页
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 设置页码，0表示第一页
	 *
	 * @param page 页码
	 */
	public void setPage(final int page) {
		this.page = page;
	}

	/**
	 * @return 每页结果数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页结果数
	 *
	 * @param pageSize 每页结果数
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return 总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 设置总页数
	 *
	 * @param totalPage 总页数
	 */
	public void setTotalPage(final int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return 总数
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置总数
	 *
	 * @param total 总数
	 */
	public void setTotal(final int total) {
		this.total = total;
	}
	//---------------------------------------------------------- Getters and Setters end

	/**
	 * @return 是否第一页
	 */
	public boolean isFirst() {
		return this.page == 0;
	}

	/**
	 * @return 是否最后一页
	 */
	public boolean isLast() {
		return this.page >= (this.totalPage - 1);
	}
}
