package cn.hutool.core.map;

import java.util.Map;

/**
 * 忽略大小写的LinkedHashMap<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 * 
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 3.3.1
 */
public class CaseInsensitiveLinkedMap<K, V> extends CustomKeyLinkedMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CaseInsensitiveLinkedMap() {
		super();
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CaseInsensitiveLinkedMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * 构造
	 * 
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveLinkedMap(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * 构造
	 * 
	 * @param m Map
	 */
	public CaseInsensitiveLinkedMap(Map<? extends K, ? extends V> m) {
		super(m);
	}

	/**
	 * 构造
	 * 
	 * @param loadFactor 加载因子
	 * @param m Map
	 * @since 3.1.2
	 */
	public CaseInsensitiveLinkedMap(float loadFactor, Map<? extends K, ? extends V> m) {
		super(loadFactor, m);
	}

	/**
	 * 将Key转为小写
	 * 
	 * @param key KEY
	 * @return 小写KEy
	 */
	@Override
	protected Object customKey(Object key) {
		if (null != key && key instanceof CharSequence) {
			key = key.toString().toLowerCase();
		}
		return key;
	}
}
