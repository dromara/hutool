package cn.hutool.core.lang;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.lang.func.Func1;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@code Either} 只能包含"左值"或"右值"其中的一个，只能在构造时传入值
 * 一般而言，{@code Either}对象左值表示Exception，右值表示正常处理的数据
 * 右值 (right)，在英文中也表示"good"、"ok"
 * 参考<a href="https://www.jianshu.com/p/a6c1d50f1ffa">Java Stream中的异常处理</a><br>
 *
 * @param <L> 左值类型
 * @param <R> 右值类型
 * @author 敢敢
 * @since 5.8.5
 */
public class Either<L, R> extends CloneSupport<Either<L, R>> implements Serializable {
	private static final long serialVersionUID = 2L;

	private final L left;
	private final R right;

	private Either(L left, R right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * 构建{@code Either}对象
	 *
	 * @param <L>   值类型
	 * @param value 左值
	 * @return {@code Either}
	 * @since 5.8.5
	 */
	public static <L, R> Either<L, R> ofLeft(L value) {
		return new Either<>(value, null);
	}

	/**
	 * 构建{@code Either}对象
	 *
	 * @param <R>   值类型
	 * @param value 右值
	 * @return {@code Either}
	 * @since 5.8.5
	 */
	public static <L, R> Either<L, R> ofRight(R value) {
		return new Either<>(null, value);
	}

	/**
	 * 获取左值，返回Optional
	 *
	 * @return {@code Optional}
	 * @since 5.8.5
	 */
	public Optional<L> getLeft() {
		return Optional.ofNullable(left);
	}

	/**
	 * 获取右值，返回Optional
	 *
	 * @return {@code Optional}
	 * @since 5.8.5
	 */
	public Optional<R> getRight() {
		return Optional.ofNullable(right);
	}

	/**
	 * 判断{@code Either}对象是否是一个左值对象
	 *
	 * @return {@code boolean}
	 * @since 5.8.5
	 */
	public boolean isLeft() {
		return left != null;
	}

	/**
	 * 判断{@code Either}对象是否是一个右值对象
	 *
	 * @return {@code boolean}
	 * @since 5.8.5
	 */
	public boolean isRight() {
		return right != null;
	}

	/**
	 * 如果该{@code Either}对象是一个右值对象
	 * 则会使用右值调用传入的 consumer
	 *
	 * @param consumer 用户自定义消费右值的函数
	 * @since 5.8.5
	 */
	public void consumerRight(Consumer<R> consumer) {
		if (isRight()) {
			consumer.accept(right);
		}
	}

	/**
	 * 如果该{@code Either}对象是一个左值对象
	 * 则会使用左值调用传入的 consumer
	 *
	 * @param consumer 用户自定义消费左值的函数
	 * @since 5.8.5
	 */
	public void consumerLeft(Consumer<L> consumer) {
		if (isLeft()) {
			consumer.accept(left);
		}
	}

	/**
	 * 此方法包装普通 function 并使其返回一个{@code Either}对象
	 * 这种包装得以使用{@code Either}对象来代替抛出异常，以防止流式处理被异常中断
	 * <p>
	 * 以下例子结合流与{@code Either}对象处理成功与失败的数据：
	 * <p>
	 * list.stream()
	 * .map(Either.funcWrapper(this::xxx))
	 * .peek(x-> x.consumerRight(this::processSuccess))
	 * .peek(x-> x.consumerLeft(this::processError))
	 * .map(Either::getRight)
	 * .filter(Optional::isPresent)
	 * .map(Optional::get)
	 * .collect(Collectors.toList());
	 *
	 * @param function 用户function
	 * @since 5.8.5
	 */
	public static <T, R> Function<T, Either<? extends Throwable, R>> funcWrapper(Func1<T, R> function) {
		return t -> {
			try {
				return Either.ofRight(function.call(t));
			} catch (Throwable ex) {
				return Either.ofLeft(ex);
			}
		};
	}

	@Override
	public String toString() {
		return "Either [left=" + left + ", right=" + right + "]";
	}

}
