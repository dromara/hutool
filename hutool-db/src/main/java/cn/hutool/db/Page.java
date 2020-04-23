package cn.hutool.db;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.db.sql.Order;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 分页对象
 * 
 * @author Looly
 *
 */
public class Page implements Serializable {
	private static final long serialVersionUID = 97792549823353462L;

	public static final int DEFAULT_PAGE_SIZE = 20;

	/** 页码，0表示第一页 */
	private int pageNumber;
	/** 每页结果数 */
	private int pageSize;
	/** 排序 */
	private Order[] orders;

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
	 * @param pageSize 每页结果数
	 */
	public Page(int pageNumber, int pageSize) {
		this.pageNumber = Math.max(pageNumber, 0);
		this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	/**
	 * 构造
	 * 
	 * @param pageNumber 页码，0表示第一页
	 * @param pageSize 每页结果数
	 * @param order 排序对象
	 */
	public Page(int pageNumber, int pageSize, Order order) {
		this(pageNumber, pageSize);
		this.orders = new Order[] { order };
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
	public void setPageNumber(int pageNumber) {
		this.pageNumber = Math.max(pageNumber, 0);
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
	public void setOrder(Order... orders) {
		this.orders = orders;
	}

	/**
	 * 设置排序
	 * 
	 * @param orders 排序
	 */
	public void addOrder(Order... orders) {
		this.orders = ArrayUtil.append(this.orders, orders);
	}
	// ---------------------------------------------------------- Getters and Setters end

	/**
	 * @return 开始位置
	 */
	public int getStartPosition() {
		return PageUtil.getStart(this.pageNumber, this.pageSize);
	}

	/**
	 * @return 结束位置
	 */
	public int getEndPosition() {
		return PageUtil.getEnd(this.pageNumber, this.pageSize);
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
		return PageUtil.transToStartEnd(pageNumber, pageSize);
	}

	@Override
	public String toString() {
		return "Page [page=" + pageNumber + ", pageSize=" + pageSize + ", order=" + Arrays.toString(orders) + "]";
	}
}
