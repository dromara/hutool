package cn.hutool.core.collection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合optional操作类, 具体用法与{@link Optional}类似，主要用于辅助{@link OptionalCollection}操作，
 * 用法可参照{@link OptionalCollection}类注释
 *
 * @author Wilson-He
 */
public class OptionalStream<T> {
	private static final OptionalStream<?> EMPTY = new OptionalStream<>(Collections.emptyList());
	private final Stream<T> stream;

	private OptionalStream(Stream<T> stream) {
		this.stream = Objects.requireNonNull(stream);
	}

	private OptionalStream(Collection<T> collection) {
		this.stream = Objects.requireNonNull(collection).stream();
	}

	/**
	 * Returns an {@code OptionalStream} with the specified present non-null stream.
	 *
	 * @param <T>    the class of the stream elements
	 * @param stream the stream to be present, which must be non-null
	 * @return an {@code OptionalStream} with the stream present
	 * @throws NullPointerException if stream is null
	 */
	public static <T> OptionalStream<T> of(Stream<T> stream) {
		return new OptionalStream<>(stream);
	}

	/**
	 * Returns an {@code OptionalStream} describing the specified stream, if non-null,
	 * otherwise returns an empty {@code OptionalStream}.
	 *
	 * @param <T>    the class of the stream elements
	 * @param stream the possibly-null stream to describe
	 * @return an {@code OptionalStream} with a present stream if the specified stream
	 * is non-null, otherwise an empty {@code OptionalStream}
	 */
	public static <T> OptionalStream<T> ofNullable(Stream<T> stream) {
		return stream == null ? empty() : new OptionalStream<>(stream);
	}

	/**
	 * Returns an {@code OptionalStream} describing the specified collection stream, if non-null,
	 * otherwise throws a {@code NullPointerException}.
	 *
	 * @param <T>        the class of the collection elements
	 * @param collection the stream holder
	 * @return an {@code OptionalStream} with a stream if the specified collection
	 * is non-null, otherwise throws a {@code NullPointerException}
	 * @throws NullPointerException if collection is null
	 */
	public static <T> OptionalStream<T> of(Collection<T> collection) {
		return new OptionalStream<>(collection);
	}

	/**
	 * Returns an {@code OptionalStream} describing the specified collection stream, if non-empty,
	 * otherwise returns an empty {@code OptionalStream}.
	 *
	 * @param <T>        the class of the collection elements
	 * @param collection the stream holder
	 * @return an {@code OptionalStream} with a stream if the specified collection
	 * is non-empty, otherwise an empty {@code OptionalStream}
	 */
	public static <T> OptionalStream<T> ofEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty() ? empty() : of(collection);
	}

	/**
	 * return an empty {@code OptionalStream}
	 *
	 * @return return an empty {@code OptionalStream}
	 */
	@SuppressWarnings("unchecked")
	private static <T> OptionalStream<T> empty() {
		return (OptionalStream<T>) EMPTY;
	}

	/**
	 * Map the collection to an {@code OptionalStream} instance with an new {@code stream} by mapper function.
	 *
	 * @param mapper mapper function
	 * @return map result stream
	 */
	public <E> OptionalStream<E> map(Function<Stream<T>, Stream<E>> mapper) {
		return of(mapper.apply(stream));
	}


	/**
	 * Map the stream to an {@code optional} instance.
	 *
	 * @param mapper map function
	 * @return an {@code Optional} instance after map operation
	 */
	public <V> Optional<V> optional(Function<Stream<T>, V> mapper) {
		return Optional.ofNullable(mapper.apply(stream));
	}

	/**
	 * Build an {@code OptionalCollection} by the stream.
	 *
	 * @return OptionalCollection
	 */
	public OptionalCollection<T> toOptionalCollection() {
		return OptionalCollection.ofEmpty(stream.collect(Collectors.toList()));
	}

	/**
	 * Return {@code true} if there is a stream present, otherwise {@code false}.
	 *
	 * @return {@code true} if there is a stream present, otherwise {@code false}
	 */
	public boolean isPresent() {
		return stream != null;
	}

	/**
	 * If a stream is present, invoke the specified consumer with the stream,
	 * otherwise do nothing.
	 *
	 * @param consumer block to be executed if a stream is present
	 * @throws NullPointerException if stream is present and {@code consumer} is
	 *                              null
	 */
	public <V> void ifPresent(Consumer<Stream<T>> consumer) {
		Objects.requireNonNull(consumer);
		if (isPresent()) {
			consumer.accept(stream);
		}
	}

	/**
	 * If a stream is present in this {@code OptionalStream}, returns the stream,
	 * otherwise throws {@code NoSuchElementException}.
	 *
	 * @return the non-null stream held by this {@code OptionalStream}
	 * @throws NoSuchElementException if there is no stream present
	 * @see OptionalStream#isPresent()
	 */
	public Stream<T> get() {
		if (stream == null) {
			throw new NoSuchElementException("No stream present");
		}
		return stream;
	}

	/**
	 * Return the stream if present, otherwise return {@code other}.
	 *
	 * @param other the stream to be returned if there is no stream present, may
	 *              be null
	 * @return the stream, if present, otherwise {@code other}
	 */
	public Stream<T> orElse(Stream<T> other) {
		return stream != null ? stream : other;
	}

	/**
	 * Return the stream if present, otherwise invoke {@code other} and return
	 * the result of that invocation.
	 *
	 * @param other a {@code Supplier} whose result is returned if no stream
	 *              is present
	 * @return the stream if present otherwise the result of {@code other.get()}
	 * @throws NullPointerException if stream is not present and {@code other} is
	 *                              null
	 */
	public Stream<T> orElseGet(Supplier<Stream<T>> other) {
		return stream != null ? stream : other.get();
	}


	/**
	 * Return the contained stream, if present, otherwise throw an exception
	 * to be created by the provided supplier.
	 *
	 * @param <X>               Type of the exception to be thrown
	 * @param exceptionSupplier The supplier which will return the exception to
	 *                          be thrown
	 * @return the present stream
	 * @throws X                    if there is no stream present
	 * @throws NullPointerException if {@code exceptionSupplier} is null
	 * @apiNote A method reference to the exception constructor with an empty
	 * argument list can be used as the supplier. For example,
	 * {@code IllegalStateException::new}
	 */
	public <X extends Throwable> Stream<T> orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (stream != null) {
			return stream;
		} else {
			throw exceptionSupplier.get();
		}
	}

	/**
	 * Indicates whether some other object is "equal to" this OptionalStream. The
	 * other object is considered equal if:
	 * <ul>
	 * <li>it is also an {@code Optional} and;
	 * <li>both instances have no stream present or;
	 * <li>the present streams are "equal to" each other via {@code equals()}.
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
		if (!(obj instanceof OptionalStream)) {
			return false;
		}
		OptionalStream<?> other = (OptionalStream<?>) obj;
		return Objects.equals(stream, other.stream);
	}

	/**
	 * Returns the hash code value of the present stream, if any, or 0 (zero) if
	 * no stream is present.
	 *
	 * @return hash code value of the present stream or 0 if no stream is present
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(stream);
	}

	/**
	 * Wraps stream.toString() with OptionalStream.
	 *
	 * @return "OptionalStream[{stream.toString()}]" or "OptionalStream.empty"
	 */
	@Override
	public String toString() {
		return stream != null ? String.format("OptionalStream[%s]", stream)
				: "OptionalStream.empty";
	}

}
