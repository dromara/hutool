package cn.hutool.core.collection;

import java.util.Iterator;

/**
 * 支持重置的{@link Iterator} 接口<br>
 * 通过实现{@link #reset()}，重置此{@link Iterator}后可实现复用重新遍历
 *
 * @param <E> 元素类型
 * @since 5.8.0
 */
public interface ResettableIter<E> extends Iterator<E> {

	/**
	 * 重置，重置后可重新遍历
	 */
	void reset();
}
