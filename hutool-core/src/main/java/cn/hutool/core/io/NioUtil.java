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

package cn.hutool.core.io;

import cn.hutool.core.io.copy.ChannelCopier;
import cn.hutool.core.io.copy.FileChannelCopier;
import cn.hutool.core.io.stream.FastByteArrayOutputStream;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.text.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/**
 * NIO相关工具封装，主要针对Channel读写、拷贝等封装
 *
 * @author looly
 * @since 5.5.3
 */
public class NioUtil {

	/**
	 * 默认缓存大小 8192
	 */
	public static final int DEFAULT_BUFFER_SIZE = 2 << 12;
	/**
	 * 默认中等缓存大小 16384
	 */
	public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 2 << 13;
	/**
	 * 默认大缓存大小 32768
	 */
	public static final int DEFAULT_LARGE_BUFFER_SIZE = 2 << 14;

	/**
	 * 数据流末尾
	 */
	public static final int EOF = -1;

	/**
	 * 拷贝流 thanks to: https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java<br>
	 * 本方法不会关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copyByNIO(final InputStream in, final OutputStream out, final int bufferSize, final StreamProgress streamProgress) throws IORuntimeException {
		return copyByNIO(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流<br>
	 * 本方法不会关闭流
	 *
	 * @param in             输入流， 非空
	 * @param out            输出流， 非空
	 * @param bufferSize     缓存大小，-1表示默认
	 * @param count          最大长度，-1表示无限制
	 * @param streamProgress 进度条，{@code null}表示无进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static long copyByNIO(final InputStream in, final OutputStream out, final int bufferSize, final long count, final StreamProgress streamProgress) throws IORuntimeException {
		Assert.notNull(in, "InputStream channel is null!");
		Assert.notNull(out, "OutputStream channel is null!");
		final long copySize = copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, count, streamProgress);
		IoUtil.flush(out);
		return copySize;
	}

	/**
	 * 拷贝文件Channel，使用NIO，拷贝后不会关闭channel
	 *
	 * @param in  {@link FileChannel}，非空
	 * @param out {@link FileChannel}，非空
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 * @since 5.5.3
	 */
	public static long copy(final FileChannel in, final FileChannel out) throws IORuntimeException {
		Assert.notNull(in, "In channel is null!");
		Assert.notNull(out, "Out channel is null!");

		return FileChannelCopier.of().copy(in, out);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭channel
	 *
	 * @param in  {@link ReadableByteChannel}
	 * @param out {@link WritableByteChannel}
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 * @since 4.5.0
	 */
	public static long copy(final ReadableByteChannel in, final WritableByteChannel out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭channel
	 *
	 * @param in         {@link ReadableByteChannel}
	 * @param out        {@link WritableByteChannel}
	 * @param bufferSize 缓冲大小，如果小于等于0，使用默认
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 * @since 4.5.0
	 */
	public static long copy(final ReadableByteChannel in, final WritableByteChannel out, final int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭channel
	 *
	 * @param in             {@link ReadableByteChannel}
	 * @param out            {@link WritableByteChannel}
	 * @param bufferSize     缓冲大小，如果小于等于0，使用默认
	 * @param streamProgress {@link StreamProgress}进度处理器
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final ReadableByteChannel in, final WritableByteChannel out, final int bufferSize, final StreamProgress streamProgress) throws IORuntimeException {
		return copy(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭channel
	 *
	 * @param in             {@link ReadableByteChannel}
	 * @param out            {@link WritableByteChannel}
	 * @param bufferSize     缓冲大小，如果小于等于0，使用默认
	 * @param count          读取总长度
	 * @param streamProgress {@link StreamProgress}进度处理器
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static long copy(final ReadableByteChannel in, final WritableByteChannel out, final int bufferSize, final long count, final StreamProgress streamProgress) throws IORuntimeException {
		Assert.notNull(in, "In channel is null!");
		Assert.notNull(out, "Out channel is null!");
		return new ChannelCopier(bufferSize, count, streamProgress).copy(in, out);
	}

	/**
	 * 从流中读取内容，读取完毕后并不关闭流
	 *
	 * @param channel 可读通道，读取完毕后并不关闭通道
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @since 4.5.0
	 */
	public static String read(final ReadableByteChannel channel, final Charset charset) throws IORuntimeException {
		final FastByteArrayOutputStream out = read(channel);
		return null == charset ? out.toString() : out.toString(charset);
	}

	/**
	 * 从流中读取内容，读到输出流中
	 *
	 * @param channel 可读通道，读取完毕后并不关闭通道
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(final ReadableByteChannel channel) throws IORuntimeException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(channel, Channels.newChannel(out));
		return out;
	}

	/**
	 * 从FileChannel中读取UTF-8编码内容
	 *
	 * @param fileChannel 文件管道
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readUtf8(final FileChannel fileChannel) throws IORuntimeException {
		return read(fileChannel, CharsetUtil.UTF_8);
	}

	/**
	 * 从FileChannel中读取内容，读取完毕后并不关闭Channel
	 *
	 * @param fileChannel 文件管道
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(final FileChannel fileChannel, final String charsetName) throws IORuntimeException {
		return read(fileChannel, CharsetUtil.charset(charsetName));
	}

	/**
	 * 从FileChannel中读取内容
	 *
	 * @param fileChannel 文件管道
	 * @param charset     字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(final FileChannel fileChannel, final Charset charset) throws IORuntimeException {
		final MappedByteBuffer buffer;
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return StrUtil.str(buffer, charset);
	}
}
