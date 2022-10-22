package cn.hutool.core.map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
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
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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

	private static final int DEFAULT_CAPACITY = 10;

	private final List<K> keys;
	private final List<V> values;

	/**
	 * 构造
	 */
	public TableMap() {
		this(DEFAULT_CAPACITY);
	}

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
		if (index > -1) {
			return values.get(index);
		}
		return null;
	}

	/**
	 * 根据value获得对应的key，只返回找到的第一个value对应的key值
	 *
	 * @param value 值
	 * @return 键
	 * @since 5.3.3
	 */
	public K getKey(V value) {
		final int index = values.indexOf(value);
		if (index > -1) {
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

	/**
	 * 移除指定的所有键和对应的所有值
	 *
	 * @param key 键
	 * @return 最后一个移除的值
	 */
	@Override
	public V remove(Object key) {
		V lastValue = null;
		int index;
		//noinspection SuspiciousMethodCalls
		while ((index = keys.indexOf(key)) > -1) {
			lastValue = removeByIndex(index);
		}
		return lastValue;
	}

	/**
	 * 移除指定位置的键值对
	 *
	 * @param index 位置，不能越界
	 * @return 移除的值
	 */
	public V removeByIndex(final int index) {
		keys.remove(index);
		return values.remove(index);
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
		return new HashSet<>(this.keys);
	}

	/**
	 * 获取所有键，可重复，不可修改
	 *
	 * @return 键列表
	 * @since 5.8.0
	 */
	public List<K> keys() {
		return Collections.unmodifiableList(this.keys);
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableList(this.values);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		final Set<Map.Entry<K, V>> hashSet = new LinkedHashSet<>();
		for (int i = 0; i < size(); i++) {
			hashSet.add(MapUtil.entry(keys.get(i), values.get(i)));
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
				return MapUtil.entry(keysIter.next(), valuesIter.next());
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

	@Override
	public void forEach(final BiConsumer<? super K, ? super V> action) {
		for (int i = 0; i < size(); i++) {
			action.accept(keys.get(i), values.get(i));
		}
	}

	@Override
	public boolean remove(final Object key, final Object value) {
		boolean removed = false;
		for (int i = 0; i < size(); i++) {
			if (ObjUtil.equals(key, keys.get(i)) && ObjUtil.equals(value, values.get(i))) {
				removeByIndex(i);
				removed = true;
				// 移除当前元素，下个元素前移
				i--;
			}
		}
		return removed;
	}

	@Override
	public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
		for (int i = 0; i < size(); i++) {
			final V newValue = function.apply(keys.get(i), values.get(i));
			values.set(i, newValue);
		}
	}

	@Override
	public boolean replace(final K key, final V oldValue, final V newValue) {
		for (int i = 0; i < size(); i++) {
			if (ObjUtil.equals(key, keys.get(i)) && ObjUtil.equals(oldValue, values.get(i))) {
				values.set(i, newValue);
				return true;
			}
		}
		return false;
	}

	/**
	 * 替换指定key的所有值为指定值
	 *
	 * @param key   指定的key
	 * @param value 替换的值
	 * @return 最后替换的值
	 */
	@Override
	public V replace(final K key, final V value) {
		V lastValue = null;
		for (int i = 0; i < size(); i++) {
			if (ObjUtil.equals(key, keys.get(i))) {
				lastValue = values.set(i, value);
			}
		}
		return lastValue;
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		if(null == remappingFunction){
			return null;
		}

		V lastValue = null;
		for (int i = 0; i < size(); i++) {
			if (ObjUtil.equals(key, keys.get(i))) {
				final V newValue = remappingFunction.apply(key, values.get(i));
				if(null != newValue){
					lastValue = values.set(i, newValue);
				} else{
					removeByIndex(i);
					// 移除当前元素，下个元素前移
					i--;
				}
			}
		}
		return lastValue;
	}
}
