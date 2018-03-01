package cn.hutool.core.bean;

/**
 * Bean对象解析器，用于获取多层嵌套Bean中的字段值或Bean对象<br>
 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
 * <ol>
 * 	<li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
 * 	<li>[]表达式，可以获取集合等对象中对应index的值</li>
 * </ol>
 * 
 * 表达式栗子：
 * <pre>
 * persion
 * persion.name
 * persons[3]
 * person.friends[5].name
 * </pre>
 * 
 * 
 * @author Looly
 * @since 3.0.7
 * @deprecated 请使用{@link BeanPath#get(Object)}
 */
@Deprecated
public class BeanResolver {
	
	private Object bean;
	private BeanPath pattern;
	
	/**
	 * 解析Bean中的属性值
	 * @param bean Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 * @return Bean属性值
	 */
	public static Object resolveBean(Object bean, String expression){
		return new BeanResolver(bean, expression).resolve();
	}

	/**
	 * 构造
	 * @param bean Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 */
	public BeanResolver(Object bean, String expression) {
		this.bean = bean;
		this.pattern = BeanPath.create(expression);
	}

	/**
	 * 解析表达式对应的Bean属性
	 * @return Bean属性值
	 */
	public Object resolve() {
		return pattern.get(this.bean);
	}
}
