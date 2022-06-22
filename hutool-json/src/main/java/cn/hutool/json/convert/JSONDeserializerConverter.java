package cn.hutool.json.convert;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.serialize.JSONDeserializer;

/**
 * 实现了{@link JSONDeserializer}接口的Bean对象转换器，用于将指定JSON转换为JSONDeserializer子对象。
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONDeserializerConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected Object convertInternal(Class<?> targetClass, Object value) {
		// 自定义反序列化
		if (value instanceof JSON) {
			final JSONDeserializer<?> target = (JSONDeserializer<?>) ConstructorUtil.newInstanceIfPossible(targetClass);
			if (null == target) {
				throw new ConvertException("Can not instance target: [{}]", targetClass);
			}
			return target.deserialize((JSON) value);
		}

		throw new ConvertException("JSONDeserializer bean must be convert from JSON!");
	}
}
