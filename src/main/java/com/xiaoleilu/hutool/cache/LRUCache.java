package com.xiaoleilu.hutool.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * LRU (least recently used) cache.
 *
 * <p>
 * Items are added to the cache as they are accessed; when the cache is full, the least recently used item is ejected. This type of cache is typically implemented as a linked list, so that an item in
 * cache, when it is accessed again, can be moved back up to the head of the queue; items are ejected from the tail of the queue. Cache access overhead is again constant time. This algorithm is simple
 * and fast, and it has a significant advantage over FIFO in being able to adapt somewhat to the data access pattern; frequently used items are less likely to be ejected from the cache. The main
 * disadvantage is that it can still get filled up with items that are unlikely to be reaccessed soon; in particular, it can become useless in the face of scanning type accesses. Nonetheless, this is
 * by far the most frequently used caching algorithm.
 * <p>
 * Implementation note: unfortunately, it was not possible to have <code>onRemove</code> callback method, since <code>LinkedHashMap</code> has its removal methods private.
 * <p>
 * Summary for LRU: fast, adaptive, not scan resistant.
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {

	public LRUCache(int cacheSize) {
		this(cacheSize, 0);
	}

	/**
	 * Creates a new LRU cache.
	 */
	public LRUCache(int capacity, long timeout) {
		this.capacity = capacity;
		this.timeout = timeout;
		cacheMap = new LinkedHashMap<K, CacheObj<K, V>>(capacity + 1, 1.0f, true){
			private static final long serialVersionUID = -1806954614512571136L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, CacheObj<K, V>> eldest) {
				return LRUCache.this.removeEldestEntry(size());
			}
		};
	}

	/**
	 * Removes the eldest entry if current cache size exceed cache size.
	 */
	protected boolean removeEldestEntry(int currentSize) {
		if (this.capacity == 0) {
			return false;
		}
		return currentSize > this.capacity;
	}

	// ---------------------------------------------------------------- prune

	/**
	 * Prune only expired objects, <code>LinkedHashMap</code> will take care of LRU if needed.
	 */
	@Override
	protected int pruneCache() {
		if (isPruneExpiredActive() == false) {
			return 0;
		}
		int count = 0;
		Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
		while (values.hasNext()) {
			CacheObj<K, V> co = values.next();
			if (co.isExpired() == true) {
				values.remove();
				count++;
			}
		}
		return count;
	}
}
