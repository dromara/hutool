package cn.hutool.core.map.multi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 值作为集合的Map实现，通过调用putValue可以在相同key时加入多个值，多个值用集合表示
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @param <C> 集合类型
 * @author looly
 * @since 5.7.4
 */
public abstract class AbsCollValueMap<K, V, C extends Collection<V>> extends MapWrapper<K, C> {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认集合初始大小
	 */
	protected static final int DEFAULT_COLLCTION_INITIAL_CAPACITY = 3;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public AbsCollValueMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public AbsCollValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public AbsCollValueMap(Map<? extends K, C> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m          Map
	 */
	public AbsCollValueMap(float loadFactor, Map<? extends K, C> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor      加载因子
	 */
	public AbsCollValueMap(int initialCapacity, float loadFactor) {
		super(new HashMap<>(initialCapacity, loadFactor));
	}
	// ------------------------------------------------------------------------- Constructor end

	/**
	 * 放入所有value
	 *
	 * @param m valueMap
	 * @since 5.7.4
	 */
	public void putAllValues(Map<? extends K, ? extends Collection<V>> m) {
		if(null != m){
			m.forEach((key, valueColl) -> {
				if(null != valueColl){
					valueColl.forEach((value) -> putValue(key, value));
				}
			});
		}
	}

	/**
	 * 放入Value<br>
	 * 如果键对应值列表有值，加入，否则创建一个新列表后加入
	 *
	 * @param key   键
	 * @param value 值
	 */
	public void putValue(K key, V value) {
		C collection = this.get(key);
		if (null == collection) {
			collection = createCollection();
			this.put(key, collection);
		}
		collection.add(value);
	}

	/**
	 * 获取值
	 *
	 * @param key   键
	 * @param index 第几个值的索引，越界返回null
	 * @return 值或null
	 */
	public V get(K key, int index) {
		final Collection<V> collection = get(key);
		return CollUtil.get(collection, index);
	}

	/**
	 * 创建集合<br>
	 * 此方法用于创建在putValue后追加值所在的集合，子类实现此方法创建不同类型的集合
	 *
	 * @return {@link Collection}
	 */
	protected abstract C createCollection();
}
