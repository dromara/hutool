package cn.hutool.json.serialize;

import cn.hutool.json.JSON;

/**
 * JSON自定义反序列化接口<br>
 * 此接口主要针对一些特殊对象的反序列化自定义转换规则，配合GlobalSerializeMapping，可以全局设定某一类对象的自定义转换，而无需修改发Bean的结构。
 *
 * @param <T> 反序列化后的类型
 * @author Looly
 */
@FunctionalInterface
public interface JSONDeserializer<T> {

	/**
	 * 反序列化，通过实现此方法，自定义实现JSON转换为指定类型的逻辑
	 *
	 * @param json {@link JSON}
	 * @return 目标对象
	 */
	T deserialize(JSON json);
}
