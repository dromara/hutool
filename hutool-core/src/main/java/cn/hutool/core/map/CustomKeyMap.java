package cn.hutool.core.map;

import java.util.Map;

/**
 * 自定义键的Map，默认HashMap实现
 * 
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
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
		for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(customKey(key));
	}

	/**
	 * 自定义键
	 * 
	 * @param key KEY
	 * @return 自定义KEY
	 */
	protected abstract Object customKey(Object key);
}
