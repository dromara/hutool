package cn.hutool.core.math;

public class NavigatePageInfo extends PageInfo{

	private final int navigatePages = 8; //导航页码数
	private int[] navigatePageNumbers;  //所有导航页号

	public NavigatePageInfo(final int total, final int pageSize) {
		super(total, pageSize);

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

	public String toString() {
		final StringBuilder str = new StringBuilder(super.toString());
		str.append(", {navigatePageNumbers=");
		final int len = navigatePageNumbers.length;
		if (len > 0) str.append(navigatePageNumbers[0]);
		for (int i = 1; i < len; i++) {
			str.append(" ").append(navigatePageNumbers[i]);
		}
		str.append("}");
		return str.toString();
	}

	/**
	 * 计算导航页
	 */
	private void calcNavigatePageNumbers() {
		//当总页数小于或等于导航页码数时
		if (pages <= navigatePages) {
			navigatePageNumbers = new int[pages];
			for (int i = 0; i < pages; i++) {
				navigatePageNumbers[i] = i + 1;
			}
		} else { //当总页数大于导航页码数时
			navigatePageNumbers = new int[navigatePages];
			int startNum = pageNo - navigatePages / 2;
			int endNum = pageNo + navigatePages / 2;

			if (startNum < 1) {
				startNum = 1;
				//(最前navPageCount页
				for (int i = 0; i < navigatePages; i++) {
					navigatePageNumbers[i] = startNum++;
				}
			} else if (endNum > pages) {
				endNum = pages;
				//最后navPageCount页
				for (int i = navigatePages - 1; i >= 0; i--) {
					navigatePageNumbers[i] = endNum--;
				}
			} else {
				//所有中间页
				for (int i = 0; i < navigatePages; i++) {
					navigatePageNumbers[i] = startNum++;
				}
			}
		}
	}
}
