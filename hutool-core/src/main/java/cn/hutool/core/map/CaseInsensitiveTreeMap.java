package cn.hutool.core.map;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 忽略大小写的{@link TreeMap}<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 *
 * @author Looly
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @since 3.3.1
 */
public class CaseInsensitiveTreeMap<K, V> extends CustomKeyMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	// ------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public CaseInsensitiveTreeMap() {
		this((Comparator<? super K>) null);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 * @since 3.1.2
	 */
	public CaseInsensitiveTreeMap(Map<? extends K, ? extends V> m) {
		this();
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 * @since 3.1.2
	 */
	public CaseInsensitiveTreeMap(SortedMap<? extends K, ? extends V> m) {
		super(new TreeMap<K, V>(m));
	}

	/**
	 * 构造
	 *
	 * @param comparator 比较器，{@code null}表示使用默认比较器
	 */
	public CaseInsensitiveTreeMap(Comparator<? super K> comparator) {
		super(new TreeMap<>(comparator));
	}
	// ------------------------------------------------------------------------- Constructor end

	/**
	 * 将Key转为小写
	 *
	 * @param key KEY
	 * @return 小写KEY
	 */
	@Override
	protected Object customKey(Object key) {
		if (key instanceof CharSequence) {
			key = key.toString().toLowerCase();
		}
		return key;
	}
}
