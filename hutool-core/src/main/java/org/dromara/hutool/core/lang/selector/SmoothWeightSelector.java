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

package org.dromara.hutool.core.lang.selector;

import org.dromara.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 平滑加权轮询选择器
 *
 * <p>
 * 来自：https://gitee.com/dromara/hutool/pulls/982/
 * </p>
 * <p>
 * 介绍：https://cloud.tencent.com/developer/beta/article/1680928
 * </p>
 *
 * <p>
 * 思路: 比如 A : 5 , B : 3 , C : 2   (服务器 A,B,C 对应权重分别是 5,3,2)
 * ip: A,B,C
 * weight: 5,3,2 (计算得到 totalWeight = 10)
 * currentWeight: 0,0,0 (当前ip的初始权重都为0)
 * <pre>
 * 请求次数: |  currentWeight = currentWeight + weight  |  最大权重为  |  返回的ip为 |  最大的权重 - totalWeight,其余不变
 *      1   |           5,3,2    (0,0,0 + 5,3,2)       |     5      |      A     |      -5,3,2
 *      2   |           0,6,4    (-5,3,2 + 5,3,2)      |     6      |      B     |       0,-4,4
 *      3   |           5,-1,6    (0,-4,4 + 5,3,2)     |     6      |     C      |       5,-1,-4
 *      4   |          10,2,-2    (5,-1,-4 + 5,3,2)    |     10     |     A      |       0,2,-2
 *      5   |           5,5,0                          |     5      |     A      |       -5,5,0
 *      6   |           0,8,2                          |     8      |     B      |       0,-2,2
 *      7   |           5,1,4                          |     5      |     A      |       -5,1,4
 *      8   |           0,4,6                          |     6      |     C      |       0,4,-4
 *      9   |           5,7,-2                         |     7      |     B      |       5,-3,-2
 *      10  |           10,0,0                         |     10     |     A      |        0,0,0
 * </pre>
 *
 * 至此结束: 可以看到负载轮询的策略是: A,B,C,A,A,B,A,C,B,A,
 *
 * @param <T> 对象类型
 * @author :Wind, Looly
 */
public class SmoothWeightSelector<T> implements Selector<T> {

	/**
	 * 创建平滑加权获取器
	 *
	 * @param <T> 对象类型
	 * @return SmoothSelector
	 */
	public static <T> SmoothWeightSelector<T> of() {
		return new SmoothWeightSelector<>();
	}

	private final List<SmoothWeightObj<T>> objList;

	// region ----- Constructors

	/**
	 * 构造
	 */
	public SmoothWeightSelector() {
		this.objList = new ArrayList<>();
	}

	/**
	 * 构造
	 *
	 * @param weightObjList 权重对象列表
	 */
	public SmoothWeightSelector(final Iterable<? extends WeightObj<T>> weightObjList) {
		this();
		for (final WeightObj<T> weightObj : weightObjList) {
			add(weightObj);
		}
	}
	// endregion

	/**
	 * 增加对象
	 *
	 * @param obj    对象
	 * @param weight 权重
	 * @return this
	 */
	public SmoothWeightSelector<T> add(final T obj, final int weight) {
		return add(new SmoothWeightObj<>(obj, weight));
	}

	/**
	 * 增加权重对象
	 *
	 * @param weightObj 权重对象
	 * @return this
	 */
	public SmoothWeightSelector<T> add(final WeightObj<T> weightObj) {
		final SmoothWeightObj<T> smoothWeightObj;
		if (weightObj instanceof SmoothWeightObj) {
			smoothWeightObj = (SmoothWeightObj<T>) weightObj;
		} else {
			smoothWeightObj = new SmoothWeightObj<>(weightObj.obj, weightObj.weight);
		}
		this.objList.add(smoothWeightObj);
		return this;
	}

	/**
	 * 通过平滑加权方法获取列表中的当前对象
	 *
	 * @return 选中的对象
	 */
	@Override
	public T select() {
		if (CollUtil.isEmpty(this.objList)) {
			return null;
		}
		int totalWeight = 0;
		SmoothWeightObj<T> selected = null;

		for (final SmoothWeightObj<T> obj : objList) {
			totalWeight += obj.getWeight();
			final int currentWeight = obj.getCurrentWeight() + obj.getWeight();
			obj.setCurrentWeight(currentWeight);
			if (null == selected || currentWeight > selected.getCurrentWeight()) {
				selected = obj;
			}
		}

		if (null == selected) {
			return null;
		}

		// 更新选择的对象的当前权重，并返回其地址
		selected.setCurrentWeight(selected.getCurrentWeight() - totalWeight);

		return selected.getObj();
	}
}
