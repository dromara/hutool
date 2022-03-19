package cn.hutool.core.bean.copier.provider;

import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.map.FuncKeyMap;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
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
 *
 * @author looly
 */
public class MapValueProvider implements ValueProvider<String> {

	@SuppressWarnings("rawtypes")
	private final Map map;
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
		this(map, ignoreCase, ignoreError, null);
	}

	/**
	 * 构造
	 *
	 * @param map         Map
	 * @param ignoreCase  是否忽略key的大小写
	 * @param ignoreError 是否忽略错误
	 * @param keyEditor   自定义键编辑器
	 * @since 5.7.23
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public MapValueProvider(Map map, boolean ignoreCase, boolean ignoreError, Editor<String> keyEditor) {
		// issue#2202@Github
		// 如果用户定义了键编辑器，则提供的map中的数据必须全部转换key
		this.map = new FuncKeyMap(new HashMap(map.size(), 1), (key)->{
			if(ignoreCase && key instanceof CharSequence){
				key = key.toString().toLowerCase();
			}
			if(null != keyEditor){
				key = keyEditor.edit(key.toString());
			}
			return key;
		});
		this.map.putAll(map);

		this.ignoreError = ignoreError;
	}

	@Override
	public Object value(String key, Type valueType) {
		final String key1 = getKey(key, valueType);
		if (null == key1) {
			return null;
		}

		return Convert.convertWithCheck(valueType, map.get(key1), null, this.ignoreError);
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
