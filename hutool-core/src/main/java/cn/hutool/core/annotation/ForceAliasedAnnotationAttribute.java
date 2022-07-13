package cn.hutool.core.annotation;

/**
 * 表示一个被指定了强制别名的注解属性。
 * 当调用{@link #getValue()}时，总是返回{@link #linked}的值
 *
 * @author huangchengxing
 * @see AliasAnnotationPostProcessor
 * @see AliasLinkAnnotationPostProcessor
 * @see RelationType#ALIAS_FOR
 * @see RelationType#FORCE_ALIAS_FOR
 */
public class ForceAliasedAnnotationAttribute extends AbstractWrappedAnnotationAttribute {

	protected ForceAliasedAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute linked) {
		super(origin, linked);
	}

	/**
	 * 总是返回{@link #linked}的{@link AnnotationAttribute#getValue()}的返回值
	 *
	 * @return {@link #linked}的{@link AnnotationAttribute#getValue()}的返回值
	 */
	@Override
	public Object getValue() {
		return linked.getValue();
	}

	/**
	 * 总是返回{@link #linked}的{@link AnnotationAttribute#isValueEquivalentToDefaultValue()}的返回值
	 *
	 * @return {@link #linked}的{@link AnnotationAttribute#isValueEquivalentToDefaultValue()}的返回值
	 */
	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return linked.isValueEquivalentToDefaultValue();
	}

	/**
	 * 总是返回{@link #linked}的{@link AnnotationAttribute#getAttributeType()}的返回值
	 *
	 * @return {@link #linked}的{@link AnnotationAttribute#getAttributeType()}的返回值
	 */
	@Override
	public Class<?> getAttributeType() {
		return linked.getAttributeType();
	}

}
