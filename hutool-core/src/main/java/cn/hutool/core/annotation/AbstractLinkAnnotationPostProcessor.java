package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link SynthesizedAnnotationPostProcessor}的基本实现，
 * 用于处理注解中带有{@link Link}注解的属性。
 *
 * @author huangchengxing
 * @see MirrorLinkAnnotationPostProcessor
 * @see AliasLinkAnnotationPostProcessor
 */
public abstract class AbstractLinkAnnotationPostProcessor implements SynthesizedAnnotationPostProcessor {

	/**
	 * 若一个注解属性上存在{@link Link}注解，注解的{@link Link#type()}返回值在{@link #processTypes()}中存在，
	 * 且此{@link Link}指定的注解对象在当前的{@link SynthesizedAggregateAnnotation}中存在，
	 * 则从聚合器中获取类型对应的合成注解对象，与该对象中的指定属性，然后将全部关联数据交给
	 * {@link #processLinkedAttribute}处理。
	 *
	 * @param synthesizedAnnotation 合成的注解
	 * @param synthesizer           合成注解聚合器
	 */
	@Override
	public void process(SynthesizedAnnotation synthesizedAnnotation, AnnotationSynthesizer synthesizer) {
		final Map<String, AnnotationAttribute> attributeMap = new HashMap<>(synthesizedAnnotation.getAttributes());
		attributeMap.forEach((originalAttributeName, originalAttribute) -> {
			// 获取注解
			final Link link = getLinkAnnotation(originalAttribute, processTypes());
			if (ObjectUtil.isNull(link)) {
				return;
			}
			// 获取注解属性
			final SynthesizedAnnotation linkedAnnotation = getLinkedAnnotation(link, synthesizer, synthesizedAnnotation.annotationType());
			if (ObjectUtil.isNull(linkedAnnotation)) {
				return;
			}
			final AnnotationAttribute linkedAttribute = linkedAnnotation.getAttributes().get(link.attribute());
			// 处理
			processLinkedAttribute(
					synthesizer, link,
					synthesizedAnnotation, synthesizedAnnotation.getAttributes().get(originalAttributeName),
					linkedAnnotation, linkedAttribute
			);
		});
	}

	// =========================== 抽象方法 ===========================

	/**
	 * 当属性上存在{@link Link}注解时，仅当{@link Link#type()}在本方法返回值内存在时才进行处理
	 *
	 * @return 支持处理的{@link RelationType}类型
	 */
	protected abstract RelationType[] processTypes();

	/**
	 * 对关联的合成注解对象及其关联属性的处理
	 *
	 * @param synthesizer        注解合成器
	 * @param annotation         {@code originalAttribute}上的{@link Link}注解对象
	 * @param originalAnnotation 当前正在处理的{@link SynthesizedAnnotation}对象
	 * @param originalAttribute  {@code originalAnnotation}上的待处理的属性
	 * @param linkedAnnotation   {@link Link}指向的关联注解对象
	 * @param linkedAttribute    {@link Link}指向的{@code originalAnnotation}中的关联属性，该参数可能为空
	 */
	protected abstract void processLinkedAttribute(
			AnnotationSynthesizer synthesizer, Link annotation,
			SynthesizedAnnotation originalAnnotation, AnnotationAttribute originalAttribute,
			SynthesizedAnnotation linkedAnnotation, AnnotationAttribute linkedAttribute
	);

	// =========================== @Link注解的处理 ===========================

	/**
	 * 从注解属性上获取指定类型的{@link Link}注解
	 *
	 * @param attribute     注解属性
	 * @param relationTypes 类型
	 * @return 注解
	 */
	protected Link getLinkAnnotation(AnnotationAttribute attribute, RelationType... relationTypes) {
		return Opt.ofNullable(attribute)
				.map(t -> AnnotationUtil.getSynthesizedAnnotation(attribute.getAttribute(), Link.class))
				.filter(a -> ArrayUtil.contains(relationTypes, a.type()))
				.get();
	}

	/**
	 * 从合成注解中获取{@link Link#type()}指定的注解对象
	 *
	 * @param annotation  {@link Link}注解
	 * @param synthesizer 注解合成器
	 * @param defaultType 默认类型
	 * @return {@link SynthesizedAnnotation}
	 */
	protected SynthesizedAnnotation getLinkedAnnotation(Link annotation, AnnotationSynthesizer synthesizer, Class<? extends Annotation> defaultType) {
		final Class<?> targetAnnotationType = getLinkedAnnotationType(annotation, defaultType);
		return synthesizer.getSynthesizedAnnotation(targetAnnotationType);
	}

	/**
	 * 若{@link Link#annotation()}获取的类型{@code Annotation#getClass()}，则返回{@code defaultType}，
	 * 否则返回{@link Link#annotation()}指定的类型
	 *
	 * @param annotation  {@link Link}注解
	 * @param defaultType 默认注解类型
	 * @return 注解类型
	 */
	protected Class<?> getLinkedAnnotationType(Link annotation, Class<?> defaultType) {
		return ObjectUtil.equals(annotation.annotation(), Annotation.class) ?
				defaultType : annotation.annotation();
	}

	// =========================== 注解属性的校验 ===========================

	/**
	 * 校验两个注解属性的返回值类型是否一致
	 *
	 * @param original 原属性
	 * @param alias    别名属性
	 */
	protected void checkAttributeType(AnnotationAttribute original, AnnotationAttribute alias) {
		Assert.equals(
				original.getAttributeType(), alias.getAttributeType(),
				"return type of the linked attribute [{}] is inconsistent with the original [{}]",
				original.getAttribute(), alias.getAttribute()
		);
	}

	/**
	 * 检查{@link Link}指向的注解属性是否就是本身
	 *
	 * @param original {@link Link}注解的属性
	 * @param linked   {@link Link}指向的注解属性
	 */
	protected void checkLinkedSelf(AnnotationAttribute original, AnnotationAttribute linked) {
		boolean linkSelf = (original == linked) || ObjectUtil.equals(original.getAttribute(), linked.getAttribute());
		Assert.isFalse(linkSelf, "cannot link self [{}]", original.getAttribute());
	}

	/**
	 * 检查{@link Link}指向的注解属性是否存在
	 *
	 * @param original        {@link Link}注解的属性
	 * @param linkedAttribute {@link Link}指向的注解属性
	 * @param annotation      {@link Link}注解
	 */
	protected void checkLinkedAttributeNotNull(AnnotationAttribute original, AnnotationAttribute linkedAttribute, Link annotation) {
		Assert.notNull(linkedAttribute, "cannot find linked attribute [{}] of original [{}] in [{}]",
				original.getAttribute(), annotation.attribute(),
				getLinkedAnnotationType(annotation, original.getAnnotationType())
		);
	}

}
