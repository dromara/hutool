package cn.hutool.core.bean.copier.provider;

import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map值提供者，支持驼峰和下划线的key兼容。<br>
 * 假设目标属性为firstName，则Map中以下形式的值都可以对应：
 * <ul>
 *     <li>firstName</li>
 *     <li>first_name</li>
 *     <li>isFirstName（如果为Boolean或boolean类型的值）</li>
 *     <li>is_first_name（如果为Boolean或boolean类型的值）</li>
 * </ul>
 * 为firstName或first_name都可以对应到值。
 *
 * @author looly
 */
public class MapValueProvider implements ValueProvider<String> {

	private final Map<?, ?> map;
	private final boolean ignoreError;

	/**
	 * 构造
	 *
	 * @param map        Map
	 * @param ignoreCase 是否忽略key的大小写
	 */
	public MapValueProvider(Map<?, ?> map, boolean ignoreCase) {
		this(map, ignoreCase, false);
	}

	/**
	 * 构造
	 *
	 * @param map         Map
	 * @param ignoreCase  是否忽略key的大小写
	 * @param ignoreError 是否忽略错误
	 * @since 5.3.2
	 */
	public MapValueProvider(Map<?, ?> map, boolean ignoreCase, boolean ignoreError) {
		if (false == ignoreCase || map instanceof CaseInsensitiveMap) {
			//不忽略大小写或者提供的Map本身为CaseInsensitiveMap则无需转换
			this.map = map;
		} else {
			//转换为大小写不敏感的Map
			this.map = new CaseInsensitiveMap<>(map);
		}
		this.ignoreError = ignoreError;
	}

	@Override
	public Object value(String key, Type valueType) {
		final String key1 = getKey(key, valueType);
		if (null == key1) {
			return null;
		}

		final Object value = map.get(key1);
		return Convert.convertWithCheck(valueType, value, null, this.ignoreError);
	}

	@Override
	public boolean containsKey(String key) {
		return null != getKey(key, null);
	}

	/**
	 * 获得map中可能包含的key,不包含返回null
	 *
	 * @param key       map中可能包含的key
	 * @param valueType 值类型，用于判断是否为Boolean，可以为null
	 * @return map中可能包含的key
	 */
	private String getKey(String key, Type valueType) {
		if (map.containsKey(key)) {
			return key;
		}

		//检查下划线模式
		String customKey = StrUtil.toUnderlineCase(key);
		if (map.containsKey(customKey)) {
			return customKey;
		}

		//检查boolean类型
		if (null == valueType || Boolean.class == valueType || boolean.class == valueType) {
			//boolean类型字段字段名支持两种方式
			customKey = StrUtil.upperFirstAndAddPre(key, "is");
			if (map.containsKey(customKey)) {
				return customKey;
			}

			//检查下划线模式
			customKey = StrUtil.toUnderlineCase(customKey);
			if (map.containsKey(customKey)) {
				return customKey;
			}
		}
		return null;
	}

}
