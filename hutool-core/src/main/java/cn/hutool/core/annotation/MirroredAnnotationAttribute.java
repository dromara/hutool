package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;

/**
 * 表示存在对应镜像属性的注解属性，当获取值时将根据{@link RelationType#MIRROR_FOR}的规则进行处理
 *
 * @author huangchengxing
 * @see MirrorLinkAttributePostProcessor
 * @see RelationType#MIRROR_FOR
 */
public class MirroredAnnotationAttribute extends AnnotationAttributeWrapper implements AnnotationAttribute {

	private final AnnotationAttribute mirrorAttribute;

	public MirroredAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute mirrorAttribute) {
		super(origin);
		this.mirrorAttribute = mirrorAttribute;
	}

	@Override
	public Object getValue() {
		boolean originIsDefault = origin.isValueEquivalentToDefaultValue();
		boolean targetIsDefault = mirrorAttribute.isValueEquivalentToDefaultValue();
		Object originValue = origin.getValue();
		Object targetValue = mirrorAttribute.getValue();

		// 都为默认值，或都为非默认值时，两方法的返回值必须相等
		if (originIsDefault == targetIsDefault) {
			Assert.equals(
				originValue, targetValue,
				"the values of attributes [{}] and [{}] that mirror each other are different: [{}] <==> [{}]",
				origin.getAttribute(), mirrorAttribute.getAttribute(), originValue, targetValue
			);
			return originValue;
		}

		// 两者有一者不为默认值时，优先返回非默认值
		return originIsDefault ? targetValue : originValue;
	}
}
