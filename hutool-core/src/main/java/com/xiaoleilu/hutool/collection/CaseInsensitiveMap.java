package com.xiaoleilu.hutool.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * 忽略大小写的Map<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 3.0.2
 */
public class CaseInsensitiveMap<K, V> extends HashMap<K, V>{
	private static final long serialVersionUID = 4043263744224569870L;
	
	/**
	 * 构造
	 */
	public CaseInsensitiveMap() {
		super();
	}
	
	/**
	 * 构造
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CaseInsensitiveMap(int initialCapacity, float loadFactor){
		super(initialCapacity, loadFactor);
	}
	
	/**
	 * 构造
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveMap(int initialCapacity) {
		this(initialCapacity, 0.75f);
	}
	
	/**
	 * 构造
	 * @param m Map
	 */
	public CaseInsensitiveMap(Map<? extends K, ? extends V> m) {
		super((int) (m.size() / 0.75));
		putAll(m);
	}
	
	@Override
	public V get(Object key) {
		return super.get(lowerCaseKey(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		return super.put((K)lowerCaseKey(key), value);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}
	
	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(lowerCaseKey(key));
	}
	
	/**
	 * 将Key转为小写
	 * @param key KEY
	 * @return 小写KEy
	 */
	private static Object lowerCaseKey(Object key){
		if(key instanceof CharSequence){
			key = key.toString().toLowerCase();
		}
		return key;
	}
}
