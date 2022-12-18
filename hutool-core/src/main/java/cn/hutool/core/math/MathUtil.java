package cn.hutool.core.math;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数学相关方法工具类<br>
 * 此工具类与{@link cn.hutool.core.util.NumberUtil}属于一类工具，NumberUtil偏向于简单数学计算的封装，MathUtil偏向复杂数学计算
 *
 * @author looly
 * @since 4.0.7
 */
public class MathUtil {

	//--------------------------------------------------------------------------------------------- Arrangement
	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 排列数
	 */
	public static long arrangementCount(int n, int m) {
		return Arrangement.count(n, m);
	}

	/**
	 * 计算排列数，即A(n, n) = n!
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long arrangementCount(int n) {
		return Arrangement.count(n);
	}

	/**
	 * 排列选择（从列表中选择n个排列）
	 *
	 * @param datas 待选列表
	 * @param m 选择个数
	 * @return 所有排列列表
	 */
	public static List<String[]> arrangementSelect(String[] datas, int m) {
		return new Arrangement(datas).select(m);
	}

	/**
	 * 全排列选择（列表全部参与排列）
	 *
	 * @param datas 待选列表
	 * @return 所有排列列表
	 */
	public static List<String[]> arrangementSelect(String[] datas) {
		return new Arrangement(datas).select();
	}

	//--------------------------------------------------------------------------------------------- Combination
	/**
	 * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 组合数
	 */
	public static long combinationCount(int n, int m) {
		return Combination.count(n, m);
	}

	/**
	 * 组合选择（从列表中选择n个组合）
	 *
	 * @param datas 待选列表
	 * @param m 选择个数
	 * @return 所有组合列表
	 */
	public static List<String[]> combinationSelect(String[] datas, int m) {
		return new Combination(datas).select(m);
	}

	/**
	 * 金额元转换为分
	 *
	 * @param yuan 金额，单位元
	 * @return 金额，单位分
	 * @since 5.7.11
	 */
	public static long yuanToCent(double yuan) {
		return new Money(yuan).getCent();
	}

	/**
	 * 金额分转换为元
	 *
	 * @param cent 金额，单位分
	 * @return 金额，单位元
	 * @since 5.7.11
	 */
	public static double centToYuan(long cent) {
		long yuan = cent / 100;
		int centPart = (int) (cent % 100);
		return new Money(yuan, centPart).getAmount().doubleValue();
	}

	/**
	 * 矩阵加法
	 * @param a 矩阵a
	 * @param b 矩阵b
	 * @return 矩阵a+b
	 */
	public static double[][] addMatrix(double[][] a, double[][] b) {
		return Matrix.addMatrix(a,b);
	}

	/**
	 * 矩阵减法
	 * @param a 矩阵a
	 * @param b 矩阵b
	 * @return 矩阵a-b
	 */
	public static double[][] subMatrix(double[][] a, double[][] b) {
		return Matrix.subMatrix(a,b);
	}

	/**
	 * 矩阵乘法(多线程)
	 * @param a 矩阵a
	 * @param b 矩阵b
	 * @return 矩阵a*b
	 */
	public static BigDecimal[][] mulMatrix(double[][] a, double[][] b) {
		return Matrix.mulMatrix(a,b);
	}

	/**
	 * 矩阵右除
	 * @param a 被除矩阵
	 * @param b 除矩阵
	 * @return 商矩阵
	 */
	public static BigDecimal[][] rightDivMatrix(double[][] a, double[][] b) {
		return Matrix.rightDivMatrix(a,b);
	}

	/**
	 * 矩阵左除
	 * @param a 被除矩阵
	 * @param b 除矩阵
	 * @return 商矩阵
	 */
	public static BigDecimal[][] leftDivMatrix(double[][] a, double[][] b) {
		return Matrix.leftDivMatrix(a,b);
	}

	/**
	 * 矩阵求逆
	 * @param a 待转置的矩阵
	 * @return 转置后的矩阵
	 */
	public static double[][] getReverseMartrix(double[][] a) {
		return Matrix.getReverseMartrix(a);
	}

	/**
	 * 矩阵转置
	 * @param a 矩阵a
	 * @return 矩阵a的转置
	 */
	public static double[][] transposeMatrix(double[][] a) {
		return Matrix.transposeMatrix(a);
	}
}
