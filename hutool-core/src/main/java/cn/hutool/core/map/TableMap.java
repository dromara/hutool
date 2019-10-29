package cn.hutool.core.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;

/**
 * 无重复键的Map
 * 
 * @author looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class TableMap<K, V> implements Map<K, V>, Serializable {
	private static final long serialVersionUID = 1L;

	private List<K> keys;
	private List<V> values;

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
	 * @param keys 键列表
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
		return ArrayUtil.isEmpty(keys);
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
		return new HashSet<>(values);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		HashSet<Map.Entry<K, V>> hashSet = new HashSet<>();
		for (int i = 0; i < size(); i++) {
			hashSet.add(new Entry<>(keys.get(i), values.get(i)));
		}
		return hashSet;
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

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

	}
}
