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

package org.dromara.hutool.core.io.stream;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link InputStream}读取器
 *
 * @author looly
 */
public class StreamReader {

	/**
	 * 创建读取器
	 *
	 * @param in             {@link InputStream}
	 * @param closeAfterRead 读取结束后是否关闭输入流
	 * @return StreamReader
	 */
	public static StreamReader of(final InputStream in, final boolean closeAfterRead) {
		return new StreamReader(in, closeAfterRead);
	}

	private final InputStream in;
	private final boolean closeAfterRead;

	/**
	 * 构造
	 *
	 * @param in             {@link InputStream}
	 * @param closeAfterRead 读取结束后是否关闭输入流
	 */
	public StreamReader(final InputStream in, final boolean closeAfterRead) {
		this.in = in;
		this.closeAfterRead = closeAfterRead;
	}

	/**
	 * 从流中读取bytes
	 *
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] readBytes() throws IORuntimeException {
		return readBytes(-1);
	}

	/**
	 * 读取指定长度的byte数组
	 *
	 * @param length 长度，小于0表示读取全部
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] readBytes(final int length) throws IORuntimeException {
		final InputStream in = this.in;
		if (null == in || length == 0) {
			return new byte[0];
		}

		//noinspection resource
		return read(length).toByteArray();
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public FastByteArrayOutputStream read() throws IORuntimeException {
		return read(-1);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @param limit 限制最大拷贝长度，-1表示无限制
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public FastByteArrayOutputStream read(final int limit) throws IORuntimeException {
		final InputStream in = this.in;
		final FastByteArrayOutputStream out;
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			try {
				int length = in.available();
				if (limit > 0 && limit < length) {
					length = limit;
				}
				out = new FastByteArrayOutputStream(length);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			out = new FastByteArrayOutputStream();
		}
		try {
			IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE, limit, null);
		} finally {
			if (closeAfterRead) {
				IoUtil.closeQuietly(in);
			}
		}
		return out;
	}

	/**
	 * 从流中读取对象，即对象的反序列化
	 *
	 * <p>注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！</p>
	 *
	 * <p>
	 * 此方法使用了{@link ValidateObjectInputStream}中的黑白名单方式过滤类，用于避免反序列化漏洞<br>
	 * 通过构造{@link ValidateObjectInputStream}，调用{@link ValidateObjectInputStream#accept(Class[])}
	 * 或者{@link ValidateObjectInputStream#refuse(Class[])}方法添加可以被序列化的类或者禁止序列化的类。
	 * </p>
	 *
	 * @param <T>           读取对象的类型
	 * @param acceptClasses 读取对象类型
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	@SuppressWarnings("unchecked")
	public <T> T readObj(final Class<?>... acceptClasses) throws IORuntimeException, UtilException {
		final InputStream in = this.in;
		if (null == in) {
			return null;
		}

		// 转换
		final ValidateObjectInputStream validateIn;
		if (in instanceof ValidateObjectInputStream) {
			validateIn = (ValidateObjectInputStream) in;
			validateIn.accept(acceptClasses);
		} else {
			try {
				validateIn = new ValidateObjectInputStream(in, acceptClasses);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}

		// 读取
		try {
			return (T) validateIn.readObject();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} catch (final ClassNotFoundException e) {
			throw new UtilException(e);
		}
	}
}
