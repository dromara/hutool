package cn.hutool.cache.impl;

import cn.hutool.core.map.FixedLinkedHashMap;

import java.util.Iterator;

/**
 * LRU (least recently used)最近最久未使用缓存<br>
 * 根据使用时间来判定对象是否被持续缓存<br>
 * 当对象被访问时放入缓存，当缓存满了，最久未被使用的对象将被移除。<br>
 * 此缓存基于LinkedHashMap，因此当被缓存的对象每被访问一次，这个对象的key就到链表头部。<br>
 * 这个算法简单并且非常快，他比FIFO有一个显著优势是经常使用的对象不太可能被移除缓存。<br>
 * 缺点是当缓存满时，不能被很快的访问。
 * @author Looly,jodd
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造<br>
	 * 默认无超时
	 * @param capacity 容量
	 */
	public LRUCache(int capacity) {
		this(capacity, 0);
	}

	/**
	 * 构造
	 * @param capacity 容量
	 * @param timeout 默认超时时间，单位：毫秒
	 */
	public LRUCache(int capacity, long timeout) {
		if(Integer.MAX_VALUE == capacity) {
			capacity -= 1;
		}

		this.capacity = capacity;
		this.timeout = timeout;
		
		//链表key按照访问顺序排序，调用get方法后，会将这次访问的元素移至头部
		cacheMap = new FixedLinkedHashMap<>(capacity);
	}

	// ---------------------------------------------------------------- prune

	/**
	 * 只清理超时对象，LRU的实现会交给<code>LinkedHashMap</code>
	 */
	@Override
	protected int pruneCache() {
		if (isPruneExpiredActive() == false) {
			return 0;
		}
		int count = 0;
		Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
		CacheObj<K, V> co;
		while (values.hasNext()) {
			co = values.next();
			if (co.isExpired()) {
				values.remove();
				onRemove(co.key, co.obj);
				count++;
			}
		}
		return count;
	}
}
