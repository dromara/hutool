package cn.hutool.json.convert;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * JSON转换器，实现Object对象转换为{@link JSON}，支持的对象：
 * <ul>
 *     <li>String: 转换为相应的对象</li>
 *     <li>Array、Iterable、Iterator：转换为JSONArray</li>
 *     <li>Bean对象：转为JSONObject</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONConverter implements Converter {
	public static final JSONConverter INSTANCE = new JSONConverter(null);

	/**
	 * 创建JSON转换器
	 *
	 * @param config JSON配置
	 * @return JSONConverter
	 */
	public static JSONConverter of(final JSONConfig config) {
		return new JSONConverter(config);
	}

	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param config JSON配置
	 */
	public JSONConverter(final JSONConfig config) {
		this.config = config;
	}

	/**
	 * 实现Object对象转换为{@link JSON}，支持的对象：
	 * <ul>
	 *     <li>String: 转换为相应的对象</li>
	 *     <li>Array、Iterable、Iterator：转换为JSONArray</li>
	 *     <li>Bean对象：转为JSONObject</li>
	 * </ul>
	 *
	 * @param obj 被转换的对象
	 * @return 转换后的对象
	 * @throws ConvertException 转换异常
	 */
	public JSON convert(final Object obj) throws ConvertException {
		return (JSON) convert(null, obj);
	}

	@Override
	public Object convert(final Type targetType, final Object obj) throws ConvertException {
		if (null == obj) {
			return null;
		}
		final JSON json;
		if (obj instanceof JSON) {
			json = (JSON) obj;
		} else if (obj instanceof CharSequence) {
			final String jsonStr = StrUtil.trim((CharSequence) obj);
			json = JSONUtil.isTypeJSONArray(jsonStr) ? new JSONArray(jsonStr, config) : new JSONObject(jsonStr, config);
		} else if (obj instanceof MapWrapper) {
			// MapWrapper实现了Iterable会被当作JSONArray，此处做修正
			json = new JSONObject(obj, config);
		} else if (obj instanceof Iterable || obj instanceof Iterator || ArrayUtil.isArray(obj)) {// 列表
			json = new JSONArray(obj, config);
		} else {// 对象
			json = new JSONObject(obj, config);
		}

		return json;
	}
}
