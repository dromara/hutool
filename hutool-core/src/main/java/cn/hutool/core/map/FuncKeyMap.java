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
	 * 构造
	 *
	 * @param m       Map
	 * @param keyFunc 自定义KEY的函数
	 */
	public FuncKeyMap(Map<K, V> m, Function<Object, K> keyFunc) {
		super(m);
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
	protected Object customKey(Object key) {
		if (null != this.keyFunc) {
			return keyFunc.apply(key);
		}
		return key;
	}
}
