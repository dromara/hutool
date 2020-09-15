package cn.hutool.cache;

import cn.hutool.cache.impl.CacheObj;
import cn.hutool.core.lang.func.Func0;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 缓存接口
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly, jodd
 */
public interface Cache<K, V> extends Iterable<V>, Serializable {

	/**
	 * 返回缓存容量，<code>0</code>表示无大小限制
	 *
	 * @return 返回缓存容量，<code>0</code>表示无大小限制
	 */
	int capacity();

	/**
	 * 缓存失效时长， <code>0</code> 表示没有设置，单位毫秒
	 *
	 * @return 缓存失效时长， <code>0</code> 表示没有设置，单位毫秒
	 */
	long timeout();

	/**
	 * 将对象加入到缓存，使用默认失效时长
	 *
	 * @param key    键
	 * @param object 缓存的对象
	 * @see Cache#put(Object, Object, long)
	 */
	void put(K key, V object);

	/**
	 * 将对象加入到缓存，使用指定失效时长<br>
	 * 如果缓存空间满了，{@link #prune()} 将被调用以获得空间来存放新对象
	 *
	 * @param key     键
	 * @param object  缓存的对象
	 * @param timeout 失效时长，单位毫秒
	 * @see Cache#put(Object, Object, long)
	 */
	void put(K key, V object, long timeout);

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回<code>null</code>
	 * <p>
	 * 调用此方法时，会检查上次调用时间，如果与当前时间差值大于超时时间返回<code>null</code>，否则返回值。
	 * <p>
	 * 每次调用此方法会刷新最后访问时间，也就是说会重新计算超时时间。
	 *
	 * @param key 键
	 * @return 键对应的对象
	 * @see #get(Object, boolean)
	 */
	default V get(K key) {
		return get(key, true);
	}

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回Func0回调产生的对象
	 * <p>
	 * 调用此方法时，会检查上次调用时间，如果与当前时间差值大于超时时间返回<code>null</code>，否则返回值。
	 * <p>
	 * 每次调用此方法会刷新最后访问时间，也就是说会重新计算超时时间。
	 *
	 * @param key      键
	 * @param supplier 如果不存在回调方法，用于生产值对象
	 * @return 值对象
	 */
	default V get(K key, Func0<V> supplier) {
		return get(key, true, supplier);
	}

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回Func0回调产生的对象
	 * <p>
	 * 调用此方法时，会检查上次调用时间，如果与当前时间差值大于超时时间返回<code>null</code>，否则返回值。
	 * <p>
	 * 每次调用此方法会刷新最后访问时间，也就是说会重新计算超时时间。
	 *
	 * @param key                键
	 * @param isUpdateLastAccess 是否更新最后访问时间，即重新计算超时时间。
	 * @param supplier           如果不存在回调方法，用于生产值对象
	 * @return 值对象
	 */
	V get(K key, boolean isUpdateLastAccess, Func0<V> supplier);

	/**
	 * 从缓存中获得对象，当对象不在缓存中或已经过期返回<code>null</code>
	 * <p>
	 * 调用此方法时，会检查上次调用时间，如果与当前时间差值大于超时时间返回<code>null</code>，否则返回值。
	 *
	 * @param key                键
	 * @param isUpdateLastAccess 是否更新最后访问时间，即重新计算超时时间。
	 * @return 键对应的对象
	 */
	V get(K key, boolean isUpdateLastAccess);

	/**
	 * 返回包含键和值得迭代器
	 *
	 * @return 缓存对象迭代器
	 * @since 4.0.10
	 */
	Iterator<CacheObj<K, V>> cacheObjIterator();

	/**
	 * 从缓存中清理过期对象，清理策略取决于具体实现
	 *
	 * @return 清理的缓存对象个数
	 */
	int prune();

	/**
	 * 缓存是否已满，仅用于有空间限制的缓存对象
	 *
	 * @return 缓存是否已满，仅用于有空间限制的缓存对象
	 */
	boolean isFull();

	/**
	 * 从缓存中移除对象
	 *
	 * @param key 键
	 */
	void remove(K key);

	/**
	 * 清空缓存
	 */
	void clear();

	/**
	 * 缓存的对象数量
	 *
	 * @return 缓存的对象数量
	 */
	int size();

	/**
	 * 缓存是否为空
	 *
	 * @return 缓存是否为空
	 */
	boolean isEmpty();

	/**
	 * 是否包含key
	 *
	 * @param key KEY
	 * @return 是否包含key
	 */
	boolean containsKey(K key);
}
