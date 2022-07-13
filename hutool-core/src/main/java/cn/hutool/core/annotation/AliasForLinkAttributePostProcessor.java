package cn.hutool.core.annotation;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理注解中带有{@link Link}注解，且{@link Link#type()}为{@link RelationType#FORCE_ALIAS_FOR}
 * 或{@link RelationType#ALIAS_FOR}的属性。
 *
 * @author huangchengxing
 * @see ForceAliasedAnnotationAttribute
 * @see AliasedAnnotationAttribute
 */
public class AliasForLinkAttributePostProcessor implements SynthesizedAnnotationPostProcessor {

	@Override
	public int order() {
		return Integer.MIN_VALUE + 2;
	}

	@Override
	public void process(SynthesizedAnnotation annotation, SyntheticAnnotation syntheticAnnotation) {
		Map<String, AnnotationAttribute> attributeMap = new HashMap<>(annotation.getAttributes());
		attributeMap.forEach((originalAttributeName, originalAttribute) -> {
			// 获取注解
			final Link link = SyntheticAnnotationUtil.getLink(
				originalAttribute, RelationType.ALIAS_FOR, RelationType.FORCE_ALIAS_FOR
			);
			if (ObjectUtil.isNull(link)) {
				return;
			}

			// 获取注解属性
			SynthesizedAnnotation aliasAnnotation = SyntheticAnnotationUtil.getLinkedAnnotation(link, syntheticAnnotation, annotation.annotationType());
			if (ObjectUtil.isNull(aliasAnnotation)) {
				return;
			}
			final AnnotationAttribute aliasAttribute = aliasAnnotation.getAttributes().get(link.attribute());
			SyntheticAnnotationUtil.checkLinkedAttributeNotNull(originalAttribute, aliasAttribute, link);
			SyntheticAnnotationUtil.checkAttributeType(originalAttribute, aliasAttribute);

			// aliasFor
			if (RelationType.ALIAS_FOR.equals(link.type())) {
				aliasAnnotation.setAttributes(aliasAttribute.getAttributeName(), new AliasedAnnotationAttribute(aliasAttribute, originalAttribute));
				return;
			}
			// forceAliasFor
			aliasAnnotation.setAttributes(aliasAttribute.getAttributeName(), new ForceAliasedAnnotationAttribute(aliasAttribute, originalAttribute));
		});
	}

}
