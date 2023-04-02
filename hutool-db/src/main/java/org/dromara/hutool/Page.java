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

package org.dromara.hutool;

import org.dromara.hutool.lang.Segment;
import org.dromara.hutool.lang.page.PageInfo;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.sql.Order;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 分页对象
 *
 * @author Looly
 */
public class Page implements Segment<Integer>, Serializable {
	private static final long serialVersionUID = 97792549823353462L;

	/**
	 * 默认
	 */
	public static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 页码，0表示第一页
	 */
	private int pageNumber;
	/**
	 * 每页结果数
	 */
	private int pageSize;
	/**
	 * 排序
	 */
	private Order[] orders;

	/**
	 * 创建Page对象
	 *
	 * @param pageNumber 页码，0表示第一页
	 * @param pageSize   每页结果数
	 * @return Page
	 * @since 5.5.3
	 */
	public static Page of(final int pageNumber, final int pageSize) {
		return new Page(pageNumber, pageSize);
	}

	// ---------------------------------------------------------- Constructor start

	/**
	 * 构造，默认第0页，每页{@value #DEFAULT_PAGE_SIZE} 条
	 *
	 * @since 4.5.16
	 */
	public Page() {
		this(0, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param pageNumber 页码，0表示第一页
	 * @param pageSize   每页结果数
	 */
	public Page(final int pageNumber, final int pageSize) {
		this.pageNumber = Math.max(pageNumber, 0);
		this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	/**
	 * 构造
	 *
	 * @param pageNumber 页码，0表示第一页
	 * @param pageSize   每页结果数
	 * @param order      排序对象
	 */
	public Page(final int pageNumber, final int pageSize, final Order order) {
		this(pageNumber, pageSize);
		this.orders = new Order[]{order};
	}
	// ---------------------------------------------------------- Constructor start

	// ---------------------------------------------------------- Getters and Setters start

	/**
	 * @return 页码，0表示第一页
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 设置页码，0表示第一页
	 *
	 * @param pageNumber 页码
	 */
	public void setPageNumber(final int pageNumber) {
		this.pageNumber = Math.max(pageNumber, 0);
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
		this.pageSize = (pageSize <= 0) ? DEFAULT_PAGE_SIZE : pageSize;
	}

	/**
	 * @return 排序
	 */
	public Order[] getOrders() {
		return this.orders;
	}

	/**
	 * 设置排序
	 *
	 * @param orders 排序
	 */
	public void setOrder(final Order... orders) {
		this.orders = orders;
	}

	/**
	 * 设置排序
	 *
	 * @param orders 排序
	 */
	public void addOrder(final Order... orders) {
		this.orders = ArrayUtil.append(this.orders, orders);
	}
	// ---------------------------------------------------------- Getters and Setters end

	/**
	 * @return 开始位置
	 * @see #getBeginIndex()
	 */
	public int getStartPosition() {
		return getBeginIndex();
	}

	@Override
	public Integer getBeginIndex() {
		return PageInfo.of(Integer.MAX_VALUE, this.pageSize)
				.setFirstPageNo(0).setPageNo(this.pageNumber).getBeginIndex();
	}

	/**
	 * @return 结束位置
	 * @see #getEndIndex()
	 */
	public int getEndPosition() {
		return getEndIndex();
	}

	@Override
	public Integer getEndIndex() {
		return PageInfo.of(Integer.MAX_VALUE, this.pageSize).setFirstPageNo(0).getEndIndex();
	}

	/**
	 * 开始位置和结束位置<br>
	 * 例如：
	 *
	 * <pre>
	 * 页码：0，每页10 =》 [0, 10]
	 * 页码：1，每页10 =》 [10, 20]
	 * 页码：2，每页10 =》 [21, 30]
	 * 。。。
	 * </pre>
	 *
	 * @return 第一个数为开始位置，第二个数为结束位置
	 */
	public int[] getStartEnd() {
		final PageInfo pageInfo = PageInfo.of(Integer.MAX_VALUE, this.pageSize)
				.setFirstPageNo(0).setPageNo(this.pageNumber);
		return new int[]{pageInfo.getBeginIndex(), pageInfo.getEndIndexExclude()};
	}

	@Override
	public String toString() {
		return "Page [page=" + pageNumber + ", pageSize=" + pageSize + ", order=" + Arrays.toString(orders) + "]";
	}
}
