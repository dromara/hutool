/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.annotation;

import org.dromara.hutool.core.annotation.elements.MetaAnnotatedElement;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.multi.Graph;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.dromara.hutool.core.array.ArrayUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>注解映射，用于包装并增强一个普通注解对象，
 * 包装后的可以通过{@code getResolvedXXX}获得注解对象或属性值，
 * 可以支持属性别名与属性覆写的属性解析机制。
 *
 * <p><strong>父子注解</strong>
 * <p>当实例创建时，可通过{@link #source}指定当前注解的子注解，多个实例通过该引用，
 * 可以构成一条表示父子/元注解关系的单向链表。<br>
 * 当{@link #source}为{@code null}时，认为当前注解即为根注解。
 *
 * <p><strong>属性别名</strong>
 * <p>注解内的属性可以通过{@link Alias}互相关联，当解析时，
 * 对绑定中的任意一个属性的赋值，会被同步给其他直接或者间接关联的属性。<br>
 * eg: 若注解存在{@code a <=> b <=> c}的属性别名关系，则对<em>a</em>赋值，此时<em>b</em>、<em>c</em>也会被一并赋值。
 *
 * <p><strong>属性覆写</strong>
 * <p>当实例中{@link #source}不为{@code null}，即当前注解存在至少一个或者多个子注解时，
 * 若在子注解中的同名、同类型的属性，则获取值时将优先获取子注解的值，若该属性存在别名，则别名属性也如此。<br>
 * 属性覆写遵循如下机制：
 * <ul>
 *     <li>
 *         当覆写的属性存在别名属性时，别名属性也会一并被覆写；<br>
 *         eg: 若注解存在{@code a <=> b <=> c}的属性别名关系，则覆写<em>a</em>,，属性<em>b</em>、<em>c</em>也会被覆写；
 *     </li>
 *     <li>
 *         当属性可被多个子注解覆写时，总是优先选择离根注解最近的子注解覆写该属性；<br>
 *         eg：若从根注解<em>a</em>到元注解<em>b</em>有依赖关系{@code a => b => c}，
 *         此时若<em>c</em>中存在属性可同时被<em>a</em>、<em>b</em>覆写，则优先选择<em>a</em>；
 *     </li>
 *     <li>
 *         当覆写属性的子注解属性也被其子注解覆写时，等同于该子注解的子注解直接覆写的当前注解的属性；<br>
 *         eg：若从根注解<em>a</em>到元注解<em>b</em>有依赖关系{@code a => b => c}，
 *         此时若<em>b</em>中存在属性被<em>a</em>覆写，而<em>b</em>中被<em>a</em>覆写的属性又覆写<em>c</em>中属性，
 *         则等同于<em>c</em>中被覆写的属性直接被<em>a</em>覆写。
 *     </li>
 * </ul>
 *
 * @author huangchengxing
 * @see MetaAnnotatedElement
 * @since 6.0.0
 */
public class ResolvedAnnotationMapping implements AnnotationMapping<Annotation> {

	/**
	 * 不存在的属性对应的默认下标
	 */
	protected static final int NOT_FOUND_INDEX = -1;

	/**
	 * 注解属性，属性在该数组中的下标等同于属性本身
	 */
	private final Method[] attributes;

	/**
	 * 别名属性设置
	 */
	private final AliasSet[] aliasSets;

	/**
	 * 解析后的属性，下标都与{@link #attributes}相同下标的属性一一对应。
	 * 当下标对应属性下标不为{@link #NOT_FOUND_INDEX}时，说明该属性存在解析：
	 * <ul>
	 *     <li>若在{@link #resolvedAttributeSources}找不到对应实例，则说明该属性是别名属性；</li>
	 *     <li>若在{@link #resolvedAttributeSources}找的到对应实例，则说明该属性是覆盖属性；</li>
	 * </ul>
	 */
	private final int[] resolvedAttributes;

	/**
	 * 解析后的属性对应的数据源 <br>
	 * 当属性被覆写时，该属性对应下标位置会指向覆写该属性的注解对象
	 */
	private final ResolvedAnnotationMapping[] resolvedAttributeSources;

	/**
	 * 子注解的映射对象，当该项为{@code null}时，则认为当前注解为根注解
	 */
	private final ResolvedAnnotationMapping source;

	/**
	 * 注解属性
	 */
	private final Annotation annotation;

	/**
	 * 代理对象缓存
	 */
	private volatile Annotation proxied;

	/**
	 * 该注解的属性是否发生了解析
	 */
	private final boolean resolved;

	/**
	 * 构建一个注解映射对象
	 *
	 * @param annotation                 注解对象
	 * @param resolveAnnotationAttribute 是否解析注解属性，为{@code true}时获得的注解皆支持属性覆盖与属性别名机制
	 * @return 注解映射对象
	 */
	public static ResolvedAnnotationMapping create(final Annotation annotation, final boolean resolveAnnotationAttribute) {
		return create(null, annotation, resolveAnnotationAttribute);
	}

	/**
	 * 构建一个注解映射对象，子注解及子注解的子注解们的属性会覆写注解对象的中的同名同名同类型属性，
	 * 当一个属性被多个子注解覆写时，优先选择离根注解最接近的注解中的属性用于覆写，
	 *
	 * @param source                     子注解
	 * @param annotation                 注解对象
	 * @param resolveAnnotationAttribute 是否解析注解属性，为{@code true}时获得的注解皆支持属性覆盖与属性别名机制
	 * @return 注解映射对象
	 */
	public static ResolvedAnnotationMapping create(
		final ResolvedAnnotationMapping source, final Annotation annotation, final boolean resolveAnnotationAttribute) {
		return new ResolvedAnnotationMapping(source, annotation, resolveAnnotationAttribute);
	}

	/**
	 * 构建一个注解映射对象
	 *
	 * @param source           当前注解的子注解
	 * @param annotation       注解对象
	 * @param resolveAttribute 是否需要解析属性
	 * @throws NullPointerException {@code source}为{@code null}时抛出
	 * @throws IllegalArgumentException
	 * <ul>
	 *     <li>当{@code annotation}已经被代理过时抛出；</li>
	 *     <li>当{@code source}包装的注解对象与{@code annotation}相同时抛出；</li>
	 *     <li>当{@code annotation}包装的注解对象类型为{@code ResolvedAnnotationMapping}时抛出；</li>
	 * </ul>
	 */
	ResolvedAnnotationMapping(final ResolvedAnnotationMapping source, final Annotation annotation, final boolean resolveAttribute) {
		Objects.requireNonNull(annotation);
		Assert.isFalse(AnnotationMappingProxy.isProxied(annotation), "annotation has been proxied");
		Assert.isFalse(annotation instanceof ResolvedAnnotationMapping, "annotation has been wrapped");
		Assert.isFalse(
			Objects.nonNull(source) && Objects.equals(source.annotation, annotation),
			"source annotation can not same with target [{}]", annotation
		);
		this.annotation = annotation;
		this.attributes = AnnotationUtil.getAnnotationAttributes(annotation.annotationType());
		this.source = source;

		// 别名属性
		this.aliasSets = new AliasSet[this.attributes.length];

		// 解析后的属性与数据源
		this.resolvedAttributeSources = new ResolvedAnnotationMapping[this.attributes.length];
		this.resolvedAttributes = new int[this.attributes.length];
		Arrays.fill(this.resolvedAttributes, NOT_FOUND_INDEX);

		// 若有必要，解析属性
		// TODO flag改为枚举，使得可以自行选择：1.只支持属性别名，2.只支持属性覆盖，3.两个都支持，4.两个都不支持
		this.resolved = resolveAttribute && resolveAttributes();
	}

	/**
	 * 解析属性
	 */
	private boolean resolveAttributes() {
		// TODO 支持处理@PropIgnore，被标记的属性无法被覆写，也不会被别名关联
		// 解析同一注解中的别名
		resolveAliasAttributes();
		// 使用子注解覆写当前注解中的属性
		resolveOverwriteAttributes();
		// 注解的属性是否发生过解析
		return IntStream.of(resolvedAttributes)
			.anyMatch(idx -> NOT_FOUND_INDEX != idx);
	}

	// ================== 通用 ==================

	/**
	 * 当前注解是否为根注解
	 *
	 * @return 是否
	 */
	@Override
	public boolean isRoot() {
		return Objects.isNull(source);
	}

	/**
	 * 获取根注解
	 *
	 * @return 根注解的映射对象
	 */
	public ResolvedAnnotationMapping getRoot() {
		ResolvedAnnotationMapping mapping = this;
		while (Objects.nonNull(mapping.source)) {
			mapping = mapping.source;
		}
		return mapping;
	}

	/**
	 * 获取注解属性
	 *
	 * @return 注解属性
	 */
	@Override
	public Method[] getAttributes() {
		return attributes;
	}

	/**
	 * 获取注解对象
	 *
	 * @return 注解对象
	 */
	@Override
	public Annotation getAnnotation() {
		return annotation;
	}

	/**
	 * 当前注解是否存在被解析的属性，当该值为{@code false}时，
	 * 通过{@code getResolvedAttributeValue}获得的值皆为注解的原始属性值，
	 * 通过{@link #getResolvedAnnotation()}获得注解对象为原始的注解对象。
	 *
	 * @return 是否
	 */
	@Override
	public boolean isResolved() {
		return resolved;
	}

	/**
	 * 根据当前映射对象，通过动态代理生成一个类型与被包装注解对象一致的合成注解，该注解相对原生注解：
	 * <ul>
	 *     <li>支持同注解内通过{@link Alias}构建的别名机制；</li>
	 *     <li>支持子注解对元注解的同名同类型属性覆盖机制；</li>
	 * </ul>
	 * 当{@link #isResolved()}为{@code false}时，则该方法返回被包装的原始注解对象。
	 *
	 * @return 所需的注解，若{@link ResolvedAnnotationMapping#isResolved()}为{@code false}则返回的是原始的注解对象
	 */
	@Override
	public Annotation getResolvedAnnotation() {
		if (!isResolved()) {
			return annotation;
		}
		// 双重检查保证线程安全的创建代理缓存
		if (Objects.isNull(proxied)) {
			synchronized (this) {
				if (Objects.isNull(proxied)) {
					proxied = AnnotationMappingProxy.create(annotationType(), this);
				}
			}
		}
		return proxied;
	}

	// ================== 属性搜索 ==================

	/**
	 * 注解是否存在指定属性
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @return 是否
	 */
	public boolean hasAttribute(final String attributeName, final Class<?> attributeType) {
		return getAttributeIndex(attributeName, attributeType) != NOT_FOUND_INDEX;
	}

	/**
	 * 该属性下标是否在注解中存在对应属性
	 *
	 * @param index 属性下标
	 * @return 是否
	 */
	public boolean hasAttribute(final int index) {
		return index != NOT_FOUND_INDEX
			&& Objects.nonNull(ArrayUtil.get(attributes, index));
	}

	/**
	 * 获取注解属性的下标
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @return 属性下标
	 */
	public int getAttributeIndex(final String attributeName, final Class<?> attributeType) {
		for (int i = 0; i < attributes.length; i++) {
			final Method attribute = attributes[i];
			if (CharSequenceUtil.equals(attribute.getName(), attributeName)
				&& ClassUtil.isAssignable(attributeType, attribute.getReturnType())) {
				return i;
			}
		}
		return NOT_FOUND_INDEX;
	}

	/**
	 * 根据下标获取注解属性
	 *
	 * @param index 属性下标
	 * @return 属性对象
	 */
	public Method getAttribute(final int index) {
		return ArrayUtil.get(attributes, index);
	}

	// ================== 属性取值 ==================

	/**
	 * 获取属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @param <R>           返回值类型
	 * @return 属性值
	 */
	@Override
	public <R> R getAttributeValue(final String attributeName, final Class<R> attributeType) {
		return getAttributeValue(getAttributeIndex(attributeName, attributeType));
	}

	/**
	 * 获取属性值
	 *
	 * @param index 属性下标
	 * @param <R>   返回值类型
	 * @return 属性值
	 */
	public <R> R getAttributeValue(final int index) {
		return hasAttribute(index) ? MethodUtil.invoke(annotation, attributes[index]) : null;
	}

	/**
	 * 获取解析后的属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @param <R>           返回值类型
	 * @return 属性值
	 */
	@Override
	public <R> R getResolvedAttributeValue(final String attributeName, final Class<R> attributeType) {
		return getResolvedAttributeValue(getAttributeIndex(attributeName, attributeType));
	}

	/**
	 * 获取解析后的属性值
	 *
	 * @param index 属性下标
	 * @param <R>   返回值类型
	 * @return 属性值
	 */
	public <R> R getResolvedAttributeValue(final int index) {
		if (!hasAttribute(index)) {
			return null;
		}
		// 如果该属性没有经过解析，则直接获得原始值
		final int resolvedIndex = resolvedAttributes[index];
		if (resolvedIndex == NOT_FOUND_INDEX) {
			return getAttributeValue(index);
		}
		// 若该属性被解析过，但是仍然还在当前实例中，则从实际属性获得值
		final ResolvedAnnotationMapping attributeSource = resolvedAttributeSources[index];
		if (Objects.isNull(attributeSource)) {
			return getAttributeValue(resolvedIndex);
		}
		// 若该属性被解析过，且不在本注解中，则从其元注解获得对应的值
		return attributeSource.getResolvedAttributeValue(resolvedIndex);
	}

	// ================== 解析覆写属性 ==================

	/**
	 * 令{@code annotationAttributes}中属性覆写当前注解中同名同类型的属性，
	 * 该步骤必须在{@link #resolveAliasAttributes()}后进行
	 */
	private void resolveOverwriteAttributes() {
		if (Objects.isNull(source)) {
			return;
		}
		// 获取除自己外的全部子注解
		final Deque<ResolvedAnnotationMapping> sources = new LinkedList<>();
		final Set<Class<? extends Annotation>> accessed = new HashSet<>();
		accessed.add(this.annotationType());
		ResolvedAnnotationMapping sourceMapping = this.source;
		while (Objects.nonNull(sourceMapping)) {
			// 检查循环依赖
			Assert.isFalse(
				accessed.contains(sourceMapping.annotationType()),
				"circular dependency between [{}] and [{}]",
				annotationType(), sourceMapping.annotationType()
			);
			sources.addFirst(sourceMapping);
			accessed.add(source.annotationType());
			sourceMapping = sourceMapping.source;
		}
		// 从根注解开始，令子注解依次覆写当前注解中的值
		for (final ResolvedAnnotationMapping mapping : sources) {
			updateResolvedAttributesByOverwrite(mapping);
		}
	}

	/**
	 * 令{@code annotationAttributes}中属性覆写当前注解中同名同类型且未被覆写的属性
	 *  @param overwriteMapping 注解属性聚合
	 *
	 */
	private void updateResolvedAttributesByOverwrite(final ResolvedAnnotationMapping overwriteMapping) {
		for (int overwriteIndex = 0; overwriteIndex < overwriteMapping.getAttributes().length; overwriteIndex++) {
			final Method overwrite = overwriteMapping.getAttribute(overwriteIndex);
			for (int targetIndex = 0; targetIndex < attributes.length; targetIndex++) {
				final Method attribute =  attributes[targetIndex];
				// 覆写的属性与被覆写的属性名称与类型必须一致
				if (!CharSequenceUtil.equals(attribute.getName(), overwrite.getName())
					|| !ClassUtil.isAssignable(attribute.getReturnType(), overwrite.getReturnType())) {
					continue;
				}
				// 若目标属性未被覆写，则覆写其属性
				overwriteAttribute(overwriteMapping, overwriteIndex, targetIndex, true);
			}
		}
	}

	/**
	 * 更新需要覆写的属性的相关映射关系，若该属性存在别名，则将别名的映射关系一并覆写
	 */
	private void overwriteAttribute(
			final ResolvedAnnotationMapping overwriteMapping, final int overwriteIndex, final int targetIndex, final boolean overwriteAliases) {
		// 若目标属性已被覆写，则不允许再次覆写
		if (isOverwrittenAttribute(targetIndex)) {
			return;
		}
		// 覆写属性
		resolvedAttributes[targetIndex] = overwriteIndex;
		resolvedAttributeSources[targetIndex] = overwriteMapping;
		// 若覆写的属性本身还存在别名，则将别名属性一并覆写
		if (overwriteAliases && Objects.nonNull(aliasSets[targetIndex])) {
			aliasSets[targetIndex].forEach(aliasIndex -> overwriteAttribute(
				overwriteMapping, overwriteIndex, aliasIndex, false
			));
		}
	}

	/**
	 * 判断该属性是否已被覆写
	 */
	private boolean isOverwrittenAttribute(final int index) {
		// 若属性未发生过解析，则必然未被覆写
		return NOT_FOUND_INDEX != resolvedAttributes[index]
			// 若属性发生过解析，且指向其他实例，则说明已被覆写
			&& Objects.nonNull(resolvedAttributeSources[index]);
	}

	// ================== 解析别名属性 ==================

	/**
	 * 解析当前注解属性中通过{@link Alias}构成别名的属性
	 */
	private void resolveAliasAttributes() {
		final Map<Method, Integer> attributeIndexes = new HashMap<>(attributes.length);

		final Graph<Method> methodGraph = new Graph<>();
		// 解析被作为别名的关联属性，根据节点关系构建邻接表
		for (int i = 0; i < attributes.length; i++) {
			// 获取属性上的@Alias注解
			final Method attribute = attributes[i];
			attributeIndexes.put(attribute, i);
			final Alias attributeAnnotation = attribute.getAnnotation(Alias.class);
			if (Objects.isNull(attributeAnnotation)) {
				continue;
			}
			// 获取别名属性
			final Method aliasAttribute = getAliasAttribute(attribute, attributeAnnotation);
			Objects.requireNonNull(aliasAttribute);
			methodGraph.putEdge(aliasAttribute, attribute);
		}

		// 按广度优先遍历邻接表，将属于同一张图上的节点分为一组，并为其建立AliasSet
		final Set<Method> accessed = new HashSet<>(attributes.length);
		final Set<Method> group = new LinkedHashSet<>();
		final Deque<Method> deque = new LinkedList<>();
		for (final Method target : methodGraph.keySet()) {
			group.clear();
			deque.addLast(target);
			while (!deque.isEmpty()) {
				final Method curr = deque.removeFirst();
				// 已经访问过的节点不再访问
				if (accessed.contains(curr)) {
					continue;
				}
				accessed.add(curr);
				// 将其添加到关系组
				group.add(curr);
				final Collection<Method> aliases = methodGraph.getAdjacentPoints(curr);
				if (CollUtil.isNotEmpty(aliases)) {
					deque.addAll(aliases);
				}
			}
			// 为同一关系组的节点构建关联关系
			final int[] groupIndexes = group.stream()
				.mapToInt(attributeIndexes::get)
				.toArray();
			updateAliasSetsForAliasGroup(groupIndexes);
		}

		// 根据AliasSet更新关联的属性
		Stream.of(aliasSets).filter(Objects::nonNull).forEach(set -> {
			final int effectiveAttributeIndex = set.determineEffectiveAttribute();
			set.forEach(index -> resolvedAttributes[index] = effectiveAttributeIndex);
		});
	}

	/**
	 * 获取属性别名，并对其进行基本校验
	 */
	private Method getAliasAttribute(final Method attribute, final Alias attributeAnnotation) {
		// 获取别名属性下标，该属性必须在当前注解中存在
		final int aliasAttributeIndex = getAttributeIndex(attributeAnnotation.value(), attribute.getReturnType());
		Assert.isTrue(hasAttribute(aliasAttributeIndex), "can not find alias attribute [{}] in [{}]", attributeAnnotation.value(), this.annotation.annotationType());

		// 获取具体的别名属性，该属性不能是其本身
		final Method aliasAttribute = getAttribute(aliasAttributeIndex);
		Assert.notEquals(aliasAttribute, attribute, "attribute [{}] can not alias for itself", attribute);

		// 互为别名的属性类型必须一致
		Assert.isAssignable(
			attribute.getReturnType(), aliasAttribute.getReturnType(),
			"aliased attributes [{}] and [{}] must have same return type",
			attribute, aliasAttribute
		);
		return aliasAttribute;
	}

	/**
	 * 为具有关联关系的别名属性构建{@link AliasSet}
	 */
	private void updateAliasSetsForAliasGroup(final int[] groupIndexes) {
		final AliasSet set = new AliasSet(groupIndexes);
		for (final int index : groupIndexes) {
			aliasSets[index] = set;
		}
	}

	/**
	 * 比较两个实例是否相等
	 *
	 * @param o 对象
	 * @return 是否
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final ResolvedAnnotationMapping that = (ResolvedAnnotationMapping)o;
		return resolved == that.resolved && annotation.equals(that.annotation);
	}

	/**
	 * 获取实例哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(annotation, resolved);
	}

	/**
	 * 别名设置，一组具有别名关系的属性会共用同一实例
	 */
	private class AliasSet {

		/**
		 * 关联的别名字段对应的属性在{@link #attributes}中的下标
		 */
		final int[] indexes;

		/**
		 * 创建一个别名设置
		 *
		 * @param indexes 互相关联的别名属性的下标
		 */
		AliasSet(final int[] indexes) {
			this.indexes = indexes;
		}

		/**
		 * 从所有关联的别名属性中，选择出唯一个最终有效的属性：
		 * <ul>
		 *     <li>若所有属性都只有默认值，则要求所有的默认值都必须相等，若符合则返回首个属性，否则报错；</li>
		 *     <li>若有且仅有一个属性具有非默认值，则返回该属性；</li>
		 *     <li>若有多个属性具有非默认值，则要求所有的非默认值都必须相等，若符合并返回该首个具有非默认值的属性，否则报错；</li>
		 * </ul>
		 */
		private int determineEffectiveAttribute() {
			int resolvedIndex = NOT_FOUND_INDEX;
			boolean hasNotDef = false;
			Object lastValue = null;
			for (final int index : indexes) {
				final Method attribute = attributes[index];

				// 获取属性的值，并确认是否为默认值
				final Object def = attribute.getDefaultValue();
				final Object undef = MethodUtil.invoke(annotation, attribute);
				final boolean isDefault = Objects.equals(def, undef);

				// 若是首个属性
				if (resolvedIndex == NOT_FOUND_INDEX) {
					resolvedIndex = index;
					lastValue = isDefault ? def : undef;
					hasNotDef = !isDefault;
					continue;
				}

				// 不是首个属性，且已存在非默认值
				if (hasNotDef) {
					// 如果当前也是非默认值，则要求两值必须相等
					if (!isDefault) {
						Assert.isTrue(
							Objects.equals(lastValue, undef),
							"aliased attribute [{}] and [{}] must have same not default value, but is different: [{}] <==> [{}]",
							attributes[resolvedIndex], attribute, lastValue, undef
						);
					}
					// 否则直接跳过，依然以上一非默认值为准
					continue;
				}

				// 不是首个属性，但是还没有非默认值，而当前值恰好是非默认值，直接更新当前有效值与对应索引
				if (!isDefault) {
					hasNotDef = true;
					lastValue = undef;
					resolvedIndex = index;
					continue;
				}

				// 不是首个属性，还没有非默认值，如果当前也是默认值，则要求两值必须相等
				Assert.isTrue(
					Objects.equals(lastValue, def),
					"aliased attribute [{}] and [{}] must have same default value, but is different: [{}] <==> [{}]",
					attributes[resolvedIndex], attribute, lastValue, def
				);
			}
			Assert.isFalse(resolvedIndex == NOT_FOUND_INDEX, "can not resolve aliased attributes from [{}]", annotation);
			return resolvedIndex;
		}

		/**
		 * 遍历下标
		 */
		void forEach(final IntConsumer consumer) {
			for (final int index : indexes) {
				consumer.accept(index);
			}
		}

	}
}
