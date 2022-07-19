package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 扫描{@link Method}上的注解
 *
 * @author huangchengxing
 */
public class MethodAnnotationScanner extends AbstractTypeAnnotationScanner<MethodAnnotationScanner> implements AnnotationScanner {

	/**
	 * 构造一个类注解扫描器，仅扫描该方法上直接声明的注解
	 */
	public MethodAnnotationScanner() {
		this(false);
	}

	/**
	 * 构造一个类注解扫描器
	 *
	 * @param scanSameSignatureMethod 是否扫描类层级结构中具有相同方法签名的方法
	 */
	public MethodAnnotationScanner(boolean scanSameSignatureMethod) {
		this(scanSameSignatureMethod, targetClass -> true, CollUtil.newLinkedHashSet());
	}

	/**
	 * 构造一个方法注解扫描器
	 *
	 * @param scanSameSignatureMethod 是否扫描类层级结构中具有相同方法签名的方法
	 * @param filter                  过滤器
	 * @param excludeTypes            不包含的类型
	 */
	public MethodAnnotationScanner(boolean scanSameSignatureMethod, Predicate<Class<?>> filter, Set<Class<?>> excludeTypes) {
		super(scanSameSignatureMethod, scanSameSignatureMethod, filter, excludeTypes);
	}

	/**
	 * 构造一个方法注解扫描器
	 *
	 * @param includeSuperClass 是否允许扫描父类中具有相同方法签名的方法
	 * @param includeInterfaces 是否允许扫描父接口中具有相同方法签名的方法
	 * @param filter            过滤器
	 * @param excludeTypes      不包含的类型
	 */
	public MethodAnnotationScanner(boolean includeSuperClass, boolean includeInterfaces, Predicate<Class<?>> filter, Set<Class<?>> excludeTypes) {
		super(includeSuperClass, includeInterfaces, filter, excludeTypes);
	}

	/**
	 * 判断是否支持扫描该注解元素，仅当注解元素是{@link Method}时返回{@code true}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return boolean 是否支持扫描该注解元素
	 */
	@Override
	public boolean support(AnnotatedElement annotatedEle) {
		return annotatedEle instanceof Method;
	}

	/**
	 * 获取声明该方法的类
	 *
	 * @param annotatedElement 注解元素
	 * @return 要递归的类型
	 * @see Method#getDeclaringClass()
	 */
	@Override
	protected Class<?> getClassFormAnnotatedElement(AnnotatedElement annotatedElement) {
		return ((Method)annotatedElement).getDeclaringClass();
	}

	/**
	 * 若父类/父接口中方法具有相同的方法签名，则返回该方法上的注解
	 *
	 * @param source      原始方法
	 * @param index       类的层级索引
	 * @param targetClass 类
	 * @return 最终所需的目标注解
	 */
	@Override
	protected Annotation[] getAnnotationsFromTargetClass(AnnotatedElement source, int index, Class<?> targetClass) {
		final Method sourceMethod = (Method) source;
		return Stream.of(ClassUtil.getDeclaredMethods(targetClass))
			.filter(superMethod -> !superMethod.isBridge())
			.filter(superMethod -> hasSameSignature(sourceMethod, superMethod))
			.map(AnnotatedElement::getAnnotations)
			.flatMap(Stream::of)
			.toArray(Annotation[]::new);
	}

	/**
	 * 设置是否扫描类层级结构中具有相同方法签名的方法
	 *
	 * @param scanSuperMethodIfOverride 是否扫描类层级结构中具有相同方法签名的方法
	 * @return 当前实例
	 */
	public MethodAnnotationScanner setScanSameSignatureMethod(boolean scanSuperMethodIfOverride) {
		setIncludeInterfaces(scanSuperMethodIfOverride);
		setIncludeSuperClass(scanSuperMethodIfOverride);
		return this;
	}

	/**
	 * 该方法是否具备与扫描的方法相同的方法签名
	 */
	private boolean hasSameSignature(Method sourceMethod, Method superMethod) {
		if (false == StrUtil.equals(sourceMethod.getName(), superMethod.getName())) {
			return false;
		}
		final Class<?>[] sourceParameterTypes = sourceMethod.getParameterTypes();
		final Class<?>[] targetParameterTypes = superMethod.getParameterTypes();
		if (sourceParameterTypes.length != targetParameterTypes.length) {
			return false;
		}
		if (!ArrayUtil.containsAll(sourceParameterTypes, targetParameterTypes)) {
			return false;
		}
		return ClassUtil.isAssignable(superMethod.getReturnType(), sourceMethod.getReturnType());
	}

}
