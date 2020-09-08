package cn.hutool.core.annotation;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ArrayUtil;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 注解工具类<br>
 * 快速获取注解对象、注解值等工具封装
 *
 * @author looly
 * @since 4.0.9
 */
public class AnnotationUtil {

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
	 * @param annotationEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param isToCombination 是否为转换为组合注解
	 * @return 注解对象
	 */
	public static Annotation[] getAnnotations(AnnotatedElement annotationEle, boolean isToCombination) {
		return (null == annotationEle) ? null : (isToCombination ? toCombination(annotationEle) : annotationEle).getAnnotations();
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
}
