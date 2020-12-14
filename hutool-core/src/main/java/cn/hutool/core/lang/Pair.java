package cn.hutool.core.lang;

import cn.hutool.core.clone.CloneSupport;

import java.io.Serializable;
import java.util.Objects;

/**
 * 键值对对象，只能在构造时传入键值
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 4.1.5
 */
public class Pair<K, V> extends CloneSupport<Pair<K, V>> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final K key;
	private final V value;

	/**
	 * 构建{@link Pair}对象
	 *
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @param key   键
	 * @param value 值
	 * @return {@link Pair}
	 * @since 5.4.3
	 */
	public static <K, V> Pair<K, V> of(K key, V value) {
		return new Pair<>(key, value);
	}

	/**
	 * 构造
	 *
	 * @param key   键
	 * @param value 值
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 获取键
	 *
	 * @return 键
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public V getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof Pair) {
			Pair<?, ?> pair = (Pair<?, ?>) o;
			return Objects.equals(getKey(), pair.getKey()) &&
					Objects.equals(getValue(), pair.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		//copy from 1.8 HashMap.Node
		return Objects.hashCode(key) ^ Objects.hashCode(value);
	}
}
