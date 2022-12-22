package cn.hutool.core.map;

import java.util.Map;
import java.util.function.Function;

/**
 * 自定义函数Key风格的Map
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 * @since 5.6.0
 */
public class FuncKeyMap<K, V> extends CustomKeyMap<K, V> {
	private static final long serialVersionUID = 1L;

	private final Function<Object, K> keyFunc;

	// ------------------------------------------------------------------------- Constructor start

	/**
	 * 构造<br>
	 * 注意提供的Map中不能有键值对，否则可能导致自定义key失效
	 *
	 * @param emptyMap       Map，提供的空map
	 * @param keyFunc 自定义KEY的函数
	 */
	public FuncKeyMap(Map<K, V> emptyMap, Function<Object, K> keyFunc) {
		super(emptyMap);
		this.keyFunc = keyFunc;
	}
	// ------------------------------------------------------------------------- Constructor end

	/**
	 * 根据函数自定义键
	 *
	 * @param key KEY
	 * @return 驼峰Key
	 */
	@Override
	protected K customKey(Object key) {
		if (null != this.keyFunc) {
			return keyFunc.apply(key);
		}
		//noinspection unchecked
		return (K)key;
	}
}
