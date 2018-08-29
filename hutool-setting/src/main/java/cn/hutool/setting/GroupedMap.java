package cn.hutool.setting;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 基于分组的Map
 * 
 * @author looly
 * @since 4.0.11
 */
public class GroupedMap extends LinkedHashMap<String, LinkedHashMap<String, String>> {
	private static final long serialVersionUID = -7777365130776081931L;

	private int size = -1;

	/**
	 * 获取分组对应的值，如果分组不存在或者值不存在则返回null
	 * 
	 * @param group 分组
	 * @param key 键
	 * @return 值，如果分组不存在或者值不存在则返回null
	 */
	public String get(String group, String key) {
		LinkedHashMap<String, String> map = this.get(StrUtil.nullToEmpty(group));
		if (MapUtil.isNotEmpty(map)) {
			return map.get(key);
		}
		return null;
	}

	/**
	 * 总的键值对数
	 * 
	 * @return 总键值对数
	 */
	public int size() {
		if (this.size < 0) {
			this.size = 0;
			for (LinkedHashMap<String, String> value : this.values()) {
				this.size += value.size();
			}
		}
		return this.size;
	}

	/**
	 * 将键值对加入到对应分组中
	 * 
	 * @param group 分组
	 * @param key 键
	 * @param value 值
	 * @return 此key之前存在的值，如果没有返回null
	 */
	public String put(String group, String key, String value) {
		group = StrUtil.nullToEmpty(group).trim();
		LinkedHashMap<String, String> valueMap = this.get(group);
		if (null == valueMap) {
			valueMap = new LinkedHashMap<>();
			this.put(group, valueMap);
		}

		return valueMap.put(key, value);
	}

	/**
	 * 加入多个键值对到某个分组下
	 * 
	 * @param group 分组
	 * @param m 键值对
	 * @return this
	 */
	public GroupedMap putAll(String group, Map<? extends String, ? extends String> m) {
		for (Entry<? extends String, ? extends String> entry : m.entrySet()) {
			this.put(group, entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * 从指定分组中删除指定值
	 * 
	 * @param group 分组
	 * @param key 键
	 * @return 被删除的值，如果值不存在，返回null
	 */
	public String remove(String group, String key) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.remove(key);
		}
		return null;
	}

	/**
	 * 某个分组对应的键值对是否为空
	 * 
	 * @param group 分组
	 * @return 是否为空
	 */
	public boolean isEmpty(String group) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.isEmpty();
		}
		return true;
	}

	/**
	 * 是否为空，如果多个分组同时为空，也按照空处理
	 * 
	 * @return 是否为空，如果多个分组同时为空，也按照空处理
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/**
	 * 指定分组中是否包含指定key
	 * 
	 * @param group 分组
	 * @param key 键
	 * @return 是否包含key
	 */
	public boolean containsKey(String group, String key) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.containsKey(key);
		}
		return false;
	}

	/**
	 * 指定分组中是否包含指定值
	 * 
	 * @param group 分组
	 * @param value 值
	 * @return 是否包含值
	 */
	public boolean containsValue(String group, String value) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.containsValue(value);
		}
		return false;
	}

	/**
	 * 清除指定分组下的所有键值对
	 * 
	 * @param group 分组
	 * @return this
	 */
	public GroupedMap clear(String group) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			valueMap.clear();
		}
		return this;
	}

	/**
	 * 指定分组所有键的Set
	 * 
	 * @param group 分组
	 * @return 键Set
	 */
	public Set<String> keySet(String group) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.keySet();
		}
		return Collections.emptySet();
	}

	/**
	 * 指定分组下所有值
	 * 
	 * @param group 分组
	 * @return 值
	 */
	public Collection<String> values(String group) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.values();
		}
		return Collections.emptyList();
	}

	/**
	 * 指定分组下所有键值对
	 * 
	 * @param group 分组
	 * @return 键值对
	 */
	public Set<java.util.Map.Entry<String, String>> entrySet(String group) {
		group = StrUtil.nullToEmpty(group).trim();
		final LinkedHashMap<String, String> valueMap = this.get(group);
		if (MapUtil.isNotEmpty(valueMap)) {
			return valueMap.entrySet();
		}
		return Collections.emptySet();
	}
}
