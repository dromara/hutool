package cn.hutool.core.collection.math;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.NumberUtil;

/**
 * 组合，即C(n, m)<br>
 * 排列组合相关类 参考：http://cgs1999.iteye.com/blog/2327664
 * 
 * @author looly
 * @since 4.0.6
 */
public class Combination {

	private String[] datas;
	private int n;
	private List<String[]> result = new ArrayList<>();

	/**
	 * 构造
	 * 
	 * @param datas 用于组合的数据
	 * @param n 选择的个数
	 */
	public Combination(String[] datas, int n) {
		this.datas = datas;
		this.n = n;
	}

	/**
	 * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
	 * 
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 组合数
	 */
	public static long count(int n, int m) {
		return (n >= m) ? NumberUtil.factorial(n) / NumberUtil.factorial(n - m) / NumberUtil.factorial(m) : 0;
	}

	/**
	 * 组合选择（从列表中选择n个组合）
	 * 
	 * @param dataList 待选列表
	 * @param n 选择个数
	 * @return 组合结果
	 */
	public List<String[]> select() {
		this.result.clear();
		select(0, new String[this.n], 0);
		return this.result;
	}

	/**
	 * 组合选择
	 * 
	 * @param dataList 待选列表
	 * @param dataIndex 待选开始索引
	 * @param resultList 前面（resultIndex-1）个的组合结果
	 * @param resultIndex 选择索引，从0开始
	 */
	private void select(int dataIndex, String[] resultList, int resultIndex) {
		int resultLen = resultList.length;
		int resultCount = resultIndex + 1;
		if (resultCount > resultLen) { // 全部选择完时，输出组合结果
			result.add(resultList);
			return;
		}

		// 递归选择下一个
		for (int i = dataIndex; i < datas.length + resultCount - resultLen; i++) {
			resultList[resultIndex] = datas[i];
			select(i + 1, resultList, resultIndex + 1);
		}
	}
}
