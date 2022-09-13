package cn.hutool.core.annotation;

import java.lang.annotation.*;

/**
 * <p>别名注解，使用此注解的字段、方法、参数等会有一个别名，用于Bean拷贝、Bean转Map等。
 *
 * <p>当在注解中使用时，可为令多个属性互相关联，当对其中任意属性赋值时，
 * 会将属性值一并同步到所有关联的属性中。<br>
 * 该功能参考{@link AnnotatedElementUtil}。
 *
 * @author Looly
 * @since 5.1.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Alias {

	/**
	 * 别名值，即使用此注解要替换成的别名名称
	 *
	 * @return 别名值
	 */
	String value();
}
