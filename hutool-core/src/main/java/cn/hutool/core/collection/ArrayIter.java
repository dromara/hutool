package cn.hutool.core.collection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 数组Iterator对象
 * @author Looly
 *
 * @param <E> 元素类型
 * @since 4.1.1
 */
public class ArrayIter<E> implements Iterator<E>, Iterable<E>, Serializable{
	private static final long serialVersionUID = 1L;

	/** 数组 */
	private final Object array;
	/** 起始位置 */
	private int startIndex;
	/** 结束位置 */
	private int endIndex;
	/** 当前位置 */
	private int index;

	/**
	 * 构造
	 * @param array 数组
	 * @throws IllegalArgumentException array对象不为数组抛出此异常
	 * @throws NullPointerException array对象为null
	 */
	public ArrayIter(final Object array) {
		this(array, 0);
	}

	/**
	 * 构造
	 * @param array 数组
	 * @param startIndex 起始位置，当起始位置小于0或者大于结束位置，置为0。
	 * @throws IllegalArgumentException array对象不为数组抛出此异常
	 * @throws NullPointerException array对象为null
	 */
	public ArrayIter(final Object array, final int startIndex) {
		this(array, startIndex, -1);
	}

	/**
	 * 构造
	 * @param array 数组
	 * @param startIndex 起始位置，当起始位置小于0或者大于结束位置，置为0。
	 * @param endIndex 结束位置，当结束位置小于0或者大于数组长度，置为数组长度。
	 * @throws IllegalArgumentException array对象不为数组抛出此异常
	 * @throws NullPointerException array对象为null
	 */
	public ArrayIter(final Object array, final int startIndex, final int endIndex) {
		this.endIndex = Array.getLength(array);
		if(endIndex > 0 && endIndex < this.endIndex){
			this.endIndex = endIndex;
		}

		if(startIndex >=0 && startIndex < this.endIndex){
			this.startIndex = startIndex;
		}
		this.array = array;
		this.index = this.startIndex;
	}

	@Override
	public boolean hasNext() {
		return (index < endIndex);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E next() {
		if (hasNext() == false) {
			throw new NoSuchElementException();
		}
		return (E)Array.get(array, index++);
	}

	/**
	 * 不允许操作数组元素
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() method is not supported");
	}

	// Properties
	// -----------------------------------------------------------------------
	/**
	 * 获得原始数组对象
	 *
	 * @return 原始数组对象
	 */
	public Object getArray() {
		return array;
	}

	/**
	 * 重置数组位置
	 */
	public void reset() {
		this.index = this.startIndex;
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

}
