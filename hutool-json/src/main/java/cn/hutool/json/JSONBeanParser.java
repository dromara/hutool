package cn.hutool.json;

/**
 * 实现此接口的类可以通过实现{@code parse(value)}方法来将JSON中的值解析为此对象的值
 *
 * @author Looly
 * @since 5.7.8
 */
public interface JSONBeanParser<T> {

	/**
	 * value转Bean<br>
	 * 通过实现此接口，将JSON中的值填充到当前对象的字段值中，即对象自行实现JSON反序列化逻辑
	 *
	 * @param value 被解析的对象类型，可能为JSON或者普通String、Number等
	 */
	void parse(T value);
}
