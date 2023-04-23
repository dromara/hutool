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

package org.dromara.hutool.core.lang.range;

import org.dromara.hutool.core.lang.Assert;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * <p>参考<em>Guava</em>的<em>Range</em>实现，用于描述作为上下界的两个{@link Bound}实例围成的一段区间。<br>
 * 作为{@link Predicate}使用时，可检验指定值是否在区间中，即指定值是否同时满足上下界的{@link Bound#test}方法。
 *
 * <p>区间的类型，支持通过工厂方法创建下述几种类型的区间：</p>
 * <table>
 * <caption>区间</caption>
 * <tr><th>区间            <th>数学定义                  <th>工厂方法
 * <tr><td>{@code (a, b)}  <td>{@code {x | a < x < b}}  <td>{@link #open}
 * <tr><td>{@code [a, b]}  <td>{@code {x | a <= x <= b}}<td>{@link #close}
 * <tr><td>{@code (a, b]}  <td>{@code {x | a < x <= b}} <td>{@link #openClose}
 * <tr><td>{@code [a, b)}  <td>{@code {x | a <= x < b}} <td>{@link #closeOpen}
 * <tr><td>{@code (a, +∞)} <td>{@code {x | x > a}}      <td>{@link #greaterThan}
 * <tr><td>{@code [a, +∞)} <td>{@code {x | x >= a}}     <td>{@link #atLeast}
 * <tr><td>{@code (-∞, b)} <td>{@code {x | x < b}}      <td>{@link #lessThan}
 * <tr><td>{@code (-∞, b]} <td>{@code {x | x <= b}}     <td>{@link #atMost}
 * <tr><td>{@code (-∞, +∞)}<td>{@code {x}}              <td>{@link #all}
 * </table>
 *
 * <p>空区间</p>
 * <p>根据数学定义，当区间中无任何实数时，认为该区间 代表的集合为空集，
 * 用户可通过{@link #isEmpty}确认当前实例是否为空区间。<br>
 * 若实例上界<em>a</em>，下界为<em>b</em>，则当实例满足下述任意条件时，认为其为一个空区间：
 * <ul>
 *     <li>{@code a > b}；</li>
 *     <li>{@code [a, b)}，且{@code a == b}；</li>
 *     <li>{@code (a, b)}，且{@code a == b}；</li>
 *     <li>{@code (a, b]}，且{@code a == b}；</li>
 * </ul>
 * 当通过工厂方法创建区间时，若区间为空，则会抛出{@link IllegalArgumentException},
 * 但是通过交并操作仍有可能创建出满足上述描述的空区间。
 * 此时若空区间参与操作可能得到意外的结果，
 * 因此对通过非工厂方法得到的区间，在操作前有必要通过{@link #isEmpty}进行检验。
 *
 * @param <T> 边界值类型
 * @author huangchengxing
 * @see Bound
 * @since 6.0.0
 */
public class BoundedRange<T extends Comparable<? super T>> implements Predicate<T> {

	/**
	 * 双向无界的区间
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static final BoundedRange ALL = new BoundedRange(Bound.noneLowerBound(), Bound.noneUpperBound());

	/**
	 * 构建一个上下界皆无限大的区间，即{@code {x | -∞ < x < +∞}}
	 *
	 * @param <T> 比较对象类型
	 * @return 区间
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> BoundedRange<T> all() {
		return ALL;
	}

	/**
	 * 构建一个闭区间，即{@code {x | lowerBound <= x <= upperBound}}
	 *
	 * @param lowerBound 下界，不能为空
	 * @param upperBound 上界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws IllegalArgumentException 当创建的区间表示的集合为空时抛出
	 * @throws NullPointerException     上界或下界为{@code null}时抛出
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> close(final T lowerBound, final T upperBound) {
		Objects.requireNonNull(lowerBound);
		Objects.requireNonNull(upperBound);
		return checkEmpty(
				new BoundedRange<>(
						Bound.atLeast(lowerBound), Bound.atMost(upperBound)
				)
		);
	}

	/**
	 * 构建一个开区间，即{@code {x | lowerBound < x < upperBound}}
	 *
	 * @param lowerBound 下界，不能为空
	 * @param upperBound 上界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws IllegalArgumentException 当创建的区间表示的集合为空时抛出
	 * @throws NullPointerException     上界或下界为{@code null}时抛出
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> open(final T lowerBound, final T upperBound) {
		Objects.requireNonNull(lowerBound);
		Objects.requireNonNull(upperBound);
		return checkEmpty(
				new BoundedRange<>(Bound.greaterThan(lowerBound), Bound.lessThan(upperBound))
		);
	}

	/**
	 * 构建一个左闭右开区间，即{@code {x | lowerBound <= x < upperBound}}
	 *
	 * @param lowerBound 下界，不能为空
	 * @param upperBound 上界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws IllegalArgumentException 当创建的区间表示的集合为空时抛出
	 * @throws NullPointerException     上界或下界为{@code null}时抛出
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> closeOpen(final T lowerBound, final T upperBound) {
		Objects.requireNonNull(lowerBound);
		Objects.requireNonNull(upperBound);
		return checkEmpty(
				new BoundedRange<>(
						Bound.atLeast(lowerBound),
						Bound.lessThan(upperBound)
				)
		);
	}

	/**
	 * 构建一个左闭右开区间，即{@code {x | lowerBound < x <= upperBound}}
	 *
	 * @param lowerBound 下界，不能为空
	 * @param upperBound 上界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws IllegalArgumentException 当创建的区间表示的集合为空时抛出
	 * @throws NullPointerException     上界或下界为{@code null}时抛出
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> openClose(final T lowerBound, final T upperBound) {
		Objects.requireNonNull(lowerBound);
		Objects.requireNonNull(upperBound);
		return checkEmpty(
				new BoundedRange<>(
						Bound.greaterThan(lowerBound),
						Bound.atMost(upperBound)
				)
		);
	}

	/**
	 * {@code {x | lowerBound < x < +∞}}
	 *
	 * @param lowerBound 下界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws NullPointerException 下界为{@code null}时抛出
	 * @see Bound#toRange()
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> greaterThan(final T lowerBound) {
		return Bound.greaterThan(lowerBound).toRange();
	}

	/**
	 * {@code {x | lowerBound < x < +∞}}
	 *
	 * @param lowerBound 下界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws NullPointerException 下界为{@code null}时抛出
	 * @see Bound#toRange()
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> atLeast(final T lowerBound) {
		return Bound.atLeast(lowerBound).toRange();
	}

	/**
	 * {@code {x | -∞ < x < upperBound}}
	 *
	 * @param upperBound 上界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws NullPointerException 上界为{@code null}时抛出
	 * @see Bound#toRange()
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> lessThan(final T upperBound) {
		return Bound.lessThan(upperBound).toRange();
	}

	/**
	 * {@code {x | -∞ < x <= max}}
	 *
	 * @param upperBound 上界，不能为空
	 * @param <T>        边界值类型
	 * @return 区间
	 * @throws NullPointerException 上界为{@code null}时抛出
	 * @see Bound#toRange()
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> atMost(final T upperBound) {
		return Bound.atMost(upperBound).toRange();
	}

	/**
	 * 下界
	 */
	private final Bound<T> lowerBound;

	/**
	 * 上界
	 */
	private final Bound<T> upperBound;

	/**
	 * 构造
	 *
	 * @param lowerBound 下界
	 * @param upperBound 上界
	 */
	BoundedRange(final Bound<T> lowerBound, final Bound<T> upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	// endregion

	// region ========== 通用方法 ==========

	/**
	 * 获取下界
	 *
	 * @return 下界
	 */
	public Bound<T> getLowerBound() {
		return lowerBound;
	}

	/**
	 * 获取下界值
	 *
	 * @return 下界值
	 */
	public T getLowerBoundValue() {
		return getLowerBound().getValue();
	}

	/**
	 * 是否有下界
	 *
	 * @return 是否
	 */
	public boolean hasLowerBound() {
		return Objects.nonNull(getLowerBound().getValue());
	}

	/**
	 * 获取上界
	 *
	 * @return 上界
	 */
	public Bound<T> getUpperBound() {
		return upperBound;
	}

	/**
	 * 获取上界值
	 *
	 * @return 上界值
	 */
	public T getUpperBoundValue() {
		return getUpperBound().getValue();
	}

	/**
	 * 是否有上界
	 *
	 * @return 是否
	 */
	public boolean hasUpperBound() {
		return Objects.nonNull(getUpperBound().getValue());
	}

	/**
	 * <p>当前区间是否为空。 <br>
	 * 当由下界<em>left</em>与上界<em>right</em>构成的区间，
	 * 符合下述任意条件时，认为当前区间为空：
	 * <ul>
	 *     <li>对任何区间，有{@code left > right}；</li>
	 *     <li>对半开半闭区间{@code [left, right)}，有{@code left == right}；</li>
	 *     <li>对开区间{@code (left, right)}，有{@code left == right}；</li>
	 *     <li>对半开半闭区间{@code (left, right]}，有{@code left == right}；</li>
	 * </ul>
	 *
	 * @return 是否
	 */
	public boolean isEmpty() {
		final Bound<T> low = getLowerBound();
		final Bound<T> up = getUpperBound();
		if (low instanceof NoneLowerBound || up instanceof NoneUpperBound) {
			return false;
		}
		final int compareValue = low.getValue().compareTo(up.getValue());
		if (compareValue < 0) {
			return false;
		}
		// 上界小于下界时为空
		return compareValue > 0
				// 上下界的边界值相等，且不为退化区间是为空
				|| !(low.getType().isClose() && up.getType().isClose());
	}

	/**
	 * 输出当前区间的字符串，格式为{@code "[a, b]"}
	 *
	 * @return 字符串
	 */
	@Override
	public String toString() {
		return getLowerBound().descBound() + ", " + getUpperBound().descBound();
	}

	/**
	 * 比较两个实例是否相等
	 *
	 * @param o 另一实例
	 * @return 是否
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final BoundedRange<?> that = (BoundedRange<?>) o;
		return lowerBound.equals(that.lowerBound) && upperBound.equals(that.upperBound);
	}

	/**
	 * 获取实例哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(lowerBound, upperBound);
	}

	// endregion

	// region ========== 判断交并集 ==========

	/**
	 * {@code other}是否是当前区间的子集
	 *
	 * @param other 另一个区间
	 * @return 是否
	 */
	public boolean isSuperset(final BoundedRange<T> other) {
		return getLowerBound().compareTo(other.getLowerBound()) <= 0
				&& getUpperBound().compareTo(other.getUpperBound()) >= 0;
	}

	/**
	 * {@code other}是否是当前区间的子集
	 *
	 * @param other 另一个区间
	 * @return 是否
	 */
	public boolean isProperSuperset(final BoundedRange<T> other) {
		return getLowerBound().compareTo(other.getLowerBound()) < 0
				&& getUpperBound().compareTo(other.getUpperBound()) > 0;
	}

	/**
	 * 当前区间是否是{@code other}的子集
	 *
	 * @param other 另一个区间
	 * @return 是否
	 */
	public boolean isSubset(final BoundedRange<T> other) {
		return getLowerBound().compareTo(other.getLowerBound()) >= 0
				&& getUpperBound().compareTo(other.getUpperBound()) <= 0;
	}

	/**
	 * 当前区间是否是{@code other}的真子集
	 *
	 * @param other 另一个区间
	 * @return 是否
	 */
	public boolean isProperSubset(final BoundedRange<T> other) {
		return getLowerBound().compareTo(other.getLowerBound()) > 0
				&& getUpperBound().compareTo(other.getUpperBound()) < 0;
	}

	/**
	 * {@code other}是否与当前区间不相交
	 *
	 * @param other 另一个区间
	 * @return 是否
	 */
	public boolean isDisjoint(final BoundedRange<T> other) {
		return BoundedRangeOperation.isDisjoint(this, other);
	}

	/**
	 * {@code other}是否与当前区间相交：
	 *
	 * @param other 另一个区间
	 * @return 是否
	 */
	public boolean isIntersected(final BoundedRange<T> other) {
		return BoundedRangeOperation.isIntersected(this, other);
	}

	/**
	 * 指定值是否在当前区间内
	 *
	 * @param value 要检测的值
	 * @return 是否
	 */
	@Override
	public boolean test(final T value) {
		return getLowerBound()
				.and(getUpperBound())
				.test(value);
	}

	// endregion

	// region ========== 交并集操作 ==========

	/**
	 * 若{@code other}与当前区间相交，则将其与当前区间合并。
	 *
	 * @param other 另一个区间
	 * @return 合并后的新区间，若两区间不相交则返回当前集合
	 */
	public BoundedRange<T> unionIfIntersected(final BoundedRange<T> other) {
		return BoundedRangeOperation.unionIfIntersected(this, other);
	}

	/**
	 * 获得包含当前区间与指定区间的最小的区间
	 *
	 * @param other 另一个区间
	 * @return 包含当前区间与指定区间的最小的区间
	 */
	public BoundedRange<T> span(final BoundedRange<T> other) {
		return BoundedRangeOperation.span(this, other);
	}

	/**
	 * 若{@code other}与当前区间不相连，则获得两区间中间的间隔部分
	 *
	 * @param other 另一个区间
	 * @return 代表间隔部分的区间，若两区间相交则返回{@code null}
	 */
	public BoundedRange<T> gap(final BoundedRange<T> other) {
		return BoundedRangeOperation.gap(this, other);
	}

	/**
	 * 若{@code other}与当前区间相交，则获得该区间与当前区间的交集
	 *
	 * @param other 另一个区间
	 * @return 代表交集的区间，若无交集则返回{@code null}
	 */
	public BoundedRange<T> intersection(final BoundedRange<T> other) {
		return BoundedRangeOperation.intersection(this, other);
	}

	/**
	 * 截取当前区间中大于{@code min}的部分，若{@code min}不在该区间中，则返回当前区间本身
	 *
	 * @param min 最大的左值
	 * @return 区间
	 */
	public BoundedRange<T> subGreatThan(final T min) {
		return BoundedRangeOperation.subGreatThan(this, min);
	}

	/**
	 * 截取当前区间中大于等于{@code min}的部分，若{@code min}不在该区间中，则返回当前区间本身
	 *
	 * @param min 最大的左值
	 * @return 区间
	 */
	public BoundedRange<T> subAtLeast(final T min) {
		return BoundedRangeOperation.subAtLeast(this, min);
	}

	/**
	 * 截取当前区间中小于{@code max}的部分，若{@code max}不在该区间中，则返回当前区间本身
	 *
	 * @param max 最大的左值
	 * @return 区间
	 */
	public BoundedRange<T> subLessThan(final T max) {
		return BoundedRangeOperation.subLessThan(this, max);
	}

	/**
	 * 截取当前区间中小于等于{@code max}的部分，若{@code max}不在该区间中，则返回当前区间本身
	 *
	 * @param max 最大的左值
	 * @return 区间
	 */
	public BoundedRange<T> subAtMost(final T max) {
		return BoundedRangeOperation.subAtMost(this, max);
	}

	// endregion

	private static <T extends Comparable<? super T>> BoundedRange<T> checkEmpty(final BoundedRange<T> range) {
		Assert.isFalse(range.isEmpty(), "{} is a empty range", range);
		return range;
	}
}
