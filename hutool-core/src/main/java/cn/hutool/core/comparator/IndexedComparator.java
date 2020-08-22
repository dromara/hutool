package cn.hutool.core.comparator;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;

import java.util.Comparator;

/**
 * 按照数组的顺序正序排列，数组的元素位置决定了对象的排序先后<br>
 * 默认的，如果参与排序的元素并不在数组中，则排序在前（可以通过atEndIfMiss设置)
 *
 * @param <T> 被排序元素类型
 * @author looly
 * @since 4.1.5
 */
public class IndexedComparator<T> implements Comparator<T> {

	private final boolean atEndIfMiss;
	private final T[] array;

	/**
	 * 构造
	 *
	 * @param objs 参与排序的数组，数组的元素位置决定了对象的排序先后
	 */
	@SuppressWarnings("unchecked")
	public IndexedComparator(T... objs) {
		this(false, objs);
	}

	/**
	 * 构造
	 *
	 * @param atEndIfMiss 如果不在列表中是否排在后边
	 * @param objs        参与排序的数组，数组的元素位置决定了对象的排序先后
	 */
	@SuppressWarnings("unchecked")
	public IndexedComparator(boolean atEndIfMiss, T... objs) {
		Assert.notNull(objs, "'objs' array must not be null");
		this.atEndIfMiss = atEndIfMiss;
		this.array = objs;
	}

	@Override
	public int compare(T o1, T o2) {
		final int index1 = getOrder(o1);
		final int index2 = getOrder(o2);

		return Integer.compare(index1, index2);
	}

	/**
	 * 查找对象类型所在列表的位置
	 *
	 * @param object 对象
	 * @return 位置，未找到位置根据{@link #atEndIfMiss}取不同值，false返回-1，否则返回列表长度
	 */
	private int getOrder(T object) {
		int order = ArrayUtil.indexOf(array, object);
		if (order < 0) {
			order = this.atEndIfMiss ? this.array.length : -1;
		}
		return order;
	}
}
