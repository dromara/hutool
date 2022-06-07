package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.convert.impl.BeanConverter;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONDeserializer;

import java.lang.reflect.Type;

/**
 * JSON转换器
 *
 * @author looly
 * @since 4.2.2
 */
public class JSONConverter implements Converter {

	public static JSONConverter INSTANCE = new JSONConverter();

	static {
		// 注册到转换中心
		final ConverterRegistry registry = ConverterRegistry.getInstance();
		registry.putCustom(JSON.class, INSTANCE);
		registry.putCustom(JSONObject.class, INSTANCE);
		registry.putCustom(JSONArray.class, INSTANCE);
	}

	@Override
	public Object convert(Type targetType, Object value) throws ConvertException {
		return JSONUtil.parse(value);
	}

	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean<br>
	 * 如果遇到{@link JSONBeanParser}，则调用其{@link JSONBeanParser#parse(Object)}方法转换。
	 *
	 * @param <T> 转换后的对象类型
	 * @param targetType 目标类型
	 * @param value 值
	 * @param ignoreError 是否忽略转换错误
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T jsonConvert(final Type targetType, final Object value, final boolean ignoreError) throws ConvertException {
		if (JSONUtil.isNull(value)) {
			return null;
		}

		// since 5.7.8，增加自定义Bean反序列化接口
		if(targetType instanceof Class){
			final Class<?> clazz = (Class<?>) targetType;
			if (JSONBeanParser.class.isAssignableFrom(clazz)){
				@SuppressWarnings("rawtypes")
				final JSONBeanParser target = (JSONBeanParser) ConstructorUtil.newInstanceIfPossible(clazz);
				if(null == target){
					throw new ConvertException("Can not instance [{}]", targetType);
				}
				target.parse(value);
				return (T) target;
			} else if(targetType == byte[].class && value instanceof CharSequence){
				// issue#I59LW4
				return (T) Base64.decode((CharSequence) value);
			}
		}

		return jsonToBean(targetType, value, ignoreError);
	}

	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean
	 *
	 * @param <T> 转换后的对象类型
	 * @param targetType 目标类型
	 * @param value 值，JSON格式
	 * @param ignoreError 是否忽略转换错误
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 * @since 5.7.10
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T jsonToBean(final Type targetType, final Object value, final boolean ignoreError) throws ConvertException {
		if (JSONUtil.isNull(value)) {
			return null;
		}

		if(value instanceof JSON){
			final JSONDeserializer<?> deserializer = GlobalSerializeMapping.getDeserializer(targetType);
			if(null != deserializer) {
				return (T) deserializer.deserialize((JSON) value);
			}

			// issue#2212@Github
			// 在JSONObject转Bean时，读取JSONObject本身的配置文件
			if(value instanceof JSONGetter
					&& targetType instanceof Class && BeanUtil.hasSetter((Class<?>) targetType)){
				final JSONConfig config = ((JSONGetter<?>) value).getConfig();
				final Converter converter = new BeanConverter(InternalJSONUtil.toCopyOptions(config).setIgnoreError(ignoreError));
				return ignoreError ? converter.convert(targetType, value, null)
						: (T) converter.convert(targetType, value);
			}
		}

		final T targetValue = Convert.convertWithCheck(targetType, value, null, ignoreError);

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
}
