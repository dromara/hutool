package cn.hutool.core.convert;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 抽象转换器，提供通用的转换逻辑，同时通过convertInternal实现对应类型的专属逻辑<br>
 * 转换器不会抛出转换异常，转换失败时会返回{@code null}
 *
 * @author Looly
 */
public abstract class AbstractConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object convert(final Type targetType, final Object value) {
		if (null == value) {
			return null;
		}
		if (TypeUtil.isUnknown(targetType)) {
			throw new ConvertException("Unsupported convert to unKnow type: {}", targetType);
		}

		final Class<?> targetClass = TypeUtil.getClass(targetType);
		Assert.notNull(targetClass, "Target type is not a class!");

		// 尝试强转
		if (targetClass.isInstance(value) && false == Map.class.isAssignableFrom(targetClass)) {
			// 除Map外，已经是目标类型，不需要转换（Map类型涉及参数类型，需要单独转换）
			return CastUtil.castTo(targetClass, value);
		}
		return convertInternal(targetClass, value);
	}

	/**
	 * 内部转换器，被 {@link AbstractConverter#convert(Type, Object)} 调用，实现基本转换逻辑<br>
	 * 内部转换器转换后如果转换失败可以做如下操作，处理结果都为返回默认值：
	 *
	 * <pre>
	 * 1、返回{@code null}
	 * 2、抛出一个{@link RuntimeException}异常
	 * </pre>
	 *
	 * @param targetClass 目标类型
	 * @param value 值
	 * @return 转换后的类型
	 */
	protected abstract Object convertInternal(Class<?> targetClass, Object value);

	/**
	 * 值转为String，用于内部转换中需要使用String中转的情况<br>
	 * 转换规则为：
	 *
	 * <pre>
	 * 1、字符串类型将被强转
	 * 2、数组将被转换为逗号分隔的字符串
	 * 3、其它类型将调用默认的toString()方法
	 * </pre>
	 *
	 * @param value 值
	 * @return String
	 */
	protected String convertToStr(final Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof CharSequence) {
			return value.toString();
		} else if (ArrayUtil.isArray(value)) {
			return ArrayUtil.toString(value);
		} else if (CharUtil.isChar(value)) {
			//对于ASCII字符使用缓存加速转换，减少空间创建
			return CharUtil.toString((char) value);
		}
		return value.toString();
	}
}
