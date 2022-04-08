package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解代理<br>
 * 通过代理指定注解，可以自定义调用注解的方法逻辑，如支持{@link Alias} 注解
 *
 * @param <T> 注解类型
 * @since 5.7.23
 */
public class AnnotationProxy<T extends Annotation> implements Annotation, InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	private final T annotation;
	private final Class<T> type;
	private final Map<String, Object> attributes;

	/**
	 * 构造
	 *
	 * @param annotation 注解
	 */
	public AnnotationProxy(T annotation) {
		this.annotation = annotation;
		//noinspection unchecked
		this.type = (Class<T>) annotation.annotationType();
		this.attributes = initAttributes();
	}


	@Override
	public Class<? extends Annotation> annotationType() {
		return type;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// 注解别名
		Alias alias = method.getAnnotation(Alias.class);
		if(null != alias){
			final String name = alias.value();
			if(StrUtil.isNotBlank(name)){
				if(false == attributes.containsKey(name)){
					throw new IllegalArgumentException(StrUtil.format("No method for alias: [{}]", name));
				}
				return attributes.get(name);
			}
		}

		final Object value = attributes.get(method.getName());
		if (value != null) {
			return value;
		}
		return method.invoke(this, args);
	}

	/**
	 * 初始化注解的属性<br>
	 * 此方法预先调用所有注解的方法，将注解方法值缓存于attributes中
	 *
	 * @return 属性（方法结果）映射
	 */
	private Map<String, Object> initAttributes() {
		final Method[] methods = ReflectUtil.getMethods(this.type);
		final Map<String, Object> attributes = new HashMap<>(methods.length, 1);

		for (Method method : methods) {
			// 跳过匿名内部类自动生成的方法
			if (method.isSynthetic()) {
				continue;
			}

			attributes.put(method.getName(), ReflectUtil.invoke(this.annotation, method));
		}

		return attributes;
	}
}
