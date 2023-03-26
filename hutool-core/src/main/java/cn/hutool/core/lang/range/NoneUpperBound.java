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

package cn.hutool.core.lang.range;

/**
 * 无限大的右边界
 *
 * @param <T> 边界值类型
 */
class NoneUpperBound<T extends Comparable<? super T>> implements Bound<T> {
	/**
	 * 无限大的右边界
	 */
	@SuppressWarnings("rawtypes")
	static final NoneUpperBound INSTANCE = new NoneUpperBound();

	private NoneUpperBound() {
	}

	/**
	 * 获取边界值
	 *
	 * @return 边界值
	 */
	@Override
	public T getValue() {
		return null;
	}

	/**
	 * 获取边界类型
	 *
	 * @return 边界类型
	 */
	@Override
	public BoundType getType() {
		return BoundType.OPEN_UPPER_BOUND;
	}

	/**
	 * 检验指定值是否在当前边界表示的范围内
	 *
	 * @param t 要检验的值，不允许为{@code null}
	 * @return 是否
	 */
	@Override
	public boolean test(final T t) {
		return true;
	}

	/**
	 * <p>比较另一边界与当前边界在坐标轴上位置的先后顺序。<br>
	 * 若令当前边界为<em>t1</em>，另一边界为<em>t2</em>，则有
	 * <ul>
	 *     <li>-1：<em>t1</em>在<em>t2</em>的左侧；</li>
	 *     <li>0：<em>t1</em>与<em>t2</em>的重合；</li>
	 *     <li>-1：<em>t1</em>在<em>t2</em>的右侧；</li>
	 * </ul>
	 *
	 * @param bound 边界
	 * @return 位置
	 */
	@Override
	public int compareTo(final Bound<T> bound) {
		return bound instanceof NoneUpperBound ? 0 : 1;
	}

	/**
	 * 获取{@code "[value"}或{@code "(value"}格式的字符串
	 *
	 * @return 字符串
	 */
	@Override
	public String descBound() {
		return INFINITE_MAX + getType().getSymbol();
	}

	/**
	 * 获得当前实例对应的{@code { x | x >= xxx}}格式的不等式字符串
	 *
	 * @return 字符串
	 */
	@Override
	public String toString() {
		return "{x | x < +∞}";
	}

	/**
	 * 对当前边界取反
	 *
	 * @return 取反后的边界
	 */
	@Override
	public Bound<T> negate() {
		return this;
	}

	/**
	 * 将当前实例转为一个区间
	 *
	 * @return 区间
	 */
	@Override
	public BoundedRange<T> toRange() {
		return BoundedRange.all();
	}

}
