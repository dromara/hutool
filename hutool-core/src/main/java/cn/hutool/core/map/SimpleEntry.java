package cn.hutool.core.map;

/**
 * {@link java.util.Map.Entry}简单实现。<br>
 * 键值对使用不可变字段表示。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 5.7.23
 */
public class SimpleEntry<K, V> extends AbsEntry<K, V> {

	private final K key;
	private final V value;

	public SimpleEntry(K key, V value) {
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
}
