package cn.hutool.cache.impl;

import java.io.Serializable;

/**
 * 缓存对象
 * @author Looly
 *
 * @param <K> Key类型
 * @param <V> Value类型
 */
public class CacheObj<K, V> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected final K key;
	protected final V obj;
	
	/** 上次访问时间 */
	private long lastAccess; 
	/** 访问次数 */
	protected long accessCount;
	/** 对象存活时长，0表示永久存活*/
	private long ttl;
	
	/**
	 * 构造
	 * 
	 * @param key 键
	 * @param obj 值
	 * @param ttl 超时时长
	 */
	protected CacheObj(K key, V obj, long ttl) {
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
		if(this.ttl > 0) {
			final long expiredTime = this.lastAccess + this.ttl;
			// expiredTime > 0 杜绝Long类型溢出变负数问题，当当前时间超过过期时间，表示过期
			return expiredTime > 0 && expiredTime < System.currentTimeMillis();
		}
		return false;
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
		return this.obj;
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
