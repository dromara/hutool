package cn.hutool.core.annotation;

import cn.hutool.core.annotation.scanner.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	 * 是否为Jdk自带的元注解。<br />
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
	public static boolean isJdkMateAnnotation(Class<? extends Annotation> annotationType) {
		return META_ANNOTATIONS.contains(annotationType);
	}

	/**
	 * 是否不为Jdk自带的元注解。<br />
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
		return !isJdkMateAnnotation(annotationType);
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
	 * @return 注解对象
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
	 * 获取别名支持后的注解
	 *
	 * @param annotationEle  被注解的类
	 * @param annotationType 注解类型Class
	 * @param <T>            注解类型
	 * @return 别名支持后的注解
	 * @since 5.7.23
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getAnnotationAlias(AnnotatedElement annotationEle, Class<T> annotationType) {
		final T annotation = getAnnotation(annotationEle, annotationType);
		return (T) Proxy.newProxyInstance(annotationType.getClassLoader(), new Class[]{annotationType}, new AnnotationProxy<>(annotation));
	}

	/**
	 * 将指定注解实例与其元注解转为合成注解
	 *
	 * @param annotation 注解
	 * @param annotationType 注解类型
	 * @param <T> 注解类型
	 * @return 合成注解
	 * @see SyntheticAnnotation
	 */
	public static <T extends Annotation> T getSynthesisAnnotation(Annotation annotation, Class<T> annotationType) {
		return SyntheticAnnotation.of(annotation).getAnnotation(annotationType);
	}

	/**
	 * 获取元素上所有指定注解
	 * <ul>
	 *     <li>若元素是类，则递归解析全部父类和全部父接口上的注解;</li>
	 *     <li>若元素是方法、属性或注解，则只解析其直接声明的注解;</li>
	 * </ul>
	 *
	 * @param annotatedElement 可注解元素
	 * @param annotationType 注解类型
	 * @param <T> 注解类型
	 * @return 注解
	 * @see SyntheticAnnotation
	 */
	public static <T extends Annotation> List<T> getAllSynthesisAnnotations(AnnotatedElement annotatedElement, Class<T> annotationType) {
		AnnotationScanner[] scanners = new AnnotationScanner[] {
			new MateAnnotationScanner(), new TypeAnnotationScanner(), new MethodAnnotationScanner(), new FieldAnnotationScanner()
		};
		return AnnotationScanner.scanByAnySupported(annotatedElement, scanners).stream()
			.map(SyntheticAnnotation::of)
			.map(annotation -> annotation.getAnnotation(annotationType))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	/**
	 * 方法是否为注解属性方法。 <br />
	 * 方法无参数，且有返回值的方法认为是注解属性的方法。
	 *
	 * @param method 方法
	 */
	static boolean isAttributeMethod(Method method) {
		return method.getParameterCount() == 0 && method.getReturnType() != void.class;
	}

	/**
	 * 获取注解的全部属性值获取方法
	 *
	 * @param annotationType 注解
	 * @return 注解的全部属性值
	 * @throws IllegalArgumentException 当别名属性在注解中不存在，或别名属性的值与原属性的值类型不一致时抛出
	 */
	static Map<String, Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
		// 获取全部注解属性值
		Map<String, Method> attributeMethods = Stream.of(annotationType.getDeclaredMethods())
			.filter(AnnotationUtil::isAttributeMethod)
			.collect(Collectors.toMap(Method::getName, Function.identity()));
		// 处理别名
		attributeMethods.forEach((methodName, method) -> {
			String alias = Opt.ofNullable(method.getAnnotation(Alias.class))
				.map(Alias::value)
				.orElse(null);
			if (ObjectUtil.isNull(alias)) {
				return;
			}
			// 存在别名，则将原本的值替换为别名对应的值
			Assert.isTrue(attributeMethods.containsKey(alias), "No method for alias: [{}]", alias);
			Method aliasAttributeMethod = attributeMethods.get(alias);
			Assert.isTrue(
				ObjectUtil.isNull(aliasAttributeMethod) || ClassUtil.isAssignable(method.getReturnType(), aliasAttributeMethod.getReturnType()),
				"Return type of the alias method [{}] is inconsistent with the original [{}]",
				aliasAttributeMethod.getClass(), method.getParameterTypes()
			);
			attributeMethods.put(methodName, aliasAttributeMethod);
		});
		return attributeMethods;
	}
}
