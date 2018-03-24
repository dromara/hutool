package cn.hutool.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Filter;

/**
 * 注解工具类<br>
 * 快速获取注解对象、注解值等工具封装
 * 
 * @author looly
 * @since 4.0.9
 */
public class AnnotationUtil {
	
	/**
	 * 获取指定注解
	 * 
	 * @param accessibleObject {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 注解对象
	 */
	public static Annotation[] getAnnotations(AccessibleObject accessibleObject) {
		return (null == accessibleObject) ? null : accessibleObject.getAnnotations();
	}

	/**
	 * 获取指定注解
	 * 
	 * @param <A> 注解类型
	 * @param accessibleObject {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 注解对象
	 */
	public static <A extends Annotation> A getAnnotation(AccessibleObject accessibleObject, Class<A> annotationType) {
		return (null == accessibleObject) ? null : accessibleObject.getAnnotation(annotationType);
	}

	/**
	 * 获取指定注解默认值<br>
	 * 如果无指定的属性方法返回null
	 * 
	 * @param <T> 注解值类型
	 * @param accessibleObject {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 */
	public static <T> T getAnnotationValue(AccessibleObject accessibleObject, Class<? extends Annotation> annotationType) throws UtilException {
		return getAnnotationValue(accessibleObject, annotationType, "value");
	}

	/**
	 * 获取指定注解属性的值<br>
	 * 如果无指定的属性方法返回null
	 * 
	 * @param <T> 注解值类型
	 * @param accessibleObject {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @param propertyName 属性名，例如注解中定义了name()方法，则 此处传入name
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 */
	public static <T> T getAnnotationValue(AccessibleObject accessibleObject, Class<? extends Annotation> annotationType, String propertyName) throws UtilException {
		final Annotation annotation = getAnnotation(accessibleObject, annotationType);
		if (null == annotation) {
			return null;
		}

		final Method method = ReflectUtil.getMethodOfObj(accessibleObject, propertyName);
		if (null == method) {
			return null;
		}
		return ReflectUtil.invoke(accessibleObject, method);
	}
	
	/**
	 * 获取指定注解中所有属性值<br>
	 * 如果无指定的属性方法返回null
	 * 
	 * @param <T> 注解值类型
	 * @param accessibleObject {@link AccessibleObject}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param annotationType 注解类型
	 * @return 注解对象
	 * @throws UtilException 调用注解中的方法时执行异常
	 */
	public static Map<String, Object> getAnnotationValueMap(AccessibleObject accessibleObject, Class<? extends Annotation> annotationType) throws UtilException {
		final Annotation annotation = getAnnotation(accessibleObject, annotationType);
		if (null == annotation) {
			return null;
		}

		final Method[] methods = ReflectUtil.getMethods(annotationType, new Filter<Method>() {
			@Override
			public boolean accept(Method t) {
				if(ArrayUtil.isEmpty(t.getParameterTypes())) {
					//只读取无参方法
					final String name = t.getName();
					if("hashCode".equals(name) || "toString".equals(name) || "annotationType".equals(name)) {
						//跳过自有的几个方法
						return false;
					}
					return true;
				}
				return false;
			}
		});
		
		final HashMap<String, Object> result = new HashMap<>(methods.length, 1);
		for (Method method : methods) {
			result.put(method.getName(), ReflectUtil.invoke(annotation, method));
		}
		return result;
	}
}
