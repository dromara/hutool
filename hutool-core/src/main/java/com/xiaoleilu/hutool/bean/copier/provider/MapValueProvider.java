package com.xiaoleilu.hutool.bean.copier.provider;

import java.lang.reflect.Type;
import java.util.Map;

import com.xiaoleilu.hutool.bean.copier.ValueProvider;
import com.xiaoleilu.hutool.map.CaseInsensitiveMap;

/**
 * Map值提供者
 * 
 * @author looly
 *
 */
public class MapValueProvider implements ValueProvider<String> {

	Map<?, ?> map;

	/**
	 * 构造
	 * 
	 * @param map Map
	 * @param ignoreCase 是否忽略key的大小写
	 */
	public MapValueProvider(Map<?, ?> map, boolean ignoreCase) {
		if(ignoreCase) {
			if(map instanceof CaseInsensitiveMap) {
				this.map = (CaseInsensitiveMap<?, ?>)map;
			}else {
				this.map = new CaseInsensitiveMap<>(map);
			}
		}
		this.map = map;
	}

	@Override
	public Object value(String key, Type valueType) {
		return map.get(key);
	}

	@Override
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

}
