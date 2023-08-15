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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.util.ReferenceUtil;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线程安全的ReferenceMap实现<br>
 * 参考：jdk.management.resource.internal.WeakKeyConcurrentHashMap
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.8.0
 */
public class ReferenceConcurrentMap<K, V> implements ConcurrentMap<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	final ConcurrentMap<Reference<K>, V> raw;
	private final ReferenceQueue<K> lastQueue;
	private final ReferenceUtil.ReferenceType keyType;
	/**
	 * 回收监听
	 */
	private BiConsumer<Reference<? extends K>, V> purgeListener;

	// region 构造

	/**
	 * 构造
	 *
	 * @param raw           {@link ConcurrentMap}实现
	 * @param referenceType Reference类型
	 */
	public ReferenceConcurrentMap(final ConcurrentMap<Reference<K>, V> raw, final ReferenceUtil.ReferenceType referenceType) {
		this.raw = raw;
		this.keyType = referenceType;
		lastQueue = new ReferenceQueue<>();
	}
	// endregion

	/**
	 * 设置对象回收清除监听
	 *
	 * @param purgeListener 监听函数
	 */
	public void setPurgeListener(final BiConsumer<Reference<? extends K>, V> purgeListener) {
		this.purgeListener = purgeListener;
	}

	@Override
	public int size() {
		this.purgeStaleKeys();
		return this.raw.size();
	}

	@Override
	public boolean isEmpty() {
		return 0 == size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(final Object key) {
		this.purgeStaleKeys();
		return this.raw.get(ofKey((K) key, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(final Object key) {
		this.purgeStaleKeys();
		return this.raw.containsKey(ofKey((K) key, null));
	}

	@Override
	public boolean containsValue(final Object value) {
		this.purgeStaleKeys();
		return this.raw.containsValue(value);
	}

	@Override
	public V put(final K key, final V value) {
		this.purgeStaleKeys();
		return this.raw.put(ofKey(key, this.lastQueue), value);
	}

	@Override
	public V putIfAbsent(final K key, final V value) {
		this.purgeStaleKeys();
		return this.raw.putIfAbsent(ofKey(key, this.lastQueue), value);
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@Override
	public V replace(final K key, final V value) {
		this.purgeStaleKeys();
		return this.raw.replace(ofKey(key, this.lastQueue), value);
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		this.purgeStaleKeys();
		return this.raw.replace(ofKey(key, this.lastQueue), oldValue, newValue);
	}

	@Override
	public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
		this.purgeStaleKeys();
		this.raw.replaceAll((kWeakKey, value) -> function.apply(kWeakKey.get(), value));
	}

	@Override
	public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
		this.purgeStaleKeys();
		return this.raw.computeIfAbsent(ofKey(key, this.lastQueue), kWeakKey -> mappingFunction.apply(key));
	}

	@Override
	public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		this.purgeStaleKeys();
		return this.raw.computeIfPresent(ofKey(key, this.lastQueue), (kWeakKey, value) -> remappingFunction.apply(key, value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(final Object key) {
		this.purgeStaleKeys();
		return this.raw.remove(ofKey((K) key, null));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(final Object key, final Object value) {
		this.purgeStaleKeys();
		return this.raw.remove(ofKey((K) key, null), value);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public void clear() {
		this.raw.clear();
		while (lastQueue.poll() != null) ;
	}

	@Override
	public Set<K> keySet() {
		// TODO 非高效方式的set转换，应该返回一个view
		final Collection<K> trans = CollUtil.trans(this.raw.keySet(), (reference) -> null == reference ? null : reference.get());
		return new HashSet<>(trans);
	}

	@Override
	public Collection<V> values() {
		this.purgeStaleKeys();
		return this.raw.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		this.purgeStaleKeys();
		return this.raw.entrySet().stream()
				.map(entry -> new AbstractMap.SimpleImmutableEntry<>(entry.getKey().get(), entry.getValue()))
				.collect(Collectors.toSet());
	}

	@Override
	public void forEach(final BiConsumer<? super K, ? super V> action) {
		this.purgeStaleKeys();
		this.raw.forEach((key, value)-> action.accept(key.get(), value));
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return entrySet().iterator();
	}

	@Override
	public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		this.purgeStaleKeys();
		return this.raw.compute(ofKey(key, this.lastQueue), (kWeakKey, value) -> remappingFunction.apply(key, value));
	}

	@Override
	public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		this.purgeStaleKeys();
		return this.raw.merge(ofKey(key, this.lastQueue), value, remappingFunction);
	}

	/**
	 * 清除被回收的键
	 */
	private void purgeStaleKeys() {
		Reference<? extends K> reference;
		V value;
		while ((reference = this.lastQueue.poll()) != null) {
			value = this.raw.remove(reference);
			if (null != purgeListener) {
				purgeListener.accept(reference, value);
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
	private Reference<K> ofKey(final K key, final ReferenceQueue<? super K> queue) {
		switch (keyType) {
			case WEAK:
				return new WeakKey<>(key, queue);
			case SOFT:
				return new SoftKey<>(key, queue);
		}
		throw new IllegalArgumentException("Unsupported key type: " + keyType);
	}

	/**
	 * 弱键
	 *
	 * @param <K> 键类型
	 */
	private static class WeakKey<K> extends WeakReference<K> {
		private final int hashCode;

		/**
		 * 构造
		 *
		 * @param key   原始Key，不能为{@code null}
		 * @param queue {@link ReferenceQueue}
		 */
		WeakKey(final K key, final ReferenceQueue<? super K> queue) {
			super(key, queue);
			hashCode = key.hashCode();
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(final Object other) {
			if (other == this) {
				return true;
			} else if (other instanceof WeakKey) {
				return ObjUtil.equals(((WeakKey<?>) other).get(), get());
			}
			return false;
		}
	}

	/**
	 * 弱键
	 *
	 * @param <K> 键类型
	 */
	private static class SoftKey<K> extends SoftReference<K> {
		private final int hashCode;

		/**
		 * 构造
		 *
		 * @param key   原始Key，不能为{@code null}
		 * @param queue {@link ReferenceQueue}
		 */
		SoftKey(final K key, final ReferenceQueue<? super K> queue) {
			super(key, queue);
			hashCode = key.hashCode();
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(final Object other) {
			if (other == this) {
				return true;
			} else if (other instanceof SoftKey) {
				return ObjUtil.equals(((SoftKey<?>) other).get(), get());
			}
			return false;
		}
	}
}
