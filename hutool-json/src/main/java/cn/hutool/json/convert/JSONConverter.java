package cn.hutool.json.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.RegisterConverter;
import cn.hutool.core.convert.impl.ArrayConverter;
import cn.hutool.core.convert.impl.CollectionConverter;
import cn.hutool.core.convert.impl.MapConverter;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.InternalJSONUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONDeserializer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

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
	/**
	 * 单例
	 */
	public static final JSONConverter INSTANCE = new JSONConverter(null);

	static {
		RegisterConverter.getInstance().putCustom(JSONObject.class, INSTANCE);
		RegisterConverter.getInstance().putCustom(JSONArray.class, INSTANCE);
	}

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

	@Override
	public Object convert(Type targetType, final Object obj) throws ConvertException {
		if (null == obj) {
			return null;
		}

		// 对象转JSON
		if (targetType instanceof JSON) {
			return toJSON(obj);
		}

		// JSON转对象
		if (obj instanceof JSON) {
			if (targetType instanceof TypeReference) {
				targetType = ((TypeReference<?>) targetType).getType();
			}
			return toBean(targetType, (JSON) obj);
		}

		// 无法转换
		throw new JSONException("Can not convert from {}: [{}] to [{}]",
				obj.getClass().getName(), obj, targetType.getTypeName());
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
	 * @throws JSONException 转换异常
	 */
	public JSON toJSON(final Object obj) throws JSONException {
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

	@SuppressWarnings("unchecked")
	private <T> T toBean(final Type targetType, final JSON json) {
		final Class<T> rawType = (Class<T>) TypeUtil.getClass(targetType);
		if(null != rawType && JSONDeserializer.class.isAssignableFrom(rawType)){
			return (T) JSONDeserializerConverter.INSTANCE.convert(targetType, json);
		}

		// 全局自定义反序列化（优先级低于实现JSONDeserializer接口）
		final JSONDeserializer<?> deserializer = GlobalSerializeMapping.getDeserializer(targetType);
		if (null != deserializer) {
			return (T) deserializer.deserialize(json);
		}

		// 其他转换不支持非Class的泛型类型
		if (null == rawType) {
			throw new JSONException("Can not get class from type: {}", targetType);
		}
		// 特殊类型转换，包括Collection、Map、强转、Array等
		final T result = toSpecial(targetType, rawType, json);
		if (null != result) {
			return result;
		}

		// 标准转换器
		final Converter converter = RegisterConverter.getInstance().getConverter(targetType, true);
		if (null != converter) {
			return (T) converter.convert(targetType, json);
		}

		// 尝试转Bean
		if (BeanUtil.isBean(rawType)) {
			return BeanCopier.of(json,
					ConstructorUtil.newInstanceIfPossible(rawType), targetType,
					InternalJSONUtil.toCopyOptions(json.getConfig())).copy();
		}

		// 跳过异常时返回null
		if(json.getConfig().isIgnoreError()){
			return null;
		}

		// 无法转换
		throw new JSONException("Can not convert from {}: [{}] to [{}]",
				json.getClass().getName(), json, targetType.getTypeName());
	}

	// ----------------------------------------------------------- Private method start

	/**
	 * 特殊类型转换<br>
	 * 包括：
	 *
	 * <pre>
	 * Collection
	 * Map
	 * 强转（无需转换）
	 * 数组
	 * </pre>
	 *
	 * @param <T>   转换的目标类型（转换器转换到的类型）
	 * @param type  类型
	 * @param value 值
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	private <T> T toSpecial(final Type type, final Class<T> rowType, final JSON value) {
		if (null == rowType) {
			return null;
		}

		// 集合转换（含有泛型参数，不可以默认强转）
		if (Collection.class.isAssignableFrom(rowType)) {
			return (T) CollectionConverter.INSTANCE.convert(type, value);
		}

		// Map类型（含有泛型参数，不可以默认强转）
		if (Map.class.isAssignableFrom(rowType)) {
			return (T) MapConverter.INSTANCE.convert(type, value);
		}

		// 默认强转
		if (rowType.isInstance(value)) {
			return (T) value;
		}

		// 数组转换
		if (rowType.isArray()) {
			return (T) ArrayConverter.INSTANCE.convert(type, value);
		}

		// 表示非需要特殊转换的对象
		return null;
	}
	// ----------------------------------------------------------- Private method end
}
