package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.impl.BeanConverter;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONDeserializer;

import java.lang.reflect.Type;

/**
 * 针对JSON的Bean转换封装。<br>
 * 此类时针对5.x中设计缺陷设计的类，在ConverterRegistry中通过反射调用
 *
 * @param <T> Bean类型
 * @since 5.8.6
 */
public class BeanConverterForJSON<T> extends BeanConverter<T> {

	public BeanConverterForJSON(Type beanType) {
		super(beanType);
	}

	@Override
	protected T convertInternal(final Object value) {
		final Class<T> targetType = getTargetType();
		if (value instanceof JSON) {
			final JSONDeserializer<?> deserializer = GlobalSerializeMapping.getDeserializer(targetType);
			if (null != deserializer) {
				//noinspection unchecked
				return (T) deserializer.deserialize((JSON) value);
			}

			// issue#2212@Github
			// 在JSONObject转Bean时，读取JSONObject本身的配置文件
			if (value instanceof JSONGetter && BeanUtil.hasSetter(targetType)) {
				final JSONConfig config = ((JSONGetter<?>) value).getConfig();
				this.copyOptions.setIgnoreError(config.isIgnoreError());
			}
		}

		return super.convertInternal(value);
	}
}
