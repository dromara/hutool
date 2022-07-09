package cn.hutool.core.lang.func;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.StrUtil;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 * 一些{@link Predicate}相关封装
 *
 * @author looly
 * @since 6.0.0
 */
public class PredicateUtil {

	public static <T> Predicate<T> negate(final Predicate<T> predicate) {
		return predicate.negate();
	}

	/**
	 * 多个条件转换为”与“复合条件，即所有条件都为true时，才返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	public static <T> Predicate<T> and(final Iterable<? extends Predicate<? super T>> components) {
		return new AndPredicate<>(ListUtil.of(components));
	}

	/**
	 * 多个条件转换为”与“复合条件，即所有条件都为true时，才返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> and(final Predicate<? super T>... components) {
		return new AndPredicate<>(components);
	}

	/**
	 * 多个条件转换为”或“复合条件，即任意一个条件都为true时，返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	public static <T> Predicate<T> or(final Iterable<? extends Predicate<? super T>> components) {
		return new OrPredicate<>(ListUtil.of(components));
	}

	/**
	 * 多个条件转换为”或“复合条件，即任意一个条件都为true时，返回true
	 *
	 * @param <T>        判断条件的对象类型
	 * @param components 多个条件
	 * @return 复合条件
	 */
	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> or(final Predicate<? super T>... components) {
		return new OrPredicate<>(components);
	}

	/**
	 * 多个{@link Predicate}的与，只有所有条件满足才为true，当任意一个条件的test为false，返回false
	 *
	 * @param <T> 泛型类型
	 * @author Guava
	 */
	private static class AndPredicate<T> implements Predicate<T>, Serializable {
		private static final long serialVersionUID = 1L;

		private final List<? extends Predicate<? super T>> components;

		@SafeVarargs
		private AndPredicate(final Predicate<? super T>... components) {
			this.components = ListUtil.of(components);
		}

		private AndPredicate(final List<? extends Predicate<? super T>> components) {
			this.components = components;
		}

		@Override
		public boolean test(final T t) {
			// Avoid using the Iterator to avoid generating garbage
			//noinspection ForLoopReplaceableByForEach
			for (int i = 0; i < components.size(); i++) {
				if (false == components.get(i).test(t)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			return "Predicates.and(" + StrUtil.join(",", this.components) + ")";
		}
	}

	/**
	 * 多个{@link Predicate}的或操作，即当一个条件满足，则全部满足，当任意一个条件的test为true，返回true
	 *
	 * @param <T> 泛型类型
	 * @author Guava
	 */
	private static class OrPredicate<T> implements Predicate<T>, Serializable {
		private static final long serialVersionUID = 1L;

		private final List<? extends Predicate<? super T>> components;

		@SafeVarargs
		private OrPredicate(final Predicate<? super T>... components) {
			this.components = ListUtil.of(components);
		}

		private OrPredicate(final List<? extends Predicate<? super T>> components) {
			this.components = components;
		}

		@Override
		public boolean test(final T t) {
			// Avoid using the Iterator to avoid generating garbage
			//noinspection ForLoopReplaceableByForEach
			for (int i = 0; i < components.size(); i++) {
				if (false == components.get(i).test(t)) {
					return true;
				}
			}
			return false;
		}
	}
}
