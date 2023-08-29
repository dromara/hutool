package cn.hutool.core.bean.copier;

import java.lang.reflect.Type;

/**
 * JSON自定义转换扩展接口,因core模块无法直接调用json模块而创建,
 * 使用此接口避免使用反射调用toBean方法而性能太差。
 *
 * @author mkeq
 * @since 5.8.22
 */
public interface IJSONTypeConverter {

	/**
	 * 设置忽略错误属性
	 *
	 * @param ignoreError 是否忽略错误属性
	 * @since 5.8.22
	 */
	void setIgnoreError(boolean ignoreError);

	/**
	 * 转为实体类对象
	 *
	 * @param <T>  Bean类型
	 * @param type {@link Type}
	 * @return 实体类对象
	 * @since 3.0.8
	 */
	<T> T toBean(Type type);

}
