package cn.hutool.core.lang.mutable;

import cn.hutool.core.lang.Pair;

/**
 * 可变{@link Pair}实现，可以修改键和值
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 5.7.16
 */
public class MutablePair<K, V> extends Pair<K, V> implements Mutable<Pair<K, V>>{
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param key   键
	 * @param value 值
	 */
	public MutablePair(final K key, final V value) {
		super(key, value);
	}

	/**
	 * 设置键
	 *
	 * @param key 新键
	 * @return this
	 */
	public MutablePair<K, V> setKey(final K key) {
		this.key = key;
		return this;
	}

	/**
	 * 设置值
	 *
	 * @param value 新值
	 * @return this
	 */
	public MutablePair<K, V> setValue(final V value) {
		this.value = value;
		return this;
	}

	@Override
	public Pair<K, V> get() {
		return this;
	}

	@Override
	public void set(final Pair<K, V> pair) {
		this.key = pair.getKey();
		this.value = pair.getValue();
	}
}
