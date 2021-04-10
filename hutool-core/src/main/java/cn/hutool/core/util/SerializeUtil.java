package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * 序列化工具类
 *
 * @author looly
 * @since 5.6.3
 */
public class SerializeUtil {

	/**
	 * 序列化后拷贝流的方式克隆<br>
	 * 对象必须实现Serializable接口
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 * @throws UtilException IO异常和ClassNotFoundException封装
	 */
	public static <T> T clone(T obj) {
		if (false == (obj instanceof Serializable)) {
			return null;
		}
		return deserialize(serialize(obj));
	}

	/**
	 * 序列化<br>
	 * 对象必须实现Serializable接口
	 *
	 * @param <T> 对象类型
	 * @param obj 要被序列化的对象
	 * @return 序列化后的字节码
	 */
	public static <T> byte[] serialize(T obj) {
		if (false == (obj instanceof Serializable)) {
			return null;
		}
		final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
		IoUtil.writeObjects(byteOut, false, (Serializable) obj);
		return byteOut.toByteArray();
	}

	/**
	 * 反序列化<br>
	 * 对象必须实现Serializable接口
	 *
	 * <p>
	 * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
	 * </p>
	 *
	 * @param <T>   对象类型
	 * @param bytes 反序列化的字节码
	 * @return 反序列化后的对象
	 */
	public static <T> T deserialize(byte[] bytes) {
		return IoUtil.readObj(new ByteArrayInputStream(bytes));
	}
}
