package com.xiaoleilu.hutool.db;

import java.util.ArrayList;

import com.xiaoleilu.hutool.util.PageUtil;

/**
 * 分页数据结果集
 * @author Looly
 *
 * @param <T> 结果集项的类型
 */
public class PageResult<T> extends ArrayList<T>{
	private static final long serialVersionUID = 9056411043515781783L;
	
	public static final int DEFAULT_NUMBER_PER_PAGE = 20;
	
	/** 页码 */
	private int page;
	/** 每页结果数 */
	private int numPerPage;
	/** 总页数 */
	private int totalPage;
	/** 总数 */
	private int total;
	
	//---------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param page 页码
	 * @param numPerPage 每页结果数
	 */
	public PageResult(int page, int numPerPage) {
		super(numPerPage <= 0 ? DEFAULT_NUMBER_PER_PAGE : numPerPage);
		
		this.page = page <= 0 ? 0 : page;
		this.numPerPage = numPerPage <= 0 ? DEFAULT_NUMBER_PER_PAGE : numPerPage;
	}
	
	/**
	 * 构造
	 * @param page 页码
	 * @param numPerPage 每页结果数
	 * @param total 结果总数
	 */
	public PageResult(int page, int numPerPage, int total) {
		this(page, numPerPage);
		
		this.total = total;
		this.totalPage = PageUtil.totalPage(total,numPerPage);
	}
	//---------------------------------------------------------- Constructor end
	
	//---------------------------------------------------------- Getters and Setters start
	/**
	 * @return 页码
	 */
	public int getPage() {
		return page;
	}
	/**
	 * 设置页码
	 * @param page 页码
	 */
	public void setPage(int page) {
		this.page = page;
	}
	
	/**
	 * @return 每页结果数
	 */
	public int getNumPerPage() {
		return numPerPage;
	}
	/**
	 * 设置每页结果数
	 * @param numPerPage 每页结果数
	 */
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	
	/**
	 * @return 总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}
	/**
	 * 设置总页数
	 * @param totalPage 总页数
	 */
	public void setTotalPage(int totalPage) {
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
	 * @param total 总数
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	//---------------------------------------------------------- Getters and Setters end
	
	/**
	 * @return 是否第一页
	 */
	public boolean isFirst(){
		return this.page == 0;
	}
	
	/**
	 * @return 是否最后一页
	 */
	public boolean isLast() {
		return this.page >= this.totalPage;
	}
}
