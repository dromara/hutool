package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.convert.JSONConverter;
import cn.hutool.json.serialize.JSONDeserializer;
import cn.hutool.json.serialize.JSONString;

/**
 * JSON支持<br>
 * 继承此类实现实体类与JSON的相互转换
 *
 * @author Looly
 */
public class JSONSupport implements JSONString, JSONDeserializer<Object> {

	/**
	 * JSON String转Bean
	 *
	 * @param jsonString JSON String
	 */
	public void deserialize(final String jsonString) {
		deserialize(new JSONObject(jsonString));
	}

	/**
	 * JSON转Bean
	 *
	 * @param json JSON
	 */
	@Override
	public Object deserialize(final JSON json) {
		final JSONSupport support = JSONConverter.jsonToBean(getClass(), json, false);
		BeanUtil.copyProperties(support, this);
		return this;
	}

	/**
	 * @return JSON对象
	 */
	public JSONObject toJSON() {
		return new JSONObject(this);
	}

	@Override
	public String toJSONString() {
		return toJSON().toString();
	}

	/**
	 * 美化的JSON（使用回车缩进显示JSON），用于打印输出debug
	 *
	 * @return 美化的JSON
	 */
	public String toPrettyString() {
		return toJSON().toStringPretty();
	}

	@Override
	public String toString() {
		return toJSONString();
	}
}
