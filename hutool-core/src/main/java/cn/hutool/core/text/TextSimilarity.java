package cn.hutool.core.text;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 文本相似度计算<br>
 * 工具类提供者：【杭州】fineliving
 *
 * @author fanqun
 * @since 3.2.3
 **/
public class TextSimilarity {

	/**
	 * 计算相似度，两个都是空串相似度为1，被认为是相同的串<br>
	 * 比较方法为：
	 * <ul>
	 *     <li>只比较两个字符串字母、数字、汉字部分，其他符号去除</li>
	 *     <li>计算出两个字符串最大子串，除以最长的字符串，结果即为相似度</li>
	 * </ul>
	 *
	 * @param strA 字符串1
	 * @param strB 字符串2
	 * @return 相似度
	 */
	public static double similar(String strA, String strB) {
		String newStrA, newStrB;
		if (strA.length() < strB.length()) {
			newStrA = removeSign(strB);
			newStrB = removeSign(strA);
		} else {
			newStrA = removeSign(strA);
			newStrB = removeSign(strB);
		}

		// 用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
		int temp = Math.max(newStrA.length(), newStrB.length());
		if(0 == temp) {
			// 两个都是空串相似度为1，被认为是相同的串
			return 1;
		}

		final int commonLength = longestCommonSubstringLength(newStrA, newStrB);
		return NumberUtil.div(commonLength, temp);
	}

	/**
	 * 计算相似度百分比
	 *
	 * @param strA 字符串1
	 * @param strB 字符串2
	 * @param scale 保留小数
	 * @return 百分比
	 */
	public static String similar(String strA, String strB, int scale) {
		return NumberUtil.formatPercent(similar(strA, strB), scale);
	}

	/**
	 * 最长公共子串，采用动态规划算法。 其不要求所求得的字符在所给的字符串中是连续的。<br>
	 * 算法解析见：https://leetcode-cn.com/problems/longest-common-subsequence/solution/zui-chang-gong-gong-zi-xu-lie-by-leetcod-y7u0/
	 *
	 * @param strA 字符串1
	 * @param strB 字符串2
	 * @return 最长公共子串
	 */
	public static String longestCommonSubstring(String strA, String strB) {
		// 初始化矩阵数据,matrix[0][0]的值为0， 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1，
		// 否则，matrix[i][j]的值等于左上方最近两个位置的较大值， 矩阵中其余各点的值为0.
		final int[][] matrix = generateMatrix(strA, strB);

		int m = strA.length();
		int n = strB.length();
		// 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值，
		// 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。
		char[] result = new char[matrix[m][n]];
		int currentIndex = result.length - 1;
		while (matrix[m][n] != 0) {
			if (matrix[m][n] == matrix[m][n - 1]) {
				n--;
			} else if (matrix[m][n] == matrix[m - 1][n]) {
				m--;
			} else {
				result[currentIndex] = strA.charAt(m - 1);
				currentIndex--;
				n--;
				m--;
			}
		}
		return new String(result);
	}

	// --------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 将字符串的所有数据依次写成一行，去除无意义字符串
	 *
	 * @param str 字符串
	 * @return 处理后的字符串
	 */
	private static String removeSign(String str) {
		int length = str.length();
		StringBuilder sb = StrUtil.builder(length);
		// 遍历字符串str,如果是汉字数字或字母，则追加到ab上面
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if(isValidChar(c)) {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 判断字符是否为汉字，数字和字母， 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。
	 *
	 * @param charValue 字符
	 * @return true表示为非汉字，数字和字母，false反之
	 */
	private static boolean isValidChar(char charValue) {
		return (charValue >= 0x4E00 && charValue <= 0X9FFF) || //
				(charValue >= 'a' && charValue <= 'z') || //
				(charValue >= 'A' && charValue <= 'Z') || //
				(charValue >= '0' && charValue <= '9');
	}

	/**
	 * 求公共子串，采用动态规划算法。 其不要求所求得的字符在所给的字符串中是连续的。
	 *
	 * @param strA 字符串1
	 * @param strB 字符串2
	 * @return 公共子串
	 */
	private static int longestCommonSubstringLength(String strA, String strB) {
		final int m = strA.length();
		final int n = strB.length();
		return generateMatrix(strA, strB)[m][n];
	}

	/**
	 * 求公共子串，采用动态规划算法。 其不要求所求得的字符在所给的字符串中是连续的。
	 *
	 * @param strA 字符串1
	 * @param strB 字符串2
	 * @return 公共串矩阵
	 */
	private static int[][] generateMatrix(String strA, String strB) {
		int m = strA.length();
		int n = strB.length();

		// 初始化矩阵数据,matrix[0][0]的值为0， 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1，
		// 否则，matrix[i][j]的值等于左上方最近两个位置的较大值， 矩阵中其余各点的值为0.
		final int[][] matrix = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (strA.charAt(i - 1) == strB.charAt(j - 1)) {
					matrix[i][j] = matrix[i - 1][j - 1] + 1;
				} else {
					matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
				}
			}
		}

		return matrix;
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}
