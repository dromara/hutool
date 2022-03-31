package cn.hutool.core.collection;


/**
 * @author luozongle
 */
public interface ListDataIterable<E> {

	/**
	 * 返回 {@code T} 集合类型元素的迭代器。
	 *
	 * @return an ListDataIterator
	 */
	ListDataIterator<E> iterator();

}
