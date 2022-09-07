package cn.hutool.json;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.json.convert.JSONConverter;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * JSON接口
 *
 * @author Looly
 */
public interface JSON extends Converter, Cloneable, Serializable {

	/**
	 * 获取JSON配置
	 *
	 * @return {@link JSONConfig}
	 * @since 5.3.0
	 */
	JSONConfig getConfig();

	/**
	 * JSON大小，对于JSONObject，是键值对的多少，JSONArray则是元素的个数
	 *
	 * @return 大小
	 */
	int size();

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
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
	 * @see BeanPath#get(Object)
	 * @since 4.0.6
	 */
	default Object getByPath(final String expression) {
		return BeanPath.of(expression).get(this);
	}

	/**
	 * 设置表达式指定位置（或filed对应）的值<br>
	 * 若表达式指向一个JSONArray则设置其坐标对应位置的值，若指向JSONObject则put对应key的值<br>
	 * 注意：如果为JSONArray，设置值下标小于其长度，将替换原有值，否则追加新值<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
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
	 * @param value      值
	 */
	default void putByPath(final String expression, final Object value) {
		BeanPath.of(expression).set(this, value);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * <p>
	 * 获取表达式对应值后转换为对应类型的值
	 *
	 * @param <T>        返回值类型
	 * @param expression 表达式
	 * @param resultType 返回值类型
	 * @return 对象
	 * @see BeanPath#get(Object)
	 * @since 4.0.6
	 */
	@SuppressWarnings("unchecked")
	default <T> T getByPath(final String expression, final Class<T> resultType){
		return (T) getConfig().getConverter().convert(resultType, getByPath(expression));
	}

	/**
	 * 格式化打印JSON，缩进为4个空格
	 *
	 * @return 格式化后的JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 * @since 3.0.9
	 */
	default String toStringPretty() throws JSONException {
		return this.toJSONString(4);
	}

	/**
	 * 格式化输出JSON字符串
	 *
	 * @param indentFactor 每层缩进空格数
	 * @return JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 */
	default String toJSONString(final int indentFactor) throws JSONException {
		final StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0, null).toString();
		}
	}

	/**
	 * 将JSON内容写入Writer，无缩进<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param writer Writer
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	default Writer write(final Writer writer) throws JSONException {
		return this.write(writer, 0, 0, null);
	}

	/**
	 * 将JSON内容写入Writer<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param writer       writer
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param predicate    过滤器，可以修改值，key（index）无法修改，{@link Predicate#test(Object)}为{@code true}保留
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 */
	Writer write(Writer writer, int indentFactor, int indent, final Predicate<MutableEntry<Object, Object>> predicate) throws JSONException;

	/**
	 * 转为实体类对象
	 *
	 * @param <T>  Bean类型
	 * @param type {@link Type}
	 * @return 实体类对象
	 * @since 4.3.2
	 */
	@SuppressWarnings("unchecked")
	default <T> T toBean(final Type type) {
		return (T) convert(type, this);
	}

	@Override
	default Object convert(Type targetType, Object value) throws ConvertException {
		return JSONConverter.of(getConfig()).convert(targetType, value);
	}
}
