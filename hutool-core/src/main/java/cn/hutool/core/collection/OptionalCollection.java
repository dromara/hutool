package cn.hutool.core.collection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 集合optional操作类, 具体用法与{@link Optional}类似
 * <p>
 * 以下是常见的示例：
 * <pre>{@code
 *
 * // 非空则进行过滤与打印操作
 * OptionalCollection.ofEmpty(list)
 *         .filter(collection -> collection.contains("aaa"))
 *         .ifNotEmpty(collection -> System.out.println("filter: " + collection));
 *
 * // 非空则根据id列表删除数据
 * List<String> ids = new ArrayList();
 * OptionalCollection.ofEmpty(ids)
 *         .ifNotEmpty(userAddressMapper::deleteBatchIds);
 *
 * // 集合空则抛异常
 * List<String> list = null;
 * String exceptionMsg = "Collection is empty";
 * OptionalCollection.ofEmpty(list)
 *         .filter(collection -> collection.contains("aaa"))
 *         .orEmptyThrow(() -> new IllegalArgumentException(exceptionMsg));
 * }</pre>
 *
 * @param <E> 集合元素类型
 * @author Wilson-He
 */
public class OptionalCollection<E> {
	private static final OptionalCollection<?> EMPTY = new OptionalCollection<>();
	private final Collection<E> collection;

	private OptionalCollection() {
		this.collection = null;
	}

	private OptionalCollection(Collection<E> collection) {
		this.collection = Objects.requireNonNull(collection);
	}

	/**
	 * Returns an {@code OptionalCollection} with the specified present non-null collection.
	 *
	 * @param <T>        the class of the collection elements
	 * @param collection the collection to be present, which must be non-null
	 * @return an {@code OptionalCollection} with the collection present
	 * @throws NullPointerException if collection is null
	 */
	public static <T> OptionalCollection<T> of(Collection<T> collection) {
		return new OptionalCollection<>(collection);
	}

	/**
	 * Returns an {@code OptionalCollection} describing the specified collection, if non-empty,
	 * otherwise returns an empty {@code OptionalCollection}.
	 *
	 * @param <T>        the class of the collection elements
	 * @param collection the possibly-empty collection to describe
	 * @return an {@code OptionalCollection} with a present collection if the specified collection
	 * is non-empty, otherwise an empty {@code OptionalCollection}
	 */
	public static <T> OptionalCollection<T> ofEmpty(Collection<T> collection) {
		return collection == null ? empty() : of(collection);
	}

	/**
	 * return an empty {@code OptionalCollection}
	 *
	 * @return an empty {@code OptionalCollection}
	 */
	@SuppressWarnings("unchecked")
	public static <T> OptionalCollection<T> empty() {
		return (OptionalCollection<T>) EMPTY;
	}

	/**	 * Fetch stream from the collection.
	 *
	 * @return the collection stream, if collection is empty, then will return a empty stream
	 */
	@SuppressWarnings("unchecked")
	public Stream<E> stream() {
		return collection != null ? collection.stream() : (Stream<E>) Collections.emptyList().stream();
	}

	/**
	 * Map the collection to an {@code OptionalCollection} instance with an new {@code collection} by mapper function.
	 *
	 * @param mapper map function
	 * @return result collection
	 */
	public <U> OptionalCollection<U> map(Function<Collection<E>, Collection<U>> mapper) {
		Objects.requireNonNull(mapper);
		return isEmpty() ? empty() : OptionalCollection.ofEmpty(mapper.apply(collection));
	}

	/**
	 * Checks the {@code collection} is empty.
	 *
	 * @return <code>true</code> if the {@code collection} is empty
	 */
	public boolean isEmpty() {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Checks the {@code collection} is not empty.
	 *
	 * @return <code>true</code> if the {@code collection} is not empty
	 */
	public boolean isNotEmpty() {
		return !isEmpty();
	}

	/**
	 * 若集合为空, 或与predicate.test()返回{@code true}, 则返回当前collection, 否则返回空集合
	 *
	 * @param predicate a predicate to apply to the collection, if present
	 * @return an {@code OptionalCollection} describing the collection of this {@link OptionalCollection}
	 * if a collection is present and the collection matches the given predicate,
	 * otherwise an empty {@link OptionalCollection}
	 * @throws NullPointerException if the predicate is null
	 */
	public OptionalCollection<E> filter(Predicate<? super Collection<E>> predicate) {
		Objects.requireNonNull(predicate);
		return isEmpty() || predicate.test(collection) ? this : empty();
	}


	/**
	 * 若集合不为空, 且与predicate.test()返回{@code true}, 则返回当前collection, 否则返回{@code orElse}集合
	 *
	 * @param predicate a predicate to apply to the collection, if present
	 * @param orElse    predicate执行为false则返回该值
	 * @return an {@code OptionalCollection} describing the collection of this {@code OptionalCollection}
	 * if a collection is present and the collection matches the given predicate,
	 * otherwise an {@code orElse} {@link OptionalCollection}
	 * @throws NullPointerException if the predicate is null
	 */
	public OptionalCollection<E> filterOrElse(Predicate<? super Collection<E>> predicate, Collection<E> orElse) {
		Objects.requireNonNull(predicate);
		return isNotEmpty() && predicate.test(collection) ? this : ofEmpty(orElse);
	}

	/**
	 * Return {@code true} if there is a collection present, otherwise {@code false}.
	 *
	 * @return {@code true} if there is a collection present, otherwise {@code false}
	 */
	public boolean isPresent() {
		return collection != null;
	}

	/**
	 * If a collection is present in this {@code OptionalCollection}, returns the collection,
	 * otherwise throws {@code NoSuchElementException}.
	 *
	 * @return the non-null collection held by this {@code OptionalCollection}
	 * @throws NoSuchElementException if there is no collection present
	 * @see OptionalCollection#isPresent()
	 */
	public Collection<E> get() {
		if (collection == null) {
			throw new NoSuchElementException("No collection present");
		}
		return collection;
	}

	/**
	 * Return the collection if not null, otherwise return {@code other}.
	 *
	 * @param other the collection to be returned if collection is null, may be null
	 * @return the collection, if null, otherwise {@code other}
	 */
	public Collection<E> orElse(Collection<E> other) {
		return collection != null ? collection : other;
	}

	/**
	 * Return the collection if not empty, otherwise return {@code other}.
	 *
	 * @param other the collection to be returned if collection is empty, may be null
	 * @return the collection, if empty, otherwise {@code other}
	 */
	public Collection<E> orEmptyElse(Collection<E> other) {
		return isEmpty() ? other : collection;
	}

	/**
	 * Return the collection if collection is not null, otherwise invoke {@code other} and return
	 * the result of that invocation.
	 *
	 * @param other a {@code Supplier} whose result is returned if collection is null
	 * @return the collection if not null otherwise the result of {@code other.get()}
	 * @throws NullPointerException if collection is null and {@code other} is null
	 */
	public Collection<E> orNullGet(Supplier<Collection<E>> other) {
		return collection != null ? collection : other.get();
	}


	/**
	 * Return the collection if collection is not empty, otherwise invoke {@code other} and return
	 * the result of that invocation.
	 *
	 * @param other a {@code Supplier} whose result is returned if collection is empty
	 * @return the collection if not empty otherwise the result of {@code other.get()}
	 * @throws NullPointerException if {@code other} is null
	 */
	public Collection<E> orEmptyGet(Supplier<Collection<E>> other) {
		return isEmpty() ? other.get() : collection;
	}

	/**
	 * If a collection is present, invoke the specified consumer with the collection,
	 * otherwise do nothing.
	 *
	 * <pre>
	 *
	 * </pre>
	 *
	 * @param consumer block to be executed if a collection is present
	 * @throws NullPointerException if collection is not empty and {@code consumer} is null
	 */
	public void ifPresent(Consumer<Collection<E>> consumer) {
		Objects.requireNonNull(consumer);
		if (collection != null) {
			consumer.accept(collection);
		}
	}

	/**
	 * If the collection is present(not null), invoke the specified function with the collection,
	 * otherwise return {@code orElse}.
	 *
	 * @param function block to be executed if a collection is present and return result
	 * @return If the collection is present, return function apply result, otherwise {@code orElse}
	 * @throws NullPointerException if collection is present and {@code consumer} is
	 *                              null
	 */
	public <V> V ifPresentOrElse(Function<Collection<E>, V> function, V orElse) {
		Objects.requireNonNull(function);
		return collection != null ? function.apply(collection) : orElse;
	}

	/**
	 * If a collection is not empty, invoke the specified consumer with the collection,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if a collection is not empty
	 * @throws NullPointerException if {@code consumer} is null
	 */
	public void ifNotEmpty(Consumer<Collection<E>> consumer) {
		Objects.requireNonNull(consumer);
		if (isNotEmpty()) {
			consumer.accept(collection);
		}
	}

	/**
	 * If the collection is present, invoke the specified function with the collection,
	 * otherwise return {@code orElse}.
	 *
	 * @param function block to be executed if a collection is present and return result
	 * @return If the collection is not empty, return function apply result, otherwise{@code orElse}
	 * @throws NullPointerException if {@code function} is null
	 */
	public <V> V ifNotEmptyOrElse(Function<Collection<E>, V> function, V orElse) {
		Objects.requireNonNull(function);
		return isNotEmpty() ? function.apply(collection) : orElse;
	}


	/**
	 * Return the contained collection, if not null, otherwise throw an exception
	 * to be created by the provided supplier.
	 *
	 * @param <X>               Type of the exception to be thrown
	 * @param exceptionSupplier The supplier which will return the exception to
	 *                          be thrown
	 * @return the present collection
	 * @throws X                    if the contained collection is null
	 * @throws NullPointerException if  {@code exceptionSupplier} is null
	 * @apiNote A method reference to the exception constructor with an empty
	 * argument list can be used as the supplier. For example,
	 * {@code IllegalStateException::new}
	 */
	public <X extends Throwable> Collection<E> orNullThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (collection != null) {
			return collection;
		} else {
			throw exceptionSupplier.get();
		}
	}

	/**
	 * Return the contained collection, if not empty, otherwise throw an exception
	 * to be created by the provided supplier.
	 *
	 * @param <X>               Type of the exception to be thrown
	 * @param exceptionSupplier The supplier which will return the exception to
	 *                          be thrown
	 * @return the present collection
	 * @throws X                    if the contained collection is empty
	 * @throws NullPointerException if  {@code exceptionSupplier} is null
	 * @apiNote A method reference to the exception constructor with an empty
	 * argument list can be used as the supplier. For example,
	 * {@code IllegalStateException::new}
	 */
	public <X extends Throwable> Collection<E> orEmptyThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (isNotEmpty()) {
			return collection;
		} else {
			throw exceptionSupplier.get();
		}
	}

	/**
	 * Indicates whether some other object is "equal to" this OptionalCollection. The
	 * other object is considered equal if:
	 * <ul>
	 * <li>it is also an {@code Optional} and;
	 * <li>both instances have no collection present or;
	 * <li>the present collections are "equal to" each other via {@code equals()}.
	 * </ul>
	 *
	 * @param obj an object to be tested for equality
	 * @return {code true} if the other object is "equal to" this object
	 * otherwise {@code false}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OptionalCollection)) {
			return false;
		}
		OptionalCollection<?> other = (OptionalCollection<?>) obj;
		return Objects.equals(collection, other.collection);
	}

	/**
	 * Returns the hash code value of the present collection, if any, or 0 (zero) if
	 * no collection is present.
	 *
	 * @return hash code value of the present collection or 0 if no collection is present
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(collection);
	}

	/**
	 * Wraps collection.toString() with OptionalCollection.
	 *
	 * @return "OptionalCollection[{collection.toString()}]" or "OptionalCollection.empty"
	 */
	@Override
	public String toString() {
		return collection != null ? String.format("OptionalCollection[%s]", collection)
				: "OptionalCollection.empty";
	}
}
