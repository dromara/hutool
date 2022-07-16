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
 * <p>用于处理注解对象中带有{@link Alias}注解的属性。<br>
 * 当该处理器执行完毕后，{@link Alias}注解指向的目标注解的属性将会被包装并替换为
 * {@link ForceAliasedAnnotationAttribute}。
 *
 * @author huangchengxing
 * @see Alias
 * @see ForceAliasedAnnotationAttribute
 */
public class AliasAnnotationPostProcessor implements SynthesizedAnnotationPostProcessor {

	@Override
	public int order() {
		return Integer.MIN_VALUE;
	}

	@Override
	public void process(SynthesizedAnnotation synthesizedAnnotation, AnnotationSynthesizer synthesizer) {
		final Map<String, AnnotationAttribute> attributeMap = synthesizedAnnotation.getAttributes();

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
				.orElse(attribute);
			Assert.isTrue(
				ObjectUtil.isNull(resolvedAttribute)
					|| ClassUtil.isAssignable(attribute.getAttributeType(), resolvedAttribute.getAttributeType()),
				"return type of the root alias method [{}] is inconsistent with the original [{}]",
				resolvedAttribute.getClass(), attribute.getAttributeType()
			);
			if (attribute != resolvedAttribute) {
				attributeMap.put(attributeName, new ForceAliasedAnnotationAttribute(attribute, resolvedAttribute));
			}
		});
		synthesizedAnnotation.setAttributes(attributeMap);
	}

}
