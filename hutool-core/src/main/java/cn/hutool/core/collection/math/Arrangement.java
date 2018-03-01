package cn.hutool.core.collection.math;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.NumberUtil;

/**
 * 排列A(n, m)<br>
 * 排列组合相关类 参考：http://cgs1999.iteye.com/blog/2327664
 * 
 * @author looly
 * @since 4.0.6
 */
public class Arrangement {

	private String[] datas;
	private int n;
	private List<String[]> result = new ArrayList<>();

	/**
	 * 构造
	 * 
	 * @param datas 用于排列的数据
	 * @param n 选择的个数
	 */
	public Arrangement(String[] datas, int n) {
		this.datas = datas;
		this.n = n;
	}

	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 * 
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 排列数
	 */
	public static long count(int n, int m) {
		return (n >= m) ? NumberUtil.factorial(n) / NumberUtil.factorial(n - m) : 0;
	}

	/**
	 * 排列选择（从列表中选择n个排列）
	 * 
	 * @param dataList 待选列表
	 * @param n 选择个数
	 */
	public List<String[]> select() {
		this.result.clear();
		select(new String[this.n], 0);
		return this.result;
	}

	/**
	 * 排列选择
	 * 
	 * @param dataList 待选列表
	 * @param resultList 前面（resultIndex-1）个的排列结果
	 * @param resultIndex 选择索引，从0开始
	 */
	private void select(String[] resultList, int resultIndex) {
		int resultLen = resultList.length;
		if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
			result.add(resultList);
			return;
		}

		// 递归选择下一个
		for (int i = 0; i < datas.length; i++) {
			// 判断待选项是否存在于排列结果中
			boolean exists = false;
			for (int j = 0; j < resultIndex; j++) {
				if (datas[i].equals(resultList[j])) {
					exists = true;
					break;
				}
			}
			if (false == exists) { // 排列结果不存在该项，才可选择
				resultList[resultIndex] = datas[i];
				select(resultList, resultIndex + 1);
			}
		}
	}
}
