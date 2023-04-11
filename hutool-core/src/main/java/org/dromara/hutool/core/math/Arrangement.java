/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.math;

import org.dromara.hutool.core.array.ArrayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 排列A(n, m)<br>
 * 排列组合相关类 参考：<a href="http://cgs1999.iteye.com/blog/2327664">http://cgs1999.iteye.com/blog/2327664</a>
 *
 * @author looly
 * @since 4.0.7
 */
public class Arrangement implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String[] datas;

	/**
	 * 构造
	 *
	 * @param datas 用于排列的数据
	 */
	public Arrangement(final String[] datas) {
		this.datas = datas;
	}

	/**
	 * 计算排列数，即A(n, n) = n!
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long count(final int n) {
		return count(n, n);
	}

	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 排列数
	 */
	public static long count(final int n, final int m) {
		if (n == m) {
			return MathUtil.factorial(n);
		}
		return (n > m) ? MathUtil.factorial(n, n - m) : 0;
	}

	/**
	 * 计算排列总数，即A(n, 1) + A(n, 2) + A(n, 3)...
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long countAll(final int n) {
		long total = 0;
		for (int i = 1; i <= n; i++) {
			total += count(n, i);
		}
		return total;
	}

	/**
	 * 全排列选择（列表全部参与排列）
	 *
	 * @return 所有排列列表
	 */
	public List<String[]> select() {
		return select(this.datas.length);
	}

	/**
	 * 排列选择（从列表中选择m个排列）
	 *
	 * @param m 选择个数
	 * @return 所有排列列表
	 */
	public List<String[]> select(final int m) {
		final List<String[]> result = new ArrayList<>((int) count(this.datas.length, m));
		select(this.datas, new String[m], 0, result);
		return result;
	}

	/**
	 * 排列所有组合，即A(n, 1) + A(n, 2) + A(n, 3)...
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
	 * 排列选择<br>
	 * 排列方式为先从数据数组中取出一个元素，再把剩余的元素作为新的基数，依次列推，直到选择到足够的元素
	 *
	 * @param datas 选择的基数
	 * @param resultList 前面（resultIndex-1）个的排列结果
	 * @param resultIndex 选择索引，从0开始
	 * @param result 最终结果
	 */
	private void select(final String[] datas, final String[] resultList, final int resultIndex, final List<String[]> result) {
		if (resultIndex >= resultList.length) { // 全部选择完时，输出排列结果
			if (! result.contains(resultList)) {
				result.add(Arrays.copyOf(resultList, resultList.length));
			}
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < datas.length; i++) {
			resultList[resultIndex] = datas[i];
			select(ArrayUtil.remove(datas, i), resultList, resultIndex + 1, result);
		}
	}
}
