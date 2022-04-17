package cn.hutool.cache.impl;

import cn.hutool.cache.CacheListener;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.mutable.Mutable;
import cn.hutool.core.map.WeakConcurrentMap;

import java.lang.ref.Reference;

/**
 * 弱引用缓存<br>
 * 对于一个给定的键，其映射的存在并不阻止垃圾回收器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收。<br>
 * 丢弃某个键时，其条目从映射中有效地移除。<br>
 *
 * @author Looly
 *
 * @param <K> 键
 * @param <V> 值
 * @author looly
 * @since 3.0.7
 */
public class WeakCache<K, V> extends TimedCache<K, V>{
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 * @param timeout 超时时常，单位毫秒，-1或0表示无限制
	 */
	public WeakCache(long timeout) {
		super(timeout, new WeakConcurrentMap<>());
	}

	@Override
	public WeakCache<K, V> setListener(CacheListener<K, V> listener) {
		super.setListener(listener);

		final WeakConcurrentMap<Mutable<K>, CacheObj<K, V>> map = (WeakConcurrentMap<Mutable<K>, CacheObj<K, V>>) this.cacheMap;
		// WeakKey回收之后，key对应的值已经是null了，因此此处的key也为null
		map.setPurgeListener((key, value)-> listener.onRemove(Opt.ofNullable(key).map(Reference::get).map(Mutable::get).get(), value.getValue()));

		return this;
	}
}
