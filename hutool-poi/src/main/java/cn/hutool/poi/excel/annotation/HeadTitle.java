package cn.hutool.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 字段注解<br>
 * 该注解可用于通过模型对象导出数据到Excel时可通过注解模型字段进行导出标题、顺序、以及是否忽略该字段的控制
 *
 * @author dningcheng
 * @since 5.8.6
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeadTitle {
	/**
	 * 标题
	 */
	String title() default "";
	/**
	 * 顺序
	 */
	int index() default  1;
	/**
	 * 是否忽略
	 */
	boolean ignore() default false;

}
