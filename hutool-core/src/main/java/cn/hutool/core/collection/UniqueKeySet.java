package cn.hutool.core.collection;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * 唯一键的Set<br>
 * 通过自定义唯一键，通过{@link #uniqueGenerator}生成节点对象对应的键作为Map的key，确定唯一<br>
 * 此Set与HashSet不同的是，HashSet依赖于{@link Object#equals(Object)}确定唯一<br>
 * 但是很多时候我们无法对对象进行修改，此时在外部定义一个唯一规则，即可完成去重。
 * <pre>
 * {@code Set<UniqueTestBean> set = new UniqueKeySet<>(UniqueTestBean::getId);}
 * </pre>
 *
 * @param <K> 唯一键类型
 * @param <V> 值对象
 * @author looly
 * @since 5.7.23
 */
public class UniqueKeySet<K, V> extends AbstractSet<V> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<K, V> map;
	private final Function<V, K> uniqueGenerator;

	//region 构造

	/**
	 * 构造
	 *
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(Function<V, K> uniqueGenerator) {
		this(false, uniqueGenerator);
	}

	/**
	 * 构造
	 *
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 * @param c 初始化加入的集合
	 * @since 5.8.0
	 */
	public UniqueKeySet(Function<V, K> uniqueGenerator, Collection<? extends V> c) {
		this(false, uniqueGenerator, c);
	}

	/**
	 * 构造
	 *
	 * @param isLinked        是否保持加入顺序
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(boolean isLinked, Function<V, K> uniqueGenerator) {
		this(MapBuilder.create(isLinked), uniqueGenerator);
	}

	/**
	 * 构造
	 *
	 * @param isLinked        是否保持加入顺序
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 * @param c 初始化加入的集合
	 * @since 5.8.0
	 */
	public UniqueKeySet(boolean isLinked, Function<V, K> uniqueGenerator, Collection<? extends V> c) {
		this(isLinked, uniqueGenerator);
		addAll(c);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      增长因子
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(int initialCapacity, float loadFactor, Function<V, K> uniqueGenerator) {
		this(MapBuilder.create(new HashMap<>(initialCapacity, loadFactor)), uniqueGenerator);
	}

	/**
	 * 构造
	 *
	 * @param builder         初始Map，定义了Map类型
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(MapBuilder<K, V> builder, Function<V, K> uniqueGenerator) {
		this.map = builder.build();
		this.uniqueGenerator = uniqueGenerator;
	}

	//endregion

	@Override
	public Iterator<V> iterator() {
		return map.values().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		//noinspection unchecked
		return map.containsKey(this.uniqueGenerator.apply((V) o));
	}

	@Override
	public boolean add(V v) {
		return null == map.put(this.uniqueGenerator.apply(v), v);
	}

	/**
	 * 加入值，如果值已经存在，则忽略之
	 *
	 * @param v 值
	 * @return 是否成功加入
	 */
	public boolean addIfAbsent(V v) {
		return null == map.putIfAbsent(this.uniqueGenerator.apply(v), v);
	}

	/**
	 * 加入集合中所有的值，如果值已经存在，则忽略之
	 *
	 * @param c 集合
	 * @return 是否有一个或多个被加入成功
	 */
	public boolean addAllIfAbsent(Collection<? extends V> c) {
		boolean modified = false;
		for (V v : c)
			if (addIfAbsent(v)) {
				modified = true;
			}
		return modified;
	}

	@Override
	public boolean remove(Object o) {
		//noinspection unchecked
		return null != map.remove(this.uniqueGenerator.apply((V) o));
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public UniqueKeySet<K, V> clone() {
		try {
			UniqueKeySet<K, V> newSet = (UniqueKeySet<K, V>) super.clone();
			newSet.map = ObjectUtil.clone(this.map);
			return newSet;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

}
