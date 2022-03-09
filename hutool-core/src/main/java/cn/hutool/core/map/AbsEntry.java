package cn.hutool.core.map;

import cn.hutool.core.util.ObjectUtil;

import java.util.Map;

/**
 * 抽象的{@link Map.Entry}实现，来自Guava<br>
 * 实现了默认的{@link #equals(Object)}、{@link #hashCode()}、{@link #toString()}方法。<br>
 * 默认{@link #setValue(Object)}抛出异常。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Guava
 * @since 5.7.23
 */
public abstract class AbsEntry<K, V> implements Map.Entry<K, V> {

	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException("Entry is read only.");
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Map.Entry) {
			final Map.Entry<?, ?> that = (Map.Entry<?, ?>) object;
			return ObjectUtil.equals(this.getKey(), that.getKey())
					&& ObjectUtil.equals(this.getValue(), that.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		//copy from 1.8 HashMap.Node
		K k = getKey();
		V v = getValue();
		return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
	}

	@Override
	public String toString() {
		return getKey() + "=" + getValue();
	}
}
