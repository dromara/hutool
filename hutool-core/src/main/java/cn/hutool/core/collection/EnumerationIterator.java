package cn.hutool.core.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Enumeration}对象转{@link Iterator}对象
 * @author Looly
 *
 * @param <E> 元素类型
 * @since 3.0.8
 */
public class EnumerationIterator<E> implements Iterator<E>{
	
	private final Enumeration<E> e;
	
	/**
	 * 构造
	 * @param enumeration {@link Enumeration}对象
	 */
	public EnumerationIterator(Enumeration<E> enumeration) {
		this.e = enumeration;
	}

	@Override
	public boolean hasNext() {
		return e.hasMoreElements();
	}

	@Override
	public E next() {
		return e.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
