package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;

/**
 * 表示一个被指定了强制别名的注解属性。
 * 当调用{@link #getValue()}时，总是返回{@link #aliasAttribute}的值
 *
 * @author huangchengxing
 * @see AliasAttributePostProcessor
 * @see AliasForLinkAttributePostProcessor
 * @see RelationType#FORCE_ALIAS_BY
 * @see RelationType#FORCE_ALIAS_FOR
 */
public class ForceAliasedAnnotationAttribute extends AnnotationAttributeWrapper implements AnnotationAttribute {

	private final AnnotationAttribute aliasAttribute;

	protected ForceAliasedAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute aliasAttribute) {
		super(origin);
		Assert.notNull(aliasAttribute, "aliasAttribute must not null");
		this.aliasAttribute = aliasAttribute;
	}

	@Override
	public Object getValue() {
		return aliasAttribute.getValue();
	}

	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return aliasAttribute.isValueEquivalentToDefaultValue();
	}

	@Override
	public Class<?> getAttributeType() {
		return aliasAttribute.getAttributeType();
	}

}
