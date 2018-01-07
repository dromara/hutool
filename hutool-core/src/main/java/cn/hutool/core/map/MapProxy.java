package cn.hutool.core.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.getter.OptNullBasicTypeFromObjectGetter;

/**
 * Map代理，提供各种getXXX方法，并提供默认值支持
 * 
 * @author looly
 * @since 3.2.0
 */
public class MapProxy extends OptNullBasicTypeFromObjectGetter<Object> implements Map<Object, Object> {

	@SuppressWarnings("rawtypes")
	Map map;

	/**
	 * 创建代理Map<br>
	 * 此类对Map做一次包装，提供各种getXXX方法
	 * 
	 * @param map 被代理的Map
	 * @return {@link MapProxy}
	 */
	public static MapProxy create(Map<?, ?> map) {
		return (map instanceof MapProxy) ? (MapProxy) map : new MapProxy(map);
	}

	/**
	 * 构造
	 * 
	 * @param map 被代理的Map
	 */
	public MapProxy(Map<?, ?> map) {
		this.map = map;
	}

	@Override
	public Object getObj(Object key, Object defaultValue) {
		final Object value = map.get(key);
		return null != value ? value : defaultValue;
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
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putAll(Map<?, ?> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Object> keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

}
