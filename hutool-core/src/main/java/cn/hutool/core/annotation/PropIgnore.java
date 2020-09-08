package cn.hutool.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性忽略注解，使用此注解的字段等会被忽略，主要用于Bean拷贝、Bean转Map等<br>
 * 此注解应用于字段时，忽略读取和设置属性值，应用于setXXX方法忽略设置值，应用于getXXX忽略读取值
 *
 * @author Looly
 * @since 5.4.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface PropIgnore {

}
