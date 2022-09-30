package cn.hutool.core.comparator;

import java.util.Comparator;

/**
 * 字符串长度比较器，短在前
 *
 * @author looly
 * @since 5.8.9
 */
public class StrLengthComparator implements Comparator<CharSequence> {
	/**
	 * 单例的字符串长度比较器，短在前
	 */
	public static final StrLengthComparator INSTANCE = new StrLengthComparator();

	@Override
	public int compare(final CharSequence o1, final CharSequence o2) {
		int result = Integer.compare(o1.length(), o2.length());
		if (0 == result) {
			result = CompareUtil.compare(o1.toString(), o2.toString());
		}
		return result;
	}
}
