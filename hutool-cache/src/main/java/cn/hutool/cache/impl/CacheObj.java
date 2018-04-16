package cn.hutool.cache.impl;

/**
 * 缓存对象
 * @author Looly
 *
 * @param <K> Key类型
 * @param <V> Value类型
 */
public class CacheObj<K, V> {
	
	final K key;
	final V obj;
	
	/** 上次访问时间 */
	long lastAccess; 
	/** 访问次数 */
	long accessCount;
	/** 对象存活时长，0表示永久存活*/
	long ttl;
	
	CacheObj(K key, V obj, long ttl) {
		this.key = key;
		this.obj = obj;
		this.ttl = ttl;
		this.lastAccess = System.currentTimeMillis();
	}
	
	/**
	 * 判断是否过期
	 * 
	 * @return 是否过期
	 */
	boolean isExpired() {
		return (this.ttl > 0) && (this.lastAccess + this.ttl < System.currentTimeMillis());
	}
	
	/**
	 * 获取值
	 * 
	 * @param isUpdateLastAccess 是否更新最后访问时间
	 * @return 获得对象
	 * @since 4.0.10
	 */
	V get(boolean isUpdateLastAccess) {
		if(isUpdateLastAccess) {
			lastAccess = System.currentTimeMillis();
		}
		accessCount++;
		return obj;
	}
	
	/**
	 * 获取键
	 * @return 键
	 * @since 4.0.10
	 */
	public K getKey() {
		return this.key;
	}
	
	/**
	 * 获取值
	 * @return 值
	 * @since 4.0.10
	 */
	public V getValue() {
		return this.obj;
	}
	
	@Override
	public String toString() {
		return "CacheObj [key=" + key + ", obj=" + obj + ", lastAccess=" + lastAccess + ", accessCount=" + accessCount + ", ttl=" + ttl + "]";
	}
}
