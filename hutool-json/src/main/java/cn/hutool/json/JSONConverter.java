package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.convert.impl.BeanConverter;
import cn.hutool.core.convert.impl.DateConverter;
import cn.hutool.core.convert.impl.TemporalAccessorConverter;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONDeserializer;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * JSON转换器
 *
 * @author looly
 * @since 4.2.2
 */
public class JSONConverter implements Converter {

	public static JSONConverter INSTANCE = new JSONConverter();

	private static final ConverterRegistry registry;
	static {
		// 注册到转换中心
		registry = new ConverterRegistry();
		registry.putCustom(JSON.class, INSTANCE);
		registry.putCustom(JSONObject.class, INSTANCE);
		registry.putCustom(JSONArray.class, INSTANCE);
	}

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		return JSONUtil.parse(value);
	}

	/**
	 * 转换值为指定类型，可选是否不抛异常转换<br>
	 * 当转换失败时返回默认值
	 *
	 * @param <T> 目标类型
	 * @param type 目标类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @param quietly 是否静默转换，true不抛异常
	 * @return 转换后的值
	 * @since 5.3.2
	 */
	public static <T> T convertWithCheck(final Type type, final Object value, final T defaultValue, final boolean quietly) {
		try {
			return registry.convert(type, value, defaultValue);
		} catch (final Exception e) {
			if(quietly){
				return defaultValue;
			}
			throw e;
		}
	}

	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean<br>
	 * 如果遇到{@link JSONDeserializer}，则调用其{@link JSONDeserializer#deserialize(JSON)}方法转换。
	 *
	 * @param <T>        转换后的对象类型
	 * @param targetType 目标类型
	 * @param value      值
	 * @param config     JSON配置项
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T jsonConvert(final Type targetType, final Object value, final JSONConfig config) throws ConvertException {
		if (null == value) {
			return null;
		}

		// since 5.7.8，增加自定义Bean反序列化接口和特殊对象的自定义转换
		if (targetType instanceof Class) {
			final Class<?> targetClass = (Class<?>) targetType;
			if (targetClass.isInstance(value)) {
				return (T) value;
			} else if (JSONDeserializer.class.isAssignableFrom(targetClass)) {
				// 自定义反序列化
				if (value instanceof JSON) {
					final JSONDeserializer<T> target = (JSONDeserializer<T>) ConstructorUtil.newInstanceIfPossible(targetClass);
					if (null == target) {
						throw new ConvertException("Can not instance target: [{}]", targetType);
					}
					return target.deserialize((JSON) value);
				}
			} else if (targetClass == byte[].class && value instanceof CharSequence) {
				// bytes二进制反序列化，默认按照Base64对待
				// issue#I59LW4
				return (T) Base64.decode((CharSequence) value);
			} else if (targetClass.isAssignableFrom(Date.class) || targetClass.isAssignableFrom(TemporalAccessor.class)) {
				// 用户指定了日期格式，获取日期属性时使用对应格式
				final String valueStr = convertWithCheck(String.class, value, null, true);
				if (null == valueStr) {
					return null;
				}

				// 日期转换，支持自定义日期格式
				final String format = getDateFormat(config);
				if (null != format) {
					if (targetClass.isAssignableFrom(Date.class)) {
						return (T) new DateConverter(format).convert(targetClass, valueStr);
					} else {
						return (T) new TemporalAccessorConverter(format).convert(targetClass, valueStr);
					}
				}
			}
		}

		return jsonToBean(targetType, value, null != config && config.isIgnoreError());
	}

	/**
	 * JSON递归转换为Bean<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean
	 *
	 * @param <T>         转换后的对象类型
	 * @param targetType  目标类型
	 * @param value       值，JSON格式
	 * @param ignoreError 是否忽略转换错误
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 * @since 5.7.10
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T jsonToBean(final Type targetType, final Object value, final boolean ignoreError) throws ConvertException {
		if (null == value) {
			return null;
		}

		if (value instanceof JSON) {
			final JSON valueJson = (JSON) value;
			// 全局自定义反序列化
			final JSONDeserializer<?> deserializer = GlobalSerializeMapping.getDeserializer(targetType);
			if (null != deserializer) {
				return (T) deserializer.deserialize(valueJson);
			}

			// issue#2212@Github
			// 在JSONObject转Bean时，读取JSONObject本身的配置文件
			if (targetType instanceof Class && BeanUtil.hasSetter((Class<?>) targetType)) {
				final JSONConfig config = valueJson.getConfig();
				final Converter converter = new BeanConverter(InternalJSONUtil.toCopyOptions(config).setIgnoreError(ignoreError));
				return ignoreError ? converter.convert(targetType, value, null)
						: (T) converter.convert(targetType, value);
			}
		}

		final T targetValue = convertWithCheck(targetType, value, null, ignoreError);

		if (null == targetValue && false == ignoreError) {
			if (StrUtil.isBlankIfStr(value)) {
				// 对于传入空字符串的情况，如果转换的目标对象是非字符串或非原始类型，转换器会返回false。
				// 此处特殊处理，认为返回null属于正常情况
				return null;
			}

			throw new ConvertException("Can not convert {} to type {}", value, ObjUtil.defaultIfNull(TypeUtil.getClass(targetType), targetType));
		}

		return targetValue;
	}

	/**
	 * 获取配置文件中的日期格式，无格式返回{@code null}
	 *
	 * @param config JSON配置
	 * @return 日期格式，无返回{@code null}
	 */
	private static String getDateFormat(final JSONConfig config) {
		if (null != config) {
			final String format = config.getDateFormat();
			if (StrUtil.isNotBlank(format)) {
				return format;
			}
		}
		return null;
	}
}
