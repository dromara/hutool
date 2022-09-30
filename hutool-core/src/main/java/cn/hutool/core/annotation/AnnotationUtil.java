package cn.hutool.core.annotation;

import cn.hutool.core.annotation.scanner.AnnotationScanner;
import cn.hutool.core.annotation.scanner.MetaAnnotationScanner;
import cn.hutool.core.annotation.scanner.MethodAnnotationScanner;
import cn.hutool.core.annotation.scanner.TypeAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.*;

import java.lang.annotation.*;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 注解工具类<br>
 * 快速获取注解对象、注解值等工具封装
 *
 * @author looly
 * @since 4.0.9
 */
public class AnnotationUtil {

	/**
	 * 元注解
	 */
	static final Set<Class<? extends Annotation>> META_ANNOTATIONS = CollUtil.newHashSet(Target.class, //
			Retention.class, //
			Inherited.class, //
			Documented.class, //
			SuppressWarnings.class, //
			Override.class, //
			Deprecated.class//
	);

	/**
	 * 是否为Jdk自带的元注解。<br>
	 * 包括：
	 * <ul>
	 *     <li>{@link Target}</li>
	 *     <li>{@link Retention}</li>
	 *     <li>{@link Inherited}</li>
	 *     <li>{@link Documented}</li>
	 *     <li>{@link SuppressWarnings}</li>
	 *     <li>{@link Override}</li>
	 *     <li>{@link Deprecated}</li>
	 * </ul>
	 *
	 * @param annotationType 注解类型
	 * @return 是否为Jdk自带的元注解
	 */
	public static boolean isJdkMetaAnnotation(Class<? extends Annotation> annotationType) {
		return META_ANNOTATIONS.contains(annotationType);
	}

	/**
	 * 是否不为Jdk自带的元注解。<br>
	 * 包括：
	 * <ul>
	 *     <li>{@link Target}</li>
	 *     <li>{@link Retention}</li>
	 *     <li>{@link Inherited}</li>
	 *     <li>{@link Documented}</li>
	 *     <li>{@link SuppressWarnings}</li>
	 *     <li>{@link Override}</li>
	 *     <li>{@link Deprecated}</li>
	 * </ul>
	 *
	 * @param annotationType 注解类型
	 * @return 是否为Jdk自带的元注解
	 */
	public static boolean isNotJdkMateAnnotation(Class<? extends Annotation> annotationType) {
		return false == isJdkMetaAnnotation(annotationType);
	}

	/**
	 * 将指定的被注解的元素转换为组合注解元素
	 *
	 * @param annotationEle 注解元素
	 * @return 组合注解元素
	 */
	public static CombinationAnnotationElement toCombination(AnnotatedElement annotationEle) {
		if (annotationEle instanceof CombinationAnnotationElement) {
			return (CombinationAnnotationElement) annotationEle;
		}
		return new CombinationAnnotationElement(annotationEle);
	}

	/**
	 * 获取指定注解
	 *
	 * @param annotationEle   {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param isToCombination 是否为转换为组合注解，组合注解可以递归获取注解的注解
	 * @return 注解对象
	 */
	public static Annotation[] getAnnotations(AnnotatedElement annotationEle, boolean isToCombination) {
		return getAnnotations(annotationEle, isToCombination, (Predicate<Annotation>) null);
	}

	/**
	 * 获取组合注解
	 *
	 * @param <T>            注解类型
	 * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 限定的
	 * @return 注解对象数组
	 * @since 5.8.0
	 */
	public static <T> T[] getCombinationAnnotations(AnnotatedElement annotationEle, Class<T> annotationType) {
		return getAnnotations(annotationEle, true, annotationType);
	}

	/**
	 * 获取指定注解
	 *
	 * @param <T>             注解类型
	 * @param annotationEle   {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param isToCombination 是否为转换为组合注解，组合注解可以递归获取注解的注解
	 * @param annotationType  限定的
	 * @return 注解对象数组
	 * @since 5.8.0
	 */
	public static <T> T[] getAnnotations(AnnotatedElement annotationEle, boolean isToCombination, Class<T> annotationType) {
		final Annotation[] annotations = getAnnotations(annotationEle, isToCombination,
				(annotation -> null == annotationType || annotationType.isAssignableFrom(annotation.getClass())));

		final T[] result = ArrayUtil.newArray(annotationType, annotations.length);
		for (int i = 0; i < annotations.length; i++) {
			//noinspection unchecked
			result[i] = (T) annotations[i];
		}
		return result;
	}

	/**
	 * 获取指定注解
	 *
	 * @param annotationEle   {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param isToCombination 是否为转换为组合注解，组合注解可以递归获取注解的注解
	 * @param predicate       过滤器，{@link Predicate#test(Object)}返回{@code true}保留，否则不保留
	 * @return 注解对象，如果提供的{@link AnnotatedElement}为{@code null}，返回{@code null}
	 * @since 5.8.0
	 */
	public static Annotation[] getAnnotations(AnnotatedElement annotationEle, boolean isToCombination, Predicate<Annotation> predicate) {
		if (null == annotationEle) {
			return null;
		}

		if (isToCombination) {
			if (null == predicate) {
				return toCombination(annotationEle).getAnnotations();
			}
			return CombinationAnnotationElement.of(annotationEle, predicate).getAnnotations();
		}

		final Annotation[] result = annotationEle.getAnnotations();
		if (null == predicate) {
			return result;
		}
		return ArrayUtil.filter(result, predicate::test);
	}

	/**
	 * 获取指定注解
	 *
	 * @param <A>            注解类型
	 * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 注解对象
	 */
	public static <A extends Annotation> A getAnnotation(AnnotatedElement annotationEle, Class<A> annotationType) {
		return (null == annotationEle) ? null : toCombination(annotationEle).getAnnotation(annotationType);
	}

	/**
	 * 检查是否包含指定注解指定注解
	 *
	 * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 是否包含指定注解
	 * @since 5.4.2
	 */
	public static boolean hasAnnotation(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) {
		return null != getAnnotation(annotationEle, annotationType);
	}

	/**
	 * 获取指定注解默认值<br>
	 * 如果无指定的属性方法返回null
	 *
	 * @param <T>            注解值类型
	 * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 */
	public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) throws UtilException {
		return getAnnotationValue(annotationEle, annotationType, "value");
	}

	/**
	 * 获取指定注解属性的值<br>
	 * 如果无指定的属性方法返回null
	 *
	 * @param <T>            注解值类型
	 * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @param propertyName   属性名，例如注解中定义了name()方法，则 此处传入name
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 */
	public static <T> T getAnnotationValue(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType, String propertyName) throws UtilException {
		final Annotation annotation = getAnnotation(annotationEle, annotationType);
		if (null == annotation) {
			return null;
		}

		final Method method = ReflectUtil.getMethodOfObj(annotation, propertyName);
		if (null == method) {
			return null;
		}
		return ReflectUtil.invoke(annotation, method);
	}

	/**
	 * 获取指定注解属性的值<br>
	 * 如果无指定的属性方法返回null
	 *
	 * @param <A>           注解类型
	 * @param <R>           注解类型值
	 * @param annotationEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param propertyName  属性名，例如注解中定义了name()方法，则 此处传入name
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 * @since 5.8.9
	 */
	public static <A extends Annotation, R> R getAnnotationValue(AnnotatedElement annotationEle, Func1<A, R> propertyName) {
		if (propertyName == null) {
			return null;
		} else {
			final SerializedLambda lambda = LambdaUtil.resolve(propertyName);
			final String instantiatedMethodType = lambda.getInstantiatedMethodType();
			final Class<A> annotationClass = ClassUtil.loadClass(StrUtil.sub(instantiatedMethodType, 2, StrUtil.indexOf(instantiatedMethodType, ';')));
			return getAnnotationValue(annotationEle, annotationClass, lambda.getImplMethodName());
		}
	}

	/**
	 * 获取指定注解中所有属性值<br>
	 * 如果无指定的属性方法返回null
	 *
	 * @param annotationEle  {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 */
	public static Map<String, Object> getAnnotationValueMap(AnnotatedElement annotationEle, Class<? extends Annotation> annotationType) throws UtilException {
		final Annotation annotation = getAnnotation(annotationEle, annotationType);
		if (null == annotation) {
			return null;
		}

		final Method[] methods = ReflectUtil.getMethods(annotationType, t -> {
			if (ArrayUtil.isEmpty(t.getParameterTypes())) {
				// 只读取无参方法
				final String name = t.getName();
				// 跳过自有的几个方法
				return (false == "hashCode".equals(name)) //
						&& (false == "toString".equals(name)) //
						&& (false == "annotationType".equals(name));
			}
			return false;
		});

		final HashMap<String, Object> result = new HashMap<>(methods.length, 1);
		for (Method method : methods) {
			result.put(method.getName(), ReflectUtil.invoke(annotation, method));
		}
		return result;
	}

	/**
	 * 获取注解类的保留时间，可选值 SOURCE（源码时），CLASS（编译时），RUNTIME（运行时），默认为 CLASS
	 *
	 * @param annotationType 注解类
	 * @return 保留时间枚举
	 */
	public static RetentionPolicy getRetentionPolicy(Class<? extends Annotation> annotationType) {
		final Retention retention = annotationType.getAnnotation(Retention.class);
		if (null == retention) {
			return RetentionPolicy.CLASS;
		}
		return retention.value();
	}

	/**
	 * 获取注解类可以用来修饰哪些程序元素，如 TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER 等
	 *
	 * @param annotationType 注解类
	 * @return 注解修饰的程序元素数组
	 */
	public static ElementType[] getTargetType(Class<? extends Annotation> annotationType) {
		final Target target = annotationType.getAnnotation(Target.class);
		if (null == target) {
			return new ElementType[]{ElementType.TYPE, //
					ElementType.FIELD, //
					ElementType.METHOD, //
					ElementType.PARAMETER, //
					ElementType.CONSTRUCTOR, //
					ElementType.LOCAL_VARIABLE, //
					ElementType.ANNOTATION_TYPE, //
					ElementType.PACKAGE//
			};
		}
		return target.value();
	}

	/**
	 * 是否会保存到 Javadoc 文档中
	 *
	 * @param annotationType 注解类
	 * @return 是否会保存到 Javadoc 文档中
	 */
	public static boolean isDocumented(Class<? extends Annotation> annotationType) {
		return annotationType.isAnnotationPresent(Documented.class);
	}

	/**
	 * 是否可以被继承，默认为 false
	 *
	 * @param annotationType 注解类
	 * @return 是否会保存到 Javadoc 文档中
	 */
	public static boolean isInherited(Class<? extends Annotation> annotationType) {
		return annotationType.isAnnotationPresent(Inherited.class);
	}

	/**
	 * 扫描注解类，以及注解类的{@link Class}层级结构中的注解，将返回除了{@link #META_ANNOTATIONS}中指定的JDK默认注解外，
	 * 按元注解对象与{@code annotationType}的距离和{@link Class#getAnnotations()}顺序排序的注解对象集合
	 *
	 * <p>比如：<br>
	 * 若{@code annotationType}为 A，且A存在元注解B，B又存在元注解C和D，则有：
	 * <pre>
	 *                              |-&gt; C.class [@a, @b]
	 *     A.class -&gt; B.class [@a] -|
	 *                              |-&gt; D.class [@a, @c]
	 * </pre>
	 * 扫描A，则该方法最终将返回 {@code [@a, @a, @b, @a, @c]}
	 *
	 * @param annotationType 注解类
	 * @return 注解对象集合
	 * @see MetaAnnotationScanner
	 */
	public static List<Annotation> scanMetaAnnotation(Class<? extends Annotation> annotationType) {
		return AnnotationScanner.DIRECTLY_AND_META_ANNOTATION.getAnnotationsIfSupport(annotationType);
	}

	/**
	 * <p>扫描类以及类的{@link Class}层级结构中的注解，将返回除了{@link #META_ANNOTATIONS}中指定的JDK默认元注解外,
	 * 全部类/接口的{@link Class#getAnnotations()}方法返回的注解对象。<br>
	 * 层级结构将按广度优先递归，遵循规则如下：
	 * <ul>
	 *     <li>同一层级中，优先处理父类，然后再处理父接口；</li>
	 *     <li>同一个接口在不同层级出现，优先选择层级距离{@code targetClass}更近的接口；</li>
	 *     <li>同一个接口在相同层级出现，优先选择其子类/子接口被先解析的那个；</li>
	 * </ul>
	 * 注解根据其声明类/接口被扫描的顺序排序，若注解都在同一个{@link Class}中被声明，则还会遵循{@link Class#getAnnotations()}的顺序。
	 *
	 * <p>比如：<br>
	 * 若{@code targetClass}为{@code A.class}，且{@code A.class}存在父类{@code B.class}、父接口{@code C.class}，
	 * 三个类的注解声明情况如下：
	 * <pre>
	 *                   |-&gt; B.class [@a, @b]
	 *     A.class [@a] -|
	 *                   |-&gt; C.class [@a, @c]
	 * </pre>
	 * 则该方法最终将返回 {@code [@a, @a, @b, @a, @c]}
	 *
	 * @param targetClass 类
	 * @return 注解对象集合
	 * @see TypeAnnotationScanner
	 */
	public static List<Annotation> scanClass(Class<?> targetClass) {
		return AnnotationScanner.TYPE_HIERARCHY.getAnnotationsIfSupport(targetClass);
	}

	/**
	 * <p>扫描方法，以及该方法所在类的{@link Class}层级结构中的具有相同方法签名的方法，
	 * 将返回除了{@link #META_ANNOTATIONS}中指定的JDK默认元注解外,
	 * 全部匹配方法上{@link Method#getAnnotations()}方法返回的注解对象。<br>
	 * 方法所在类的层级结构将按广度优先递归，遵循规则如下：
	 * <ul>
	 *     <li>同一层级中，优先处理父类，然后再处理父接口；</li>
	 *     <li>同一个接口在不同层级出现，优先选择层级距离{@code targetClass}更近的接口；</li>
	 *     <li>同一个接口在相同层级出现，优先选择其子类/子接口被先解析的那个；</li>
	 * </ul>
	 * 方法上的注解根据方法的声明类/接口被扫描的顺序排序，若注解都在同一个类的同一个方法中被声明，则还会遵循{@link Method#getAnnotations()}的顺序。
	 *
	 * <p>比如：<br>
	 * 若方法X声明于{@code A.class}，且重载/重写自父类{@code B.class}，并且父类中的方法X由重写至其实现的接口{@code C.class}，
	 * 三个类的注解声明情况如下：
	 * <pre>
	 *     A#X()[@a] -&gt; B#X()[@b] -&gt; C#X()[@c]
	 * </pre>
	 * 则该方法最终将返回 {@code [@a, @b, @c]}
	 *
	 * @param method 方法
	 * @return 注解对象集合
	 * @see MethodAnnotationScanner
	 */
	public static List<Annotation> scanMethod(Method method) {
		return AnnotationScanner.TYPE_HIERARCHY.getAnnotationsIfSupport(method);
	}

	/**
	 * 设置新的注解的属性（字段）值
	 *
	 * @param annotation      注解对象
	 * @param annotationField 注解属性（字段）名称
	 * @param value           要更新的属性值
	 * @since 5.5.2
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void setValue(Annotation annotation, String annotationField, Object value) {
		final Map memberValues = (Map) ReflectUtil.getFieldValue(Proxy.getInvocationHandler(annotation), "memberValues");
		memberValues.put(annotationField, value);
	}

	/**
	 * 该注解对象是否为通过代理类生成的合成注解
	 *
	 * @param annotation 注解对象
	 * @return 是否
	 * @see SynthesizedAnnotationProxy#isProxyAnnotation(Class)
	 */
	public static boolean isSynthesizedAnnotation(Annotation annotation) {
		return SynthesizedAnnotationProxy.isProxyAnnotation(annotation.getClass());
	}

	/**
	 * 获取别名支持后的注解
	 *
	 * @param annotationEle  被注解的类
	 * @param annotationType 注解类型Class
	 * @param <T>            注解类型
	 * @return 别名支持后的注解
	 * @since 5.7.23
	 */
	public static <T extends Annotation> T getAnnotationAlias(AnnotatedElement annotationEle, Class<T> annotationType) {
		final T annotation = getAnnotation(annotationEle, annotationType);
		return aggregatingFromAnnotation(annotation).synthesize(annotationType);
	}

	/**
	 * 将指定注解实例与其元注解转为合成注解
	 *
	 * @param annotationType 注解类
	 * @param annotations    注解对象
	 * @param <T>            注解类型
	 * @return 合成注解
	 * @see SynthesizedAggregateAnnotation
	 */
	public static <T extends Annotation> T getSynthesizedAnnotation(Class<T> annotationType, Annotation... annotations) {
		// TODO 缓存合成注解信息，避免重复解析
		return Opt.ofNullable(annotations)
				.filter(ArrayUtil::isNotEmpty)
				.map(AnnotationUtil::aggregatingFromAnnotationWithMeta)
				.map(a -> a.synthesize(annotationType))
				.get();
	}

	/**
	 * <p>获取元素上距离指定元素最接近的合成注解
	 * <ul>
	 *     <li>若元素是类，则递归解析全部父类和全部父接口上的注解;</li>
	 *     <li>若元素是方法、属性或注解，则只解析其直接声明的注解;</li>
	 * </ul>
	 *
	 * <p>注解合成规则如下：
	 * 若{@code AnnotatedEle}按顺序从上到下声明了A，B，C三个注解，且三注解存在元注解如下：
	 * <pre>
	 *    A -&gt; M3
	 *    B -&gt; M1 -&gt; M2 -&gt; M3
	 *    C -&gt; M2 -&gt; M3
	 * </pre>
	 * 此时入参{@code annotationType}类型为{@code M2}，则最终将优先返回基于根注解B合成的合成注解
	 *
	 * @param annotatedEle   {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类
	 * @param <T>            注解类型
	 * @return 合成注解
	 * @see SynthesizedAggregateAnnotation
	 */
	public static <T extends Annotation> T getSynthesizedAnnotation(AnnotatedElement annotatedEle, Class<T> annotationType) {
		T target = annotatedEle.getAnnotation(annotationType);
		if (ObjectUtil.isNotNull(target)) {
			return target;
		}
		return AnnotationScanner.DIRECTLY
				.getAnnotationsIfSupport(annotatedEle).stream()
				.map(annotation -> getSynthesizedAnnotation(annotationType, annotation))
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}

	/**
	 * 获取元素上所有指定注解
	 * <ul>
	 *     <li>若元素是类，则递归解析全部父类和全部父接口上的注解;</li>
	 *     <li>若元素是方法、属性或注解，则只解析其直接声明的注解;</li>
	 * </ul>
	 *
	 * <p>注解合成规则如下：
	 * 若{@code AnnotatedEle}按顺序从上到下声明了A，B，C三个注解，且三注解存在元注解如下：
	 * <pre>
	 *    A -&gt; M1 -&gt; M2
	 *    B -&gt; M3 -&gt; M1 -&gt; M2
	 *    C -&gt; M2
	 * </pre>
	 * 此时入参{@code annotationType}类型为{@code M1}，则最终将返回基于根注解A与根注解B合成的合成注解。
	 *
	 * @param annotatedEle   {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类
	 * @param <T>            注解类型
	 * @return 合成注解
	 * @see SynthesizedAggregateAnnotation
	 */
	public static <T extends Annotation> List<T> getAllSynthesizedAnnotations(AnnotatedElement annotatedEle, Class<T> annotationType) {
		return AnnotationScanner.DIRECTLY
				.getAnnotationsIfSupport(annotatedEle).stream()
				.map(annotation -> getSynthesizedAnnotation(annotationType, annotation))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * 对指定注解对象进行聚合
	 *
	 * @param annotations 注解对象
	 * @return 聚合注解
	 */
	public static SynthesizedAggregateAnnotation aggregatingFromAnnotation(Annotation... annotations) {
		return new GenericSynthesizedAggregateAnnotation(Arrays.asList(annotations), AnnotationScanner.NOTHING);
	}

	/**
	 * 对指定注解对象及其元注解进行聚合
	 *
	 * @param annotations 注解对象
	 * @return 聚合注解
	 */
	public static SynthesizedAggregateAnnotation aggregatingFromAnnotationWithMeta(Annotation... annotations) {
		return new GenericSynthesizedAggregateAnnotation(Arrays.asList(annotations), AnnotationScanner.DIRECTLY_AND_META_ANNOTATION);
	}

	/**
	 * 方法是否为注解属性方法。 <br>
	 * 方法无参数，且有返回值的方法认为是注解属性的方法。
	 *
	 * @param method 方法
	 */
	static boolean isAttributeMethod(Method method) {
		return method.getParameterCount() == 0 && method.getReturnType() != void.class;
	}

}
