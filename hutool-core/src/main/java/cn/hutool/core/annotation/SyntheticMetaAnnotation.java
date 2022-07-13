package cn.hutool.core.annotation;

import cn.hutool.core.annotation.scanner.MetaAnnotationScanner;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 表示一个根注解与根注解上的多层元注解合成的注解
 *
 * <p>假设现有注解A，A上存在元注解B，B上存在元注解C，则对A解析得到的合成注解X，则CBA都是X的元注解，X为根注解。<br>
 * 通过{@link #isAnnotationPresent(Class)}可确定指定类型是注解是否是该合成注解的元注解，即是否为当前实例的“父类”。
 * 若指定注解是当前实例的元注解，则通过{@link #getAnnotation(Class)}可获得动态代理生成的对应的注解实例。<br>
 * 需要注意的是，由于认为合并注解X以最初的根注解A作为元注解，因此{@link #getAnnotations()}或{@link #getDeclaredAnnotations()}
 * 都将只能获得A。
 *
 * <p>若认为该合成注解X在第0层，则根注解A在第1层，B在第2层......以此类推,
 * 则相同或不同的层级中可能会出现类型相同的注解对象，此时将通过{@link SynthesizedAnnotationSelector}选择出最合适的注解对象，
 * 该注解对象将在合成注解中作为唯一有效的元注解用于进行相关操作。<br>
 * 默认情况下，将选择{@link SynthesizedAnnotationSelector#NEAREST_AND_OLDEST_PRIORITY}选择器实例，
 * 即层级越低的注解离根注解距离近，则该注解优先级越高，即遵循“就近原则”。
 *
 * <p>合成注解中获取到的注解中可能会具有一些同名且同类型的属性，
 * 此时将根据{@link SynthesizedAnnotationAttributeProcessor}决定如何从这些注解的相同属性中获取属性值。<br>
 * 默认情况下，将选择{@link CacheableSynthesizedAnnotationAttributeProcessor}用于获取属性，
 * 该处理器将选择距离根注解最近的注解中的属性用于获取属性值，{@link #getAnnotation(Class)}获得的代理类实例的属性值遵循该规则。<br>
 * 举个例子：若CBA同时存在属性y，则将X视为C，B或者A时，获得的y属性的值都与最底层元注解A的值保持一致。
 * 若两相同注解处于同一层级，则按照从其上一级“子注解”的{@link AnnotatedElement#getAnnotations()}的调用顺序排序。
 *
 * <p>别名在合成注解中仍然有效，若注解X中任意属性上存在{@link Alias}注解，则{@link Alias#value()}指定的属性值将会覆盖注解属性的本身的值。<br>
 * {@link Alias}注解仅能指定注解X中存在的属性作为别名，不允许指定元注解或子类注解的属性。
 *
 * @author huangchengxing
 * @see AnnotationUtil
 * @see SynthesizedAnnotationSelector
 */
public class SyntheticMetaAnnotation implements SyntheticAnnotation {

	/**
	 * 根注解，即当前查找的注解
	 */
	private final Annotation source;

	/**
	 * 包含根注解以及其元注解在内的全部注解实例
	 */
	private final Map<Class<? extends Annotation>, SynthesizedAnnotation> metaAnnotationMap;

	/**
	 * 合成注解选择器
	 */
	private final SynthesizedAnnotationSelector annotationSelector;

	/**
	 * 合成注解属性处理器
	 */
	private final SynthesizedAnnotationAttributeProcessor attributeProcessor;

	/**
	 * 合成注解属性处理器
	 */
	private final List<SynthesizedAnnotationPostProcessor> postProcessors;

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解。
	 * 当层级结构中出现了相同的注解对象时，将优先选择以距离根注解最近，且优先被扫描的注解对象,
	 * 当获取值时，同样遵循该规则。
	 *
	 * @param source 源注解
	 */
	public SyntheticMetaAnnotation(Annotation source) {
		this(
			source, SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY,
			new CacheableSynthesizedAnnotationAttributeProcessor(
				Comparator.comparing(SynthesizedAnnotation::getVerticalDistance)
					.thenComparing(SynthesizedAnnotation::getHorizontalDistance)
			),
			Arrays.asList(
				new AliasAttributePostProcessor(),
				new MirrorLinkAttributePostProcessor(),
				new AliasForLinkAttributePostProcessor()
			)
		);
	}

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解
	 *
	 * @param annotation         当前查找的注解对象
	 * @param annotationSelector 合成注解选择器
	 * @param attributeProcessor 注解属性处理器
	 */
	public SyntheticMetaAnnotation(
		Annotation annotation,
		SynthesizedAnnotationSelector annotationSelector,
		SynthesizedAnnotationAttributeProcessor attributeProcessor,
		Collection<? extends SynthesizedAnnotationPostProcessor> annotationPostProcessors) {
		Assert.notNull(annotation, "annotation must not null");
		Assert.notNull(annotationSelector, "annotationSelector must not null");
		Assert.notNull(attributeProcessor, "attributeProcessor must not null");
		Assert.notNull(annotationPostProcessors, "attributePostProcessors must not null");

		// 初始化属性
		this.source = annotation;
		this.annotationSelector = annotationSelector;
		this.attributeProcessor = attributeProcessor;
		this.postProcessors = new ArrayList<>(annotationPostProcessors);
		this.postProcessors.sort(Comparator.comparing(SynthesizedAnnotationPostProcessor::order));
		this.metaAnnotationMap = new LinkedHashMap<>();

		// 初始化元注解信息，并进行后置处理
		// TODO 缓存元注解信息，避免重复解析
		loadMetaAnnotations();
		annotationPostProcessors.forEach(processor ->
			metaAnnotationMap.values().forEach(synthesized -> processor.process(synthesized, this))
		);
	}

	/**
	 * 获取根注解
	 *
	 * @return 根注解
	 */
	public Annotation getSource() {
		return source;
	}

	/**
	 * 获取已解析的元注解信息
	 *
	 * @return 已解析的元注解信息
	 */
	Map<Class<? extends Annotation>, SynthesizedAnnotation> getMetaAnnotationMap() {
		return metaAnnotationMap;
	}

	/**
	 * 获取合成注解选择器
	 *
	 * @return 合成注解选择器
	 */
	@Override
	public SynthesizedAnnotationSelector getAnnotationSelector() {
		return this.annotationSelector;
	}

	/**
	 * 获取合成注解属性处理器
	 *
	 * @return 合成注解属性处理器
	 */
	@Override
	public SynthesizedAnnotationAttributeProcessor getAttributeProcessor() {
		return this.attributeProcessor;
	}

	/**
	 * 获取合成注解属性后置处理器
	 *
	 * @return 合成注解属性后置处理器
	 */
	@Override
	public Collection<SynthesizedAnnotationPostProcessor> getSynthesizedAnnotationAttributePostProcessors() {
		return this.postProcessors;
	}

	/**
	 * 获取已合成的注解
	 *
	 * @param annotationType 注解类型
	 * @return 已合成的注解
	 */
	@Override
	public SynthesizedAnnotation getSynthesizedAnnotation(Class<?> annotationType) {
		return metaAnnotationMap.get(annotationType);
	}

	/**
	 * 获取根注解类型
	 *
	 * @return 注解类型
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return this.getClass();
	}

	/**
	 * 根据指定的属性名与属性类型获取对应的属性值，若存在{@link Alias}则获取{@link Alias#value()}指定的别名属性的值
	 * <p>当不同层级的注解之间存在同名同类型属性时，将优先获取更接近根注解的属性
	 *
	 * @param attributeName 属性名
	 * @param attributeType 属性类型
	 * @return 属性
	 */
	@Override
	public Object getAttribute(String attributeName, Class<?> attributeType) {
		return attributeProcessor.getAttributeValue(attributeName, attributeType, metaAnnotationMap.values());
	}

	/**
	 * 获取被合成的注解
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return Opt.ofNullable(annotationType)
			.map(metaAnnotationMap::get)
			.map(SynthesizedAnnotation::getAnnotation)
			.map(annotationType::cast)
			.orElse(null);
	}

	/**
	 * 当前合成注解中是否存在指定元注解
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return metaAnnotationMap.containsKey(annotationType);
	}

	/**
	 * 获取全部注解
	 *
	 * @return 注解对象
	 */
	@Override
	public Annotation[] getAnnotations() {
		return getMetaAnnotationMap().values().toArray(new MetaAnnotation[0]);
	}

	/**
	 * 若合成注解在存在指定元注解，则使用动态代理生成一个对应的注解实例
	 *
	 * @param annotationType 注解类型
	 * @return 合成注解对象
	 * @see SyntheticAnnotationProxy#create(Class, SyntheticAnnotation)
	 */
	@Override
	public <T extends Annotation> T syntheticAnnotation(Class<T> annotationType) {
		return SyntheticAnnotationProxy.create(annotationType, this);
	}

	/**
	 * 获取根注解直接声明的注解，该方法正常情况下当只返回原注解
	 *
	 * @return 直接声明注解
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return new Annotation[]{getSource()};
	}

	/**
	 * 广度优先遍历并缓存该根注解上的全部元注解
	 */
	private void loadMetaAnnotations() {
		Assert.isFalse(SyntheticAnnotationProxy.isProxyAnnotation(source.getClass()), "source [{}] has been synthesized");
		// 扫描元注解
		metaAnnotationMap.put(source.annotationType(), new MetaAnnotation(this, source, source, 0, 0));
		new MetaAnnotationScanner().scan(
				(index, annotation) -> {
						SynthesizedAnnotation oldAnnotation = metaAnnotationMap.get(annotation.annotationType());
						SynthesizedAnnotation newAnnotation = new MetaAnnotation(this, source, annotation, index, metaAnnotationMap.size());
						if (ObjectUtil.isNull(oldAnnotation)) {
							metaAnnotationMap.put(annotation.annotationType(), newAnnotation);
						} else {
							metaAnnotationMap.put(annotation.annotationType(), annotationSelector.choose(oldAnnotation, newAnnotation));
						}
				},
				source.annotationType(), null
		);
	}

	/**
	 * 元注解包装类
	 *
	 * @author huangchengxing
	 */
	public static class MetaAnnotation implements Annotation, SynthesizedAnnotation {

		private final SyntheticAnnotation owner;
		private final Annotation root;
		private final Annotation annotation;
		private final Map<String, AnnotationAttribute> attributeMethodCaches;
		private final int verticalDistance;
		private final int horizontalDistance;

		public MetaAnnotation(SyntheticAnnotation owner, Annotation root, Annotation annotation, int verticalDistance, int horizontalDistance) {
			this.owner = owner;
			this.root = root;
			this.annotation = annotation;
			this.verticalDistance = verticalDistance;
			this.horizontalDistance = horizontalDistance;
			this.attributeMethodCaches = Stream.of(annotation.annotationType().getDeclaredMethods())
				.filter(AnnotationUtil::isAttributeMethod)
				.collect(Collectors.toMap(Method::getName, method -> new CacheableAnnotationAttribute(annotation, method)));
		}

		/**
		 * 获取所属的合成注解
		 *
		 * @return 合成注解
		 */
		@Override
		public SyntheticAnnotation getOwner() {
			return owner;
		}

		/**
		 * 获取注解类型
		 *
		 * @return 注解类型
		 */
		@Override
		public Class<? extends Annotation> annotationType() {
			return annotation.annotationType();
		}

		/**
		 * 获取根注解
		 *
		 * @return 根注解
		 */
		@Override
		public Annotation getRoot() {
			return this.root;
		}

		/**
		 * 获取元注解
		 *
		 * @return 元注解
		 */
		@Override
		public Annotation getAnnotation() {
			return annotation;
		}

		/**
		 * 获取该合成注解与根注解之间相隔的层级数
		 *
		 * @return 该合成注解与根注解之间相隔的层级数
		 */
		@Override
		public int getVerticalDistance() {
			return verticalDistance;
		}

		/**
		 * 获取该合成注解与根注解之间相隔的注解树
		 *
		 * @return 该合成注解与根注解之间相隔的注解树
		 */
		@Override
		public int getHorizontalDistance() {
			return horizontalDistance;
		}

		/**
		 * 元注解是否存在该属性
		 *
		 * @param attributeName 属性名
		 * @return 是否存在该属性
		 */
		public boolean hasAttribute(String attributeName) {
			return attributeMethodCaches.containsKey(attributeName);
		}

		/**
		 * 元注解是否存在该属性，且该属性的值类型是指定类型或其子类
		 *
		 * @param attributeName 属性名
		 * @param returnType    返回值类型
		 * @return 是否存在该属性
		 */
		@Override
		public boolean hasAttribute(String attributeName, Class<?> returnType) {
			return Opt.ofNullable(attributeMethodCaches.get(attributeName))
					.filter(method -> ClassUtil.isAssignable(returnType, method.getAttributeType()))
					.isPresent();
		}

		/**
		 * 获取该注解的全部属性
		 *
		 * @return 注解属性
		 */
		@Override
		public Map<String, AnnotationAttribute> getAttributes() {
			return this.attributeMethodCaches;
		}

		/**
		 * 设置属性值
		 *
		 * @param attributeName 属性名称
		 * @param attribute     注解属性
		 */
		@Override
		public void setAttribute(String attributeName, AnnotationAttribute attribute) {
			attributeMethodCaches.put(attributeName, attribute);
		}

		/**
		 * 替换属性值
		 *
		 * @param attributeName 属性名
		 * @param operator      替换操作
		 */
		@Override
		public void replaceAttribute(String attributeName, UnaryOperator<AnnotationAttribute> operator) {
			AnnotationAttribute old = attributeMethodCaches.get(attributeName);
			if (ObjectUtil.isNotNull(old)) {
				attributeMethodCaches.put(attributeName, operator.apply(old));
			}
		}

		/**
		 * 获取属性值
		 *
		 * @param attributeName 属性名
		 * @return 属性值
		 */
		@Override
		public Object getAttributeValue(String attributeName) {
			return Opt.ofNullable(attributeMethodCaches.get(attributeName))
				.map(AnnotationAttribute::getValue)
				.get();
		}

	}

}
