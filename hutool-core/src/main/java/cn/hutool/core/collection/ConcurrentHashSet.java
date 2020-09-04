package cn.hutool.core.collection;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过{@link ConcurrentHashMap}实现的线程安全HashSet
 * 
 * @author Looly
 *
 * @param <E> 元素类型
 * @since 3.1.0
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements java.io.Serializable {
	private static final long serialVersionUID = 7997886765361607470L;

	/** 持有对象。如果值为此对象表示有数据，否则无数据 */
	private static final Boolean PRESENT = true;
	private final ConcurrentHashMap<E, Boolean> map;

	// ----------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造<br>
	 * 触发因子为默认的0.75
	 */
	public ConcurrentHashSet() {
		map = new ConcurrentHashMap<>();
	}

	/**
	 * 构造<br>
	 * 触发因子为默认的0.75
	 * 
	 * @param initialCapacity 初始大小
	 */
	public ConcurrentHashSet(int initialCapacity) {
		map = new ConcurrentHashMap<>(initialCapacity);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子。此参数决定数据增长时触发的百分比
	 */
	public ConcurrentHashSet(int initialCapacity, float loadFactor) {
		map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 * @param loadFactor 触发因子。此参数决定数据增长时触发的百分比
	 * @param concurrencyLevel 线程并发度
	 */
	public ConcurrentHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
		map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
	}
	
	/**
	 * 从已有集合中构造
	 * @param iter {@link Iterable}
	 */
	public ConcurrentHashSet(Iterable<E> iter) {
		if(iter instanceof Collection) {
			final Collection<E> collection = (Collection<E>)iter;
			map = new ConcurrentHashMap<>((int)(collection.size() / 0.75f));
			this.addAll(collection);
		}else {
			map = new ConcurrentHashMap<>();
			for (E e : iter) {
				this.add(e);
			}
		}
	}
	// ----------------------------------------------------------------------------------- Constructor end

	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
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
		return map.containsKey(o);
	}

	@Override
	public boolean add(E e) {
		return map.put(e, PRESENT) == null;
	}

	@Override
	public boolean remove(Object o) {
		return PRESENT.equals(map.remove(o));
	}

	@Override
	public void clear() {
		map.clear();
	}
}