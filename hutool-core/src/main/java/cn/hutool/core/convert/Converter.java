package cn.hutool.core.convert;

/**
 * 转换器接口，实现类型转换
 *
 * @param <T> 转换到的目标类型
 * @author Looly
 */
public interface Converter<T> {

	/**
	 * 转换为指定类型<br>
	 * 如果类型无法确定，将读取默认值的类型做为目标类型
	 *
	 * @param value 原始值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws IllegalArgumentException 无法确定目标类型，且默认值为{@code null}，无法确定类型
	 */
	T convert(Object value, T defaultValue) throws IllegalArgumentException;

}