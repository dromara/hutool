package cn.hutool.core.collection;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过{@link ConcurrentHashMap}实现的线程安全HashSet
 *
 * @author Looly
 *
 * @param <E> 元素类型
 * @since 3.1.0
 */
public class ConcurrentHashSet<E> extends SetFromMap<E> {
	private static final long serialVersionUID = 7997886765361607470L;

	// ----------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造<br>
	 * 触发因子为默认的0.75
	 */
	public ConcurrentHashSet() {
		super(new ConcurrentHashMap<>());
	}

	/**
	 * 构造<br>
	 * 触发因子为默认的0.75
	 *
	 * @param initialCapacity 初始大小
	 */
	public ConcurrentHashSet(final int initialCapacity) {
		super(new ConcurrentHashMap<>(initialCapacity));
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子。此参数决定数据增长时触发的百分比
	 */
	public ConcurrentHashSet(final int initialCapacity, final float loadFactor) {
		super(new ConcurrentHashMap<>(initialCapacity, loadFactor));
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor 触发因子。此参数决定数据增长时触发的百分比
	 * @param concurrencyLevel 线程并发度
	 */
	public ConcurrentHashSet(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
		super(new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel));
	}

	/**
	 * 从已有集合中构造
	 * @param iter {@link Iterable}
	 */
	public ConcurrentHashSet(final Iterable<E> iter) {
		super(iter instanceof  Collection ? new ConcurrentHashMap<>((int)(((Collection<E>)iter).size() / 0.75f)) : new ConcurrentHashMap<>());
		if(iter instanceof Collection) {
			this.addAll((Collection<E>)iter);
		}else {
			for (final E e : iter) {
				this.add(e);
			}
		}
	}
	// ----------------------------------------------------------------------------------- Constructor end
}
