package cn.hutool.core.annotation;

import cn.hutool.core.annotation.scanner.MetaAnnotationScanner;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
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
public class SyntheticMetaAnnotation<A extends Annotation> implements SyntheticAnnotation<SyntheticMetaAnnotation.MetaAnnotation> {

	/**
	 * 根注解，即当前查找的注解
	 */
	private final A source;

	/**
	 * 包含根注解以及其元注解在内的全部注解实例
	 */
	private final Map<Class<? extends Annotation>, MetaAnnotation> metaAnnotationMap;

	/**
	 * 合成注解选择器
	 */
	private final SynthesizedAnnotationSelector annotationSelector;

	/**
	 * 合成注解属性处理器
	 */
	private final SynthesizedAnnotationAttributeProcessor<SyntheticMetaAnnotation.MetaAnnotation> attributeProcessor;

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解。
	 * 当层级结构中出现了相同的注解对象时，将优先选择以距离根注解最近，且优先被扫描的注解对象,
	 * 当获取值时，同样遵循该规则。
	 *
	 * @param source 源注解
	 */
	public SyntheticMetaAnnotation(A source) {
		this(
			source, SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY,
			new CacheableSynthesizedAnnotationAttributeProcessor<>(
				Comparator.comparing(MetaAnnotation::getVerticalDistance)
					.thenComparing(MetaAnnotation::getHorizontalDistance)
			)
		);
	}

	/**
	 * 基于指定根注解，为其层级结构中的全部注解构造一个合成注解
	 *
	 * @param annotation 当前查找的注解类
	 * @param annotationSelector   合成注解选择器
	 */
	public SyntheticMetaAnnotation(
		A annotation,
		SynthesizedAnnotationSelector annotationSelector,
		SynthesizedAnnotationAttributeProcessor<SyntheticMetaAnnotation.MetaAnnotation> attributeProcessor) {
		Assert.notNull(annotation, "annotation must not null");
		Assert.notNull(annotationSelector, "annotationSelector must not null");
		Assert.notNull(attributeProcessor, "attributeProcessor must not null");

		this.source = annotation;
		this.annotationSelector = annotationSelector;
		this.attributeProcessor = attributeProcessor;
		this.metaAnnotationMap = new LinkedHashMap<>();
		loadMetaAnnotations();
	}

	/**
	 * 获取根注解
	 *
	 * @return 根注解
	 */
	public A getSource() {
		return source;
	}

	/**
	 * 获取已解析的元注解信息
	 *
	 * @return 已解析的元注解信息
	 */
	Map<Class<? extends Annotation>, MetaAnnotation> getMetaAnnotationMap() {
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
	public SynthesizedAnnotationAttributeProcessor<MetaAnnotation> getAttributeProcessor() {
		return null;
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
			.map(MetaAnnotation::getAnnotation)
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
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T syntheticAnnotation(Class<T> annotationType) {
		if (metaAnnotationMap.containsKey(annotationType)) {
			return (T) Proxy.newProxyInstance(
				annotationType.getClassLoader(),
				new Class[]{annotationType, Synthesized.class},
				new SyntheticAnnotationProxy<>(this, annotationType)
			);
		}
		return null;
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
		// 若该注解已经是合成注解，则直接使用已解析好的元注解信息
		if (source instanceof SyntheticMetaAnnotation.Synthesized) {
			this.metaAnnotationMap.putAll(((Synthesized) source).getMetaAnnotationMap());
			return;
		}
		// 扫描元注解
		metaAnnotationMap.put(source.annotationType(), new MetaAnnotation(source, source, 0, 0));
		new MetaAnnotationScanner().scan(
				(index, annotation) -> {
						MetaAnnotation oldAnnotation = metaAnnotationMap.get(annotation.annotationType());
						MetaAnnotation newAnnotation = new MetaAnnotation(source, annotation, index, metaAnnotationMap.size());
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
	public static class MetaAnnotation implements Annotation, SynthesizedAnnotation<Annotation> {

		private final Annotation root;
		private final Annotation annotation;
		private final Map<String, Method> attributeMethodCaches;
		private final int verticalDistance;
		private final int horizontalDistance;

		public MetaAnnotation(Annotation root, Annotation annotation, int verticalDistance, int horizontalDistance) {
			this.root = root;
			this.annotation = annotation;
			this.verticalDistance = verticalDistance;
			this.horizontalDistance = horizontalDistance;
			this.attributeMethodCaches = AnnotationUtil.getAttributeMethods(annotation.annotationType());
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
		 * 获取根注解到元注解的距离
		 *
		 * @return 根注解到元注解的距离
		 */
		@Override
		public int getVerticalDistance() {
			return verticalDistance;
		}

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
					.filter(method -> ClassUtil.isAssignable(returnType, method.getReturnType()))
					.isPresent();
		}

		/**
		 * 获取元注解的属性值
		 *
		 * @param attributeName 属性名
		 * @return 元注解的属性值
		 */
		@Override
		public Object getAttribute(String attributeName) {
			return Opt.ofNullable(attributeMethodCaches.get(attributeName))
					.map(method -> ReflectUtil.invoke(annotation, method))
					.orElse(null);
		}

	}

	/**
	 * 表示一个已经被合成的注解
	 *
	 * @author huangchengxing
	 */
	interface Synthesized {

		/**
		 * 获取合成注解中已解析的元注解信息
		 *
		 * @return 合成注解中已解析的元注解信息
		 */
		Map<Class<? extends Annotation>, MetaAnnotation> getMetaAnnotationMap();

		static boolean isMetaAnnotationMapMethod(Method method) {
			return StrUtil.equals("getMetaAnnotationMap", method.getName());
		}

	}

	/**
	 * 合成注解代理类
	 *
	 * @author huangchengxing
	 */
	static class SyntheticAnnotationProxy<A extends Annotation> implements Annotation, InvocationHandler {

		private final Class<A> annotationType;
		private final SyntheticMetaAnnotation<?> syntheticMetaAnnotation;

		public SyntheticAnnotationProxy(SyntheticMetaAnnotation<?> syntheticMetaAnnotation, Class<A> annotationType) {
			this.syntheticMetaAnnotation = syntheticMetaAnnotation;
			this.annotationType = annotationType;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return annotationType;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (Synthesized.isMetaAnnotationMapMethod(method)) {
				return syntheticMetaAnnotation.getMetaAnnotationMap();
			}
			if (ReflectUtil.isHashCodeMethod(method)) {
				return getHashCode();
			}
			if (ReflectUtil.isToStringMethod(method)) {
				return getToString();
			}
			return ObjectUtil.defaultIfNull(
					syntheticMetaAnnotation.getAttribute(method.getName(), method.getReturnType()),
					() -> ReflectUtil.invoke(this, method, args)
			);
		}

		/**
		 * 获取toString值
		 *
		 * @return toString值
		 */
		private String getToString() {
			final String attributes = Stream.of(annotationType().getDeclaredMethods())
					.filter(AnnotationUtil::isAttributeMethod)
					.map(method -> StrUtil.format("{}={}", method.getName(), syntheticMetaAnnotation.getAttribute(method.getName(), method.getReturnType())))
					.collect(Collectors.joining(", "));
			return StrUtil.format("@{}({})", annotationType().getName(), attributes);
		}

		/**
		 * 获取hashcode值
		 *
		 * @return hashcode值
		 */
		private int getHashCode() {
			return Objects.hash((Object) syntheticMetaAnnotation.getAnnotations());
		}

	}
}
