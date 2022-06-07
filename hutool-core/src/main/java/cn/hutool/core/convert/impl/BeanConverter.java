package cn.hutool.core.convert.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapProxy;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean转换器，支持：
 * <pre>
 * Map =》 Bean
 * Bean =》 Bean
 * ValueProvider =》 Bean
 * </pre>
 *
 * @author Looly
 * @since 4.0.2
 */
public class BeanConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	public static BeanConverter INSTANCE = new BeanConverter();

	private final CopyOptions copyOptions;

	/**
	 * 构造
	 */
	public BeanConverter() {
		this(CopyOptions.create().setIgnoreError(true));
	}

	/**
	 * 构造
	 *
	 * @param copyOptions Bean转换选项参数
	 */
	public BeanConverter(final CopyOptions copyOptions) {
		this.copyOptions = copyOptions;
	}

	@Override
	public Object convert(Type targetType, Object value) throws ConvertException {
		Assert.notNull(targetType);
		if (null == value) {
			return null;
		}

		Class<?> targetClass = TypeUtil.getClass(targetType);
		Assert.notNull(targetClass, "Target type is not a class!");

		return convertInternal(targetType, targetClass, value);
	}

	private Object convertInternal(final Type targetType, final Class<?> targetClass, final Object value) {
		if (value instanceof Map ||
				value instanceof ValueProvider ||
				BeanUtil.isBean(value.getClass())) {
			if (value instanceof Map && targetClass.isInterface()) {
				// 将Map动态代理为Bean
				return MapProxy.create((Map<?, ?>) value).toProxyBean(targetClass);
			}

			//限定被转换对象类型
			return BeanCopier.create(value, ConstructorUtil.newInstanceIfPossible(targetClass), targetType, this.copyOptions).copy();
		} else if (value instanceof byte[]) {
			// 尝试反序列化
			return ObjUtil.deserialize((byte[]) value);
		}

		throw new ConvertException("Unsupported source type: {}", value.getClass());
	}
}
