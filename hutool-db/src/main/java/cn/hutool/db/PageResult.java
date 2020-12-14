package cn.hutool.db;

import cn.hutool.core.util.PageUtil;

import java.util.ArrayList;

/**
 * 分页数据结果集
 *
 * @param <T> 结果集项的类型
 * @author Looly
 */
public class PageResult<T> extends ArrayList<T> {
	private static final long serialVersionUID = 9056411043515781783L;

	public static final int DEFAULT_PAGE_SIZE = Page.DEFAULT_PAGE_SIZE;

	/**
	 * 页码，0表示第一页
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
	public PageResult(int page, int pageSize) {
		super(pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize);

		this.page = Math.max(page, 0);
		this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	/**
	 * 构造
	 *
	 * @param page     页码，0表示第一页
	 * @param pageSize 每页结果数
	 * @param total    结果总数
	 */
	public PageResult(int page, int pageSize, int total) {
		this(page, pageSize);

		this.total = total;
		this.totalPage = PageUtil.totalPage(total, pageSize);
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
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return 每页结果数
	 * @deprecated 请使用{@link #getPageSize()}
	 */
	@Deprecated
	public int getNumPerPage() {
		return pageSize;
	}

	/**
	 * 设置每页结果数
	 *
	 * @param pageSize 每页结果数
	 * @deprecated 请使用 {@link #setPageSize(int)}
	 */
	@Deprecated
	public void setNumPerPage(int pageSize) {
		this.pageSize = pageSize;
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
	public void setPageSize(int pageSize) {
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
	 *
	 * @param total 总数
	 */
	public void setTotal(int total) {
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
