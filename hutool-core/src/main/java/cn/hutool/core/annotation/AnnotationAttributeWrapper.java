package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>表示一个被包装过的{@link AnnotationAttribute}，
 * 该实例中的一些方法可能会被代理到其他的注解属性对象中，
 * 从而使得通过原始的注解属性的方法获取到另一注解属性的值。
 *
 * @author huangchengxing
 * @see AnnotationAttribute
 * @see ForceAliasedAnnotationAttribute
 * @see AliasedAnnotationAttribute
 * @see MirroredAnnotationAttribute
 */
public abstract class AnnotationAttributeWrapper implements AnnotationAttribute {

	protected final AnnotationAttribute origin;

	protected AnnotationAttributeWrapper(AnnotationAttribute origin) {
		Assert.notNull(origin, "target must not null");
		this.origin = origin;
	}

	@Override
	public Annotation getAnnotation() {
		return origin.getAnnotation();
	}

	@Override
	public Method getAttribute() {
		return origin.getAttribute();
	}

	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return origin.isValueEquivalentToDefaultValue();
	}

	@Override
	public Class<?> getAttributeType() {
		return origin.getAttributeType();
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return origin.getAnnotation(annotationType);
	}

	@Override
	public boolean isWrapped() {
		return true;
	}
}
