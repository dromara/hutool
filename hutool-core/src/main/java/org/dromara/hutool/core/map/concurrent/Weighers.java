/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package org.dromara.hutool.core.map.concurrent;

import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.lang.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A common set of {@link Weigher} and {@link EntryWeigher} implementations.
 *
 * @author ben.manes@gmail.com (Ben Manes)
 * @see <a href="http://code.google.com/p/concurrentlinkedhashmap/">
 * http://code.google.com/p/concurrentlinkedhashmap/</a>
 */
public final class Weighers {

	private Weighers() {
		throw new AssertionError();
	}

	/**
	 * A entry weigher backed by the specified weigher. The selector of the value
	 * determines the selector of the entry.
	 *
	 * @param weigher the weigher to be "wrapped" in a entry weigher.
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @return A entry weigher view of the specified weigher.
	 */
	public static <K, V> EntryWeigher<K, V> asEntryWeigher(
		final Weigher<? super V> weigher) {
		return (weigher == singleton())
			? Weighers.entrySingleton()
			: new EntryWeigherView<>(weigher);
	}

	/**
	 * A weigher where an entry has a selector of <b>1</b>. A map bounded with
	 * this weigher will evict when the number of key-value pairs exceeds the
	 * capacity.
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return A weigher where a value takes one unit of capacity.
	 */
	@SuppressWarnings({"cast", "unchecked"})
	public static <K, V> EntryWeigher<K, V> entrySingleton() {
		return (EntryWeigher<K, V>) SingletonEntryWeigher.INSTANCE;
	}

	/**
	 * A weigher where a value has a selector of <b>1</b>. A map bounded with
	 * this weigher will evict when the number of key-value pairs exceeds the
	 * capacity.
	 *
	 * @param <V> 值类型
	 * @return A weigher where a value takes one unit of capacity.
	 */
	@SuppressWarnings({"cast", "unchecked"})
	public static <V> Weigher<V> singleton() {
		return (Weigher<V>) SingletonWeigher.INSTANCE;
	}

	/**
	 * A weigher where the value is a byte array and its selector is the number of
	 * bytes. A map bounded with this weigher will evict when the number of bytes
	 * exceeds the capacity rather than the number of key-value pairs in the map.
	 * This allows for restricting the capacity based on the memory-consumption
	 * and is primarily for usage by dedicated caching servers that hold the
	 * serialized data.
	 * <p>
	 * A value with a selector of <b>0</b> will be rejected by the map. If a value
	 * with this selector can occur then the caller should eagerly evaluate the
	 * value and treat it as a removal operation. Alternatively, a custom weigher
	 * may be specified on the map to assign an empty value a positive selector.
	 *
	 * @return A weigher where each byte takes one unit of capacity.
	 */
	public static Weigher<byte[]> byteArray() {
		return ByteArrayWeigher.INSTANCE;
	}

	/**
	 * A weigher where the value is a {@link Iterable} and its selector is the
	 * number of elements. This weigher only should be used when the alternative
	 * {@link #collection()} weigher cannot be, as evaluation takes O(n) time. A
	 * map bounded with this weigher will evict when the total number of elements
	 * exceeds the capacity rather than the number of key-value pairs in the map.
	 * <p>
	 * A value with a selector of <b>0</b> will be rejected by the map. If a value
	 * with this selector can occur then the caller should eagerly evaluate the
	 * value and treat it as a removal operation. Alternatively, a custom weigher
	 * may be specified on the map to assign an empty value a positive selector.
	 *
	 * @param <E> 元素类型
	 * @return A weigher where each element takes one unit of capacity.
	 */
	public static <E> Weigher<? super Iterable<E>> iterable() {
		return IterableWeigher.INSTANCE;
	}

	/**
	 * A weigher where the value is a {@link Collection} and its selector is the
	 * number of elements. A map bounded with this weigher will evict when the
	 * total number of elements exceeds the capacity rather than the number of
	 * key-value pairs in the map.
	 * <p>
	 * A value with a selector of <b>0</b> will be rejected by the map. If a value
	 * with this selector can occur then the caller should eagerly evaluate the
	 * value and treat it as a removal operation. Alternatively, a custom weigher
	 * may be specified on the map to assign an empty value a positive selector.
	 *
	 * @param <E> 元素类型
	 * @return A weigher where each element takes one unit of capacity.
	 */
	public static <E> Weigher<? super Collection<E>> collection() {
		return CollectionWeigher.INSTANCE;
	}

	/**
	 * A weigher where the value is a {@link List} and its selector is the number
	 * of elements. A map bounded with this weigher will evict when the total
	 * number of elements exceeds the capacity rather than the number of
	 * key-value pairs in the map.
	 * <p>
	 * A value with a selector of <b>0</b> will be rejected by the map. If a value
	 * with this selector can occur then the caller should eagerly evaluate the
	 * value and treat it as a removal operation. Alternatively, a custom weigher
	 * may be specified on the map to assign an empty value a positive selector.
	 *
	 * @param <E> 元素类型
	 * @return A weigher where each element takes one unit of capacity.
	 */
	@SuppressWarnings({"cast"})
	public static <E> Weigher<? super List<E>> list() {
		return ListWeigher.INSTANCE;
	}

	/**
	 * A weigher where the value is a {@link Set} and its selector is the number
	 * of elements. A map bounded with this weigher will evict when the total
	 * number of elements exceeds the capacity rather than the number of
	 * key-value pairs in the map.
	 * <p>
	 * A value with a selector of <b>0</b> will be rejected by the map. If a value
	 * with this selector can occur then the caller should eagerly evaluate the
	 * value and treat it as a removal operation. Alternatively, a custom weigher
	 * may be specified on the map to assign an empty value a positive selector.
	 *
	 * @param <E> 元素类型
	 * @return A weigher where each element takes one unit of capacity.
	 */
	@SuppressWarnings({"cast"})
	public static <E> Weigher<? super Set<E>> set() {
		return SetWeigher.INSTANCE;
	}

	/**
	 * A weigher where the value is a {@link Map} and its selector is the number of
	 * entries. A map bounded with this weigher will evict when the total number of
	 * entries across all values exceeds the capacity rather than the number of
	 * key-value pairs in the map.
	 * <p>
	 * A value with a selector of <b>0</b> will be rejected by the map. If a value
	 * with this selector can occur then the caller should eagerly evaluate the
	 * value and treat it as a removal operation. Alternatively, a custom weigher
	 * may be specified on the map to assign an empty value a positive selector.
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return A weigher where each entry takes one unit of capacity.
	 */
	@SuppressWarnings({"cast"})
	public static <K, V> Weigher<? super Map<K, V>> map() {
		return MapWeigher.INSTANCE;
	}

	static final class EntryWeigherView<K, V> implements EntryWeigher<K, V>, Serializable {
		private static final long serialVersionUID = 1;
		final Weigher<? super V> weigher;

		EntryWeigherView(final Weigher<? super V> weigher) {
			Assert.notNull(weigher);
			this.weigher = weigher;
		}

		@Override
		public int weightOf(final K key, final V value) {
			return weigher.weightOf(value);
		}
	}

	enum SingletonEntryWeigher implements EntryWeigher<Object, Object> {
		INSTANCE;

		@Override
		public int weightOf(final Object key, final Object value) {
			return 1;
		}
	}

	enum SingletonWeigher implements Weigher<Object> {
		INSTANCE;

		@Override
		public int weightOf(final Object value) {
			return 1;
		}
	}

	enum ByteArrayWeigher implements Weigher<byte[]> {
		INSTANCE;

		@Override
		public int weightOf(final byte[] value) {
			return value.length;
		}
	}

	enum IterableWeigher implements Weigher<Iterable<?>> {
		INSTANCE;

		@Override
		public int weightOf(final Iterable<?> values) {
			if (values instanceof Collection<?>) {
				return ((Collection<?>) values).size();
			}
			return IterUtil.size(values);
		}
	}

	enum CollectionWeigher implements Weigher<Collection<?>> {
		INSTANCE;

		@Override
		public int weightOf(final Collection<?> values) {
			return values.size();
		}
	}

	enum ListWeigher implements Weigher<List<?>> {
		INSTANCE;

		@Override
		public int weightOf(final List<?> values) {
			return values.size();
		}
	}

	enum SetWeigher implements Weigher<Set<?>> {
		INSTANCE;

		@Override
		public int weightOf(final Set<?> values) {
			return values.size();
		}
	}

	enum MapWeigher implements Weigher<Map<?, ?>> {
		INSTANCE;

		@Override
		public int weightOf(final Map<?, ?> values) {
			return values.size();
		}
	}
}
