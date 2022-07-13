package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;

/**
 * <p>表示一个具有别名的属性。
 * 当别名属性值为默认值时，优先返回原属性的值，当别名属性不为默认值时，优先返回别名属性的值
 *
 * @author huangchengxing
 * @see AliasForLinkAttributePostProcessor
 * @see RelationType#ALIAS_BY
 * @see RelationType#ALIAS_FOR
 */
public class AliasedAnnotationAttribute extends AnnotationAttributeWrapper implements AnnotationAttribute {

	private final AnnotationAttribute aliasAttribute;

	protected AliasedAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute aliasAttribute) {
		super(origin);
		Assert.notNull(aliasAttribute, "aliasAttribute must not null");
		this.aliasAttribute = aliasAttribute;
	}

	@Override
	public Object getValue() {
		return aliasAttribute.isValueEquivalentToDefaultValue() ? super.getValue() : aliasAttribute.getValue();
	}

}
