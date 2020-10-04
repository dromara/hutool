package cn.hutool.core.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 组合，即C(n, m)<br>
 * 排列组合相关类 参考：http://cgs1999.iteye.com/blog/2327664
 *
 * @author looly
 * @since 4.0.6
 */
public class Combination implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 0-20对应的阶乘，超过20的阶乘会超过Long.MAX_VALUE
	 */
	private static final long[] COMBINATIONS_SUM = new long[]{0L,1L,3L,7L,15L,31L,63L,127L,255L,511L,1023L,2047L,4095L,
			8191L, 16383L,32767L,65535L,131071L,262143L,524287L, 1048575L,2097151L, 4194303L,8388607L,16777215L,33554431L,
			67108863L,134217727L, 268435455L,536870911L, 1073741823L,2147483647L,4294967295L, 8589934591L,17179869183L,
			34359738367L,68719476735L,137438953471L, 274877906943L,549755813887L,1099511627775L,2199023255551L,
			4398046511103L,8796093022207L,17592186044415L, 35184372088831L, 70368744177663L,140737488355327L,
			281474976710655L,562949953421311L, 1125899906842623L, 2251799813685247L,4503599627370495L,9007199254740991L,
			18014398509481983L,36028797018963967L, 72057594037927935L,144115188075855871L, 288230376151711743L,
			576460752303423487L,1152921504606846975L, 2305843009213693951L, 4611686018427387903L,9223372036854775807L};

	private final String[] datas;

	/**
	 * 组合，即C(n, m)<br>
	 * 排列组合相关类 参考：http://cgs1999.iteye.com/blog/2327664
	 *
	 * @param datas 用于组合的数据
	 */
	public Combination(String[] datas) {
		this.datas = datas;
	}

	/**
	 * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 组合数
	 */
	public static long count(int n, int m) {
		if (0 == m) {
			return 1;
		}
		if (n == m) {
			return NumberUtil.factorial(n) / NumberUtil.factorial(m);
		}
		return (n > m) ? NumberUtil.factorial(n, n - m) / NumberUtil.factorial(m) : 0;
	}

	/**
	 * 计算组合总数，即C(n, 1) + C(n, 2) + C(n, 3)...
	 *
	 * @param n 总数
	 * @return 组合数
	 */
	public static long countAll(int n) {
		if (n < 0 || n > 63) {
			throw new IllegalArgumentException(StrUtil.format("countAll must have n >= 0 and n <= 63 for n!, but got n = {}", n));
		}
		return COMBINATIONS_SUM[n];
	}

	/**
	 * 组合选择（从列表中选择m个组合）
	 *
	 * @param m 选择个数
	 * @return 组合结果
	 */
	public List<String[]> select(int m) {
		final List<String[]> result = new ArrayList<>((int) count(this.datas.length, m));
		select(0, new String[m], 0, result);
		return result;
	}

	/**
	 * 全组合
	 *
	 * @return 全排列结果
	 */
	public List<String[]> selectAll() {
		final List<String[]> result = new ArrayList<>((int) countAll(this.datas.length));
		for (int i = 1; i <= this.datas.length; i++) {
			result.addAll(select(i));
		}
		return result;
	}

	/**
	 * 组合选择
	 *
	 * @param dataIndex   待选开始索引
	 * @param resultList  前面（resultIndex-1）个的组合结果
	 * @param resultIndex 选择索引，从0开始
	 * @param result      结果集
	 */
	private void select(int dataIndex, String[] resultList, int resultIndex, List<String[]> result) {
		int resultLen = resultList.length;
		int resultCount = resultIndex + 1;
		if (resultCount > resultLen) { // 全部选择完时，输出组合结果
			result.add(Arrays.copyOf(resultList, resultList.length));
			return;
		}

		// 递归选择下一个
		for (int i = dataIndex; i < datas.length + resultCount - resultLen; i++) {
			resultList[resultIndex] = datas[i];
			select(i + 1, resultList, resultIndex + 1, result);
		}
	}
}
