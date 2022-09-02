package cn.hutool.core.stream;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * {@link StreamWrapper}的基本实现，用于包装一个已有的流实例，
 * 使其支持相对原生{@link Stream}更多的中间操作与终端操作。
 *
 * @author huangchengxing
 * @see EasyStream
 * @see EntryStream
 */
public abstract class AbstractEnhancedStreamWrapper<T, I extends AbstractEnhancedStreamWrapper<T, I>>
	implements TerminableStreamWrapper<T, I>, TransformableStreamWrapper<T, I> {

	/**
	 * 原始的流实例
	 */
	protected final Stream<T> stream;

	/**
	 * 获取被包装的元素流实例
	 */
	@Override
	public Stream<T> stream() {
		return stream;
	}

	/**
	 * 创建一个流包装器
	 *
	 * @param stream 包装的流对象
	 * @throws NullPointerException 当{@code stream}为{@code null}时抛出
	 */
	protected AbstractEnhancedStreamWrapper(Stream<T> stream) {
		this.stream = Objects.requireNonNull(stream, "stream must not null");
	}

}
