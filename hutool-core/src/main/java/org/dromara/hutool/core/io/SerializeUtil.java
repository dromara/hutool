/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * 序列化工具类<br>
 * 注意！此工具类依赖于JDK的序列化机制，某些版本的JDK中可能存在远程注入漏洞。
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
	public static <T> T clone(final T obj) {
		if (!(obj instanceof Serializable)) {
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
	public static <T> byte[] serialize(final T obj) {
		if (!(obj instanceof Serializable)) {
			return null;
		}
		final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
		IoUtil.writeObjects(byteOut, false, obj);
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
	 * @param <T>           对象类型
	 * @param bytes         反序列化的字节码
	 * @param acceptClasses 读取对象类型
	 * @return 反序列化后的对象
	 */
	public static <T> T deserialize(final byte[] bytes, final Class<?>... acceptClasses) {
		return IoUtil.readObj(new ByteArrayInputStream(bytes), acceptClasses);
	}
}
