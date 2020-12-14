package cn.hutool.core.util;

/**
 * 分页工具类
 *
 * @author xiaoleilu
 */
public class PageUtil {

	private static int firstPageNo = 0;

	/**
	 * 获得首页的页码，可以为0或者1
	 *
	 * @return 首页页码
	 */
	public static int getFirstPageNo() {
		return firstPageNo;
	}

	/**
	 * 设置首页页码，可以为0或者1
	 *
	 * <pre>
	 *     当设置为0时，页码0表示第一页，开始位置为0
	 *     当设置为1时，页码1表示第一页，开始位置为0
	 * </pre>
	 *
	 * @param customFirstPageNo 自定义的首页页码，为0或者1
	 */
	public static void setFirstPageNo(int customFirstPageNo) {
		firstPageNo = customFirstPageNo;
	}

	/**
	 * 设置首页页码为1
	 *
	 * <pre>
	 *     当设置为1时，页码1表示第一页，开始位置为0
	 * </pre>
	 */
	public static void setOneAsFirstPageNo() {
		setFirstPageNo(1);
	}

	/**
	 * 将页数和每页条目数转换为开始位置<br>
	 * 此方法用于不包括结束位置的分页方法<br>
	 * 例如：
	 *
	 * <pre>
	 * 页码：0，每页10 =》 0
	 * 页码：1，每页10 =》 10
	 * ……
	 * </pre>
	 *
	 * <p>
	 * 当{@link #setFirstPageNo(int)}设置为1时：
	 * <pre>
	 * 页码：1，每页10 =》 0
	 * 页码：2，每页10 =》 10
	 * ……
	 * </pre>
	 *
	 * @param pageNo   页码（从0计数）
	 * @param pageSize 每页条目数
	 * @return 开始位置
	 */
	public static int getStart(int pageNo, int pageSize) {
		if (pageNo < firstPageNo) {
			pageNo = firstPageNo;
		}

		if (pageSize < 1) {
			pageSize = 0;
		}

		return (pageNo - firstPageNo) * pageSize;
	}

	/**
	 * 将页数和每页条目数转换为结束位置<br>
	 * 此方法用于不包括结束位置的分页方法<br>
	 * 例如：
	 *
	 * <pre>
	 * 页码：0，每页10 =》 9
	 * 页码：1，每页10 =》 19
	 * ……
	 * </pre>
	 *
	 * <p>
	 * 当{@link #setFirstPageNo(int)}设置为1时：
	 * <pre>
	 * 页码：1，每页10 =》 9
	 * 页码：2，每页10 =》 19
	 * ……
	 * </pre>
	 *
	 * @param pageNo   页码（从0计数）
	 * @param pageSize 每页条目数
	 * @return 开始位置
	 * @since 5.2.5
	 */
	public static int getEnd(int pageNo, int pageSize) {
		final int start = getStart(pageNo, pageSize);
		return getEndByStart(start, pageSize);
	}

	/**
	 * 将页数和每页条目数转换为开始位置和结束位置<br>
	 * 此方法用于包括结束位置的分页方法<br>
	 * 例如：
	 *
	 * <pre>
	 * 页码：0，每页10 =》 [0, 10]
	 * 页码：1，每页10 =》 [10, 20]
	 * ……
	 * </pre>
	 *
	 * <p>
	 * 当{@link #setFirstPageNo(int)}设置为1时：
	 * <pre>
	 * 页码：1，每页10 =》 [0, 10]
	 * 页码：2，每页10 =》 [10, 20]
	 * ……
	 * </pre>
	 *
	 * @param pageNo   页码（从0计数）
	 * @param pageSize 每页条目数
	 * @return 第一个数为开始位置，第二个数为结束位置
	 */
	public static int[] transToStartEnd(int pageNo, int pageSize) {
		final int start = getStart(pageNo, pageSize);
		return new int[]{start, getEndByStart(start, pageSize)};
	}

	/**
	 * 根据总数计算总页数
	 *
	 * @param totalCount 总数
	 * @param pageSize   每页数
	 * @return 总页数
	 */
	public static int totalPage(int totalCount, int pageSize) {
		if (pageSize == 0) {
			return 0;
		}
		return totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1);
	}

	/**
	 * 分页彩虹算法<br>
	 * 来自：https://github.com/iceroot/iceroot/blob/master/src/main/java/com/icexxx/util/IceUtil.java<br>
	 * 通过传入的信息，生成一个分页列表显示
	 *
	 * @param pageNo       当前页
	 * @param totalPage    总页数
	 * @param displayCount 每屏展示的页数
	 * @return 分页条
	 */
	public static int[] rainbow(int pageNo, int totalPage, int displayCount) {
		// displayCount % 2
		boolean isEven = (displayCount & 1) == 0;
		int left = displayCount >> 1;
		int right = displayCount >> 1;

		int length = displayCount;
		if (isEven) {
			right++;
		}
		if (totalPage < displayCount) {
			length = totalPage;
		}
		int[] result = new int[length];
		if (totalPage >= displayCount) {
			if (pageNo <= left) {
				for (int i = 0; i < result.length; i++) {
					result[i] = i + 1;
				}
			} else if (pageNo > totalPage - right) {
				for (int i = 0; i < result.length; i++) {
					result[i] = i + totalPage - displayCount + 1;
				}
			} else {
				for (int i = 0; i < result.length; i++) {
					result[i] = i + pageNo - left + (isEven ? 1 : 0);
				}
			}
		} else {
			for (int i = 0; i < result.length; i++) {
				result[i] = i + 1;
			}
		}
		return result;

	}

	/**
	 * 分页彩虹算法(默认展示10页)<br>
	 * 来自：https://github.com/iceroot/iceroot/blob/master/src/main/java/com/icexxx/util/IceUtil.java
	 *
	 * @param currentPage 当前页
	 * @param pageCount   总页数
	 * @return 分页条
	 */
	public static int[] rainbow(int currentPage, int pageCount) {
		return rainbow(currentPage, pageCount, 10);
	}

	//------------------------------------------------------------------------- Private method start

	/**
	 * 根据起始位置获取结束位置
	 *
	 * @param start    起始位置
	 * @param pageSize 每页条目数
	 * @return 结束位置
	 */
	private static int getEndByStart(int start, int pageSize) {
		if (pageSize < 1) {
			pageSize = 0;
		}
		return start + pageSize;
	}

	//------------------------------------------------------------------------- Private method end
}
