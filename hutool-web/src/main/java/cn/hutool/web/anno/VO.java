package cn.hutool.web.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * VO 字段注解
 *
 * @author 码小瑞
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VO {

    /**
     * 页面ID，用于组织不同页面
     */
    String[] pageIds();

    /**
     * 给返回到页面上的字段取个别名
     */
    String alias() default "";
}
