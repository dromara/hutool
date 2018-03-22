package com.xiaoleilu.hutool.collection;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * {@link Iterable} 和 {@link Iterator} 相关工具类
 * 
 * @author Looly
 * @since 3.1.0
 */
public class IterUtil {

	/**
	 * Iterable是否为空
	 * 
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 * 
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterator<?> Iterator) {
		return null == Iterator || false == Iterator.hasNext();
	}

	/**
	 * Iterable是否为空
	 * 
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 * 
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterator<?> Iterator) {
		return null != Iterator && Iterator.hasNext();
	}

	/**
	 * 是否包含{@code null}元素
	 * 
	 * @param iter 被检查的{@link Iterable}对象，如果为{@code null} 返回false
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(Iterable<?> iter) {
		return hasNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 是否包含{@code null}元素
	 * 
	 * @param iter 被检查的{@link Iterator}对象，如果为{@code null} 返回false
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(Iterator<?> iter) {
		if (null == iter) {
			return true;
		}
		while (iter.hasNext()) {
			if (null == iter.next()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否全部元素为null
	 * 
	 * @param iter iter 被检查的{@link Iterable}对象，如果为{@code null} 返回true
	 * @return 是否全部元素为null
	 * @since 3.3.0
	 */
	public static boolean isAllNull(Iterable<?> iter) {
		return isAllNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 是否全部元素为null
	 * 
	 * @param iter iter 被检查的{@link Iterator}对象，如果为{@code null} 返回true
	 * @return 是否全部元素为null
	 * @since 3.3.0
	 */
	public static boolean isAllNull(Iterator<?> iter) {
		if (null == iter) {
			return true;
		}

		while (iter.hasNext()) {
			if (null != iter.next()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 * 
	 * @param <T> 集合元素类型
	 * @param iter {@link Iterable}，如果为null返回一个空的Map
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(Iterable<T> iter) {
		return countMap(null == iter ? null : iter.iterator());
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 * 
	 * @param <T> 集合元素类型
	 * @param iter {@link Iterator}，如果为null返回一个空的Map
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(Iterator<T> iter) {
		final HashMap<T, Integer> countMap = new HashMap<>();
		if (null != iter) {
			Integer count;
			T t;
			while (iter.hasNext()) {
				t = iter.next();
				count = countMap.get(t);
				if (null == count) {
					countMap.put(t, 1);
				} else {
					countMap.put(t, count + 1);
				}
			}
		}
		return countMap;
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 * 
	 * @param <T> 集合元素类型
	 * @param iterable {@link Iterable}
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
		if (null == iterable) {
			return null;
		}
		return join(iterable.iterator(), conjunction);
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 * 
	 * @param <T> 集合元素类型
	 * @param iterator 集合
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
		if (null == iterator) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		T item;
		while (iterator.hasNext()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}

			item = iterator.next();
			if (ArrayUtil.isArray(item)) {
				sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction));
			} else if (item instanceof Iterable<?>) {
				sb.append(join((Iterable<?>) item, conjunction));
			} else if (item instanceof Iterator<?>) {
				sb.append(join((Iterator<?>) item, conjunction));
			} else {
				sb.append(item);
			}
		}
		return sb.toString();
	}

	/**
	 * 将Entry集合转换为HashMap
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param entryIter entry集合
	 * @return Map
	 */
	public static <K, V> HashMap<K, V> toMap(Iterable<Entry<K, V>> entryIter) {
		final HashMap<K, V> map = new HashMap<K, V>();
		if (isNotEmpty(entryIter)) {
			for (Entry<K, V> entry : entryIter) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

	/**
	 * 将键列表和值列表转换为Map<br>
	 * 以键为准，值与键位置需对应。如果键元素数多于值元素，多余部分值用null代替。<br>
	 * 如果值多于键，忽略多余的值。
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param keys 键列表
	 * @param values 值列表
	 * @return 标题内容Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values) {
		return toMap(null == keys ? null : keys.iterator(), null == values ? null : values.iterator());
	}

	/**
	 * 将键列表和值列表转换为Map<br>
	 * 以键为准，值与键位置需对应。如果键元素数多于值元素，多余部分值用null代替。<br>
	 * 如果值多于键，忽略多余的值。
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param keys 键列表
	 * @param values 值列表
	 * @return 标题内容Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values) {
		final Map<K, V> resultMap = new HashMap<>();
		if (isNotEmpty(keys)) {
			while (keys.hasNext()) {
				resultMap.put(keys.next(), (null != values && values.hasNext()) ? values.next() : null);
			}
		}
		return resultMap;
	}

	/**
	 * Enumeration转换为Iterator
	 * <p>
	 * Adapt the specified <code>Enumeration</code> to the <code>Iterator</code> interface
	 * 
	 * @param <E> 集合元素类型
	 * @param e {@link Enumeration}
	 * @return {@link Iterator}
	 */
	public static <E> Iterator<E> asIterator(Enumeration<E> e) {
		return new EnumerationIterator<E>(e);
	}

	/**
	 * {@link Iterator} 转为 {@link Iterable}
	 * 
	 * @param <E> 元素类型
	 * @param iter {@link Iterator}
	 * @return {@link Iterable}
	 */
	public static <E> Iterable<E> asIterable(final Iterator<E> iter) {
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return iter;
			}
		};
	}

	/**
	 * 获取集合的第一个元素
	 * 
	 * @param <T> 集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterable<T> iterable) {
		if (null != iterable) {
			return getFirst(iterable.iterator());
		}
		return null;
	}

	/**
	 * 获取集合的第一个元素
	 * 
	 * @param <T> 集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterator<T> iterator) {
		if (null != iterator && iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	/**
	 * 获得{@link Iterable}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 * 
	 * @param iterable {@link Iterable}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 */
	public static Class<?> getElementType(Iterable<?> iterable) {
		if (null != iterable) {
			final Iterator<?> iterator = iterable.iterator();
			return getElementType(iterator);
		}
		return null;
	}

	/**
	 * 获得{@link Iterator}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 * 
	 * @param iterator {@link Iterator}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 */
	public static Class<?> getElementType(Iterator<?> iterator) {
		final Iterator<?> iter2 = new CopiedIterator<>(iterator);
		if (null != iter2) {
			Object t;
			while (iter2.hasNext()) {
				t = iter2.next();
				if (null != t) {
					return t.getClass();
				}
			}
		}
		return null;
	}
}
