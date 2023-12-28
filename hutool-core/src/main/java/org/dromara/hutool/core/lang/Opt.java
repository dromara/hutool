/*
 * Copyright (c) 2012-2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.func.SerSupplier;
import org.dromara.hutool.core.stream.EasyStream;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 复制jdk16中的Optional，以及自己进行了一点调整和新增，比jdk8中的Optional多了几个实用的函数<br>
 * 详细见：<a href="https://gitee.com/dromara/hutool/pulls/426"></a>
 *
 * @param <T> 包裹里元素的类型
 * @author VampireAchao
 * @author Cizai
 * @author kongweiguang
 * @see java.util.Optional
 */
public class Opt<T> {
	/**
	 * 一个空的{@code Opt}
	 */
	private static final Opt<?> EMPTY = new Opt<>(null);

	/**
	 * 返回一个空的{@code Opt}
	 *
	 * @param <T> 包裹里元素的类型
	 * @return Opt
	 */
	public static <T> Opt<T> empty() {
		@SuppressWarnings("unchecked") final Opt<T> t = (Opt<T>) EMPTY;
		return t;
	}

	/**
	 * 返回一个包裹里元素不可能为空的{@code Opt}
	 *
	 * @param value 包裹里的元素
	 * @param <T>   包裹里元素的类型
	 * @return 一个包裹里元素不可能为空的 {@code Opt}
	 * @throws NullPointerException 如果传入的元素为空，抛出 {@code NPE}
	 */
	public static <T> Opt<T> of(final T value) {
		return new Opt<>(Objects.requireNonNull(value));
	}

	/**
	 * 返回一个包裹里元素可能为空的{@code Opt}
	 *
	 * @param value 传入需要包裹的元素
	 * @param <T>   包裹里元素的类型
	 * @return 一个包裹里元素可能为空的 {@code Opt}
	 */
	public static <T> Opt<T> ofNullable(final T value) {
		return value == null ? empty() : new Opt<>(value);
	}

	/**
	 * 返回一个包裹里元素可能为空的{@code Opt}，额外判断了空字符串的情况
	 *
	 * @param <T>   字符串类型
	 * @param value 传入需要包裹的元素
	 * @return 一个包裹里元素可能为空，或者为空字符串的 {@code Opt}
	 */
	public static <T extends CharSequence> Opt<T> ofBlankAble(final T value) {
		return StrUtil.isBlank(value) ? empty() : new Opt<>(value);
	}

	/**
	 * 返回一个包裹里{@code List}集合可能为空的{@code Opt}，额外判断了集合内元素为空的情况
	 *
	 * @param <T>   包裹里元素的类型
	 * @param <R>   集合值类型
	 * @param value 传入需要包裹的元素，支持CharSequence、Map、Iterable、Iterator、Array类型
	 * @return 一个包裹里元素可能为空的 {@code Opt}
	 * @since 5.7.17
	 */
	public static <T, R extends Collection<T>> Opt<R> ofEmptyAble(final R value) {
		return ObjUtil.isEmpty(value) ? empty() : new Opt<>(value);
	}

	/**
	 * @param supplier 操作
	 * @param <T>      类型
	 * @return 操作执行后的值
	 */
	public static <T> Opt<T> ofTry(final SerSupplier<T> supplier) {
		try {
			return ofNullable(supplier.getting());
		} catch (final Throwable e) {
			final Opt<T> empty = new Opt<>(null);
			empty.throwable = e;
			return empty;
		}
	}

	/**
	 * 根据 {@link Optional} 构造 {@code Opt}
	 *
	 * @param optional optional
	 * @param <T>      包裹的元素类型
	 * @return 一个包裹里元素可能为空的 {@code Opt}
	 * @since 6.0.0
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static <T> Opt<T> of(final Optional<T> optional) {
		return ofNullable(optional.orElse(null));
	}

	/**
	 * 包裹里实际的元素
	 */
	private final T value;
	private Throwable throwable;

	/**
	 * {@code Opt}的构造函数
	 *
	 * @param value 包裹里的元素
	 */
	private Opt(final T value) {
		this.value = value;
	}

	/**
	 * 返回包裹里的元素，取不到则为{@code null}，注意！！！此处和{@link java.util.Optional#get()}不同的一点是本方法并不会抛出{@code NoSuchElementException}
	 * 如果元素为空，则返回{@code null}，如果需要一个绝对不能为{@code null}的值，则使用{@link #orElseThrow()}
	 *
	 * <p>
	 * 如果需要一个绝对不能为 {@code null}的值，则使用{@link #orElseThrow()}
	 * 做此处修改的原因是，有时候我们确实需要返回一个null给前端，并且这样的时候并不少见
	 * 而使用 {@code .orElse(null)}需要写整整12个字符，用{@code .get()}就只需要6个啦
	 *
	 * @return 包裹里的元素，有可能为{@code null}
	 */
	public T get() {
		return this.value;
	}

	/**
	 * 判断包裹里元素的值是否不存在，不存在为 {@code true}，否则为{@code false}
	 *
	 * @return 包裹里元素的值不存在 则为 {@code true}，否则为{@code false}
	 * @since 11 这是jdk11{@link java.util.Optional}中的新函数
	 */
	public boolean isEmpty() {
		return value == null;
	}

	/**
	 * 获取异常<br>
	 * 当调用 {@link #ofTry(SerSupplier)}时，异常信息不会抛出，而是保存，调用此方法获取抛出的异常
	 *
	 * @return 异常
	 * @since 5.7.17
	 */
	public Throwable getThrowable() {
		return this.throwable;
	}

	/**
	 * 是否失败<br>
	 * 当调用 {@link #ofTry(SerSupplier)}时，抛出异常则表示失败
	 *
	 * @return 是否失败
	 * @since 5.7.17
	 */
	public boolean isFail() {
		return null != this.throwable;
	}

	/**
	 * 如果包裹内容失败了，则执行传入的操作({@link Consumer#accept})
	 *
	 * <p> 例如执行有异常就打印结果
	 * <pre>{@code
	 *     Opt.ofTry(() -> 1 / 0).ifFail(Console::log);
	 * }</pre>
	 *
	 * @param action 你想要执行的操作
	 * @return this
	 * @throws NullPointerException 如果包裹里的值存在，但你传入的操作为{@code null}时抛出
	 */
	public Opt<T> ifFail(final Consumer<? super Throwable> action) throws NullPointerException {
		Objects.requireNonNull(action, "action is null");

		if (isFail()) {
			action.accept(throwable);
		}

		return this;
	}

	/**
	 * 如果包裹内容失败了，同时是指定的异常执行传入的操作({@link Consumer#accept})
	 *
	 * <p> 例如如果值存在就打印结果
	 * <pre>{@code
	 *     Opt.ofTry(() -> 1 / 0).ifFail(Console::log, ArithmeticException.class);
	 * }</pre>
	 *
	 * @param action 你想要执行的操作
	 * @param exs    限定的异常
	 * @return this
	 * @throws NullPointerException 如果包裹里的值存在，但你传入的操作为{@code null}时抛出
	 */
	@SafeVarargs
	public final Opt<T> ifFail(final Consumer<? super Throwable> action, final Class<? extends Throwable>... exs) throws NullPointerException {
		Objects.requireNonNull(action, "action is null");

		if (isFail() && EasyStream.of(exs).anyMatch(e -> e.isAssignableFrom(throwable.getClass()))) {
			action.accept(throwable);
		}

		return this;
	}

	/**
	 * 判断包裹里元素的值是否存在，存在为 {@code true}，否则为{@code false}
	 *
	 * @return 包裹里元素的值存在为 {@code true}，否则为{@code false}
	 */
	public boolean isPresent() {
		return value != null;
	}

	/**
	 * 如果包裹里的值存在，就执行传入的操作({@link Consumer#accept})
	 *
	 * <p> 例如如果值存在就打印结果
	 * <pre>{@code
	 * Opt.ofNullable("Hello Hutool!").ifPresent(Console::log);
	 * }</pre>
	 *
	 * @param action 你想要执行的操作
	 * @return this
	 * @throws NullPointerException 如果包裹里的值存在，但你传入的操作为{@code null}时抛出
	 */
	public Opt<T> ifPresent(final Consumer<? super T> action) {
		if (isPresent()) {
			action.accept(value);
		}
		return this;
	}

	/**
	 * 判断包裹里的值存在并且与给定的条件是否满足 ({@link Predicate#test}执行结果是否为true)
	 * 如果满足条件则返回本身
	 * 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opt}
	 *
	 * @param predicate 给定的条件
	 * @return 如果满足条件则返回本身, 不满足条件或者元素本身为空时返回一个空的{@code Opt}
	 * @throws NullPointerException 如果给定的条件为 {@code null}抛出{@code NPE}
	 */
	public Opt<T> filter(final Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (isEmpty() || isFail()) {
			return this;
		} else {
			return predicate.test(value) ? this : empty();
		}
	}

	/**
	 * 如果包裹里的值存在，就执行传入的操作({@link Function#apply})并返回一个包裹了该操作返回值的{@code Opt}
	 * 如果不存在，返回一个空的{@code Opt}
	 *
	 * @param mapper 值存在时执行的操作
	 * @param <U>    操作返回值的类型
	 * @return 如果包裹里的值存在，就执行传入的操作({@link Function#apply})并返回一个包裹了该操作返回值的{@code Opt}，
	 * 如果不存在，返回一个空的{@code Opt}
	 * @throws NullPointerException 如果给定的操作为 {@code null}，抛出 {@code NPE}
	 */
	@SuppressWarnings("unchecked")
	public <U> Opt<U> map(final Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (isFail()) {
			return (Opt<U>) this;
		} else if (isEmpty()) {
			return empty();
		} else {
			return Opt.ofNullable(mapper.apply(value));
		}
	}

	/**
	 * 如果包裹里的值存在，就执行传入的操作({@link Function#apply})并返回该操作返回值
	 * 如果不存在，返回一个空的{@code Opt}
	 * 和 {@link Opt#map}的区别为 传入的操作返回值必须为 Opt
	 *
	 * @param mapper 值存在时执行的操作
	 * @param <U>    操作返回值的类型
	 * @return 如果包裹里的值存在，就执行传入的操作({@link Function#apply})并返回该操作返回值
	 * 如果不存在，返回一个空的{@code Opt}
	 * @throws NullPointerException 如果给定的操作为 {@code null}或者给定的操作执行结果为 {@code null}，抛出 {@code NPE}
	 */
	@SuppressWarnings("unchecked")
	public <U> Opt<U> flatMap(final Function<? super T, ? extends Opt<? extends U>> mapper) {
		Objects.requireNonNull(mapper);
		if (isFail()) {
			return (Opt<U>) this;
		} else if (isEmpty()) {
			return empty();
		} else {
			@SuppressWarnings("unchecked") final Opt<U> r = (Opt<U>) mapper.apply(value);
			return Objects.requireNonNull(r);
		}
	}

	/**
	 * 如果包裹里的值存在，就执行传入的操作({@link Function#apply})并返回该操作返回值
	 * 如果不存在，返回一个空的{@code Opt}
	 * 和 {@link Opt#map}的区别为 传入的操作返回值必须为 {@link Optional}
	 *
	 * @param mapper 值存在时执行的操作
	 * @param <U>    操作返回值的类型
	 * @return 如果包裹里的值存在，就执行传入的操作({@link Function#apply})并返回该操作返回值
	 * 如果不存在，返回一个空的{@code Opt}
	 * @throws NullPointerException 如果给定的操作为 {@code null}或者给定的操作执行结果为 {@code null}，抛出 {@code NPE}
	 * @see Optional#flatMap(Function)
	 * @since 5.7.16
	 */
	@SuppressWarnings("unchecked")
	public <U> Opt<U> flattedMap(final Function<? super T, ? extends Optional<? extends U>> mapper) {
		Objects.requireNonNull(mapper);
		if (isFail()) {
			return (Opt<U>) this;
		} else if (isEmpty()) {
			return empty();
		} else {
			return ofNullable(mapper.apply(value).orElse(null));
		}
	}

	/**
	 * 如果包裹里元素的值存在，就执行对应的操作，并返回本身
	 * 如果不存在，返回一个空的{@code Opt}
	 *
	 * <p>属于 {@link #ifPresent}的链式拓展
	 *
	 * @param action 值存在时执行的操作
	 * @return this
	 * @throws NullPointerException 如果值存在，并且传入的操作为 {@code null}
	 * @author VampireAchao
	 */
	public Opt<T> peek(final Consumer<T> action) throws NullPointerException {
		return ifPresent(action);
	}


	/**
	 * 如果包裹里元素的值存在，就执行对应的操作集，并返回本身
	 * 如果不存在，返回一个空的{@code Opt}
	 *
	 * <p>属于 {@link #ifPresent}的链式拓展
	 * <p>属于 {@link #peek(Consumer)}的动态拓展
	 *
	 * @param actions 值存在时执行的操作，动态参数，可传入数组，当数组为一个空数组时并不会抛出 {@code NPE}
	 * @return this
	 * @throws NullPointerException 如果值存在，并且传入的操作集中的元素为 {@code null}
	 * @author VampireAchao
	 */
	@SafeVarargs
	public final Opt<T> peeks(final Consumer<T>... actions) throws NullPointerException {
		return peek(Stream.of(actions).reduce(Consumer::andThen).orElseGet(() -> o -> {
		}));
	}

	/**
	 * 如果包裹里元素的值存在，就返回本身，如果不存在，则使用传入的操作执行后获得的 {@code Opt}
	 *
	 * @param supplier 不存在时的操作
	 * @return 如果包裹里元素的值存在，就返回本身，如果不存在，则使用传入的函数执行后获得的 {@code Opt}
	 * @throws NullPointerException 如果传入的操作为空，或者传入的操作执行后返回值为空，则抛出 {@code NPE}
	 */
	public Opt<T> or(final Supplier<? extends Opt<? extends T>> supplier) {
		Objects.requireNonNull(supplier);
		if (isPresent()) {
			return this;
		} else {
			@SuppressWarnings("unchecked") final Opt<T> r = (Opt<T>) supplier.get();
			return Objects.requireNonNull(r);
		}
	}

	/**
	 * 如果包裹里元素的值存在，就返回一个包含该元素的 {@link Stream},
	 * 否则返回一个空元素的 {@link Stream}
	 *
	 * <p> 该方法能将 Opt 中的元素传递给 {@link Stream}
	 * <pre>{@code
	 *     Stream<Opt<T>> os = ..
	 *     Stream<T> s = os.flatMap(Opt::stream)
	 * }</pre>
	 *
	 * @return 返回一个包含该元素的 {@link Stream}或空的 {@link Stream}
	 */
	public Stream<T> stream() {
		if (isEmpty()) {
			return Stream.empty();
		} else {
			return Stream.of(value);
		}
	}

	/**
	 * 如果包裹里元素的值存在，则返回该值，否则返回传入的{@code other}
	 *
	 * @param other 元素为空时返回的值，有可能为 {@code null}.
	 * @return 如果包裹里元素的值存在，则返回该值，否则返回传入的{@code other}
	 */
	public T orElse(final T other) {
		return isPresent() ? value : other;
	}

	/**
	 * 异常则返回另一个可选值
	 *
	 * @param other 可选值
	 * @return 如果未发生异常，则返回该值，否则返回传入的{@code other}
	 * @since 5.7.17
	 */
	public T exceptionOrElse(final T other) {
		return isFail() ? other : value;
	}

	/**
	 * 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值
	 *
	 * @param supplier 值不存在时需要执行的操作，返回一个类型与 包裹里元素类型 相同的元素
	 * @return 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值
	 * @throws NullPointerException 如果之不存在，并且传入的操作为空，则抛出 {@code NPE}
	 */
	public T orElseGet(final Supplier<? extends T> supplier) {
		return isPresent() ? value : supplier.get();
	}

	/**
	 * 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值
	 *
	 * @param supplier 值不存在时需要执行的操作，返回一个类型与 包裹里元素类型 相同的元素
	 * @return 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值
	 * @throws NullPointerException 如果之不存在，并且传入的操作为空，则抛出 {@code NPE}
	 */
	public Opt<T> orElseOpt(final Supplier<? extends T> supplier) {
		return or(() -> ofNullable(supplier.get()));
	}

	/**
	 * 如果包裹里元素的值存在，则返回该值，否则执行传入的操作
	 *
	 * @param action 值不存在时执行的操作
	 * @return 如果包裹里元素的值存在，则返回该值，否则执行传入的操作
	 * @throws NullPointerException 如果值不存在，并且传入的操作为 {@code null}
	 */
	public T orElseRun(final Runnable action) {
		if (isPresent()) {
			return value;
		} else {
			action.run();
			return null;
		}
	}

	/**
	 * 如果包裹里的值存在，则返回该值，否则抛出 {@code NoSuchElementException}
	 *
	 * @return 返回一个不为 {@code null} 的包裹里的值
	 * @throws NoSuchElementException 如果包裹里的值不存在则抛出该异常
	 */
	public T orElseThrow() {
		return orElseThrow(() -> new NoSuchElementException("No value present"));
	}

	/**
	 * 如果包裹里的值存在，则返回该值，否则执行传入的操作，获取异常类型的返回值并抛出
	 * <p>往往是一个包含无参构造器的异常 例如传入{@code IllegalStateException::new}
	 *
	 * @param <X>               异常类型
	 * @param exceptionSupplier 值不存在时执行的操作，返回值继承 {@link Throwable}
	 * @return 包裹里不能为空的值
	 * @throws X                    如果值不存在
	 * @throws NullPointerException 如果值不存在并且 传入的操作为 {@code null}或者操作执行后的返回值为{@code null}
	 */
	public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
		if (isPresent()) {
			return value;
		} else {
			throw exceptionSupplier.get();
		}
	}

	/**
	 * 转换为 {@link Optional}对象
	 *
	 * @return {@link Optional}对象
	 * @since 5.7.16
	 */
	public Optional<T> toOptional() {
		return Optional.ofNullable(value);
	}

	/**
	 * 转换为 {@link EasyStream}对象
	 *
	 * @return {@link EasyStream}对象
	 */
	public EasyStream<T> toEasyStream() {
		return EasyStream.of(value);
	}

	/**
	 * 判断传入参数是否与 {@code Opt}相等
	 * 在以下情况下返回true
	 * <ul>
	 * <li>它也是一个 {@code Opt} 并且
	 * <li>它们包裹住的元素都为空 或者
	 * <li>它们包裹住的元素之间相互 {@code equals()}
	 * </ul>
	 *
	 * @param obj 一个要用来判断是否相等的参数
	 * @return 如果传入的参数也是一个 {@code Opt}并且它们包裹住的元素都为空
	 * 或者它们包裹住的元素之间相互 {@code equals()} 就返回{@code true}
	 * 否则返回 {@code false}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Opt)) {
			return false;
		}

		final Opt<?> other = (Opt<?>) obj;
		return Objects.equals(value, other.value);
	}

	/**
	 * 如果包裹内元素为空，则返回0，否则返回元素的 {@code hashcode}
	 *
	 * @return 如果包裹内元素为空，则返回0，否则返回元素的 {@code hashcode}
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	/**
	 * 返回包裹内元素调用{@code toString()}的结果，不存在则返回{@code null}
	 *
	 * @return 包裹内元素调用{@code toString()}的结果，不存在则返回{@code null}
	 */
	@Override
	public String toString() {
		return StrUtil.toStringOrNull(value);
	}
}
