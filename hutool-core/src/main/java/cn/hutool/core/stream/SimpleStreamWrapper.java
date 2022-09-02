package cn.hutool.core.stream;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * {@link AbstractEnhancedStreamWrapper}的基本实现
 *
 * @author huangchengxing
 */
public class SimpleStreamWrapper<T> extends AbstractEnhancedStreamWrapper<T, SimpleStreamWrapper<T>> {

	/**
	 * 创建一个流包装器
	 *
	 * @param stream 包装的流对象
	 * @throws NullPointerException 当{@code stream}为{@code null}时抛出
	 */
	SimpleStreamWrapper(Stream<T> stream) {
		super(stream);
	}

	/**
	 * 将一个流包装为{@link SimpleStreamWrapper}
	 *
	 * @param source 被包装的流
	 * @return 包装后的流
	 */
	@Override
	public SimpleStreamWrapper<T> wrapping(Stream<T> source) {
		return new SimpleStreamWrapper<>(source);
	}

	/**
	 * 将当前流中元素映射为另一类型
	 *
	 * @param mapper 映射方法
	 * @return 映射后的流
	 */
	@Override
	public <R> SimpleStreamWrapper<R> map(Function<? super T, ? extends R> mapper) {
		return new SimpleStreamWrapper<>(stream().map(mapper));
	}

	/**
	 * 将当前流中元素展开为流，然后返回由这些新流中元素组成的流
	 *
	 * @param mapper 映射方法
	 * @return 映射后的流
	 */
	@Override
	public <R> SimpleStreamWrapper<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return new SimpleStreamWrapper<>(stream().flatMap(mapper));
	}
}
