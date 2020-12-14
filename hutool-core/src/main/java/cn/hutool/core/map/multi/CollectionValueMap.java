package cn.hutool.core.map.multi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 值作为集合的Map实现，通过调用putValue可以在相同key时加入多个值，多个值用集合表示
 * 
 * @author looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 4.3.3
 */
public abstract class CollectionValueMap<K, V> extends MapWrapper<K, Collection<V>> {

	private static final long serialVersionUID = 9012989578038102983L;

	/** 默认集合初始大小 */
	protected static final int DEFAULT_COLLCTION_INITIAL_CAPACITY = 3;

	// ------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public CollectionValueMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 */
	public CollectionValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 * 
	 * @param m Map
	 */
	public CollectionValueMap(Map<? extends K, ? extends Collection<V>> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 * 
	 * @param loadFactor 加载因子
	 * @param m Map
	 */
	public CollectionValueMap(float loadFactor, Map<? extends K, ? extends Collection<V>> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CollectionValueMap(int initialCapacity, float loadFactor) {
		super(new HashMap<>(initialCapacity, loadFactor));
	}
	// ------------------------------------------------------------------------- Constructor end

	/**
	 * 放入Value<br>
	 * 如果键对应值列表有值，加入，否则创建一个新列表后加入
	 * 
	 * @param key 键
	 * @param value 值
	 */
	public void putValue(K key, V value) {
		Collection<V> collection = this.get(key);
		if (null == collection) {
			collection = createCollection();
			this.put(key, collection);
		}
		collection.add(value);
	}

	/**
	 * 获取值
	 * 
	 * @param key 键
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
	protected abstract Collection<V> createCollection();
}
