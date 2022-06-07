package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.reflect.TypeUtil;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * {@link Reference}转换器
 *
 * @author Looly
 * @since 3.0.8
 */
@SuppressWarnings("rawtypes")
public class ReferenceConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	public static ReferenceConverter INSTANCE = new ReferenceConverter();

	@SuppressWarnings("unchecked")
	@Override
	protected Reference<?> convertInternal(final Class<?> targetClass, final Object value) {

		//尝试将值转换为Reference泛型的类型
		Object targetValue = null;
		final Type paramType = TypeUtil.getTypeArgument(targetClass);
		if(false == TypeUtil.isUnknown(paramType)){
			targetValue = ConverterRegistry.getInstance().convert(paramType, value);
		}
		if(null == targetValue){
			targetValue = value;
		}

		if(targetClass == WeakReference.class){
			return new WeakReference(targetValue);
		}else if(targetClass == SoftReference.class){
			return new SoftReference(targetValue);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Reference type: {}", targetClass.getName()));
	}

}
