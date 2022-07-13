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
 * {@link SynthesizedAnnotationAggregator}的基本实现，表示一个根注解与根注解上的多层元注解的聚合状态
 *
 * <p>假设现有注解A，A上存在元注解B，B上存在元注解C，则对注解A进行解析，
 * 将得到包含根注解A，以及其元注解B、C在内的合成元注解聚合{@link SynthesizedMetaAnnotationAggregator}。
 * 从{@link AnnotatedElement}的角度来说，得到的合成注解是一个同时承载有ABC三个注解对象的被注解元素，
 * 因此通过调用{@link AnnotatedElement}的相关方法将返回对应符合语义的注解对象。
 *
 * <p>在扫描指定根注解及其元注解时，若在不同的层级出现了类型相同的注解实例，
 * 将会根据实例化时指定的{@link SynthesizedAnnotationSelector}选择最优的注解，
 * 完成对根注解及其元注解的扫描后，合成注解中每种类型的注解对象都将有且仅有一个。<br>
 * 默认情况下，将使用{@link SynthesizedAnnotationSelector#NEAREST_AND_OLDEST_PRIORITY}作为选择器，
 * 此时若出现扫描时得到了多个同类型的注解对象，有且仅有最接近根注解的注解对象会被作为有效注解。
 *
 * <p>当扫描的注解对象经过{@link SynthesizedAnnotationSelector}处理后，
 * 将会被转为{@link MetaAnnotation}，并使用在实例化时指定的{@link AliasAttributePostProcessor}
 * 进行后置处理。<br>
 * 默认情况下，将注册以下后置处理器以对{@link Alias}与{@link Link}和其扩展注解提供支持：
 * <ul>
 *     <li>{@link AliasAttributePostProcessor}；</li>
 *     <li>{@link MirrorLinkAttributePostProcessor}；</li>
 *     <li>{@link AliasForLinkAttributePostProcessor}；</li>
 * </ul>
 * 若用户需要自行扩展，则需要保证上述三个处理器被正确注入当前实例。
 *
 * <p>{@link SynthesizedMetaAnnotationAggregator}支持通过{@link #getAttribute(String, Class)}，
 * 或通过{@link #synthesize(Class)}获得注解代理对象后获取指定类型的注解属性值，
 * 返回的属性值将根据合成注解中对应原始注解属性上的{@link Alias}与{@link Link}注解而有所变化。
 * 通过当前实例获取属性值时，将经过{@link SynthesizedAnnotationAttributeProcessor}的处理。<br>
 * 默认情况下，实例将会注册{@link CacheableSynthesizedAnnotationAttributeProcessor}，
 * 该处理器将令元注解中与子注解类型与名称皆一致的属性被子注解的属性覆盖，并且缓存最终获取到的属性值。
 *
 * @author huangchengxing
 * @see AnnotationUtil
 * @see SynthesizedAnnotationSelector
 */
public class SynthesizedMetaAnnotationAggregator implements SynthesizedAnnotationAggregator {

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
	public SynthesizedMetaAnnotationAggregator(Annotation source) {
		this(
			source, SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY,
			new CacheableSynthesizedAnnotationAttributeProcessor(),
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
	public SynthesizedMetaAnnotationAggregator(
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
	public SynthesizedAnnotationAttributeProcessor getAnnotationAttributeProcessor() {
		return this.attributeProcessor;
	}

	/**
	 * 获取合成注解属性后置处理器
	 *
	 * @return 合成注解属性后置处理器
	 */
	@Override
	public Collection<SynthesizedAnnotationPostProcessor> getAnnotationAttributePostProcessors() {
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
	 * 获取全部的已合成注解
	 *
	 * @return 合成注解
	 */
	@Override
	public Collection<SynthesizedAnnotation> getAllSynthesizedAnnotation() {
		return metaAnnotationMap.values();
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
	 * 获取合成注解中包含的指定注解
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
	 * 获取合成注解中包含的全部注解
	 *
	 * @return 注解对象
	 */
	@Override
	public Annotation[] getAnnotations() {
		return metaAnnotationMap.values().stream()
			.map(SynthesizedAnnotation::getAnnotation)
			.toArray(Annotation[]::new);
	}

	/**
	 * 若合成注解在存在指定元注解，则使用动态代理生成一个对应的注解实例
	 *
	 * @param annotationType 注解类型
	 * @return 合成注解对象
	 * @see SyntheticAnnotationProxy#create(Class, SynthesizedAnnotationAggregator)
	 */
	@Override
	public <T extends Annotation> T synthesize(Class<T> annotationType) {
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

		private final SynthesizedAnnotationAggregator owner;
		private final Annotation root;
		private final Annotation annotation;
		private final Map<String, AnnotationAttribute> attributeMethodCaches;
		private final int verticalDistance;
		private final int horizontalDistance;

		public MetaAnnotation(SynthesizedAnnotationAggregator owner, Annotation root, Annotation annotation, int verticalDistance, int horizontalDistance) {
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
		public SynthesizedAnnotationAggregator getOwner() {
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
