package com.xiaoleilu.hutool.json;

import java.io.Writer;

import com.xiaoleilu.hutool.bean.BeanResolver;

/**
 * JSON接口
 * 
 * @author Looly
 *
 */
public interface JSON {

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * 
	 * 表达式栗子：
	 * 
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * 
	 * @param expression 表达式
	 * @return 对象
	 * @see BeanResolver#resolveBean(Object, String)
	 */
	public Object getByExp(String expression);
	
	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * 
	 * 表达式栗子：
	 * 
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * 
	 * 获取表达式对应值后转换为对应类型的值
	 * 
	 * @param <T> 返回值类型
	 * @param expression 表达式
	 * @param resultType 返回值类型
	 * @return 对象
	 * @see BeanResolver#resolveBean(Object, String)
	 * @since 3.1.0
	 */
	public <T> T getByExp(String expression, Class<T> resultType);

	/**
	 * 将JSON内容写入Writer，无缩进<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * @param writer Writer
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	public Writer write(Writer writer) throws JSONException;

	/**
	 * 将JSON内容写入Writer<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * @param writer writer
	 * @param indentFactor 每一级别的缩进量
	 * @param indent 顶级别缩进量
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException;

	/**
	 * 转换为JSON字符串
	 * 
	 * @param indentFactor 每一级别的缩进
	 * @return JSON字符串
	 * @throws JSONException JSON相关异常
	 */
	public String toJSONString(int indentFactor) throws JSONException;
	
	/**
	 * 格式化打印JSON，缩进为4个空格
	 * @return 格式化后的JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 * @since 3.0.9
	 */
	public String toStringPretty() throws JSONException;
}
