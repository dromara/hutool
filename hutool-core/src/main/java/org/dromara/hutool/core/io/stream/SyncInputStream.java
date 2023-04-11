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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.StreamProgress;
import org.dromara.hutool.core.text.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 同步流，可将包装的流同步为ByteArrayInputStream，以便持有内容并关闭原流
 *
 * @author looly
 * @since 6.0.0
 */
public class SyncInputStream extends FilterInputStream {

	private final long length;
	private final boolean isIgnoreEOFError;
	/**
	 * 是否异步，异步下只持有流，否则将在初始化时直接读取body内容
	 */
	private volatile boolean asyncFlag = true;

	/**
	 * 构造<br>
	 * 如果isAsync为{@code true}，则直接持有原有流，{@code false}，则将流中内容，按照给定length读到{@link ByteArrayInputStream}中备用
	 *
	 * @param in               数据流
	 * @param length           限定长度，-1表示未知长度
	 * @param isAsync          是否异步
	 * @param isIgnoreEOFError 是否忽略EOF错误，在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束<br>
	 *                         如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。<br>
	 */
	public SyncInputStream(final InputStream in, final long length, final boolean isAsync, final boolean isIgnoreEOFError) {
		super(in);
		this.length = length;
		this.isIgnoreEOFError = isIgnoreEOFError;
		if (! isAsync) {
			sync();
		}
	}

	/**
	 * 同步数据到内存
	 * @return this
	 */
	public SyncInputStream sync() {
		if (asyncFlag) {
			this.in = new ByteArrayInputStream(readBytes());
			this.asyncFlag = false;
		}

		return this;
	}

	/**
	 * 读取流中所有bytes
	 *
	 * @return bytes
	 */
	public byte[] readBytes() {
		final FastByteArrayOutputStream bytesOut = new FastByteArrayOutputStream(length > 0 ? (int)length : 1024);
		final long length = copyTo(bytesOut, null);
		return length > 0 ? bytesOut.toByteArray() : new byte[0];
	}

	/**
	 * 将流的内容拷贝到输出流
	 * @param out 输出流
	 * @param streamProgress 进度条
	 * @return 拷贝长度
	 */
	public long copyTo(final OutputStream out, final StreamProgress streamProgress){
		long copyLength = -1;
		try {
			copyLength = IoUtil.copy(this.in, out, IoUtil.DEFAULT_BUFFER_SIZE, this.length, streamProgress);
		} catch (final IORuntimeException e) {
			if (! (isIgnoreEOFError && isEOFException(e.getCause()))) {
				throw e;
			}
			// 忽略读取流中的EOF错误
		}finally {
			// 读取结束
			IoUtil.closeQuietly(in);
		}
		return copyLength;
	}

	/**
	 * 是否为EOF异常，包括<br>
	 * <ul>
	 *     <li>FileNotFoundException：服务端无返回内容</li>
	 *     <li>EOFException：EOF异常</li>
	 * </ul>
	 *
	 * @param e 异常
	 * @return 是否EOF异常
	 */
	private static boolean isEOFException(final Throwable e) {
		if (e instanceof FileNotFoundException) {
			// 服务器无返回内容，忽略之
			return true;
		}
		return e instanceof EOFException || StrUtil.containsIgnoreCase(e.getMessage(), "Premature EOF");
	}
}
