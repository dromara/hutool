package cn.hutool.core.lang.range;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.lang.Opt;

import java.util.Objects;

/**
 * 边界区间的操作工具，如子区间、合并区间等
 *
 * @author huangchengxing
 * @since 6.0.0
 */
public class BoundedRangeOperation {

	/**
	 * 若{@code other}与当前区间相交，则将其与当前区间合并。
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param other        另一个区间
	 * @return 合并后的新区间，若两区间不相交则返回当前集合
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> unionIfIntersected(final BoundedRange<T> boundedRange, final BoundedRange<T> other) {
		Objects.requireNonNull(boundedRange);
		Objects.requireNonNull(other);
		if (isDisjoint(boundedRange, other)) {
			return boundedRange;
		}
		return new BoundedRange<>(
				CompareUtil.min(boundedRange.getLowerBound(), other.getLowerBound()),
				CompareUtil.max(boundedRange.getUpperBound(), other.getUpperBound())
		);
	}

	/**
	 * 获得包含当前区间与指定区间的最小的区间
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param other        另一个区间
	 * @return 包含当前区间与指定区间的最小的区间
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> span(final BoundedRange<T> boundedRange, final BoundedRange<T> other) {
		Objects.requireNonNull(boundedRange);
		Objects.requireNonNull(other);
		return new BoundedRange<>(
				CompareUtil.min(boundedRange.getLowerBound(), other.getLowerBound()),
				CompareUtil.max(boundedRange.getUpperBound(), other.getUpperBound())
		);
	}

	/**
	 * 若{@code other}与当前区间不相连，则获得两区间中间的间隔部分
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param other        另一个区间
	 * @return 代表间隔部分的区间，若两区间相交则返回{@code null}
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> gap(final BoundedRange<T> boundedRange, final BoundedRange<T> other) {
		Objects.requireNonNull(boundedRange);
		Objects.requireNonNull(other);
		if (isIntersected(boundedRange, other)) {
			return null;
		}
		return new BoundedRange<>(
				CompareUtil.min(boundedRange.getUpperBound(), other.getUpperBound()).negate(),
				CompareUtil.max(boundedRange.getLowerBound(), other.getLowerBound()).negate()
		);
	}

	/**
	 * 若{@code other}与当前区间相交，则获得该区间与当前区间的交集
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param other        另一个区间
	 * @return 代表交集的区间，若无交集则返回{@code null}
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> intersection(final BoundedRange<T> boundedRange, final BoundedRange<T> other) {
		Objects.requireNonNull(boundedRange);
		Objects.requireNonNull(other);
		if (isDisjoint(boundedRange, other)) {
			return null;
		}
		return new BoundedRange<>(
				CompareUtil.max(boundedRange.getLowerBound(), other.getLowerBound()),
				CompareUtil.min(boundedRange.getUpperBound(), other.getUpperBound())
		);
	}

	/**
	 * 截取当前区间中大于{@code min}的部分，若{@code min}不在该区间中，则返回当前区间本身
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param min          最大的左值
	 * @return 区间
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> subGreatThan(final BoundedRange<T> boundedRange, final T min) {
		return Opt.ofNullable(min)
				.filter(boundedRange)
				.map(t -> new BoundedRange<>(Bound.greaterThan(t), boundedRange.getUpperBound()))
				.orElse(boundedRange);
	}

	/**
	 * 截取当前区间中大于等于{@code min}的部分，若{@code min}不在该区间中，则返回当前区间本身
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param min          最大的左值
	 * @return 区间
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> subAtLeast(final BoundedRange<T> boundedRange, final T min) {
		return Opt.ofNullable(min)
				.filter(boundedRange)
				.map(t -> new BoundedRange<>(Bound.atLeast(t), boundedRange.getUpperBound()))
				.orElse(boundedRange);
	}

	/**
	 * 截取当前区间中小于{@code max}的部分，若{@code max}不在该区间中，则返回当前区间本身
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param max 最大的左值
	 * @return 区间
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> subLessThan(final BoundedRange<T> boundedRange, final T max) {
		return Opt.ofNullable(max)
				.filter(boundedRange)
				.map(t -> new BoundedRange<>(boundedRange.getLowerBound(), Bound.lessThan(max)))
				.orElse(boundedRange);
	}

	/**
	 * 截取当前区间中小于等于{@code max}的部分，若{@code max}不在该区间中，则返回当前区间本身
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param max 最大的左值
	 * @return 区间
	 */
	public static <T extends Comparable<? super T>> BoundedRange<T> subAtMost(final BoundedRange<T> boundedRange, final T max) {
		return Opt.ofNullable(max)
				.filter(boundedRange)
				.map(t -> new BoundedRange<>(boundedRange.getLowerBound(), Bound.atMost(max)))
				.orElse(boundedRange);
	}

	// region ========== 判断交并集 ==========
	/**
	 * {@code boundedRange}是否与{@code other}相交
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param other        另一个区间
	 * @return 是否相交
	 */
	public static <T extends Comparable<? super T>> boolean isIntersected(final BoundedRange<T> boundedRange, final BoundedRange<T> other) {
		return false == isDisjoint(boundedRange, other);
	}

	/**
	 * {@code boundedRange}是否与{@code other}前区间不相交
	 *
	 * @param <T>          可比较对象类型
	 * @param boundedRange 区间
	 * @param other        另一个区间
	 * @return 是否
	 */
	public static <T extends Comparable<? super T>> boolean isDisjoint(final BoundedRange<T> boundedRange, final BoundedRange<T> other) {
		Objects.requireNonNull(boundedRange);
		Objects.requireNonNull(other);
		return boundedRange.getLowerBound().compareTo(other.getUpperBound()) > 0
				|| boundedRange.getUpperBound().compareTo(other.getLowerBound()) < 0;
	}
	// endregion
}
