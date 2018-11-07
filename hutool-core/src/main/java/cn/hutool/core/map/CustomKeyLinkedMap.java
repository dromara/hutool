package cn.hutool.core.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义键的Map，默认LinkedHashMap实现
 * 
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 4.0.7
 */
public abstract class CustomKeyLinkedMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CustomKeyLinkedMap() {
		super();
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CustomKeyLinkedMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 */
	public CustomKeyLinkedMap(int initialCapacity) {
		this(initialCapacity, 0.75f);
	}

	/**
	 * 构造
	 * 
	 * @param m Map
	 */
	public CustomKeyLinkedMap(Map<? extends K, ? extends V> m) {
		super((int) (m.size() / 0.75));
		putAll(m);
	}

	/**
	 * 构造
	 * 
	 * @param loadFactor 加载因子
	 * @param m Map
	 * @since 3.1.2
	 */
	public CustomKeyLinkedMap(float loadFactor, Map<? extends K, ? extends V> m) {
		super(m.size(), loadFactor);
		putAll(m);
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
	 * 将Key转为驼峰风格，如果key为字符串的话
	 * 
	 * @param key KEY
	 * @return 小写KEy
	 */
	protected abstract Object customKey(Object key);
}
