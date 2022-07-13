package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理注解中带有{@link Link}注解，且{@link Link#type()}为{@link RelationType#MIRROR_FOR}的属性。
 *
 * @author huangchengxing
 * @see MirroredAnnotationAttribute
 */
public class MirrorLinkAttributePostProcessor implements SynthesizedAnnotationPostProcessor {

	@Override
	public int order() {
		return Integer.MIN_VALUE + 1;
	}

	@Override
	public void process(SynthesizedAnnotation annotation, SyntheticAnnotation syntheticAnnotation) {
		Map<String, AnnotationAttribute> attributeMap = new HashMap<>(annotation.getAttributes());
		attributeMap.forEach((originalAttributeName, originalAttribute) -> {
			// 跳过已经解析的镜像属性
			if (originalAttribute instanceof MirroredAnnotationAttribute) {
				return;
			}

			// 获取注解
			final Link link = SyntheticAnnotationUtil.getLink(originalAttribute, RelationType.MIRROR_FOR);
			if (ObjectUtil.isNull(link)) {
				return;
			}

			// 获取指定镜像属性所在的注解
			final SynthesizedAnnotation mirrorAnnotation = SyntheticAnnotationUtil.getLinkedAnnotation(link, syntheticAnnotation, annotation.annotationType());
			if (ObjectUtil.isNull(mirrorAnnotation)) {
				return;
			}

			// 获取镜像属性，并进行校验
			final AnnotationAttribute mirrorAttribute = mirrorAnnotation.getAttributes().get(link.attribute());
			checkMirrorRelation(link, originalAttribute, mirrorAttribute);

			// 包装这一对镜像属性，并替换原注解中的对应属性
			final AnnotationAttribute mirroredOriginalAttribute = new MirroredAnnotationAttribute(originalAttribute, mirrorAttribute);
			syntheticAnnotation.getSynthesizedAnnotation(originalAttribute.getAnnotationType())
				.setAttribute(originalAttributeName, mirroredOriginalAttribute);
			final AnnotationAttribute mirroredTargetAttribute = new MirroredAnnotationAttribute(mirrorAttribute, originalAttribute);
			mirrorAnnotation.setAttribute(link.attribute(), mirroredTargetAttribute);
		});
	}

	private void checkMirrorRelation(Link annotation, AnnotationAttribute original, AnnotationAttribute mirror) {
		// 镜像属性必须存在
		SyntheticAnnotationUtil.checkLinkedAttributeNotNull(original, mirror, annotation);
		// 镜像属性返回值必须一致
		SyntheticAnnotationUtil.checkAttributeType(original, mirror);
		// 镜像属性上必须存在对应的注解
		final Link mirrorAttributeAnnotation = SyntheticAnnotationUtil.getLink(mirror, RelationType.MIRROR_FOR);
		Assert.isTrue(
			ObjectUtil.isNotNull(mirrorAttributeAnnotation) && RelationType.MIRROR_FOR.equals(mirrorAttributeAnnotation.type()),
			"mirror attribute [{}] of original attribute [{}] must marked by @Link, and also @LinkType.type() must is [{}]",
			mirror.getAttribute(), original.getAttribute(), RelationType.MIRROR_FOR
		);
	}

}
