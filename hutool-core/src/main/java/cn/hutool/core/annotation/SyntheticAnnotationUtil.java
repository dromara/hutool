package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.Comparator;

/**
 * {@link SynthesizedAnnotationAggregator}相关工具，用于内部使用
 *
 * @author huangchengxing
 */
class SyntheticAnnotationUtil {

	/**
	 * 从注解属性上获取指定类型的{@link Link}注解
	 *
	 * @param attribute     注解属性
	 * @param relationTypes 类型
	 * @return 注解
	 */
	static Link getLink(AnnotationAttribute attribute, RelationType... relationTypes) {
		return Opt.ofNullable(attribute)
			.map(t -> AnnotationUtil.getSynthesizedAnnotation(attribute.getAttribute(), Link.class))
			.filter(a -> ArrayUtil.contains(relationTypes, a.type()))
			.get();
	}

	/**
	 * 从合成注解中获取{@link Link#type()}指定的注解对象
	 *
	 * @param annotation {@link Link}注解
	 * @param synthesizedAnnotationAggregator 合成注解
	 */
	static SynthesizedAnnotation getLinkedAnnotation(
		Link annotation, SynthesizedAnnotationAggregator synthesizedAnnotationAggregator, Class<? extends Annotation> defaultType) {
		final Class<?> targetAnnotationType = getLinkedAnnotationType(annotation, defaultType);
		return synthesizedAnnotationAggregator.getSynthesizedAnnotation(targetAnnotationType);
	}

	/**
	 * 若{@link Link#annotation()}获取的类型{@link Annotation#getClass()}，则返回{@code defaultType}，
	 * 否则返回{@link Link#annotation()}指定的类型
	 *
	 * @param annotation  {@link Link}注解
	 * @param defaultType 默认注解类型
	 * @return 注解类型
	 */
	static Class<?> getLinkedAnnotationType(Link annotation, Class<?> defaultType) {
		return ObjectUtil.equals(annotation.annotation(), Annotation.class) ?
			defaultType : annotation.annotation();
	}

	/**
	 * 校验两个注解属性的返回值类型是否一致
	 *
	 * @param original 原属性
	 * @param alias    别名属性
	 */
	static void checkAttributeType(AnnotationAttribute original, AnnotationAttribute alias) {
		Assert.equals(
			original.getAttributeType(), alias.getAttributeType(),
			"return type of the linked attribute [{}] is inconsistent with the original [{}]",
			original.getAttribute(), alias.getAttribute()
		);
	}

	/**
	 * 检查{@link Link}指向的注解属性是否就是本身
	 *
	 * @param original        {@link Link}注解的属性
	 * @param linked          {@link Link}指向的注解属性
	 */
	static void checkLinkedSelf(AnnotationAttribute original, AnnotationAttribute linked) {
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
	static void checkLinkedAttributeNotNull(AnnotationAttribute original, AnnotationAttribute linkedAttribute, Link annotation) {
		Assert.notNull(linkedAttribute, "cannot find linked attribute [{}] of original [{}] in [{}]",
			original.getAttribute(), annotation.attribute(),
			getLinkedAnnotationType(annotation, original.getAnnotationType())
		);
	}

	/**
	 * 获取按{@link SynthesizedAnnotation#getVerticalDistance()}和{@link SynthesizedAnnotation#getHorizontalDistance()}排序的比较器
	 *
	 * @return 比较值
	 */
	static Comparator<SynthesizedAnnotation> getChildPriorityAnnotationCompare() {
		return Comparator.comparing(SynthesizedAnnotation::getVerticalDistance)
			.thenComparing(SynthesizedAnnotation::getHorizontalDistance);
	}

}
