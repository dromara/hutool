package com.xiaoleilu.hutool.db;

import com.xiaoleilu.hutool.PageUtil;
import com.xiaoleilu.hutool.db.sql.Order;

public class Page {

	/** 页码 */
	private int pageNumber;
	/** 每页结果数 */
	private int numPerPage;
	/** 排序 */
	private Order order;

	// ---------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param pageNumber 页码
	 * @param numPerPage 每页结果数
	 */
	public Page(int pageNumber, int numPerPage) {
		this.pageNumber = pageNumber;
		this.numPerPage = numPerPage;
	}
	// ---------------------------------------------------------- Constructor start

	// ---------------------------------------------------------- Getters and Setters start
	/**
	 * @return 页码
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 设置页码
	 * @param pageNumber 页码
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return 每页结果数
	 */
	public int getNumPerPage() {
		return numPerPage;
	}

	/**
	 * 设置每页结果数
	 * 
	 * @param numPerPage 每页结果数
	 */
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	/**
	 * @return 排序
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order 排序
	 */
	public void setOrder(Order order) {
		this.order = order;
	}
	// ---------------------------------------------------------- Getters and Setters end

	/**
	 * @return 开始位置
	 */
	public int getStartPosition() {
		return getStartEnd()[0];
	}

	/**
	 * @return 结束位置
	 */
	public int getEndPosition() {
		return getStartEnd()[0];
	}

	/**
	 * 开始位置和结束位置<br>
	 * 例如：<br>
	 * 页码：1，每页10 -> [0, 10]<br>
	 * 页码：2，每页10 -> [10, 20]<br>
	 * 。。。<br>
	 * 
	 * @return 第一个数为开始位置，第二个数为结束位置
	 */
	public int[] getStartEnd() {
		return PageUtil.transToStartEnd(pageNumber, numPerPage);
	}

	@Override
	public String toString() {
		return "Page [page=" + pageNumber + ", numPerPage=" + numPerPage + ", order=" + order + "]";
	}
}
