package cn.hutool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 带有类验证的对象流，用于避免反序列化漏洞<br>
 * 详细见：https://xz.aliyun.com/t/41/
 *
 * @author looly
 * @since 5.2.6
 */
public class ValidateObjectInputStream extends ObjectInputStream {

	private Class<?> acceptClass;

	/**
	 * 构造
	 *
	 * @param inputStream 流
	 * @param acceptClass 接受的类
	 * @throws IOException IO异常
	 */
	public ValidateObjectInputStream(InputStream inputStream, Class<?> acceptClass) throws IOException {
		super(inputStream);
		this.acceptClass = acceptClass;
	}

	/**
	 * 接受反序列化的类，用于反序列化验证
	 *
	 * @param acceptClass 接受反序列化的类
	 */
	public void accept(Class<?> acceptClass) {
		this.acceptClass = acceptClass;
	}

	/**
	 * 只允许反序列化SerialObject class
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		if (null != this.acceptClass && false == desc.getName().equals(acceptClass.getName())) {
			throw new InvalidClassException(
					"Unauthorized deserialization attempt",
					desc.getName());
		}
		return super.resolveClass(desc);
	}
}
