package cn.hutool.core.annotation;

import java.lang.annotation.*;

/**
 * <p>{@link Link}的子注解。表示注解的属性与指定的属性互为镜像，通过一个属性将能够获得对方的值。<br>
 * 它们遵循下述规则：
 * <ul>
 *     <li>互为镜像的两个属性，必须同时通过指定模式为{@code MIRROR_FOR}的{@link Link}注解指定对方；</li>
 *     <li>互为镜像的两个属性，类型必须一致；</li>
 *     <li>互为镜像的两个属性在获取值，且两者的值皆不同时，必须且仅允许有一个非默认值，该值被优先返回；</li>
 *     <li>互为镜像的两个属性，在值都为默认值或都不为默认值时，两者的值必须相等；</li>
 * </ul>
 * <b>注意，该注解与{@link Link}、{@link ForceAliasFor}或{@link AliasFor}一起使用时，将只有被声明在最上面的注解会生效</b>
 *
 * @author huangchengxing
 * @see Link
 * @see RelationType#MIRROR_FOR
 */
@Link(type = RelationType.MIRROR_FOR)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface MirrorFor {

	/**
	 * 产生关联的注解类型，当不指定时，默认指注释的属性所在的类
	 *
	 * @return 关联的注解类型
	 */
	@Link(annotation = Link.class, attribute = "annotation", type = RelationType.FORCE_ALIAS_FOR)
	Class<? extends Annotation> annotation() default Annotation.class;

	/**
	 * {@link #annotation()}指定注解中关联的属性
	 *
	 * @return 属性名
	 */
	@Link(annotation = Link.class, attribute = "attribute", type = RelationType.FORCE_ALIAS_FOR)
	String attribute() default "";

}
