package cn.hutool.core.bean.copier.provider;

import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map值提供者，支持驼峰和下划线的key兼容。<br>
 * 假设目标属性为firstName，则Map中为firstName或first_name都可以对应到值。
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
		Object value = map.get(key);
		if (null == value) {
			//检查下划线模式
			value = map.get(StrUtil.toUnderlineCase(key));
		}

		return Convert.convertWithCheck(valueType, value, null, this.ignoreError);
	}

	@Override
	public boolean containsKey(String key) {
		if (map.containsKey(key)) {
			return true;
		} else {
			//检查下划线模式
			return map.containsKey(StrUtil.toUnderlineCase(key));
		}
	}

}
