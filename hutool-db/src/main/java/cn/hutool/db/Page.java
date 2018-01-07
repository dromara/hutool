package cn.hutool.db;

import java.util.Arrays;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.db.sql.Order;

/**
 * 分页对象
 * @author Looly
 *
 */
public class Page {
	
	public static final int DEFAULT_PAGE_SIZE = 20;

	/** 页码 */
	private int pageNumber;
	/** 每页结果数 */
	private int pageSize;
	/** 排序 */
	private Order[] orders;

	// ---------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param pageNumber 页码
	 * @param pageSize 每页结果数
	 */
	public Page(int pageNumber, int pageSize) {
		this.pageNumber = pageNumber < 0 ? 0 : pageNumber;
		this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
	}
	
	/**
	 * 构造
	 * 
	 * @param pageNumber 页码
	 * @param numPerPage 每页结果数
	 * @param order 排序对象
	 */
	public Page(int pageNumber, int numPerPage, Order order) {
		this(pageNumber, numPerPage);
		this.orders = new Order[]{order};
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
		this.pageNumber = pageNumber < 0 ? 0 : pageNumber;
	}

	/**
	 * @return 每页结果数
	 * @deprecated 使用 {@link #getPageSize()} 代替
	 */
	@Deprecated
	public int getNumPerPage() {
		return getPageSize();
	}

	/**
	 * 设置每页结果数
	 * 
	 * @param pageSize 每页结果数
	 * @deprecated 使用 {@link #setPageSize(int)} 代替
	 */
	@Deprecated
	public void setNumPerPage(int pageSize) {
		setPageSize(pageSize);
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
		this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
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
	public void setOrder(Order... orders) {
		this.orders = orders;
	}
	
	/**
	 * 设置排序
	 * 
	 * @param orders 排序
	 */
	public void addOrder(Order... orders) {
		if(null != this.orders){
			ArrayUtil.append(this.orders, orders);
		}
		this.orders = orders;
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
		return getStartEnd()[1];
	}

	/**
	 * 开始位置和结束位置<br>
	 * 例如：<br>
	 * 页码：1，每页10 =》 [0, 10]<br>
	 * 页码：2，每页10 =》 [10, 20]<br>
	 * 。。。<br>
	 * 
	 * @return 第一个数为开始位置，第二个数为结束位置
	 */
	public int[] getStartEnd() {
		return PageUtil.transToStartEnd(pageNumber, pageSize);
	}

	@Override
	public String toString() {
		return "Page [page=" + pageNumber + ", pageSize=" + pageSize + ", order=" + Arrays.toString(orders) + "]";
	}
}
