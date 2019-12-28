package cn.hutool.core.util;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 分页工具类
 * 
 * @author xiaoleilu
 * 
 */
public class PageUtil {

	/**
	 * 将页数和每页条目数转换为开始位置<br>
	 * 此方法用于不包括结束位置的分页方法<br>
	 * 例如：
	 * 
	 * <pre>
	 * 页码：1，每页10 =》 0
	 * 页码：2，每页10 =》 10
	 * ……
	 * </pre>
	 * 
	 * @param pageNo 页码（从1计数）
	 * @param pageSize 每页条目数
	 * @return 开始位置
	 */
	public static int getStart(int pageNo, int pageSize) {
		if (pageNo < 1) {
			pageNo = 1;
		}

		if (pageSize < 1) {
			pageSize = 0;
		}

		return (pageNo - 1) * pageSize;
	}

	/**
	 * 将页数和每页条目数转换为开始位置和结束位置<br>
	 * 此方法用于包括结束位置的分页方法<br>
	 * 例如：
	 * 
	 * <pre>
	 * 页码：1，每页10 =》 [0, 10]
	 * 页码：2，每页10 =》 [10, 20]
	 * ……
	 * </pre>
	 * 
	 * @param pageNo 页码（从1计数）
	 * @param pageSize 每页条目数
	 * @return 第一个数为开始位置，第二个数为结束位置
	 */
	public static int[] transToStartEnd(int pageNo, int pageSize) {
		final int start = getStart(pageNo, pageSize);
		if (pageSize < 1) {
			pageSize = 0;
		}
		final int end = start + pageSize;

		return new int[] { start, end };
	}

	/**
	 * 根据总数计算总页数
	 * 
	 * @param totalCount 总数
	 * @param pageSize 每页数
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
	 * @param currentPage 当前页
	 * @param pageCount 总页数
	 * @param displayCount 每屏展示的页数
	 * @return 分页条
	 */
	public static int[] rainbow(int currentPage, int pageCount, int displayCount) {
		boolean isEven = displayCount % 2 == 0;
		int left = displayCount / 2;
		int right = displayCount / 2;

		int length = displayCount;
		if (isEven) {
			right++;
		}
		if (pageCount < displayCount) {
			length = pageCount;
		}
		int[] result = new int[length];
		if (pageCount >= displayCount) {
			if (currentPage <= left) {
				result = IntStream.range(0, length).map(i -> i + 1).toArray();
			} else if (currentPage > pageCount - right) {
				Arrays.setAll(result, i -> i + pageCount - displayCount + 1);
			} else {
				Arrays.setAll(result, i -> i + currentPage - left + (isEven ? 1 : 0));
			}
		} else {
			Arrays.setAll(result, i -> i + 1);
		}
		return result;

	}

	/**
	 * 分页彩虹算法(默认展示10页)<br>
	 * 来自：https://github.com/iceroot/iceroot/blob/master/src/main/java/com/icexxx/util/IceUtil.java
	 * 
	 * @param currentPage 当前页
	 * @param pageCount 总页数
	 * @return 分页条
	 */
	public static int[] rainbow(int currentPage, int pageCount) {
		return rainbow(currentPage, pageCount, 10);
	}
}
