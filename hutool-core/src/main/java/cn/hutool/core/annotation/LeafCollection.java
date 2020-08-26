package cn.hutool.core.annotation;

import java.lang.annotation.*;

/**
 * 叶容器-当对象存在树型结构,通过此注解标注属性告知存储位置
 * 一般使用容器存储Set,List等
 *
 * @author shadow
 * @version 5.4.1
 * @since 5.4.1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LeafCollection {

}
