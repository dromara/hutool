package cn.hutool.core.annotation;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 合成注解代理类
 *
 * @param <A> 代理的注解类型
 * @author huangchengxing
 */
class SyntheticAnnotationProxy<A extends Annotation> implements InvocationHandler {

	private final SyntheticAnnotation<?> syntheticAnnotation;
	private final SynthesizedAnnotation<A> annotation;
	private final Map<String, BiFunction<Method, Object[], Object>> methods;

	SyntheticAnnotationProxy(SyntheticAnnotation<?> syntheticAnnotation, SynthesizedAnnotation<A> annotation) {
		this.syntheticAnnotation = syntheticAnnotation;
		this.annotation = annotation;
		this.methods = new HashMap<>(9);
		loadMethods();
	}

	/**
	 * 创建一个代理注解，生成的代理对象将是{@link SyntheticProxyAnnotation}与指定的注解类的子类。
	 * <ul>
	 *     <li>当作为{@code annotationType}所指定的类型使用时，其属性将通过合成它的{@link SyntheticAnnotation}获取；</li>
	 *     <li>当作为{@link SyntheticProxyAnnotation}或{@link SynthesizedAnnotation}使用时，将可以获得原始注解实例的相关信息；</li>
	 * </ul>
	 *
	 * @param annotationType      注解类型
	 * @param syntheticAnnotation 合成注解
	 * @param <A>                 代理的注解类型
	 * @return 代理注解
	 */
	@SuppressWarnings("unchecked")
	static <A extends Annotation> A create(
		Class<A> annotationType, SyntheticAnnotation<?> syntheticAnnotation) {
		final SynthesizedAnnotation<A> annotation = syntheticAnnotation.getSynthesizedAnnotation(annotationType);
		final SyntheticAnnotationProxy<A> proxyHandler = new SyntheticAnnotationProxy<>(syntheticAnnotation, annotation);
		if (ObjectUtil.isNull(annotation)) {
			return null;
		}
		return (A) Proxy.newProxyInstance(
			annotationType.getClassLoader(),
			new Class[]{annotationType, SyntheticProxyAnnotation.class},
			proxyHandler
		);
	}

	static boolean isProxyAnnotation(Class<?> targetClass) {
		return ClassUtil.isAssignable(SyntheticProxyAnnotation.class, targetClass);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return Opt.ofNullable(methods.get(method.getName()))
			.map(m -> m.apply(method, args))
			.orElseGet(() -> ReflectUtil.invoke(this, method, args));
	}

	// ========================= 代理方法 =========================

	void loadMethods() {
		methods.put("toString", (method, args) -> proxyToString());
		methods.put("hashCode", (method, args) -> proxyHashCode());
		methods.put("getSyntheticAnnotation", (method, args) -> proxyGetSyntheticAnnotation());
		methods.put("getSynthesizedAnnotation", (method, args) -> proxyGetSynthesizedAnnotation());
		methods.put("getRoot", (method, args) -> annotation.getRoot());
		methods.put("isRoot", (method, args) -> annotation.isRoot());
		methods.put("getVerticalDistance", (method, args) -> annotation.getVerticalDistance());
		methods.put("getHorizontalDistance", (method, args) -> annotation.getHorizontalDistance());
		methods.put("hasAttribute", (method, args) -> annotation.hasAttribute((String)args[0], (Class<?>)args[1]));
		methods.put("getAttribute", (method, args) -> annotation.getAttribute((String)args[0]));
		methods.put("annotationType", (method, args) -> annotation.annotationType());
		for (Method declaredMethod : annotation.getAnnotation().annotationType().getDeclaredMethods()) {
			methods.put(declaredMethod.getName(), (method, args) -> proxyAttributeValue(method));
		}
	}

	private String proxyToString() {
		final String attributes = Stream.of(annotation.annotationType().getDeclaredMethods())
			.filter(AnnotationUtil::isAttributeMethod)
			.map(method -> StrUtil.format("{}={}", method.getName(), syntheticAnnotation.getAttribute(method.getName(), method.getReturnType())))
			.collect(Collectors.joining(", "));
		return StrUtil.format("@{}({})", annotation.annotationType().getName(), attributes);
	}

	private int proxyHashCode() {
		return Objects.hash(syntheticAnnotation, annotation);
	}

	private Object proxyGetSyntheticAnnotation() {
		return syntheticAnnotation;
	}

	private Object proxyGetSynthesizedAnnotation() {
		return annotation;
	}

	private Object proxyAttributeValue(Method attributeMethod) {
		return syntheticAnnotation.getAttribute(attributeMethod.getName(), attributeMethod.getReturnType());
	}

	/**
	 * 通过代理类生成的合成注解
	 *
	 * @author huangchengxing
	 */
	interface SyntheticProxyAnnotation<A extends Annotation, T extends SynthesizedAnnotation<?>> extends SynthesizedAnnotation<A> {

		/**
		 * 获取该注解所属的合成注解
		 *
		 * @return 合成注解
		 */
		SyntheticAnnotation<T> getSyntheticAnnotation();

		/**
		 * 获取该代理注解对应的已合成注解
		 *
		 * @return 理注解对应的已合成注解
		 */
		SynthesizedAnnotation<A> getSynthesizedAnnotation();

	}

}
