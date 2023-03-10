package cn.hutool.json.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.RegisterConverter;
import cn.hutool.core.convert.impl.ArrayConverter;
import cn.hutool.core.convert.impl.CollectionConverter;
import cn.hutool.core.convert.impl.DateConverter;
import cn.hutool.core.convert.impl.MapConverter;
import cn.hutool.core.convert.impl.TemporalAccessorConverter;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.math.NumberUtil;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.*;
import cn.hutool.json.serialize.JSONDeserializer;
import cn.hutool.json.serialize.JSONStringer;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON转换器，实现Object对象转换为{@link JSON}，支持的对象：
 * <ul>
 *     <li>任意支持的对象，转换为JSON</li>
 *     <li>JSOn转换为指定对象Bean</li>
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
	public Object convert(Type targetType, Object value) throws ConvertException {
		if (null == value) {
			return null;
		}
		if (value instanceof JSONStringer) {
			// 被JSONString包装的对象，获取其原始类型
			value = ((JSONStringer) value).getRaw();
		}

		// JSON转对象
		if (value instanceof JSON) {
			if (targetType instanceof TypeReference) {
				// 还原原始类型
				targetType = ((TypeReference<?>) targetType).getType();
			}
			return toBean(targetType, (JSON) value);
		}

		// 对象转JSON
		final Class<?> targetClass = TypeUtil.getClass(targetType);
		if(null != targetClass){
			if (JSON.class.isAssignableFrom(targetClass)) {
				return toJSON(value);
			}
			// 自定义日期格式
			if(Date.class.isAssignableFrom(targetClass) || TemporalAccessor.class.isAssignableFrom(targetClass)){
				final Object date = toDateWithFormat(targetClass, value);
				if(null != date){
					return date;
				}
			}
		}

		return Convert.convertWithCheck(targetType, value, null, config.isIgnoreError());
	}

	/**
	 * 实现Object对象转换为{@link JSON}，支持的对象：
	 * <ul>
	 *     <li>String: 转换为相应的对象，</li>
	 *     <li>Array、Iterable、Iterator：转换为JSONArray</li>
	 *     <li>Bean对象：转为JSONObject</li>
	 *     <li>Number：返回原对象</li>
	 * </ul>
	 *
	 * @param obj 被转换的对象
	 * @return 转换后的对象
	 * @throws JSONException 转换异常
	 */
	public Object toJSON(final Object obj) throws JSONException {
		final JSON json;
		if (obj instanceof JSON || obj instanceof Number) {
			return obj;
		} else if (obj instanceof CharSequence) {
			final String jsonStr = StrUtil.trim((CharSequence) obj);
			switch (jsonStr.charAt(0)){
				case '"':
				case '\'':
					// JSON字符串值
					return jsonStr;
				case '[':
					return new JSONArray(jsonStr, config);
				case '{':
					return new JSONObject(jsonStr, config);
				default:
					if(NumberUtil.isNumber(jsonStr)){
						return NumberUtil.toBigDecimal(jsonStr);
					}
					throw new JSONException("Unsupported String to JSON: {}", jsonStr);
			}
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

	// ----------------------------------------------------------- Private method start

	/**
	 * JSON转Bean
	 *
	 * @param <T>        目标类型
	 * @param targetType 目标类型，
	 * @param json       JSON
	 * @return bean
	 */
	@SuppressWarnings("unchecked")
	private <T> T toBean(final Type targetType, final JSON json) {

		// 自定义对象反序列化
		final JSONDeserializer<Object> deserializer = InternalJSONUtil.getDeserializer(targetType);
		if (null != deserializer) {
			return (T) deserializer.deserialize(json);
		}

		final Class<T> rawType = (Class<T>) TypeUtil.getClass(targetType);
		if (null == rawType) {
			// 当目标类型不确定时，返回原JSON
			return (T) json;
			//throw new JSONException("Can not get class from type: {}", targetType);
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
		if (json.getConfig().isIgnoreError()) {
			return null;
		}

		// 无法转换
		throw new JSONException("Can not convert from {}: [{}] to [{}]",
				json.getClass().getName(), json, targetType.getTypeName());
	}

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

	private Object toDateWithFormat(final Class<?> targetClass, final Object value){
		// 日期转换，支持自定义日期格式
		final String format = config.getDateFormat();
		if (StrUtil.isNotBlank(format)) {
			if (Date.class.isAssignableFrom(targetClass)) {
				return new DateConverter(format).convert(targetClass, value);
			} else {
				return new TemporalAccessorConverter(format).convert(targetClass, value);
			}
		}
		return null;
	}
	// ----------------------------------------------------------- Private method end
}
