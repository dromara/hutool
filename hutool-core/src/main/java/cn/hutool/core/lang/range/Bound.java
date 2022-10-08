package cn.hutool.core.lang.range;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * <p>边界对象，描述具有特定上界或下界的单侧无界的区间。
 *
 * <p>边界的类型</p>
 * <p>边界根据其{@link #getType()}所获得的类型，可用于描述基于边界值<em>t</em>的不等式:
 * <ul>
 *     <li>{@link #noneLowerBound()}：{@code {x | x > -∞}}；</li>
 *     <li>{@link #noneUpperBound()}：{@code {x | x < +∞}}；</li>
 *     <li>{@link #greaterThan}：{@code {x | x > t}}；</li>
 *     <li>{@link #atLeast}：{@code {x | x >= t}}；</li>
 *     <li>{@link #lessThan}：{@code {x | x < t}}；</li>
 *     <li>{@link #atMost}：{@code {x | x <= t}}；</li>
 * </ul>
 * 当作为{@link Predicate}使用时，可用于判断入参对象是否能满足当前实例所对应的不等式。
 *
 * <p>边界的比较</p>
 * <p>边界对象本身实现了{@link Comparable}接口，
 * 当使用{@link Comparable#compareTo}比较两个边界对象时，
 * 返回的比较值表示两个边界对象对应的点在实数轴上从左到右的先后顺序。<br>
 * 比如：
 * 若令当前边界点为<em>t1</em>，另一边界点为<em>t2</em>，则有
 * <ul>
 *     <li>-1：<em>t1</em>在<em>t2</em>的左侧；</li>
 *     <li>0：<em>t1</em>与<em>t2</em>所表示的点彼此重合；</li>
 *     <li>-1：<em>t1</em>在<em>t2</em>的右侧；</li>
 * </ul>
 *
 * @param <T> 边界值类型
 * @author huangchengxing
 * @see BoundType
 * @see BoundedRange
 * @since 6.0.0
 */
public interface Bound<T extends Comparable<? super T>> extends Predicate<T>, Comparable<Bound<T>> {

	/**
	 * 无穷小的描述
	 */
	String INFINITE_MIN = "-\u221e";

	/**
	 * 无穷大的藐视
	 */
	String INFINITE_MAX = "+\u221e";

	// region --------------- static methods
	/**
	 * {@code {x | x > -∞}}
	 *
	 * @param <T> 边界值类型
	 * @return 区间
	 */
	@SuppressWarnings("unchecked")
	static <T extends Comparable<? super T>> Bound<T> noneLowerBound() {
		return NoneLowerBound.INSTANCE;
	}

	/**
	 * {@code {x | x < +∞}}
	 *
	 * @param <T> 边界值类型
	 * @return 区间
	 */
	@SuppressWarnings("unchecked")
	static <T extends Comparable<? super T>> Bound<T> noneUpperBound() {
		return NoneUpperBound.INSTANCE;
	}

	/**
	 * {@code {x | x > min}}
	 *
	 * @param min 最小值
	 * @param <T> 边界值类型
	 * @return 区间
	 */
	static <T extends Comparable<? super T>> Bound<T> greaterThan(final T min) {
		return new FiniteBound<>(Objects.requireNonNull(min), BoundType.OPEN_LOWER_BOUND);
	}

	/**
	 * {@code {x | x >= min}}
	 *
	 * @param min 最小值
	 * @param <T> 边界值类型
	 * @return 区间
	 */
	static <T extends Comparable<? super T>> Bound<T> atLeast(final T min) {
		return new FiniteBound<>(Objects.requireNonNull(min), BoundType.CLOSE_LOWER_BOUND);
	}

	/**
	 * {@code {x | x < max}}
	 *
	 * @param max 最大值
	 * @param <T> 边界值类型
	 * @return 区间
	 */
	static <T extends Comparable<? super T>> Bound<T> lessThan(final T max) {
		return new FiniteBound<>(Objects.requireNonNull(max), BoundType.OPEN_UPPER_BOUND);
	}

	/**
	 * {@code {x | x <= max}}
	 *
	 * @param max 最大值
	 * @param <T> 边界值类型
	 * @return 区间
	 */
	static <T extends Comparable<? super T>> Bound<T> atMost(final T max) {
		return new FiniteBound<>(Objects.requireNonNull(max), BoundType.CLOSE_UPPER_BOUND);
	}
	// endregion --------------- static methods

	/**
	 * 获取边界值
	 *
	 * @return 边界值
	 */
	T getValue();

	/**
	 * 获取边界类型
	 *
	 * @return 边界类型
	 */
	BoundType getType();

	/**
	 * 检验指定值是否在当前边界表示的范围内
	 *
	 * @param t 要检验的值，不允许为{@code null}
	 * @return 是否
	 */
	@SuppressWarnings("AbstractMethodOverridesAbstractMethod")
	@Override
	boolean test(T t);

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
	@SuppressWarnings("AbstractMethodOverridesAbstractMethod")
	@Override
	int compareTo(final Bound<T> bound);

	/**
	 * 获取{@code "[value"}或{@code "(value"}格式的字符串
	 *
	 * @return 字符串
	 */
	String descBound();

	/**
	 * 对当前边界取反
	 *
	 * @return 取反后的边界
	 */
	@Override
	Bound<T> negate();

	/**
	 * 将当前实例转为一个区间
	 *
	 * @return 区间
	 */
	BoundedRange<T> toRange();

	/**
	 * 获得当前实例对应的{@code {x| x >= xxx}}格式的不等式字符串
	 *
	 * @return 字符串
	 */
	@Override
	String toString();
}
