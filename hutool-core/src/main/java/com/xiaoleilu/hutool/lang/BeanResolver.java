package com.xiaoleilu.hutool.lang;

import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Bean对象解析器<br>
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
 * person.friend[5].name
 * </pre>
 * 
 * 
 * @author Looly
 * @since 3.0.7
 */
public class BeanResolver {
	private Object bean;
	private String expression;
	
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
		this.expression = expression;
	}

	/**
	 * 解析表达式对应的Bean属性
	 * @return Bean属性值
	 */
	public Object resolve() {
		Object subBean = this.bean;
		int length = expression.length();

		StringBuilder sb = new StringBuilder();
		char c;
		boolean isNumStart = false;
		boolean isFirst = true;
		for (int i = 0; i < length; i++) {
			c = expression.charAt(i);
			if (StrUtil.C_DOT == c || StrUtil.C_BRACKET_START == c || StrUtil.C_BRACKET_END == c) {
				// 处理边界符号
				if (StrUtil.C_BRACKET_END == c) {
					// 中括号结束
					if (false == isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, i));
					}
					isNumStart = false;
				} else {
					// 非结束中括号情况下发现起始中括号报错（中括号未关闭）
					if (isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, i));
					} else if (StrUtil.C_BRACKET_START == c) {
						isNumStart = true;
					}
				}
				if (sb.length() > 0) {
					final String name = sb.toString();
					subBean = getSubBean(subBean, name);
					if (null == subBean) {
						//支持表达式的第一个对象为Bean本身
						if(isFirst && bean.getClass().getName().equals(StrUtil.upperFirst(name))){
							subBean = bean;
							isFirst = false;
						}else{
							return null;
						}
					}
				}
				sb = new StringBuilder();
			} else {
				// 非边界符号，追加字符
				if (isNumStart && (c < '0' || c > '9')) {
					throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, it must number between '[' and ']', but contains '{}' !", expression, i, c));
				}
				sb.append(c);
			}
		}

		// 末尾边界符检查
		if (isNumStart) {
			throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, length - 1));
		} else {
			if(sb.length() > 0){
				subBean = getSubBean(subBean, sb.toString());
			}
		}
		return subBean;
	}

	/**
	 * 获得子Bean
	 * 
	 * @param bean 父Bean
	 * @param name 属性名（或者字段名）
	 * @return 子Bean
	 */
	private Object getSubBean(Object bean, String name) {
		 return BeanUtil.getFieldValue(bean, name);
	}
}
