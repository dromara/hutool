package cn.hutool.json.serialize;

import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.json.JSON;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局的序列化和反序列化器映射<br>
 * 在JSON和Java对象转换过程中，优先使用注册于此处的自定义转换
 *
 * @author Looly
 *
 */
public class GlobalSerializeMapping {

	private static Map<Type, JSONSerializer<? extends JSON, ?>> serializerMap;
	private static Map<Type, JSONDeserializer<?>> deserializerMap;

	static {
		serializerMap = new SafeConcurrentHashMap<>();
		deserializerMap = new SafeConcurrentHashMap<>();

		final TemporalAccessorSerializer localDateSerializer = new TemporalAccessorSerializer(LocalDate.class);
		serializerMap.put(LocalDate.class, localDateSerializer);
		deserializerMap.put(LocalDate.class, localDateSerializer);

		final TemporalAccessorSerializer localDateTimeSerializer = new TemporalAccessorSerializer(LocalDateTime.class);
		serializerMap.put(LocalDateTime.class, localDateTimeSerializer);
		deserializerMap.put(LocalDateTime.class, localDateTimeSerializer);

		final TemporalAccessorSerializer localTimeSerializer = new TemporalAccessorSerializer(LocalTime.class);
		serializerMap.put(LocalTime.class, localTimeSerializer);
		deserializerMap.put(LocalTime.class, localTimeSerializer);
	}

	/**
	 * 加入自定义的序列化器
	 *
	 * @param type 对象类型
	 * @param serializer 序列化器实现
	 */
	public static void put(Type type, JSONArraySerializer<?> serializer) {
		putInternal(type, serializer);
	}

	/**
	 * 加入自定义的序列化器
	 *
	 * @param type 对象类型
	 * @param serializer 序列化器实现
	 */
	public static void put(Type type, JSONObjectSerializer<?> serializer) {
		putInternal(type, serializer);
	}

	/**
	 * 加入自定义的序列化器
	 *
	 * @param type 对象类型
	 * @param serializer 序列化器实现
	 */
	synchronized private static void putInternal(Type type, JSONSerializer<? extends JSON, ?> serializer) {
		if(null == serializerMap) {
			serializerMap = new SafeConcurrentHashMap<>();
		}
		serializerMap.put(type, serializer);
	}

	/**
	 * 加入自定义的反序列化器
	 *
	 * @param type 对象类型
	 * @param deserializer 反序列化器实现
	 */
	synchronized public static void put(Type type, JSONDeserializer<?> deserializer) {
		if(null == deserializerMap) {
			deserializerMap = new ConcurrentHashMap<>();
		}
		deserializerMap.put(type, deserializer);
	}

	/**
	 * 获取自定义的序列化器，如果未定义返回{@code null}
	 * @param type 类型
	 * @return 自定义的序列化器或者{@code null}
	 */
	public static JSONSerializer<? extends JSON, ?> getSerializer(Type type){
		if(null == serializerMap) {
			return null;
		}
		return serializerMap.get(type);
	}

	/**
	 * 获取自定义的反序列化器，如果未定义返回{@code null}
	 * @param type 类型
	 * @return 自定义的反序列化器或者{@code null}
	 */
	public static JSONDeserializer<?> getDeserializer(Type type){
		if(null == deserializerMap) {
			return null;
		}
		return deserializerMap.get(type);
	}
}
