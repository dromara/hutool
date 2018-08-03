package cn.hutool.core.lang;

import cn.hutool.core.clone.CloneSupport;

/**
 * 键值对对象，只能在构造时传入键值
 * 
 * @author looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 4.1.5
 */
public class Pair<K, V> extends CloneSupport<Pair<K, V>> {
	
	private K key;
	private V value;

	/**
	 * 构造
	 * 
	 * @param key 键
	 * @param value 值
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 获取键
	 * @return 键
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * 获取值
	 * @return 值
	 */
	public V getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}
}
