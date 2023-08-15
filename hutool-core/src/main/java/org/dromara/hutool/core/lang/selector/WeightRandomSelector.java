/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.selector;

import org.dromara.hutool.core.collection.CollUtil;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 权重随机选择算法实现<br>
 * <p>
 * 平时，经常会遇到权重随机算法，从不同权重的N个元素中随机选择一个，并使得总体选择结果是按照权重分布的。如广告投放、负载均衡等。
 * </p>
 * <p>
 * 如有4个元素A、B、C、D，权重分别为1、2、3、4，随机结果中A:B:C:D的比例要为1:2:3:4。<br>
 * </p>
 * 总体思路：累加每个元素的权重A(1)-B(3)-C(6)-D(10)，则4个元素的的权重管辖区间分别为[0,1)、[1,3)、[3,6)、[6,10)。<br>
 * 然后随机出一个[0,10)之间的随机数。落在哪个区间，则该区间之后的元素即为按权重命中的元素。<br>
 *
 * <p>
 * 参考博客：https://www.cnblogs.com/waterystone/p/5708063.html
 *
 * @param <T> 权重随机获取的对象类型
 * @author looly
 * @since 3.3.0
 */
public class WeightRandomSelector<T> implements Selector<T>, Serializable {
	private static final long serialVersionUID = -8244697995702786499L;

	/**
	 * 创建权重随机获取器
	 *
	 * @param <T> 权重随机获取的对象类型
	 * @return WeightRandomSelector
	 */
	public static <T> WeightRandomSelector<T> of() {
		return new WeightRandomSelector<>();
	}

	private final TreeMap<Integer, T> weightMap;

	// region ----- Constructors

	/**
	 * 构造
	 */
	public WeightRandomSelector() {
		weightMap = new TreeMap<>();
	}

	/**
	 * 构造
	 *
	 * @param weightObj 带有权重的对象
	 */
	public WeightRandomSelector(final WeightObj<T> weightObj) {
		this();
		if (null != weightObj) {
			add(weightObj);
		}
	}

	/**
	 * 构造
	 *
	 * @param weightObjs 带有权重的对象
	 */
	public WeightRandomSelector(final Iterable<WeightObj<T>> weightObjs) {
		this();
		if (CollUtil.isNotEmpty(weightObjs)) {
			for (final WeightObj<T> weightObj : weightObjs) {
				add(weightObj);
			}
		}
	}

	/**
	 * 构造
	 *
	 * @param weightObjs 带有权重的对象
	 */
	public WeightRandomSelector(final WeightObj<T>[] weightObjs) {
		this();
		for (final WeightObj<T> weightObj : weightObjs) {
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
	public WeightRandomSelector<T> add(final T obj, final int weight) {
		return add(new WeightObj<>(obj, weight));
	}

	/**
	 * 增加对象权重
	 *
	 * @param weightObj 权重对象
	 * @return this
	 */
	public WeightRandomSelector<T> add(final WeightObj<T> weightObj) {
		if (null != weightObj) {
			final int weight = weightObj.getWeight();
			if (weight > 0) {
				final int lastWeight = this.weightMap.isEmpty() ? 0 : this.weightMap.lastKey();
				this.weightMap.put(weight + lastWeight, weightObj.getObj());// 权重累加
			}
		}
		return this;
	}

	/**
	 * 清空权重表
	 *
	 * @return this
	 */
	public WeightRandomSelector<T> clear() {
		if (null != this.weightMap) {
			this.weightMap.clear();
		}
		return this;
	}

	/**
	 * 下一个随机对象
	 *
	 * @return 随机对象
	 */
	@Override
	public T select() {
		final int randomWeight = (int) (this.weightMap.lastKey() * Math.random());
		final SortedMap<Integer, T> tailMap = this.weightMap.tailMap(randomWeight, false);
		return this.weightMap.get(tailMap.firstKey());
	}
}
