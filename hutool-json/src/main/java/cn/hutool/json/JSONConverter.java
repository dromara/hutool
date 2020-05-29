package cn.hutool.json;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.convert.impl.ArrayConverter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONDeserializer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON转换器
 * 
 * @author looly
 * @since 4.2.2
 */
public class JSONConverter implements Converter<JSON> {

	static {
		// 注册到转换中心
		ConverterRegistry registry = ConverterRegistry.getInstance();
		registry.putCustom(JSON.class, JSONConverter.class);
		registry.putCustom(JSONObject.class, JSONConverter.class);
		registry.putCustom(JSONArray.class, JSONConverter.class);
	}

	/**
	 * JSONArray转数组
	 * 
	 * @param jsonArray JSONArray
	 * @param arrayClass 数组元素类型
	 * @return 数组对象
	 */
	protected static Object toArray(JSONArray jsonArray, Class<?> arrayClass) {
		return new ArrayConverter(arrayClass).convert(jsonArray, null);
	}

	/**
	 * 将JSONArray转换为指定类型的对量列表
	 * 
	 * @param <T> 元素类型
	 * @param jsonArray JSONArray
	 * @param elementType 对象元素类型
	 * @return 对象列表
	 */
	protected static <T> List<T> toList(JSONArray jsonArray, Class<T> elementType) {
		return Convert.toList(elementType, jsonArray);
	}

	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean
	 *
	 * @param <T> 转换后的对象类型
	 * @param targetType 目标类型
	 * @param value 值
	 * @param ignoreError 是否忽略转换错误
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T jsonConvert(Type targetType, Object value, boolean ignoreError) throws ConvertException {
		if (JSONUtil.isNull(value)) {
			return null;
		}
		
		if(value instanceof JSON) {
			final JSONDeserializer<?> deserializer = GlobalSerializeMapping.getDeserializer(targetType);
			if(null != deserializer) {
				return (T) deserializer.deserialize((JSON)value);
			}
		}

		final T targetValue = ignoreError ?
				Convert.convertQuietly(targetType, value):
				Convert.convert(targetType, value);

		if (null == targetValue && false == ignoreError) {
			if (StrUtil.isBlankIfStr(value)) {
				// 对于传入空字符串的情况，如果转换的目标对象是非字符串或非原始类型，转换器会返回false。
				// 此处特殊处理，认为返回null属于正常情况
				return null;
			}
			
			throw new ConvertException("Can not convert {} to type {}", value, ObjectUtil.defaultIfNull(TypeUtil.getClass(targetType), targetType));
		}

		return targetValue;
	}

	@Override
	public JSON convert(Object value, JSON defaultValue) throws IllegalArgumentException {
		return JSONUtil.parse(value);
	}

}
