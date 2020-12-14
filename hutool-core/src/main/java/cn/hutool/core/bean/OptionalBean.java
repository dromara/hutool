package cn.hutool.core.bean;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * optional对象判空，参考：https://mp.weixin.qq.com/s/0c8iC0OTtx5LqPkhvkK0tw<br>
 * from：https://github.com/looly/hutool/pull/1182
 *
 * @param <T> Bean类型
 * @author totalo
 * @since 5.4.7
 */
public final class OptionalBean<T> {

	private static final OptionalBean<?> EMPTY = new OptionalBean<>();

	/**
	 * 空值常量
	 *
	 * @param <T> 对象类型
	 * @return 空
	 */
	public static <T> OptionalBean<T> empty() {
		@SuppressWarnings("unchecked")
		OptionalBean<T> none = (OptionalBean<T>) EMPTY;
		return none;
	}

	private final T value;

	/**
	 * 构造
	 */
	private OptionalBean() {
		this.value = null;
	}

	/**
	 * 空值会抛出空指针
	 *
	 * @param value Bean值
	 */
	private OptionalBean(T value) {
		this.value = Objects.requireNonNull(value);
	}

	/**
	 * 包装一个不能为空的 bean
	 *
	 * @param <T>   bean类型
	 * @param value Bean值
	 * @return OptionalBean
	 */
	public static <T> OptionalBean<T> of(T value) {
		return new OptionalBean<>(value);
	}

	/**
	 * 包装一个可能为空的 bean
	 *
	 * @param value Bean值
	 * @param <T>   bean类型
	 * @return OptionalBean
	 */
	public static <T> OptionalBean<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}

	/**
	 * 取出具体的值
	 *
	 * @return bean值
	 */
	public T get() {
		return value;
	}

	/**
	 * 取出一个可能为空的对象
	 *
	 * @param <R> 对象类型
	 * @param fn  从已有bean中获取新bean字段的函数
	 * @return 新的bean
	 */
	public <R> OptionalBean<R> getBean(Function<? super T, ? extends R> fn) {
		return Objects.isNull(value) ? OptionalBean.empty() : OptionalBean.ofNullable(fn.apply(value));
	}

	/**
	 * 如果目标值为空 获取一个默认值
	 *
	 * @param other 默认值
	 * @return 空返回默认值，否则返回原值
	 */

	public T orElse(T other) {
		return ObjectUtil.defaultIfNull(this.value, other);
	}

	/**
	 * 如果目标值为空 通过lambda表达式获取一个值
	 *
	 * @param other 默认值函数
	 * @return 空返回默认值函数获得的值，否则返回原值
	 */
	public T orElseGet(Supplier<? extends T> other) {
		return null != value ? value : other.get();
	}

	/**
	 * 如果目标值为空 抛出一个异常
	 *
	 * @param exceptionSupplier 抛出的异常
	 * @param <X>               异常类型
	 * @return 非空值
	 * @throws X 对象为空时抛出的异常
	 */
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		return Assert.notNull(this.value, exceptionSupplier);
	}

	/**
	 * 检查值是否为空
	 *
	 * @return 是否为空
	 */
	public boolean isPresent() {
		return null != this.value;
	}

	/**
	 * 如果值非空，则使用指定函数处理值
	 *
	 * @param consumer 处理非空对象的函数
	 */
	public void ifPresent(Consumer<? super T> consumer) {
		if (value != null) {
			consumer.accept(value);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}