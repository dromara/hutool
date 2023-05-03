package org.dromara.hutool.core.lang.hierarchy;

import org.dromara.hutool.core.lang.mutable.Mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 用于遍历层级结构的迭代器，提供了遍历过程中的回调方法：
 * <ul>
 *     <li>{@link #isBreak}: 是否中断遍历，其在{@link #nextHierarchies}之前调用；</li>
 *     <li>{@link #nextHierarchies}: 获得下一需要遍历的层级，当为空时结束；</li>
 *     <li>{@link #getResult}: 当遍历完成后，获取迭代器结果；</li>
 * </ul>
 *
 * <p>默认提供了三类{@link HierarchyIterator}实现：
 * <ul>
 *     <li>scan: 遍历所有的层级结构；</li>
 *     <li>collector: 收集层级结构中所有符合条件的结果，并在结束遍历后返回所有结果；</li>
 *     <li>find: 找到层级结构中符合条件的结果后立刻中断遍历，并返回该结果；</li>
 * </ul>
 * 可以实现自定义的{@link HierarchyIterator}来支持更多的遍历模式。
 *
 * @param <H> 层级类型
 * @param <R> 结果类型
 * @author huangchengxing
 * @since 6.0.0
 */
@FunctionalInterface
public interface HierarchyIterator<H, R> {

	/**
	 * 获取下一层级
	 *
	 * @param result 结果
	 * @param hierarchy 当前层级
	 * @return 向容器中添加元素的方法
	 */
	Collection<H> nextHierarchies(R result, H hierarchy);

	/**
	 * 是否中断遍历
	 *
	 * @param hierarchy 当前层级
	 * @return 是否中断遍历
	 */
	default boolean isBreak(H hierarchy) {
		return false;
	}

	/**
	 * 获取结果
	 *
	 * @return 结果
	 */
	default R getResult() {
		return null;
	}

	/**
	 * 创建一个{@link HierarchyIterator}对象, 当{@code finder}返回非空时, 迭代器立刻中断, 返回结果
	 *
	 * @param function 迭代器处理函数
	 * @param finder 查找器
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @return {@link HierarchyIterator}
	 */
	static <H, R> HierarchyIterator<H, R> find(
		final Function<H, Collection<H>> function, final Function<H, R> finder) {
		Objects.requireNonNull(function);
		Objects.requireNonNull(finder);
		final Mutable<R> mutable = Mutable.of(null);
		final Predicate<H> terminator = h -> {
			R r = finder.apply(h);
			if (r != null) {
				mutable.set(r);
				return true;
			}
			return false;
		};
		return new HierarchyIteratorImpl<>(mutable::get, terminator, (r, h) -> function.apply(h));
	}

	/**
	 * 创建一个{@link HierarchyIterator}对象, 迭代器结果总是为{@code null}
	 *
	 * @param function 迭代器处理函数
	 * @param terminator 是否终止遍历
	 * @param <H> 层级结构类型
	 * @return {@link HierarchyIterator}
	 */
	static <H> HierarchyIterator<H, Void> scan(
		final Function<H, Collection<H>> function, final Predicate<H> terminator) {
		Objects.requireNonNull(function);
		return new HierarchyIteratorImpl<>(() -> null, terminator, (r, h) -> function.apply(h));
	}

	/**
	 * 创建一个{@link HierarchyIterator}对象, 迭代器结果总是为{@code null}
	 *
	 * @param function 迭代器处理函数
	 * @param <H> 层级结构类型
	 * @return {@link HierarchyIterator}
	 */
	static <H> HierarchyIterator<H, Void> scan(final Function<H, Collection<H>> function) {
		return scan(function, h -> false);
	}

	/**
	 * 创建一个{@link HierarchyIterator}对象, 若{@code mapper}返回非空, 则将结果添加到集合中，最终返回集合
	 *
	 * @param function 迭代器处理函数
	 * @param collFactory 集合工厂
	 * @param mapper 迭代器结果映射函数
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @param <C> 集合类型
	 * @return {@link HierarchyIterator}
	 */
	static <H, R, C extends Collection<R>> HierarchyIterator<H, C> collect(
		final Function<H, Collection<H>> function, final Supplier<C> collFactory, final Function<H, R> mapper) {
		Objects.requireNonNull(function);
		Objects.requireNonNull(collFactory);
		Objects.requireNonNull(mapper);
		final C collection = collFactory.get();
		return new HierarchyIteratorImpl<>(() -> collection, h -> false, (r, h) -> {
			final R apply = mapper.apply(h);
			if (Objects.nonNull(apply)) {
				collection.add(apply);
			}
			return function.apply(h);
		});
	}

	/**
	 * 创建一个{@link HierarchyIterator}对象, 若{@code mapper}返回非空, 则将结果添加到集合中，最终返回集合
	 *
	 * @param function 迭代器处理函数
	 * @param mapper 迭代器结果映射函数
	 * @param <H> 层级结构类型
	 * @param <R> 迭代器结果类型
	 * @return {@link HierarchyIterator}
	 */
	static <H, R> HierarchyIterator<H, List<R>> collect(
		final Function<H, Collection<H>> function, final Function<H, R> mapper) {
		return collect(function, ArrayList::new, mapper);
	}

	/**
	 * 创建一个{@link HierarchyIterator}对象, 则将非空结果添加到集合中，最终返回集合
	 *
	 * @param function 迭代器处理函数
	 * @param <H> 层级结构类型
	 * @return {@link HierarchyIterator}
	 */
	static <H> HierarchyIterator<H, List<H>> collect(
		final Function<H, Collection<H>> function) {
		return collect(function, Function.identity());
	}

	/**
	 * {@link HierarchyIterator}的基本实现。
	 *
	 * @param <H> 层级类型
	 * @param <R> 结果类型
	 */
	class HierarchyIteratorImpl<H, R> implements HierarchyIterator<H, R> {

		private final Supplier<? extends R> resultFactory;
		private final Predicate<? super H> hierarchyFilter;
		private final BiFunction<? super R, ? super H, ? extends Collection<H>> hierarchyFinder;

		public HierarchyIteratorImpl(
			final Supplier<? extends R> resultFactory, final Predicate<? super H> hierarchyFilter,
			final BiFunction<? super R, ? super H, ? extends Collection<H>> hierarchyFinder) {
			this.resultFactory = resultFactory;
			this.hierarchyFilter = hierarchyFilter;
			this.hierarchyFinder = hierarchyFinder;
		}

		/**
		 * 获取下一层级
		 *
		 * @param result    结果
		 * @param hierarchy 当前层级
		 * @return 向容器中添加元素的方法
		 */
		@Override
		public Collection<H> nextHierarchies(final R result, final H hierarchy) {
			return hierarchyFinder.apply(result, hierarchy);
		}

		/**
		 * 是否中断遍历
		 *
		 * @param hierarchy 当前层级
		 * @return 是否中断遍历
		 */
		@Override
		public boolean isBreak(final H hierarchy) {
			return hierarchyFilter.test(hierarchy);
		}

		/**
		 * 获取结果
		 *
		 * @return 结果
		 */
		@Override
		public R getResult() {
			return resultFactory.get();
		}
	}
}
