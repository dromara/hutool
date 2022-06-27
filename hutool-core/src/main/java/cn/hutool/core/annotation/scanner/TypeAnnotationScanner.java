package cn.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 扫描{@link Class}上的注解
 *
 * @author huangchengxing
 */
public class TypeAnnotationScanner implements AnnotationScanner {

	/**
	 * 是否允许扫描父类
	 */
	private boolean includeSupperClass;

	/**
	 * 是否允许扫描父接口
	 */
	private boolean includeInterfaces;

	/**
	 * 过滤器，若类型无法通过该过滤器，则该类型及其树结构将直接不被查找
	 */
	private Predicate<Class<?>> filter;

	/**
	 * 排除的类型，以上类型及其树结构将直接不被查找
	 */
	private final Set<Class<?>> excludeTypes;

	/**
	 * 转换器
	 */
	private final List<UnaryOperator<Class<?>>> converters;

	/**
	 * 是否有转换器
	 */
	private boolean hasConverters;

	/**
	 * 构造一个类注解扫描器
	 *
	 * @param includeSupperClass 是否允许扫描父类
	 * @param includeInterfaces 是否允许扫描父接口
	 */
	public TypeAnnotationScanner(boolean includeSupperClass, boolean includeInterfaces, Predicate<Class<?>> filter, Set<Class<?>> excludeTypes) {
		Assert.notNull(filter, "filter must not null");
		Assert.notNull(excludeTypes, "excludeTypes must not null");
		this.includeSupperClass = includeSupperClass;
		this.includeInterfaces = includeInterfaces;
		this.filter = filter;
		this.excludeTypes = excludeTypes;
		this.converters = new ArrayList<>();
	}

	/**
	 * 构建一个类注解扫描器，默认允许扫描指定元素的父类以及父接口
	 */
	public TypeAnnotationScanner() {
		this(true, true, t -> true, CollUtil.newHashSet());
	}

	/**
	 * 是否允许扫描父类
	 *
	 * @return 是否允许扫描父类
	 */
	public boolean isIncludeSupperClass() {
		return includeSupperClass;
	}

	/**
	 * 是否允许扫描父接口
	 *
	 * @return 是否允许扫描父接口
	 */
	public boolean isIncludeInterfaces() {
		return includeInterfaces;
	}

	/**
	 * 设置过滤器，若类型无法通过该过滤器，则该类型及其树结构将直接不被查找
	 *
	 * @param filter 过滤器
	 * @return 当前实例
	 */
	public TypeAnnotationScanner setFilter(Predicate<Class<?>> filter) {
		Assert.notNull(filter, "filter must not null");
		this.filter = filter;
		return this;
	}

	/**
	 * 添加不扫描的类型，该类型及其树结构将直接不被查找
	 *
	 * @param excludeTypes 不扫描的类型
	 * @return 当前实例
	 */
	public TypeAnnotationScanner addExcludeTypes(Class<?>... excludeTypes) {
		CollUtil.addAll(this.excludeTypes, excludeTypes);
		return this;
	}

	/**
	 * 添加转换器
	 *
	 * @param converter 转换器
	 * @return 当前实例
	 * @see JdkProxyClassConverter
	 */
	public TypeAnnotationScanner addConverters(UnaryOperator<Class<?>> converter) {
		Assert.notNull(converter, "converter must not null");
		this.converters.add(converter);
		if (!this.hasConverters) {
			this.hasConverters = true;
		}
		return this;
	}

	/**
	 * 是否允许扫描父类
	 *
	 * @param includeSupperClass 是否
	 * @return 当前实例
	 */
	public TypeAnnotationScanner setIncludeSupperClass(boolean includeSupperClass) {
		this.includeSupperClass = includeSupperClass;
		return this;
	}

	/**
	 * 是否允许扫描父接口
	 *
	 * @param includeInterfaces 是否
	 * @return 当前实例
	 */
	public TypeAnnotationScanner setIncludeInterfaces(boolean includeInterfaces) {
		this.includeInterfaces = includeInterfaces;
		return this;
	}

	@Override
	public boolean support(AnnotatedElement annotatedElement) {
		return annotatedElement instanceof Class;
	}

	@Override
	public List<Annotation> getAnnotations(AnnotatedElement annotatedElement) {
		return scan((Class<?>)annotatedElement).stream()
			.map(Class::getAnnotations)
			.flatMap(Stream::of)
			.filter(a -> !AnnotationUtil.isJdkMetaAnnotation(a.annotationType()))
			.collect(Collectors.toList());
	}

	private Class<?> convert(Class<?> target) {
		if (hasConverters) {
			converters.forEach(c -> c.apply(target));
		}
		return target;
	}

	/**
	 * 递归遍历当前类、父类及其实现的父接口
	 *
	 * @param targetClass 类
	 */
	private Set<Class<?>> scan(Class<?> targetClass) {
		Deque<Class<?>> classDeque = CollUtil.newLinkedList(targetClass);
		Set<Class<?>> accessedTypes = new HashSet<>();
		while (!classDeque.isEmpty()) {
			Class<?> target = convert(classDeque.removeFirst());
			// 若当前类已经访问过，则无需再次处理
			if (ObjectUtil.isNull(target) || accessedTypes.contains(target) || excludeTypes.contains(target) || filter.negate().test(target)) {
				continue;
			}
			accessedTypes.add(target);

			// 扫描父类
			if (includeSupperClass) {
				Class<?> superClass = target.getSuperclass();
				if (!ObjectUtil.equals(superClass, Object.class) && ObjectUtil.isNotNull(superClass)) {
					classDeque.addLast(superClass);
				}
			}

			// 扫描接口
			if (includeInterfaces) {
				Class<?>[] interfaces = target.getInterfaces();
				if (ArrayUtil.isNotEmpty(interfaces)) {
					CollUtil.addAll(classDeque, interfaces);
				}
			}
		}
		return accessedTypes;
	}

	/**
	 * 若类型为jdk代理类，则尝试转换为原始被代理类
	 */
	public static class JdkProxyClassConverter implements UnaryOperator<Class<?>> {

		@Override
		public Class<?> apply(Class<?> sourceClass) {
			return Proxy.isProxyClass(sourceClass) ? sourceClass.getSuperclass() : sourceClass;
		}
	}

}
