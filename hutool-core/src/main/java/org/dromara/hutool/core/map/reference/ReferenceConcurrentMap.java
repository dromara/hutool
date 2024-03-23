/*
 * Copyright (c) 2023-2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.map.reference;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.ReferenceUtil;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 线程安全的ReferenceMap实现
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 */
public abstract class ReferenceConcurrentMap<K, V> implements ConcurrentMap<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	final ConcurrentMap<Reference<K>, Reference<V>> raw;
	private final ReferenceQueue<K> lastKeyQueue;
	private final ReferenceQueue<V> lastValueQueue;
	/**
	 * 回收监听
	 */
	private BiConsumer<Reference<? extends K>, Reference<? extends V>> purgeListener;

	// region 构造

	/**
	 * 构造
	 *
	 * @param raw {@link ConcurrentMap}实现
	 */
	public ReferenceConcurrentMap(final ConcurrentMap<Reference<K>, Reference<V>> raw) {
		this.raw = raw;
		lastKeyQueue = new ReferenceQueue<>();
		lastValueQueue = new ReferenceQueue<>();
	}
	// endregion

	/**
	 * 设置对象回收清除监听
	 *
	 * @param purgeListener 监听函数
	 */
	public void setPurgeListener(final BiConsumer<Reference<? extends K>, Reference<? extends V>> purgeListener) {
		this.purgeListener = purgeListener;
	}

	@Override
	public int size() {
		this.purgeStale();
		return this.raw.size();
	}

	@Override
	public boolean isEmpty() {
		this.purgeStale();
		return this.raw.isEmpty();
	}

	@Override
	public V get(final Object key) {
		this.purgeStale();
		return unwrap(this.raw.get(wrapKey(key)));
	}

	@Override
	public boolean containsKey(final Object key) {
		this.purgeStale();
		return this.raw.containsKey(wrapKey(key));
	}

	@Override
	public boolean containsValue(final Object value) {
		this.purgeStale();
		return this.raw.containsValue(wrapValue(value));
	}

	@Override
	public V put(final K key, final V value) {
		this.purgeStale();
		final Reference<V> vReference = this.raw.put(wrapKey(key), wrapValue(value));
		return unwrap(vReference);
	}

	@Override
	public V putIfAbsent(final K key, final V value) {
		this.purgeStale();
		final Reference<V> vReference = this.raw.putIfAbsent(wrapKey(key), wrapValue(value));
		return unwrap(vReference);
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@Override
	public V replace(final K key, final V value) {
		this.purgeStale();
		final Reference<V> vReference = this.raw.replace(wrapKey(key), wrapValue(value));
		return unwrap(vReference);
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		this.purgeStale();
		return this.raw.replace(wrapKey(key), wrapValue(oldValue), wrapValue(newValue));
	}

	@Override
	public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
		this.purgeStale();
		this.raw.replaceAll((rKey, rValue) -> wrapValue(function.apply(unwrap(rKey), unwrap(rValue))));
	}

	@Override
	public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
		this.purgeStale();
		final Reference<V> vReference = this.raw.computeIfAbsent(wrapKey(key),
			kReference -> wrapValue(mappingFunction.apply(unwrap(kReference))));
		return unwrap(vReference);
	}

	@Override
	public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		this.purgeStale();
		final Reference<V> vReference = this.raw.computeIfPresent(wrapKey(key),
			(kReference, vReference1) -> wrapValue(remappingFunction.apply(unwrap(kReference), unwrap(vReference1))));
		return unwrap(vReference);
	}

	@Override
	public V remove(final Object key) {
		this.purgeStale();
		return unwrap(this.raw.remove(wrapKey(key)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(final Object key, final Object value) {
		this.purgeStale();
		return this.raw.remove(wrapKey((K) key, null), value);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public void clear() {
		this.raw.clear();
		while (lastKeyQueue.poll() != null) ;
		while (lastValueQueue.poll() != null) ;
	}

	@Override
	public Set<K> keySet() {
		this.purgeStale();
		final Set<Reference<K>> referenceSet = this.raw.keySet();
		return new AbstractSet<K>() {
			@Override
			public Iterator<K> iterator() {
				final Iterator<Reference<K>> referenceIter = referenceSet.iterator();
				return new Iterator<K>() {
					@Override
					public boolean hasNext() {
						return referenceIter.hasNext();
					}

					@Override
					public K next() {
						return unwrap(referenceIter.next());
					}
				};
			}

			@Override
			public int size() {
				return referenceSet.size();
			}
		};
	}

	@Override
	public Collection<V> values() {
		this.purgeStale();
		final Collection<Reference<V>> referenceValues = this.raw.values();
		return new AbstractCollection<V>() {
			@Override
			public Iterator<V> iterator() {
				final Iterator<Reference<V>> referenceIter = referenceValues.iterator();
				return new Iterator<V>() {
					@Override
					public boolean hasNext() {
						return referenceIter.hasNext();
					}

					@Override
					public V next() {
						return unwrap(referenceIter.next());
					}
				};
			}

			@Override
			public int size() {
				return referenceValues.size();
			}
		};
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		this.purgeStale();
		final Set<Entry<Reference<K>, Reference<V>>> referenceEntrySet = this.raw.entrySet();
		return new AbstractSet<Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				final Iterator<Entry<Reference<K>, Reference<V>>> referenceIter = referenceEntrySet.iterator();
				return new Iterator<Entry<K, V>>() {
					@Override
					public boolean hasNext() {
						return referenceIter.hasNext();
					}

					@Override
					public Entry<K, V> next() {
						final Entry<Reference<K>, Reference<V>> next = referenceIter.next();
						return new Entry<K, V>() {
							@Override
							public K getKey() {
								return unwrap(next.getKey());
							}

							@Override
							public V getValue() {
								return unwrap(next.getValue());
							}

							@Override
							public V setValue(final V value) {
								return unwrap(next.setValue(wrapValue(value)));
							}
						};
					}
				};
			}

			@Override
			public int size() {
				return referenceEntrySet.size();
			}
		};
	}

	@Override
	public void forEach(final BiConsumer<? super K, ? super V> action) {
		this.purgeStale();
		this.raw.forEach((key, rValue) -> action.accept(key.get(), unwrap(rValue)));
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return entrySet().iterator();
	}

	@Override
	public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		this.purgeStale();
		return unwrap(this.raw.compute(wrapKey(key),
			(kReference, vReference) -> wrapValue(remappingFunction.apply(unwrap(kReference), unwrap(vReference)))));
	}

	@Override
	public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		this.purgeStale();
		return unwrap(this.raw.merge(wrapKey(key), wrapValue(value),
			(vReference, vReference2) -> wrapValue(remappingFunction.apply(unwrap(vReference), unwrap(vReference2)))));
	}

	/**
	 * 清除被回收的键和值
	 */
	@SuppressWarnings("unchecked")
	private void purgeStale() {
		Reference<? extends K> key;
		Reference<? extends V> value;

		// 清除无效key对应键值对
		while ((key = this.lastKeyQueue.poll()) != null) {
			value = this.raw.remove(key);
			if (null != purgeListener) {
				purgeListener.accept(key, value);
			}
		}

		// 清除无效value对应的键值对
		while ((value = this.lastValueQueue.poll()) != null) {
			MapUtil.removeByValue(this.raw, (Reference<V>) value);
			if (null != purgeListener) {
				purgeListener.accept(null, value);
			}
		}
	}

	/**
	 * 根据Reference类型构建key对应的{@link Reference}
	 *
	 * @param key   键
	 * @param queue {@link ReferenceQueue}
	 * @return {@link Reference}
	 */
	abstract Reference<K> wrapKey(final K key, final ReferenceQueue<? super K> queue);

	/**
	 * 根据Reference类型构建value对应的{@link Reference}
	 *
	 * @param value 值
	 * @param queue {@link ReferenceQueue}
	 * @return {@link Reference}
	 */
	abstract Reference<V> wrapValue(final V value, final ReferenceQueue<? super V> queue);

	/**
	 * 根据Reference类型构建key对应的{@link Reference}
	 *
	 * @param key 键
	 * @return {@link Reference}
	 */
	@SuppressWarnings("unchecked")
	private Reference<K> wrapKey(final Object key) {
		return wrapKey((K) key, this.lastKeyQueue);
	}

	/**
	 * 根据Reference类型构建value对应的{@link Reference}
	 *
	 * @param value 键
	 * @return {@link Reference}
	 */
	@SuppressWarnings("unchecked")
	private Reference<V> wrapValue(final Object value) {
		return wrapValue((V) value, this.lastValueQueue);
	}

	/**
	 * 去包装对象
	 *
	 * @param <T> 对象类型
	 * @param obj 对象
	 * @return 值
	 */
	private static <T> T unwrap(final Reference<T> obj) {
		return ReferenceUtil.get(obj);
	}
}
