package cn.hutool.core.annotation;

import java.lang.annotation.*;

/**
 * <p>用于在同一注解中，或具有一定关联的不同注解的属性中，表明这些属性之间具有特定的关联关系。
 * 在通过{@link SyntheticAnnotation}获取合成注解后，合成注解获取属性值时会根据该注解进行调整。<br />
 * <b>注意：该注解的优先级低于{@link Alias}
 *
 * @author huangchengxing
 * @see SyntheticAnnotation
 * @see RelationType
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Link {

	/**
	 * 产生关联的注解类型，当不指定时，默认指注释的属性所在的类
	 */
	Class<? extends Annotation> annotation() default Annotation.class;

	/**
	 * {@link #annotation()}指定注解中关联的属性
	 */
	String attribute() default "";

	/**
	 * {@link #attribute()}指定属性与当前注解的属性建的关联关系类型
	 */
	RelationType type() default RelationType.MIRROR_FOR;

}
