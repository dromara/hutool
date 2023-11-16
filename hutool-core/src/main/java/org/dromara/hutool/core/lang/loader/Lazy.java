package org.dromara.hutool.core.lang.loader;


import org.dromara.hutool.core.lang.Assert;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 *  一种 来实现的Lazy 的实现
 *
 *  @see LazyLoader
 *  @see Supplier
 * @author wh
 */
public class Lazy<T> extends LazyLoader<T> {

	private static final Lazy< ? > EMPTY = new Lazy<>(() -> null, null, true);

	private final Supplier< ? extends T > supplier;

	private T value;
	private volatile boolean resolved;

	/**
	 * 私有的构造方法
	 * 相关创建 全部通过of  来实现
	 *
	 * @param supplier supplier
	 */
	private Lazy( Supplier< ? extends T > supplier ) {
		this(supplier, null, false);
	}

	/**
	 * 为给定的 {@link Supplier} 创建一个新的 {@link Lazy}，值以及它是否已解决。
	 *
	 * @param supplier must not be {@literal null}.
	 * @param value    can be {@literal null}.
	 * @param resolved whether the value handed into the constructor represents a resolved value.
	 */
	private Lazy( Supplier< ? extends T > supplier, T value, boolean resolved ) {

		Assert.notNull(supplier);
		this.supplier = supplier;
		this.value = value;
		this.resolved = resolved;
	}

	/**
	 * 创建一个新的 {@link Lazy} 以延迟生成对象
	 *
	 * @param <T>      the type of which to produce an object of eventually.
	 * @param supplier the {@link Supplier} to create the object lazily.
	 * @return
	 */
	public static < T > Lazy< T > of( Supplier< ? extends T > supplier ) {
		return new Lazy<>(supplier);
	}

	/**
	 * 通过传入的值 来生成一个新的Lazy
	 *
	 * @param <T>   the type of the value to return eventually.
	 * @param value the value to return.
	 * @return
	 */
	public static < T > Lazy< T > of( T value ) {

		Assert.notNull(value, "Value must not be null!");

		return new Lazy<>(() -> value);
	}

	/**
	 * 创建一个预解析的空的 {@link Lazy}。
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static < T > Lazy< T > empty() {
		return (Lazy< T >) EMPTY;
	}


	@Override
	protected T init() {
		T value = this.getNullable();
		if (value == null) {
			throw new IllegalStateException("Expected lazy evaluation to yield a non-null value but got null!");
		}

		return value;

	}

	/**
	 * 返回由配置的 {@link Supplier} 创建的 {@link Optional} 值，允许在
	 * 与 {@link #get()} 形成对比。 将返回计算的实例以供后续查找。
	 *
	 * @return
	 */
	public Optional< T > getOptional() {
		return Optional.ofNullable(getNullable());
	}

	/**
	 * 返回一个新的 Lazy，如果当前的Supplier 没有产生结果 将处理新的 supplier
	 *
	 * @param supplier must not be {@literal null}.
	 * @return
	 */
	public Lazy< T > or( Supplier< ? extends T > supplier ) {

		Assert.notNull(supplier, "Supplier must not be null!");

		return Lazy.of(() -> orElseGet(supplier));
	}

	/**
	 * 返回一个新的 Lazy，如果当前的值没有在结果中产生，它将返回给定的值。
	 *
	 * @return
	 */
	public Lazy< T > or( T value ) {

		Assert.notNull(value, "Value must not be null!");

		return Lazy.of(() -> orElse(value));
	}

	/**
	 * 返回惰性计算的值或给定的默认值，以防计算产生
	 * {@literal null}.
	 *
	 * @param value
	 * @return
	 */

	public T orElse( T value ) {

		T nullable = getNullable();

		return nullable == null ? value : nullable;
	}

	/**
	 * 返回惰性计算的值或给定 {@link Supplier} 产生的值，以防原始异常
	 * value is {@literal null}.
	 *
	 * @param supplier must not be {@literal null}.
	 * @return
	 */

	private T orElseGet( Supplier< ? extends T > supplier ) {

		Assert.notNull(supplier, "Default value supplier must not be null!");

		T value = getNullable();

		return value == null ? supplier.get() : value;
	}

	/**
	 * 创建一个新的 {@link Lazy} 并将给定的 {@link Function} 延迟应用于当前。
	 *
	 * @param function must not be {@literal null}.
	 * @return
	 */
	public < S > Lazy< S > map( Function< ? super T, ? extends S > function ) {

		Assert.notNull(function, "Function must not be null!");

		return Lazy.of(() -> function.apply(get()));
	}

	/**
	 * 创建一个新的 {@link Lazy} 并将给定的 {@link Function} 延迟应用于当前。
	 *
	 * @param function must not be {@literal null}.
	 * @return
	 */
	public < S > Lazy< S > flatMap( Function< ? super T, Lazy< ? extends S > > function ) {

		Assert.notNull(function, "Function must not be null!");

		return Lazy.of(() -> function.apply(get()).get());
	}

	/**
	 * 返回惰性求值的值。
	 *
	 * @return
	 */

	public T getNullable() {

		if (resolved) {
			return value;
		}

		this.value = supplier.get();
		this.resolved = true;

		return value;
	}

}
