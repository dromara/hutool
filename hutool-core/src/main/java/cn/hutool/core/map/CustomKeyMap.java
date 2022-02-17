package cn.hutool.core.map;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * 自定义键的Map，默认HashMap实现
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 * @since 4.0.7
 */
public abstract class CustomKeyMap<K, V> extends MapWrapper<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造<br>
	 * 通过传入一个Map从而确定Map的类型，子类需创建一个空的Map，而非传入一个已有Map，否则值可能会被修改
	 *
	 * @param m Map 被包装的Map
	 * @since 3.1.2
	 */
	public CustomKeyMap(Map<K, V> m) {
		super(m);
	}

	@Override
	public V get(Object key) {
		return super.get(customKey(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		return super.put((K) customKey(key), value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(customKey(key));
	}

	@Override
	public V remove(Object key) {
		return super.remove(customKey(key));
	}

	@Override
	public boolean remove(Object key, Object value) {
		return super.remove(customKey(key), value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		//noinspection unchecked
		return super.replace((K) customKey(key), oldValue, newValue);
	}

	@Override
	public V replace(K key, V value) {
		//noinspection unchecked
		return super.replace((K) customKey(key), value);
	}

	//---------------------------------------------------------------------------- Override default methods start
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return super.getOrDefault(customKey(key), defaultValue);
	}

	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		//noinspection unchecked
		return super.computeIfPresent((K) customKey(key), remappingFunction);
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		//noinspection unchecked
		return super.compute((K) customKey(key), remappingFunction);
	}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		//noinspection unchecked
		return super.merge((K) customKey(key), value, remappingFunction);
	}
	//---------------------------------------------------------------------------- Override default methods end

	/**
	 * 自定义键
	 *
	 * @param key KEY
	 * @return 自定义KEY
	 */
	protected abstract Object customKey(Object key);
}
