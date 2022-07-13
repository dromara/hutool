package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.ForestMap;
import cn.hutool.core.map.LinkedForestMap;
import cn.hutool.core.map.TreeEntry;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.Map;

/**
 * 用于处理合成注解属性中{@link Alias}的映射关系，
 * 该处理器会令{@link Alias#value()}指向的属性值强制覆盖当前属性
 *
 * @author huangchengxing
 * @see ForceAliasedAnnotationAttribute
 */
public class AliasAttributePostProcessor implements SynthesizedAnnotationPostProcessor {

	@Override
	public int order() {
		return Integer.MIN_VALUE;
	}

	@Override
	public void process(SynthesizedAnnotation annotation, SyntheticAnnotation syntheticAnnotation) {
		final Map<String, AnnotationAttribute> attributeMap = annotation.getAttributes();

		// 记录别名与属性的关系
		final ForestMap<String, AnnotationAttribute> attributeAliasMappings = new LinkedForestMap<>(false);
		attributeMap.forEach((attributeName, attribute) -> {
			final String alias = Opt.ofNullable(attribute.getAnnotation(Alias.class))
				.map(Alias::value)
				.orElse(null);
			if (ObjectUtil.isNull(alias)) {
				return;
			}
			final AnnotationAttribute aliasAttribute = attributeMap.get(alias);
			Assert.notNull(aliasAttribute, "no method for alias: [{}]", alias);
			attributeAliasMappings.putLinkedNodes(alias, aliasAttribute, attributeName, attribute);
		});

		// 处理别名
		attributeMap.forEach((attributeName, attribute) -> {
			final AnnotationAttribute resolvedAttribute = Opt.ofNullable(attributeName)
				.map(attributeAliasMappings::getRootNode)
				.map(TreeEntry::getValue)
				.map(aliasAttribute -> (AnnotationAttribute)new ForceAliasedAnnotationAttribute(attribute, aliasAttribute))
				.orElse(attribute);
			Assert.isTrue(
				ObjectUtil.isNull(resolvedAttribute)
					|| ClassUtil.isAssignable(attribute.getAttributeType(), resolvedAttribute.getAttributeType()),
				"return type of the root alias method [{}] is inconsistent with the original [{}]",
				resolvedAttribute.getClass(), attribute.getAttributeType()
			);
			attributeMap.put(attributeName, resolvedAttribute);
		});
		annotation.setAttributes(attributeMap);
	}

}
