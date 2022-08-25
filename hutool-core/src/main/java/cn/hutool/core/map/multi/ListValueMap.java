package cn.hutool.core.map.multi;

import java.util.*;
import java.util.function.Supplier;

/**
 * 值作为集合List的Map实现，通过调用putValue可以在相同key时加入多个值，多个值用集合表示
 *
 * @author looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 4.3.3
 */
public class ListValueMap<K, V> extends AbsCollValueMap<K, V> {
	private static final long serialVersionUID = 6044017508487827899L;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 *  基于{@code mapFactory}创建一个值为{@link List}的多值映射集合
	 *
	 * @param mapFactory 创建集合的工厂反方
	 */
	public ListValueMap(Supplier<Map<K, Collection<V>>> mapFactory) {
		super(mapFactory);
	}

	/**
	 *  基于{@link HashMap}创建一个值为{@link List}的多值映射集合
	 *
	 * @param map 提供数据的原始集合
	 */
	public ListValueMap(Map<K, Collection<V>> map) {
		super(map);
	}

	/**
	 * 基于{@link HashMap}创建一个值为{@link List}的多值映射集合
	 */
	public ListValueMap() {
	}

	// ------------------------------------------------------------------------- Constructor end

	@Override
	protected List<V> createCollection() {
		return new ArrayList<>(DEFAULT_COLLECTION_INITIAL_CAPACITY);
	}
}
