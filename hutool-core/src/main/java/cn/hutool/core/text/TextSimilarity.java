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
	 * 计算相似度
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
		int temp2 = longestCommonSubstring(newStrA, newStrB).length();
		return NumberUtil.div(temp2, temp);
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

	// --------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 将字符串的所有数据依次写成一行，去除无意义字符串
	 * 
	 * @param str 字符串
	 * @return 处理后的字符串
	 */
	private static String removeSign(String str) {
		StringBuilder sb = StrUtil.builder(str.length());
		// 遍历字符串str,如果是汉字数字或字母，则追加到ab上面
		int length = str.length();
		for (int i = 0; i < length; i++) {
			sb.append(charReg(str.charAt(i)));
		}
		return sb.toString();
	}

	/**
	 * 判断字符是否为汉字，数字和字母， 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。
	 * 
	 * @param charValue 字符
	 * @return 是否为汉字，数字和字母
	 */
	private static boolean charReg(char charValue) {
		return (charValue >= 0x4E00 && charValue <= 0XFFF) || //
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
	private static String longestCommonSubstring(String strA, String strB) {
		char[] chars_strA = strA.toCharArray();
		char[] chars_strB = strB.toCharArray();
		int m = chars_strA.length;
		int n = chars_strB.length;

		// 初始化矩阵数据,matrix[0][0]的值为0， 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1， 否则，matrix[i][j]的值等于左上方最近两个位置的较大值， 矩阵中其余各点的值为0.
		int[][] matrix = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (chars_strA[i - 1] == chars_strB[j - 1]) {
					matrix[i][j] = matrix[i - 1][j - 1] + 1;
				} else {
					matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
				}
			}
		}

		// 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值， 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。
		char[] result = new char[matrix[m][n]];
		int currentIndex = result.length - 1;
		while (matrix[m][n] != 0) {
			if (matrix[m][n] == matrix[m][n - 1]) {
				n--;
			} else if (matrix[m][n] == matrix[m - 1][n]) {
				m--;
			} else {
				result[currentIndex] = chars_strA[m - 1];
				currentIndex--;
				n--;
				m--;
			}
		}
		return new String(result);
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}
