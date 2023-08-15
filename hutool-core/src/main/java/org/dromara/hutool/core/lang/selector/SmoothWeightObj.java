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

/**
 * 平滑权重对象
 *
 * @param <T> 对象类型
 * @author Wind, Loolt
 */
public class SmoothWeightObj<T> extends WeightObj<T> {

	private int currentWeight;

	/**
	 * 构造
	 *
	 * @param obj           对象
	 * @param weight        权重
	 */
	public SmoothWeightObj(final T obj, final int weight) {
		this(obj, weight, 0);
	}

	/**
	 * 构造
	 *
	 * @param obj           对象
	 * @param weight        权重
	 * @param currentWeight 当前权重
	 */
	public SmoothWeightObj(final T obj, final int weight, final int currentWeight) {
		super(obj, weight);
		this.currentWeight = currentWeight;
	}

	/**
	 * 获取当前权重
	 *
	 * @return int 临时权重
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}

	/**
	 * setCurrentWeight
	 * <p>设置当前权重
	 *
	 * @param currentWeight 权重值
	 */
	public void setCurrentWeight(final int currentWeight) {
		this.currentWeight = currentWeight;
	}
}
