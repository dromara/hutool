package cn.hutool.core.map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 可重复键和值的Map<br>
 * 通过键值单独建立List方式，使键值对一一对应，实现正向和反向两种查找<br>
 * 无论是正向还是反向，都是遍历列表查找过程，相比标准的HashMap要慢，数据越多越慢
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 */
public class TableMap<K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<K> keys;
	private final List<V> values;

	/**
	 * 构造
	 *
	 * @param size 初始容量
	 */
	public TableMap(int size) {
		this.keys = new ArrayList<>(size);
		this.values = new ArrayList<>(size);
	}

	/**
	 * 构造
	 *
	 * @param keys   键列表
	 * @param values 值列表
	 */
	public TableMap(K[] keys, V[] values) {
		this.keys = CollUtil.toList(keys);
		this.values = CollUtil.toList(values);
	}

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public boolean isEmpty() {
		return CollUtil.isEmpty(keys);
	}

	@Override
	public boolean containsKey(Object key) {
		//noinspection SuspiciousMethodCalls
		return keys.contains(key);
	}

	@Override
	public boolean containsValue(Object value) {
		//noinspection SuspiciousMethodCalls
		return values.contains(value);
	}

	@Override
	public V get(Object key) {
		//noinspection SuspiciousMethodCalls
		final int index = keys.indexOf(key);
		if (index > -1 && index < values.size()) {
			return values.get(index);
		}
		return null;
	}

	/**
	 * 根据value获得对应的key，只返回找到的第一个value对应的key值
	 * @param value 值
	 * @return 键
	 * @since 5.3.3
	 */
	public K getKey(V value){
		final int index = values.indexOf(value);
		if (index > -1 && index < keys.size()) {
			return keys.get(index);
		}
		return null;
	}

	/**
	 * 获取指定key对应的所有值
	 *
	 * @param key 键
	 * @return 值列表
	 * @since 5.2.5
	 */
	public List<V> getValues(K key) {
		return CollUtil.getAny(
				this.values,
				ListUtil.indexOfAll(this.keys, (ele) -> ObjectUtil.equal(ele, key))
		);
	}

	/**
	 * 获取指定value对应的所有key
	 *
	 * @param value 值
	 * @return 值列表
	 * @since 5.2.5
	 */
	public List<K> getKeys(V value) {
		return CollUtil.getAny(
				this.keys,
				ListUtil.indexOfAll(this.values, (ele) -> ObjectUtil.equal(ele, value))
		);
	}

	@Override
	public V put(K key, V value) {
		keys.add(key);
		values.add(value);
		return null;
	}

	@Override
	public V remove(Object key) {
		//noinspection SuspiciousMethodCalls
		int index = keys.indexOf(key);
		if (index > -1) {
			keys.remove(index);
			if (index < values.size()) {
				values.remove(index);
			}
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		keys.clear();
		values.clear();
	}

	@Override
	public Set<K> keySet() {
		return new HashSet<>(keys);
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableList(this.values);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		final Set<Map.Entry<K, V>> hashSet = new LinkedHashSet<>();
		for (int i = 0; i < size(); i++) {
			hashSet.add(new Entry<>(keys.get(i), values.get(i)));
		}
		return hashSet;
	}

	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		return new Iterator<Map.Entry<K, V>>() {
			private final Iterator<K> keysIter = keys.iterator();
			private final Iterator<V> valuesIter = values.iterator();

			@Override
			public boolean hasNext() {
				return keysIter.hasNext() && valuesIter.hasNext();
			}

			@Override
			public Map.Entry<K, V> next() {
				return new Entry<>(keysIter.next(), valuesIter.next());
			}

			@Override
			public void remove() {
				keysIter.remove();
				valuesIter.remove();
			}
		};
	}

	@Override
	public String toString() {
		return "TableMap{" +
				"keys=" + keys +
				", values=" + values +
				'}';
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {

		private final K key;
		private final V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException("setValue not supported.");
		}

		@Override
		public final boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof Map.Entry) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
				return Objects.equals(key, e.getKey()) &&
						Objects.equals(value, e.getValue());
			}
			return false;
		}

		@Override
		public int hashCode() {
			//copy from 1.8 HashMap.Node
			return Objects.hashCode(key) ^ Objects.hashCode(value);
		}
	}
}
