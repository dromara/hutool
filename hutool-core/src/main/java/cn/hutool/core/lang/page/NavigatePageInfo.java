package cn.hutool.core.lang.page;

/**
 * 导航分页信息类<br>
 * 根据提供的总页数、每页记录数、导航页码数等信息，生成导航数组。
 * <pre>{@code
 *     [1] 2 3 4 5 >>
 *     << 1 [2] 3 4 5 >>
 *     << 1 2 3 4 [5]
 * }</pre>
 *
 * @author 莫取网名
 */
public class NavigatePageInfo extends PageInfo {

	private final int navigatePageCount; //导航页码数
	private int[] navigatePageNumbers;  //所有导航页号

	/**
	 * 构造
	 *
	 * @param total             总记录数
	 * @param pageSize          每页显示记录数
	 * @param navigatePageCount 导航页码数
	 */
	public NavigatePageInfo(final int total, final int pageSize, final int navigatePageCount) {
		super(total, pageSize);
		this.navigatePageCount = navigatePageCount;

		//基本参数设定之后进行导航页面的计算
		calcNavigatePageNumbers();
	}

	/**
	 * 得到所有导航页号
	 *
	 * @return {int[]}
	 */
	public int[] getNavigatePageNumbers() {
		return navigatePageNumbers;
	}

	@Override
	public NavigatePageInfo setFirstPageNo(final int firstPageNo) {
		super.setFirstPageNo(firstPageNo);
		return this;
	}

	@Override
	public NavigatePageInfo setPageNo(final int pageNo) {
		super.setPageNo(pageNo);
		// 重新计算导航
		calcNavigatePageNumbers();
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();

		if (false == isFirstPage()) {
			str.append("<< ");
		}
		if (navigatePageNumbers.length > 0) {
			str.append(wrapForDisplay(navigatePageNumbers[0]));
		}
		for (int i = 1; i < navigatePageNumbers.length; i++) {
			str.append(" ").append(wrapForDisplay(navigatePageNumbers[i]));
		}
		if (false == isLastPage()) {
			str.append(" >>");
		}
		return str.toString();
	}

	// region ----- private methods

	/**
	 * 用于显示的包装<br>
	 * 当前页显示'[pageNumber]'，否则直接显示
	 *
	 * @param pageNumber 页码
	 * @return 包装的页码
	 */
	private String wrapForDisplay(final int pageNumber) {
		if (this.pageNo == pageNumber) {
			return "[" + pageNumber + "]";
		}
		return String.valueOf(pageNumber);
	}

	/**
	 * 计算导航页
	 */
	private void calcNavigatePageNumbers() {
		//当总页数小于或等于导航页码数时，全部显示
		if (pageCount <= navigatePageCount) {
			navigatePageNumbers = new int[pageCount];
			for (int i = 0; i < pageCount; i++) {
				navigatePageNumbers[i] = i + 1;
			}
		} else { //当总页数大于导航页码数时，部分显示
			navigatePageNumbers = new int[navigatePageCount];
			int startNum = pageNo - navigatePageCount / 2;
			int endNum = pageNo + navigatePageCount / 2;

			if (startNum < 1) {
				startNum = 1;
				//(最前navPageCount页
				for (int i = 0; i < navigatePageCount; i++) {
					navigatePageNumbers[i] = startNum++;
				}
			} else if (endNum > pageCount) {
				endNum = pageCount;
				//最后navPageCount页
				for (int i = navigatePageCount - 1; i >= 0; i--) {
					navigatePageNumbers[i] = endNum--;
				}
			} else {
				//所有中间页
				for (int i = 0; i < navigatePageCount; i++) {
					navigatePageNumbers[i] = startNum++;
				}
			}
		}
	}
	// endregion
}
