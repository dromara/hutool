package cn.hutool.core.lang.page;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.DefaultSegment;
import cn.hutool.core.lang.Segment;

/**
 * 分页信息，通过提供的总数、页码、每页记录数等信息，计算总页数等信息<br>
 * 来自：https://bbs.csdn.net/topics/360010907
 *
 * @author 莫取网名
 */
public class PageInfo {

	private static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 创建{@code PageInfo}，默认当前页是1
	 *
	 * @param total    总记录数
	 * @param pageSize 每页显示记录数
	 * @return {@code PageInfo}
	 */
	public static PageInfo of(final int total, final int pageSize) {
		return new PageInfo(total, pageSize);
	}

	/**
	 * 总记录数
	 */
	int total;
	/**
	 * 每页显示记录数
	 */
	int pageSize;
	/**
	 * 总页数
	 */
	int pages;
	/**
	 * 首页标识
	 */
	int firstPageNo = 1;
	/**
	 * 当前页
	 */
	int pageNo = firstPageNo;

	/**
	 * 构造
	 *
	 * @param total    总记录数
	 * @param pageSize 每页显示记录数
	 */
	public PageInfo(final int total, final int pageSize) {
		init(total, pageSize);
	}

	/**
	 * 初始化
	 *
	 * @param total    总记录数
	 * @param pageSize 每页显示记录数
	 */
	private void init(final int total, int pageSize) {
		Assert.isTrue(total >= 0, "Total must >= 0");

		//设置基本参数
		this.total = total;

		if (pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
		// 因为总条数除以页大小的最大余数是页大小数-1，
		// 因此加一个最大余数，保证舍弃的余数与最大余数凑1.x，就是一旦有余数则+1页
		this.pages = (total + pageSize - 1) / pageSize;
	}

	/**
	 * 得到记录总数
	 *
	 * @return {int}
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 得到每页显示多少条记录
	 *
	 * @return {int}
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 得到页面总数
	 *
	 * @return {int}
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * 得到当前页号
	 *
	 * @return {int}
	 */
	public int getPageNo() {
		return pageNo;
	}


	/**
	 * 是否首页
	 *
	 * @return 是否首页
	 */
	public boolean isFirstPage() {
		return getPageIndexBase1() == 1;
	}

	/**
	 * 是否尾页
	 *
	 * @return 是否尾页
	 */
	public boolean isLastPage() {
		return getPageIndexBase1() == this.pages;
	}

	/**
	 * 是否有前一页
	 *
	 * @return 是否有前一页
	 */
	public boolean hasPreviousPage() {
		return getPageIndexBase1() > 1;
	}

	/**
	 * 是否有下一页
	 *
	 * @return 是否有下一页
	 */
	public boolean hasNextPage() {
		return getBeginIndex() < this.pages;
	}

	/**
	 * 当前页是否可用（是否大于firstPageNum且小于总页数）
	 * @return 是否可用
	 */
	public boolean isValidPage(){
		return this.getPageIndexBase1() <= this.pages;
	}

	/**
	 * 获取当前页的开始记录index（包含）
	 *
	 * @return 开始记录index（包含）
	 */
	public int getBeginIndex() {
		return (getPageIndexBase1() -1) * this.pageSize;
	}

	/**
	 * 获取当前页的结束记录index（不包含）
	 * <ul>
	 *     <li>当开始index超出total时，返回正常值</li>
	 *     <li>当开始index未超出total但是计算的end超出时，返回total + 1</li>
	 *     <li>当开始index和end都未超出时，返回正常值</li>
	 * </ul>
	 *
	 * @return 结束记录index（不包含）
	 */
	public int getEndIndexExclude() {
		return getEndIndex() + 1;
	}

	/**
	 * 获取当前页的结束记录index（包含）
	 * <ul>
	 *     <li>当开始index超出total时，返回正常值</li>
	 *     <li>当开始index未超出total但是计算的end超出时，返回total</li>
	 *     <li>当开始index和end都未超出时，返回正常值</li>
	 * </ul>
	 *
	 * @return 结束记录index（包含）
	 */
	public int getEndIndex() {
		final int begin = getBeginIndex();
		int end = begin + this.pageSize - 1;
		if (begin <= this.total && end > this.total) {
			end = this.total;
		}
		return end;
	}

	/**
	 * 将页数和每页条目数转换为开始位置和结束位置<br>
	 * 此方法用于包括结束位置的分页方法<br>
	 * 例如：
	 * <pre>
	 * 页码：1，每页10 =》 [0, 9]
	 * 页码：2，每页10 =》 [10, 19]
	 * ……
	 * </pre>
	 *
	 * @return {@link Segment}
	 */
	public Segment<Integer> getSegment() {
		return new DefaultSegment<>(getBeginIndex(), getEndIndex());
	}

	/**
	 * 获取设置首页编号，即以数字几为第一页标志
	 *
	 * @return 设置首页编号
	 */
	public int getFirstPageNo() {
		return this.firstPageNo;
	}

	/**
	 * 设置首页编号，即以数字几为第一页标志<br>
	 * 如设置0，则0表示第一页，1表示第二页<br>
	 * 设置此参数后，须调用{@link #setPageNo(int)} 重新设置当前页的页码
	 *
	 * @param firstPageNo 首页编号
	 * @return this
	 */
	public PageInfo setFirstPageNo(final int firstPageNo) {
		this.firstPageNo = firstPageNo;
		return this;
	}

	/**
	 * 设置当前页码，具体这个页码代表实际页，取决于{@link #setFirstPageNo(int)}设置的值。
	 * 例如当{@link #setFirstPageNo(int)}设置为1时，1表示首页；设置为0时，0表示首页，依次类推。<br>
	 * 当设置页码小于{@link #getFirstPageNo()}值时，始终为{@link #getFirstPageNo()}
	 *
	 * @param pageNo 当前页码
	 * @return this
	 */
	public PageInfo setPageNo(final int pageNo) {
		//根据输入可能错误的当前号码进行自动纠正
		// 不判断后边界，因为当页码超出边界，应该返回一个空区间的数据，而非始终保持在最后一页
		this.pageNo = Math.max(pageNo, firstPageNo);
		return this;
	}

	/**
	 * 下一页，即当前页码+1<br>
	 * 当超过末页时，此方法指向的页码值始终为{@link #getPages()} + 1，即最后一页后的空白页。
	 *
	 * @return this
	 */
	public PageInfo nextPage() {
		return setPageNo(this.pageNo + 1);
	}

	/**
	 * 上一页，即当前页码-1,直到第一页则始终为第一页
	 *
	 * @return this
	 */
	public PageInfo previousPage() {
		return setPageNo(this.pageNo - 1);
	}

	public String toString() {
		return "{" +
				"total=" + total +
				",pages=" + pages +
				",pageNumber=" + pageNo +
				",limit=" + pageSize +
				",isFirstPage=" + isFirstPage() +
				",isLastPage=" + isLastPage() +
				",hasPreviousPage=" + hasPreviousPage() +
				",hasNextPage=" + hasNextPage() +
				"}";
	}

	/**
	 * 获取页码序号，第一个序号就是1
	 * @return 页码序号
	 */
	private int getPageIndexBase1(){
		return this.pageNo - this.firstPageNo + 1;
	}
}
