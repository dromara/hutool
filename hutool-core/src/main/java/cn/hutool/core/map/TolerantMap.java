package cn.hutool.core.map;

import cn.hutool.core.util.ObjectUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 一个可以提供默认值的Map
 *
 * @author pantao
 * @since 2020/1/3
 */
public class TolerantMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

	private static final long serialVersionUID = -4158133823263496197L;

	private transient Map<K, V> map;

	private transient V defaultValue;

	public TolerantMap(V defaultValue) {
		this(new HashMap<>(), defaultValue);
	}

	public TolerantMap(int initialCapacity, float loadFactor, V defaultValue) {
		this(new HashMap<>(initialCapacity, loadFactor), defaultValue);
	}

	public TolerantMap(int initialCapacity, V defaultValue) {
		this(new HashMap<>(initialCapacity), defaultValue);
	}

	public TolerantMap(Map<K, V> map, V defaultValue) {
		this.map = map;
		this.defaultValue = defaultValue;
	}

	public static <K, V> TolerantMap<K, V> of(Map<K, V> map, V defaultValue) {
		return new TolerantMap<>(map, defaultValue);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public V get(Object key) {
		return getOrDefault(key, defaultValue);
	}

	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		map.forEach(action);
	}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		map.replaceAll(function);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		return map.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return map.remove(key, value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		return map.replace(key, oldValue, newValue);
	}

	@Override
	public V replace(K key, V value) {
		return map.replace(key, value);
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return map.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.computeIfPresent(key, remappingFunction);
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.compute(key, remappingFunction);
	}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return map.merge(key, value, remappingFunction);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		TolerantMap<?, ?> that = (TolerantMap<?, ?>) o;
		return map.equals(that.map) && Objects.equals(defaultValue, that.defaultValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(map, defaultValue);
	}

	@Override
	public String toString() {
		return "TolerantMap{" + "map=" + map + ", defaultValue=" + defaultValue + '}';
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.writeObject(ObjectUtil.serialize(map));
		s.writeObject(ObjectUtil.serialize(defaultValue));
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		map = ObjectUtil.deserialize((byte[]) s.readObject());
		defaultValue = ObjectUtil.deserialize((byte[]) s.readObject());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
