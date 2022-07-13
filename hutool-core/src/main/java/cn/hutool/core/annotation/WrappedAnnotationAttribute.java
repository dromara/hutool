package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * <p>表示一个被包装过的{@link AnnotationAttribute}，
 * 该实例中的一些方法可能会被代理到另一个注解属性对象中，
 * 从而使得通过原始的注解属性的方法获取到另一注解属性的值。<br>
 * 除了{@link #getValue()}以外，其他方法的返回值应当尽可能与{@link #getOriginal()}
 * 返回的{@link AnnotationAttribute}对象的方法返回值一致。
 *
 * <p>当包装类被包装了多层后，则规则生效优先级按包装的先后顺序倒序排序，
 * 比如a、b互为镜像，此时a、b两属性应当都被{@link MirroredAnnotationAttribute}包装，
 * 若再指定c为a的别名字段，则c、a、b都要在原基础上再次包装一层{@link AliasedAnnotationAttribute}。<br>
 * 此时a、b同时被包装了两层，则执行时，优先执行{@link AliasedAnnotationAttribute}的逻辑，
 * 当该规则不生效时，比如c只有默认值，此时上一次的{@link MirroredAnnotationAttribute}的逻辑才会生效。
 *
 * <p>被包装的{@link AnnotationAttribute}实际结构为一颗二叉树，
 * 当包装类再次被包装时，实际上等于又添加了一个新的根节点，
 * 此时需要同时更新树的全部关联叶子节点。
 *
 * @author huangchengxing
 * @see AnnotationAttribute
 * @see ForceAliasedAnnotationAttribute
 * @see AliasedAnnotationAttribute
 * @see MirroredAnnotationAttribute
 */
public interface WrappedAnnotationAttribute extends AnnotationAttribute {

	// =========================== 新增方法 ===========================

	/**
	 * 获取被包装的{@link AnnotationAttribute}对象，该对象也可能是{@link AnnotationAttribute}
	 *
	 * @return 被包装的{@link AnnotationAttribute}对象
	 */
	AnnotationAttribute getOriginal();

	/**
	 * 获取最初的被包装的{@link AnnotationAttribute}
	 *
	 * @return 最初的被包装的{@link AnnotationAttribute}
	 */
	AnnotationAttribute getNonWrappedOriginal();

	/**
	 * 获取包装{@link #getOriginal()}的{@link AnnotationAttribute}对象，该对象也可能是{@link AnnotationAttribute}
	 *
	 * @return 包装对象
	 */
	AnnotationAttribute getLinked();

	/**
	 * 遍历以当前实例为根节点的树结构，获取所有未被包装的属性
	 *
	 * @return 叶子节点
	 */
	Collection<AnnotationAttribute> getAllLinkedNonWrappedAttributes();

	// =========================== 代理实现 ===========================

	/**
	 * 获取注解对象
	 *
	 * @return 注解对象
	 */
	@Override
	default Annotation getAnnotation() {
		return getOriginal().getAnnotation();
	}

	/**
	 * 获取注解属性对应的方法
	 *
	 * @return 注解属性对应的方法
	 */
	@Override
	default Method getAttribute() {
		return getOriginal().getAttribute();
	}

	/**
	 * 该注解属性的值是否等于默认值 <br>
	 * 默认仅当{@link #getOriginal()}与{@link #getLinked()}返回的注解属性
	 * 都为默认值时，才返回{@code true}
	 *
	 * @return 该注解属性的值是否等于默认值
	 */
	@Override
	boolean isValueEquivalentToDefaultValue();

	/**
	 * 获取属性类型
	 *
	 * @return 属性类型
	 */
	@Override
	default Class<?> getAttributeType() {
		return getOriginal().getAttributeType();
	}

	/**
	 * 获取属性上的注解
	 *
	 * @param annotationType 注解类型
	 * @return 注解对象
	 */
	@Override
	default <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return getOriginal().getAnnotation(annotationType);
	}

	/**
	 * 当前注解属性是否已经被{@link WrappedAnnotationAttribute}包装
	 *
	 * @return boolean
	 */
	@Override
	default boolean isWrapped() {
		return true;
	}

}
