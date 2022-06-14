package cn.hutool.core.annotation;

import cn.hutool.core.annotation.scanner.MateAnnotationScanner;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 表示一个根注解与根注解上的多层元注解合成的注解
 *
 * <p>假设现有根注解A，A上存在元注解B，B上存在元注解C，则对A解析得到的合成注解X，X作为新的根注解，则CBA都是X的元注解。<br />
 * 通过{@link #isAnnotationPresent(Class)}可确定指定类型是注解是否是该合成注解的元注解，即是否为当前实例的“父类”。
 * 若指定注解是当前实例的元注解，则通过{@link #getAnnotation(Class)}可获得动态代理生成的对应的注解实例。<br />
 * 需要注意的是，由于认为合并注解X以最初的根注解A作为元注解，因此{@link #getAnnotations()}或{@link #getDeclaredAnnotations()}
 * 都将只能获得A。
 *
 * <p>不同层级的元注解可能存在同名的属性，此时，若认为该合成注解X在第0层，则根注解A在第1层，B在第2层......以此类推。
 * 层级越低的注解中离根注解距离近，则属性优先级越高，即遵循“就近原则”。<br />
 * 举个例子：若CBA同时存在属性y，则将X视为C，B或者A时，获得的y属性的值都与最底层元注解A的值保持一致。
 * 同理，不同层级可能会出现相同的元注解，比如A注解存在元注解B，C，但是C又存在元注解B，因此根据就近原则，A上的元注解B将优先于C上的元注解B生效。
 * 若两相同注解处于同一层级，则按照从其上一级“子注解”的{@link AnnotatedElement#getAnnotations()}的调用顺序排序。<br />
 * {@link #getAnnotation(Class)}获得的代理类实例的属性值遵循该规则。
 *
 * <p>别名在合成注解中仍然有效，若注解X中任意属性上存在{@link Alias}注解，则{@link Alias#value()}指定的属性值将会覆盖注解属性的本身的值。<br />
 * {@link Alias}注解仅能指定注解X中存在的属性作为别名，不允许指定元注解或子类注解的属性。
 *
 * @author huangchengxing
 * @see AnnotationUtil
 */
public class SyntheticAnnotation<A extends Annotation> implements Annotation, AnnotatedElement {

	/**
	 * 根注解，即当前查找的注解
	 */
	private final A source;

	/**
	 * 包含根注解以及其元注解在内的全部注解实例
	 */
	private final Map<Class<? extends Annotation>, MetaAnnotation> metaAnnotationMap;

	/**
	 * 属性值缓存
	 */
	private final Map<String, Object> attributeCaches;

	/**
	 * 构造
	 *
	 * @param annotation 当前查找的注解类
	 */
	SyntheticAnnotation(A annotation) {
		this.source = annotation;
		this.metaAnnotationMap = new LinkedHashMap<>();
		this.attributeCaches = new HashMap<>();
		loadMetaAnnotations(); // TODO 是否可以添加注解类对应的元注解信息缓存，避免每次都要解析？
	}

	/**
	 * 基于指定根注解，构建包括其元注解在内的合成注解
	 *
	 * @param rootAnnotation 根注解
	 * @return 合成注解
	 */
	public static <T extends Annotation> SyntheticAnnotation<T> of(T rootAnnotation) {
		return new SyntheticAnnotation<>(rootAnnotation);
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
	 * 获取根注解类型
	 *
	 * @return java.lang.Class<? extends java.lang.annotation.Annotation>
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return getSource().annotationType();
	}

	/**
	 * 获取属性值，若存在{@link Alias}则获取{@link Alias#value()}指定的别名属性的值
	 * <p>当不同层级的注解之间存在同名属性时，将优先获取更接近根注解的属性
	 *
	 * @param attributeName 属性名
	 */
	public Object getAttribute(String attributeName) {
		return attributeCaches.computeIfAbsent(attributeName, a -> metaAnnotationMap.values()
			.stream()
			.filter(ma -> ma.hasAttribute(attributeName)) // 集合默认是根据distance有序的，故此处无需再排序
			.findFirst()
			.map(ma -> ma.getAttribute(attributeName))
			.orElse(null)
		);
	}

	/**
	 * 若合成注解在存在指定元注解，则使用动态代理生成一个对应的注解实例
	 *
	 * @param annotationType 注解类型
	 * @param <T> 注解类型
	 * @return 注解
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
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
	 * 获取全部注解
	 *
	 * @return java.lang.annotation.Annotation[]
	 */
	@Override
	public Annotation[] getAnnotations() {
		return getMetaAnnotationMap().values().toArray(new MetaAnnotation[0]);
	}

	/**
	 * 获取根注解直接声明注解
	 *
	 * @return 直接声明注解
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return new Annotation[]{ getSource() };
	}

	/**
	 * 广度优先遍历并缓存该根注解上的全部元注解
	 */
	private void loadMetaAnnotations() {
		// 若该注解已经是合成注解，则直接使用已解析好的元注解信息
		if (source instanceof SyntheticAnnotation.Synthesized) {
			this.metaAnnotationMap.putAll(((Synthesized)source).getMetaAnnotationMap());
			return;
		}
		// 扫描元注解
		metaAnnotationMap.put(source.annotationType(), new MetaAnnotation(source, 0));
		new MateAnnotationScanner().scan(
			(index, annotation) -> metaAnnotationMap.computeIfAbsent(
				// 当出现重复的注解时，由于后添加的注解必然层级更高，优先级更低，因此当直接忽略
				annotation.annotationType(), t -> new MetaAnnotation(annotation, index)
			),
			source.annotationType(), null
		);
	}

	/**
	 * 元注解包装类
	 *
	 * @author huangchengxing
	 */
	static class MetaAnnotation implements Annotation {

		private final Annotation annotation;
		private final Map<String, Method> attributeMethodCaches;
		private final int distance;

		public MetaAnnotation(Annotation annotation, int distance) {
			this.annotation = annotation;
			this.distance = distance;
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
		 * 获取元注解
		 *
		 * @return 元注解
		 */
		public Annotation get() {
			return annotation;
		}

		/**
		 * 获取根注解到元注解的距离
		 *
		 * @return 根注解到元注解的距离
		 */
		public int getDistance() {
			return distance;
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
		 * 获取元注解的属性值
		 *
		 * @param attributeName 属性名
		 * @return 元注解的属性值
		 */
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
		private final SyntheticAnnotation<?> syntheticAnnotation;

		public SyntheticAnnotationProxy(SyntheticAnnotation<?> syntheticAnnotation, Class<A> annotationType) {
			this.syntheticAnnotation = syntheticAnnotation;
			this.annotationType = annotationType;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return annotationType;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (Synthesized.isMetaAnnotationMapMethod(method)) {
				return syntheticAnnotation.getMetaAnnotationMap();
			}
			if (ReflectUtil.isHashCodeMethod(method)) {
				return getHashCode();
			}
			if (ReflectUtil.isToStringMethod(method)) {
				return getToString();
			}
			return ObjectUtil.defaultIfNull(
				syntheticAnnotation.getAttribute(method.getName()),
				() -> ReflectUtil.invoke(this, method, args)
			);
		}

		/**
		 * 获取toString值
		 *
		 * @return toString值
		 */
		private String getToString() {
			String attributes = Stream.of(annotationType().getDeclaredMethods())
				.filter(AnnotationUtil::isAttributeMethod)
				.map(method -> StrUtil.format("{}={}", method.getName(), syntheticAnnotation.getAttribute(method.getName())))
				.collect(Collectors.joining(", "));
			return StrUtil.format("@{}({})", annotationType().getName(), attributes);
		}

		/**
		 * 获取hashcode值
		 *
		 * @return hashcode值
		 */
		private int getHashCode() {
			return Objects.hash((Object)syntheticAnnotation.getAnnotations());
		}

	}
}
