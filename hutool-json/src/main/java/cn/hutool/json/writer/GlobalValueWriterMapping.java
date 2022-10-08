package cn.hutool.json.writer;

import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.reflect.NullType;
import cn.hutool.core.util.ObjUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 全局自定义对象写出<br>
 * 用户通过此全局定义，可针对某些特殊对象
 *
 * @author looly
 * @since 6.0.0
 */
public class GlobalValueWriterMapping {

	private static final Map<Type, JSONValueWriter<?>> writerMap;

	static {
		writerMap = new SafeConcurrentHashMap<>();
	}

	/**
	 * 加入自定义的对象值写出规则
	 *
	 * @param type   对象类型
	 * @param writer 自定义对象写出实现
	 */
	public static void put(final Type type, final JSONValueWriter<?> writer) {
		writerMap.put(ObjUtil.defaultIfNull(type, NullType.INSTANCE), writer);
	}

	/**
	 * 获取自定义对象值写出规则
	 *
	 * @param type 对象类型
	 * @return 自定义的 {@link JSONValueWriter}
	 */
	public static JSONValueWriter<?> get(final Type type) {
		return writerMap.get(ObjUtil.defaultIfNull(type, NullType.INSTANCE));
	}
}
