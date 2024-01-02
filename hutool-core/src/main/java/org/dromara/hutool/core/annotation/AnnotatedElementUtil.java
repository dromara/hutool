/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.annotation;

import org.dromara.hutool.core.annotation.elements.HierarchicalAnnotatedElements;
import org.dromara.hutool.core.annotation.elements.MetaAnnotatedElement;
import org.dromara.hutool.core.annotation.elements.RepeatableMetaAnnotatedElement;
import org.dromara.hutool.core.map.WeakConcurrentMap;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>{@link AnnotatedElement}工具类，提供对层级结构中{@link AnnotatedElement}上注解及元注解的访问支持，
 * 并提供诸如基于{@link Alias}的属性别名、基于父子注解间的属性值覆盖等特殊的属性映射机制支持。
 *
 * <p><strong>搜索层级结构</strong>
 * <p>参考 Spring 中{@code AnnotatedElementUtils}，
 * 工具类提供<em>get</em>以及<em>find</em>两种语义的搜索：
 * <ul>
 *     <li><em>get</em>：表示搜索范围仅限于指定的{@link AnnotatedElement}本身；</li>
 *     <li>
 *         <em>find</em>：表示搜索范围除了指定的{@link AnnotatedElement}本身外，
 *         若{@link AnnotatedElement}是类，则还会搜索其所有关联的父类和父接口；
 *         若{@link AnnotatedElement}是方法，则还会搜索其声明类关联的所有父类和父接口中，与该方法具有相同方法签名的方法对象；<br>
 *     </li>
 * </ul>
 * eg: <br>
 * 若类<em>A</em>分别有父类和父接口<em>B</em>、<em>C</em>，
 * 则通过<em>getXXX</em>方法将只能获得<em>A</em>上的注解，
 * 而通过<em>findXXX</em>方法将能获得<em>A</em>、<em>B</em>、<em>C</em>上的注解。
 *
 * <p><strong>搜索元注解</strong>
 * <p>工具类支持搜索注解的元注解。在所有格式为<em>getXXX</em>或<em>findXXX</em>的静态方法中，
 * 若不带有<em>directly</em>关键字，则该方法支持搜索元注解，否则不支持搜索元注解。<br>
 * eg: <br>
 * 若类<em>A</em>分别有父类和父接口<em>B</em>、<em>C</em>，上面分别有注解<em>X</em>与其元注解<em>Y</em>，
 * 则此时基于<em>A</em>有：
 * <ul>
 *     <li><em>getDirectlyXXX</em>：能够获得<em>A</em>上的注解<em>X</em>；</li>
 *     <li><em>getXXX</em>：能够获得<em>A</em>上的注解<em>X</em>及元注解<em>Y</em>；</li>
 *     <li><em>findDirectlyXXX</em>：能够分别获得<em>A</em>、<em>B</em>、<em>C</em>上的注解<em>X</em>；</li>
 *     <li><em>findXXX</em>：能够分别获得<em>A</em>、<em>B</em>、<em>C</em>上的注解<em>X</em>及元注解<em>Y</em>；</li>
 * </ul>
 * 注意：在当前实例中将无视{@link Inherited}的效果，即通过<em>directly</em>方法将无法获得父类上带有{@link Inherited}的注解。
 *
 * <p><strong>注解属性映射</strong>
 * <p>工具类支持注解对象属性上的一些属性映射机制，即当注解被扫描时，
 * 将根据一些属性映射机制“解析”为其他类型的属性，这里支持的机制包括：
 * <ul>
 *     <li>
 *         基于{@link Alias}的属性别名：若注解属性通过{@link Alias}互相关联，则对其中任意属性赋值，则等同于对所有关联属性赋值；<br>
 *         eg：
 *         <pre><code>
 *          // set aliased attributes
 *         {@literal @}interface FooAnnotation {
 *             {@literal @}Alias("alias")
 *              default String value() default "";
 *             {@literal @}Alias("value")
 *              default String alias() default "";
 *          }
 *         {@literal @}FooAnnotation("foo")
 *          class Foo { }
 *
 *          // get resolved annotation
 *          FooAnnotation annotation = getResolvedAnnotation(Foo.class, FooAnnotation.class);
 *          annotation.value(); // = "foo"
 *          annotation.alias(); // = "foo"
 *         }</code></pre>
 *     </li>
 *     <li>
 *         基于父子注解的属性覆写：若子注解中存在属性，与其元注解的属性名称、类型皆相同，则子注解的属性值将会覆写其元注解的属性值，
 *         若被覆写的属性值存在关联别名，则关联别名也会被一并覆写。<br>
 *         eg：
 *         <pre><code>
 *         {@literal @}interface Meta {
 *              default String value() default "";
 *          }
 *         {@literal @}Meta("meta")
 *          {@literal @}interface Root {
 *              default String value() default ""; // overwrite for @Meta.value
 *          }
 *         {@literal @}Root("foo")
 *          class Foo { }
 *
 *          // get resolved annotation
 *          Meta meta = getResolvedAnnotation(Foo.class, Meta.class);
 *          meta.value(); // = "foo"
 *          Root root = getResolvedAnnotation(Foo.class, Root.class);
 *          root.value(); // = "foo"
 *         </code></pre>
 *     </li>
 * </ul>
 *
 * <p><strong>可重复注解支持</strong>
 * <p>工具类中格式为<em>findAllXXX</em>或<em>getAllXXX</em>格式的方法，
 * 支持获得{@link AnnotatedElement}上的可重复注解。
 * 此处的可重复注解定义包括两方面：
 * <ul>
 *     <li>
 *         若{@link AnnotatedElement}存在直接声明的注解，该注解有且仅有一个<em>value</em>属性，
 *         该属性类型为注解数组，且数组中注解被{@link java.lang.annotation.Repeatable}注解，
 *         则认为被包括的注解为可重复注解；<br>
 *         eg:<br>
 *         A上存在注解<em>X</em>，该注解是一个容器注解，内部包含可重复注解<em>Y</em>，
 *         解析<em>X</em>后，得到注解<em>X</em>与它包含的可重复注解<em>Y</em>；
 *     </li>
 *     <li>
 *         若{@link AnnotatedElement}存在直接声明的注解，该注解与其他根注解皆有相同的元注解，
 *         则获得元注解时，可以获得多个该相同的元注解。<br>
 *         eg:<br>
 *         A上存在注解<em>X</em>、<em>Y</em>，两者皆有元注解<em>Z</em>，
 *         则通过{@link AnnotatedElement}可以获得两个<em>Z</em>
 *     </li>
 * </ul>
 *
 * <p><strong>缓存</strong>
 * <p>为了避免注解以及{@link AnnotatedElement}层级结构解析过程中的大量反射调用，
 * 工具类为{@link AnnotatedElement}及其元注解信息进行了缓存。<br>
 * 缓存功能默认基于{@link WeakConcurrentMap}实现，会在gc时自动回收部分缓存数据。
 * 但是若有必要，也可以调用{@link #clearCaches()}方法主动清空缓存。
 *
 * @author huangchengxing
 * @see ResolvedAnnotationMapping
 * @see GenericAnnotationMapping
 * @see HierarchicalAnnotatedElements
 * @see RepeatableMetaAnnotatedElement
 * @see MetaAnnotatedElement
 * @see RepeatableAnnotationCollector
 * @since 6.0.0
 */
public class AnnotatedElementUtil {

	/**
	 * 支持属性解析的{@link MetaAnnotatedElement}缓存
	 */
	private static final Map<AnnotatedElement, MetaAnnotatedElement<ResolvedAnnotationMapping>> RESOLVED_ELEMENT_CACHE = new WeakConcurrentMap<>();

	/**
	 * 不支持属性解析的{@link MetaAnnotatedElement}缓存
	 */
	private static final Map<AnnotatedElement, MetaAnnotatedElement<GenericAnnotationMapping>> ELEMENT_CACHE = new WeakConcurrentMap<>();

	/**
	 * 不支持属性解析的{@link RepeatableMetaAnnotatedElement}缓存
	 */
	private static final Map<AnnotatedElement, RepeatableMetaAnnotatedElement<ResolvedAnnotationMapping>> RESOLVED_REPEATABLE_ELEMENT_CACHE = new WeakConcurrentMap<>();

	/**
	 * 不支持属性解析的{@link RepeatableMetaAnnotatedElement}缓存
	 */
	private static final Map<AnnotatedElement, RepeatableMetaAnnotatedElement<GenericAnnotationMapping>> REPEATABLE_ELEMENT_CACHE = new WeakConcurrentMap<>();

	// region ========== find ==========

	/**
	 * 在{@code element}所处层级结构的所有{@link AnnotatedElement}上，是否存在该类型的注解或元注解
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	public static boolean isAnnotated(final AnnotatedElement element, final Class<? extends Annotation> annotationType) {
		return toHierarchyMetaElement(element, false)
			.isAnnotationPresent(annotationType);
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上，获取该类型的注解或元注解
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T findAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyMetaElement(element, false)
			.getAnnotation(annotationType);
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上直接声明的注解、
	 * 这些注解包含的可重复注解，以及上述所有注解的元注解中获取指定类型注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] findAllAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyRepeatableMetaElement(element, false)
			.getAnnotationsByType(annotationType);
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上，获取所有的注解或元注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] findAnnotations(final AnnotatedElement element) {
		return toHierarchyMetaElement(element, false)
			.getAnnotations();
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上，获取所有的注解或元注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名、及子注解对元注解中同名同类型属性进行覆写的特殊机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T findResolvedAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyMetaElement(element, true)
			.getAnnotation(annotationType);
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上，获取所有的注解或元注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名、及子注解对元注解中同名同类型属性进行覆写的特殊机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] findResolvedAnnotations(final AnnotatedElement element) {
		return toHierarchyMetaElement(element, true)
			.getAnnotations();
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上直接声明的注解、
	 * 这些注解包含的可重复注解，以及上述所有注解的元注解中获取指定类型注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名、及子注解对元注解中同名同类型属性进行覆写的特殊机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] findAllResolvedAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyRepeatableMetaElement(element, true)
			.getAnnotationsByType(annotationType);
	}

	// endregion

	// region ========== find & direct ==========

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上获取该类型的注解
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T findDirectlyAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyMetaElement(element, false)
			.getDeclaredAnnotation(annotationType);
	}

	/**
	 * 从{@code element}上直接声明的注解、这些注解包含的可重复注解，
	 * 以及上述所有注解的元注解中获取指定类型注解。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] findAllDirectlyAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyRepeatableMetaElement(element, false)
			.getDeclaredAnnotationsByType(annotationType);
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上获取所有的注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] findDirectlyAnnotations(final AnnotatedElement element) {
		return toHierarchyMetaElement(element, false)
			.getDeclaredAnnotations();
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上，获取所有的注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名、及子注解对元注解中同名同类型属性进行覆写的特殊机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T findDirectlyResolvedAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyMetaElement(element, true)
			.getDeclaredAnnotation(annotationType);
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上获取所有的注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名、及子注解对元注解中同名同类型属性进行覆写的特殊机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] findDirectlyResolvedAnnotations(final AnnotatedElement element) {
		return toHierarchyMetaElement(element, true)
			.getDeclaredAnnotations();
	}

	/**
	 * 从{@code element}所处层级结构的所有{@link AnnotatedElement}上直接声明的注解、
	 * 这些注解包含的可重复注解，以及上述所有注解的元注解中获取指定类型注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名、及子注解对元注解中同名同类型属性进行覆写的特殊机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] findAllDirectlyResolvedAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toHierarchyRepeatableMetaElement(element, true)
			.getDeclaredAnnotationsByType(annotationType);
	}

	// endregion

	// region ========== get ==========

	/**
	 * 在{@code element}上，是否存在该类型的注解或元注解
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	public static boolean isAnnotationPresent(final AnnotatedElement element, final Class<? extends Annotation> annotationType) {
		return toMetaElement(element, false)
			.isAnnotationPresent(annotationType);
	}

	/**
	 * 从{@code element}上，获取该类型的注解或元注解
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T getAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toMetaElement(element, false)
			.getAnnotation(annotationType);
	}

	/**
	 * 从{@code element}上，获取所有的注解或元注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] getAnnotations(final AnnotatedElement element) {
		return toMetaElement(element, false)
			.getAnnotations();
	}

	/**
	 * 从{@code element}上直接声明的注解、这些注解包含的可重复注解，以及上述所有注解的元注解中获取指定类型注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] getAllAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toRepeatableMetaElement(element, false)
			.getAnnotationsByType(annotationType);
	}

	/**
	 * 从{@code element}上，获取所有的注解或元注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T getResolvedAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toMetaElement(element, true)
			.getAnnotation(annotationType);
	}

	/**
	 * 从{@code element}上，获取所有的注解或元注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] getResolvedAnnotations(final AnnotatedElement element) {
		return toMetaElement(element, true)
			.getAnnotations();
	}

	/**
	 * 从{@code element}上直接声明的注解、这些注解包含的可重复注解，以及上述所有注解的元注解中获取指定类型注解<br>
	 * 得到的注解支持基于{@link Alias}的别名机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] getAllResolvedAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toRepeatableMetaElement(element, true)
			.getAnnotationsByType(annotationType);
	}

	// endregion

	// region ========== get & direct ==========

	/**
	 * 从{@code element}上获取该类型的注解
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T getDirectlyAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toMetaElement(element, false)
			.getDeclaredAnnotation(annotationType);
	}

	/**
	 * 从{@code element}上直接声明的注解、这些注解包含的可重复注解中获取指定类型注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] getAllDirectlyAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toRepeatableMetaElement(element, false)
			.getDeclaredAnnotationsByType(annotationType);
	}

	/**
	 * 从{@code element}上获取所有的注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] getDirectlyAnnotations(final AnnotatedElement element) {
		return toMetaElement(element, false)
			.getDeclaredAnnotations();
	}

	/**
	 * 从{@code element}上，获取所有的注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名机制。
	 *
	 * @param element        {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T getDirectlyResolvedAnnotation(final AnnotatedElement element, final Class<T> annotationType) {
		return toMetaElement(element, true)
			.getDeclaredAnnotation(annotationType);
	}

	/**
	 * 从{@code element}上，获取所有的注解。<br>
	 * 得到的注解支持基于{@link Alias}的别名机制。
	 *
	 * @param element {@link AnnotatedElement}
	 * @return 注解对象
	 */
	public static Annotation[] getDirectlyResolvedAnnotations(final AnnotatedElement element) {
		return toMetaElement(element, true)
			.getDeclaredAnnotations();
	}

	/**
	 * 从{@code element}上直接声明的注解、这些注解包含的可重复注解中获取指定类型注解
	 *
	 * @param element {@link AnnotatedElement}
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	public static <T extends Annotation> T[] getAllDirectlyResolvedAnnotations(final AnnotatedElement element, final Class<T> annotationType) {
		return toRepeatableMetaElement(element, true)
			.getDeclaredAnnotationsByType(annotationType);
	}

	// endregion

	// region ========== to element ==========

	/**
	 * <p>扫描{@code element}所处层级结构中的{@link AnnotatedElement}，
	 * 并将其全部转为{@link MetaAnnotatedElement}后，
	 * 再把所有对象合并为{@link HierarchicalAnnotatedElements}。<br>
	 * 得到的对象可访问{@code element}所处层级结构中所有{@link AnnotatedElement}上的注解及元注解。
	 *
	 * @param element  元素
	 * @param resolved 是否解析注解属性，若为{@code true}则获得的注解将支持属性别名以及属性覆盖机制
	 * @return {@link HierarchicalAnnotatedElements}实例
	 * @see #getMetaElementCache(AnnotatedElement)
	 * @see #getResolvedMetaElementCache(AnnotatedElement)
	 */
	public static AnnotatedElement toHierarchyMetaElement(final AnnotatedElement element, final boolean resolved) {
		if (Objects.isNull(element)) {
			return emptyElement();
		}
		if (resolved) {
			return HierarchicalAnnotatedElements.of(element, (es, e) -> getResolvedMetaElementCache(e));
		}
		return HierarchicalAnnotatedElements.of(element, (es, e) -> getMetaElementCache(e));
	}

	/**
	 * <p>扫描{@code element}所处层级结构中的{@link AnnotatedElement}，
	 * 并将其全部转为{@link RepeatableMetaAnnotatedElement}后，
	 * 再把所有对象合并为{@link HierarchicalAnnotatedElements}。<br>
	 * 得到的对象可访问{@code element}所处层级结构中所有{@link AnnotatedElement}上的直接声明的注解，
	 * 这些注解包含的可重复注解，以及上述注解的所有元注解。
	 *
	 * @param element  元素
	 * @param resolved 是否解析注解属性，若为{@code true}则获得的注解将支持属性别名以及属性覆盖机制
	 * @return {@link HierarchicalAnnotatedElements}实例
	 * @see #getRepeatableMetaElementCache(AnnotatedElement)
	 * @see #getResolvedRepeatableMetaElementCache(AnnotatedElement)
	 */
	public static AnnotatedElement toHierarchyRepeatableMetaElement(final AnnotatedElement element, final boolean resolved) {
		if (Objects.isNull(element)) {
			return emptyElement();
		}
		if (resolved) {
			return HierarchicalAnnotatedElements.of(element, (es, e) -> getResolvedRepeatableMetaElementCache(e));
		}
		return HierarchicalAnnotatedElements.of(element, (es, e) -> getRepeatableMetaElementCache(e));
	}

	/**
	 * <p>扫描{@code element}所处层级结构中的{@link AnnotatedElement}，
	 * 再把所有对象合并为{@link HierarchicalAnnotatedElements}
	 * 得到的对象可访问{@code element}所处层级结构中所有{@link AnnotatedElement}上的注解。
	 *
	 * @param element  元素
	 * @return {@link AnnotatedElement}实例
	 */
	public static AnnotatedElement toHierarchyElement(final AnnotatedElement element) {
		return ObjUtil.defaultIfNull(
			element, ele -> HierarchicalAnnotatedElements.of(ele, (es, e) -> e), emptyElement()
		);
	}

	/**
	 * 将{@link AnnotatedElement}转为{@link MetaAnnotatedElement}，
	 * 得到的对象可访问{@code element}上所有的注解及元注解。
	 *
	 * @param element  元素
	 * @param resolved 是否解析注解属性，若为{@code true}则获得的注解将支持属性别名以及属性覆盖机制
	 * @return {@link AnnotatedElement}实例
	 * @see #getMetaElementCache(AnnotatedElement)
	 * @see #getResolvedMetaElementCache(AnnotatedElement)
	 */
	public static AnnotatedElement toMetaElement(final AnnotatedElement element, final boolean resolved) {
		return ObjUtil.defaultIfNull(
			element, e -> resolved ? getResolvedMetaElementCache(e) : getMetaElementCache(e), emptyElement()
		);
	}

	/**
	 * 将{@link AnnotatedElement}转为{@link RepeatableMetaAnnotatedElement}，
	 * 得到的对象可访问{@link AnnotatedElement}上的直接声明的注解，这些注解包含的可重复注解，以及上述注解的所有元注解。
	 *
	 * @param element  元素
	 * @param resolved 是否解析注解属性，若为{@code true}则获得的注解将支持属性别名以及属性覆盖机制
	 * @return {@link AnnotatedElement}实例
	 * @see #getMetaElementCache(AnnotatedElement)
	 * @see #getResolvedMetaElementCache(AnnotatedElement)
	 */
	public static AnnotatedElement toRepeatableMetaElement(final AnnotatedElement element, final boolean resolved) {
		return ObjUtil.defaultIfNull(
			element, e -> resolved ? getResolvedRepeatableMetaElementCache(e) : getRepeatableMetaElementCache(e), emptyElement()
		);
	}

	/**
	 * <p>将{@link AnnotatedElement}转为{@link RepeatableMetaAnnotatedElement}，
	 * 得到的对象可访问{@link AnnotatedElement}上的直接声明的注解，
	 * 通过{@code collector}从这些注解获得的可重复注解，以及上述注解的所有元注解。<br>
	 * 注意：方法将不会通过缓存结果，因此每次调用都需要重新通过反射并获得相关注解。
	 *
	 * @param collector 可重复注解收集器，为{@code null}时等同于{@link RepeatableAnnotationCollector#none()}
	 * @param element   元素
	 * @param resolved  是否解析注解属性，若为{@code true}则获得的注解将支持属性别名以及属性覆盖机制
	 * @return {@link AnnotatedElement}实例
	 */
	public static AnnotatedElement toRepeatableMetaElement(
		final AnnotatedElement element, RepeatableAnnotationCollector collector, final boolean resolved) {
		if (Objects.isNull(element)) {
			return emptyElement();
		}
		collector = ObjUtil.defaultIfNull(collector, RepeatableAnnotationCollector.none());
		if (resolved) {
			return RepeatableMetaAnnotatedElement.create(
				collector, element, (source, annotation) -> ResolvedAnnotationMapping.create((ResolvedAnnotationMapping)source, annotation, true)
			);
		}
		return RepeatableMetaAnnotatedElement.create(
			collector, element, (source, annotation) -> GenericAnnotationMapping.create(annotation, Objects.isNull(source))
		);
	}

	/**
	 * 将一组注解中的非{@code null}注解对象合并为一个{@link AnnotatedElement}
	 *
	 * @param annotations 注解
	 * @return {@link AnnotatedElement}实例
	 * @see ConstantElement
	 */
	public static AnnotatedElement asElement(Annotation... annotations) {
		annotations = ArrayUtil.filter(annotations, Objects::nonNull);
		return ArrayUtil.isEmpty(annotations) ?
			emptyElement() : new ConstantElement(annotations);
	}

	/**
	 * 获取一个不包含任何注解的{@link AnnotatedElement}
	 *
	 * @return {@link AnnotatedElement}实例
	 * @see EmptyElement
	 */
	public static AnnotatedElement emptyElement() {
		return EmptyElement.INSTANCE;
	}

	// endregion

	// region ========== private ==========

	/**
	 * 创建一个支持注解解析的{@link MetaAnnotatedElement}
	 *
	 * @param element {@link AnnotatedElement}
	 * @return {@link MetaAnnotatedElement}实例
	 */
	static MetaAnnotatedElement<ResolvedAnnotationMapping> getResolvedMetaElementCache(final AnnotatedElement element) {
		return RESOLVED_ELEMENT_CACHE.computeIfAbsent(element, ele -> MetaAnnotatedElement.create(
			element, (source, annotation) -> ResolvedAnnotationMapping.create(source, annotation, true)
		));
	}

	/**
	 * 创建一个不支持注解解析的{@link MetaAnnotatedElement}
	 *
	 * @param element {@link AnnotatedElement}
	 * @return {@link MetaAnnotatedElement}实例
	 */
	static MetaAnnotatedElement<GenericAnnotationMapping> getMetaElementCache(final AnnotatedElement element) {
		return ELEMENT_CACHE.computeIfAbsent(element, ele -> MetaAnnotatedElement.create(
			element, (source, annotation) -> GenericAnnotationMapping.create(annotation, Objects.isNull(source))
		));
	}

	/**
	 * 创建一个支持注解解析的{@link RepeatableMetaAnnotatedElement}
	 *
	 * @param element {@link AnnotatedElement}
	 * @return {@link MetaAnnotatedElement}实例
	 */
	static RepeatableMetaAnnotatedElement<ResolvedAnnotationMapping> getResolvedRepeatableMetaElementCache(final AnnotatedElement element) {
		return RESOLVED_REPEATABLE_ELEMENT_CACHE.computeIfAbsent(element, ele -> RepeatableMetaAnnotatedElement.create(
			element, (source, annotation) -> ResolvedAnnotationMapping.create(source, annotation, true)
		));
	}

	/**
	 * 创建一个不支持注解解析的{@link RepeatableMetaAnnotatedElement}
	 *
	 * @param element {@link AnnotatedElement}
	 * @return {@link MetaAnnotatedElement}实例
	 */
	static RepeatableMetaAnnotatedElement<GenericAnnotationMapping> getRepeatableMetaElementCache(final AnnotatedElement element) {
		return REPEATABLE_ELEMENT_CACHE.computeIfAbsent(element, ele -> RepeatableMetaAnnotatedElement.create(
			element, (source, annotation) -> GenericAnnotationMapping.create(annotation, Objects.isNull(source))
		));
	}

	// endregion

	/**
	 * 清空相关缓存，包括：
	 * <ul>
	 *     <li>{@code AnnotatedElementUtil}中的{@link AnnotatedElement}及{@link  AnnotationMapping}缓存；</li>
	 *     <li>{@link AnnotationUtil}中的{@link AnnotatedElement}上直接声明的注解缓存；</li>
	 *     <li>{@link RepeatableAnnotationCollector}中单例的注解属性缓存；</li>
	 * </ul>
	 *
	 * @see AnnotationUtil#clearCaches()
	 * @see RepeatableAnnotationCollector#clearSingletonCaches()
	 */
	public static void clearCaches() {
		ELEMENT_CACHE.clear();
		RESOLVED_ELEMENT_CACHE.clear();
		REPEATABLE_ELEMENT_CACHE.clear();
		RESOLVED_REPEATABLE_ELEMENT_CACHE.clear();
		RepeatableAnnotationCollector.clearSingletonCaches();
		AnnotationUtil.clearCaches();
	}

	/**
	 * 由一组注解聚合来的{@link AnnotatedElement}
	 */
	private static class ConstantElement implements AnnotatedElement {

		/**
		 * 注解对象
		 */
		private final Annotation[] annotations;

		/**
		 * 构造
		 *
		 * @param annotations 注解
		 */
		ConstantElement(final Annotation[] annotations) {
			this.annotations = Objects.requireNonNull(annotations);
		}

		/**
		 * 获取指定类型的注解对象
		 *
		 * @param annotationClass 注解类型
		 * @param <T>             注解类型
		 * @return 注解
		 */
		@Override
		public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
			return Stream.of(annotations)
				.filter(annotation -> Objects.equals(annotation.annotationType(), annotationClass))
				.findFirst()
				.map(annotationClass::cast)
				.orElse(null);
		}

		/**
		 * 获取指定直接所有的注解对象
		 *
		 * @return 注解
		 */
		@Override
		public Annotation[] getAnnotations() {
			return annotations.clone();
		}

		/**
		 * 获取指定直接声明的注解对象
		 *
		 * @return 注解
		 */
		@Override
		public Annotation[] getDeclaredAnnotations() {
			return annotations.clone();
		}
	}

	/**
	 * 不包含任何注解的{@link AnnotatedElement}
	 */
	private static class EmptyElement implements AnnotatedElement {

		/**
		 * 默认的空实例
		 */
		static final EmptyElement INSTANCE = new EmptyElement();

		/**
		 * 固定返回{@code null}
		 *
		 * @param annotationClass 注解类型
		 * @param <T>             注解类型
		 * @return {@code null}
		 */
		@Override
		public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
			return null;
		}

		/**
		 * 固定返回空数组
		 *
		 * @return 空数组
		 */
		@Override
		public Annotation[] getAnnotations() {
			return new Annotation[0];
		}

		/**
		 * 固定返回空数组
		 *
		 * @return 空数组
		 */
		@Override
		public Annotation[] getDeclaredAnnotations() {
			return new Annotation[0];
		}
	}

}
