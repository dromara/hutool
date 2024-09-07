/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.io.stream;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

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
		return read(length).toByteArrayZeroCopyIfPossible();
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
		final FastByteArrayOutputStream out = FastByteArrayOutputStream.of(in, limit);
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
	 * 从流中读取内容，直到遇到给定token满足{@link Predicate#test(Object)}
	 *
	 * @param predicate 读取结束条件, {@link Predicate#test(Object)}返回true表示结束
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public FastByteArrayOutputStream readTo(final Predicate<Integer> predicate) throws IORuntimeException {
		final InputStream in = this.in;
		final FastByteArrayOutputStream out = FastByteArrayOutputStream.of(in, -1);
		int read;
		try {
			while ((read = in.read()) > 0) {
				if (null != predicate && predicate.test(read)) {
					break;
				}
				out.write(read);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
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
	 * @throws HutoolException    ClassNotFoundException包装
	 */
	@SuppressWarnings("unchecked")
	public <T> T readObj(final Class<?>... acceptClasses) throws IORuntimeException, HutoolException {
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
			throw new HutoolException(e);
		}
	}
}
