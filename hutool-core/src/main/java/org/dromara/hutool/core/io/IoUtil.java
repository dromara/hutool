/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.collection.iter.LineIter;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.copy.FileChannelCopier;
import org.dromara.hutool.core.io.copy.ReaderWriterCopier;
import org.dromara.hutool.core.io.copy.StreamCopier;
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.core.io.stream.StreamReader;
import org.dromara.hutool.core.io.stream.StreamWriter;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.func.SerConsumer;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 *
 * @author Looly
 */
public class IoUtil extends NioUtil {

	// region -------------------------------------------------------------------------------------- Copy

	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小，拷贝后不关闭Reader
	 *
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final Reader reader, final Writer writer) throws IORuntimeException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader     Reader
	 * @param writer     Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final Reader reader, final Writer writer, final int bufferSize) throws IORuntimeException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final Reader reader, final Writer writer, final int bufferSize, final StreamProgress streamProgress) throws IORuntimeException {
		return copy(reader, writer, bufferSize, -1, streamProgress);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader，非空
	 * @param writer         Writer，非空
	 * @param bufferSize     缓存大小，-1表示默认
	 * @param count          最大长度，-1表示无限制
	 * @param streamProgress 进度处理器，{@code null}表示无
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final Reader reader, final Writer writer, final int bufferSize, final long count, final StreamProgress streamProgress) throws IORuntimeException {
		Assert.notNull(reader, "Reader is null !");
		Assert.notNull(writer, "Writer is null !");
		return new ReaderWriterCopier(bufferSize, count, streamProgress).copy(reader, writer);
	}

	/**
	 * 拷贝流，使用默认Buffer大小，拷贝后不关闭流
	 *
	 * @param in  输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final InputStream in, final OutputStream out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in         输入流
	 * @param out        输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final InputStream in, final OutputStream out, final int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final InputStream in, final OutputStream out, final int bufferSize, final StreamProgress streamProgress) throws IORuntimeException {
		return copy(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param count          总拷贝长度，-1表示无限制
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static long copy(final InputStream in, final OutputStream out, final int bufferSize, final long count, final StreamProgress streamProgress) throws IORuntimeException {
		Assert.notNull(in, "InputStream is null !");
		Assert.notNull(out, "OutputStream is null !");
		return new StreamCopier(bufferSize, count, streamProgress).copy(in, out);
	}

	/**
	 * 拷贝文件流，使用NIO
	 *
	 * @param in  输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(final FileInputStream in, final FileOutputStream out) throws IORuntimeException {
		Assert.notNull(in, "FileInputStream is null!");
		Assert.notNull(out, "FileOutputStream is null!");

		return FileChannelCopier.of().copy(in, out);
	}

	// endregion -------------------------------------------------------------------------------------- Copy

	// region -------------------------------------------------------------------------------------- toReader and toWriter

	/**
	 * 获得一个文件读取器，默认使用 UTF-8 编码
	 *
	 * @param in 输入流
	 * @return BufferedReader对象
	 * @since 5.1.6
	 */
	public static BufferedReader toUtf8Reader(final InputStream in) {
		return toReader(in, CharsetUtil.UTF_8);
	}

	/**
	 * 从{@link InputStream}中获取{@link BomReader}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BomReader}
	 * @since 5.7.14
	 */
	public static BomReader toBomReader(final InputStream in) {
		return new BomReader(in);
	}

	/**
	 * 获得一个Reader
	 *
	 * @param in      输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader toReader(final InputStream in, final Charset charset) {
		if (null == in) {
			return null;
		}

		final InputStreamReader reader;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}

	/**
	 * 获得一个Writer，默认编码UTF-8
	 *
	 * @param out 输入流
	 * @return OutputStreamWriter对象
	 * @since 5.1.6
	 */
	public static OutputStreamWriter toUtf8Writer(final OutputStream out) {
		return toWriter(out, CharsetUtil.UTF_8);
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out     输入流
	 * @param charset 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter toWriter(final OutputStream out, final Charset charset) {
		if (null == out) {
			return null;
		}

		if (null == charset) {
			return new OutputStreamWriter(out);
		} else {
			return new OutputStreamWriter(out, charset);
		}
	}
	// endregion -------------------------------------------------------------------------------------- toReader and toWriter

	// region -------------------------------------------------------------------------------------- read

	/**
	 * 从流中读取UTF8编码的内容
	 *
	 * @param in 输入流
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @since 5.4.4
	 */
	public static String readUtf8(final InputStream in) throws IORuntimeException {
		return read(in, CharsetUtil.UTF_8);
	}

	/**
	 * 从流中读取内容，读取完毕后关闭流
	 *
	 * @param in      输入流，读取完毕后关闭流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(final InputStream in, final Charset charset) throws IORuntimeException {
		return StrUtil.str(readBytes(in), charset);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后关闭流
	 *
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(final InputStream in) throws IORuntimeException {
		return read(in, true);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @param in      输入流
	 * @param isClose 读取完毕后是否关闭流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @since 5.5.3
	 */
	public static FastByteArrayOutputStream read(final InputStream in, final boolean isClose) throws IORuntimeException {
		return StreamReader.of(in, isClose).read();
	}

	/**
	 * 从Reader中读取String，读取完毕后关闭Reader
	 *
	 * @param reader Reader
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(final Reader reader) throws IORuntimeException {
		return read(reader, true);
	}

	/**
	 * 从{@link Reader}中读取String
	 *
	 * @param reader  {@link Reader}
	 * @param isClose 是否关闭{@link Reader}
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(final Reader reader, final boolean isClose) throws IORuntimeException {
		final StringBuilder builder = StrUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip());
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isClose) {
				IoUtil.closeQuietly(reader);
			}
		}
		return builder.toString();
	}

	/**
	 * 从流中读取bytes，读取完毕后关闭流
	 *
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(final InputStream in) throws IORuntimeException {
		return readBytes(in, true);
	}

	/**
	 * 从流中读取bytes
	 *
	 * @param in      {@link InputStream}
	 * @param isClose 是否关闭输入流
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 * @since 5.0.4
	 */
	public static byte[] readBytes(final InputStream in, final boolean isClose) throws IORuntimeException {
		return StreamReader.of(in, isClose).readBytes();
	}

	/**
	 * 读取指定长度的byte数组，不关闭流
	 *
	 * @param in     {@link InputStream}，为{@code null}返回{@code null}
	 * @param length 长度，小于等于0返回空byte数组
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(final InputStream in, final int length) throws IORuntimeException {
		return StreamReader.of(in, false).readBytes(length);
	}

	/**
	 * 读取16进制字符串
	 *
	 * @param in          {@link InputStream}
	 * @param length      长度
	 * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex(final InputStream in, final int length, final boolean toLowerCase) throws IORuntimeException {
		return HexUtil.encodeStr(readBytes(in, length), toLowerCase);
	}

	/**
	 * 从流中读取对象，即对象的反序列化，读取后不关闭流
	 *
	 * <p>
	 * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
	 * </p>
	 *
	 * @param <T>           读取对象的类型
	 * @param in            输入流
	 * @param acceptClasses 读取对象类型
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws HutoolException      ClassNotFoundException包装
	 */
	public static <T> T readObj(final InputStream in, final Class<?>... acceptClasses) throws IORuntimeException, HutoolException {
		return StreamReader.of(in, false).readObj(acceptClasses);
	}

	/**
	 * 从流中读取内容，使用UTF-8编码
	 *
	 * @param <T>        集合类型
	 * @param in         输入流
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readUtf8Lines(final InputStream in, final T collection) throws IORuntimeException {
		return readLines(in, CharsetUtil.UTF_8, collection);
	}

	/**
	 * 从流中读取内容
	 *
	 * @param <T>        集合类型
	 * @param in         输入流
	 * @param charset    字符集
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(final InputStream in, final Charset charset, final T collection) throws IORuntimeException {
		return readLines(toReader(in, charset), collection);
	}

	/**
	 * 从Reader中读取内容
	 *
	 * @param <T>        集合类型
	 * @param reader     {@link Reader}
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(final Reader reader, final T collection) throws IORuntimeException {
		readLines(reader, (SerConsumer<String>) collection::add);
		return collection;
	}

	/**
	 * 按行读取UTF-8编码数据，针对每行的数据做处理
	 *
	 * @param in          {@link InputStream}
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static void readUtf8Lines(final InputStream in, final SerConsumer<String> lineHandler) throws IORuntimeException {
		readLines(in, CharsetUtil.UTF_8, lineHandler);
	}

	/**
	 * 按行读取数据，针对每行的数据做处理
	 *
	 * @param in          {@link InputStream}
	 * @param charset     {@link Charset}编码
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void readLines(final InputStream in, final Charset charset, final SerConsumer<String> lineHandler) throws IORuntimeException {
		readLines(toReader(in, charset), lineHandler);
	}

	/**
	 * 按行读取数据，针对每行的数据做处理<br>
	 * {@link Reader}自带编码定义，因此读取数据的编码跟随其编码。<br>
	 * 此方法不会关闭流，除非抛出异常
	 *
	 * @param reader      {@link Reader}
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IORuntimeException IO异常
	 */
	public static void readLines(final Reader reader, final SerConsumer<String> lineHandler) throws IORuntimeException {
		Assert.notNull(reader);
		Assert.notNull(lineHandler);

		for (final String line : lineIter(reader)) {
			lineHandler.accept(line);
		}
	}

	// endregion -------------------------------------------------------------------------------------- read

	// region -------------------------------------------------------------------------------------- toStream

	/**
	 * String 转为UTF-8编码的字节流流
	 *
	 * @param content 内容
	 * @return 字节流
	 * @since 4.5.1
	 */
	public static ByteArrayInputStream toUtf8Stream(final String content) {
		return toStream(content, CharsetUtil.UTF_8);
	}

	/**
	 * String 转为流
	 *
	 * @param content 内容
	 * @param charset 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(final String content, final Charset charset) {
		if (content == null) {
			return null;
		}
		return toStream(ByteUtil.toBytes(content, charset));
	}

	/**
	 * 文件转为{@link InputStream}
	 *
	 * @param file 文件，非空
	 * @return {@link InputStream}
	 */
	public static InputStream toStream(final File file) {
		Assert.notNull(file);
		return toStream(file.toPath());
	}

	/**
	 * 文件转为{@link InputStream}
	 *
	 * @param path {@link Path}，非空
	 * @return {@link InputStream}
	 */
	public static InputStream toStream(final Path path) {
		Assert.notNull(path);
		try {
			return Files.newInputStream(path);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * byte[] 转为{@link ByteArrayInputStream}
	 *
	 * @param content 内容bytes
	 * @return 字节流
	 * @since 4.1.8
	 */
	public static ByteArrayInputStream toStream(final byte[] content) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(content);
	}

	/**
	 * {@link ByteArrayOutputStream}转为{@link ByteArrayInputStream}
	 *
	 * @param out {@link ByteArrayOutputStream}
	 * @return 字节流
	 * @since 5.3.6
	 */
	public static ByteArrayInputStream toStream(final ByteArrayOutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * {@link FastByteArrayOutputStream}转为{@link ByteArrayInputStream}
	 *
	 * @param out {@link FastByteArrayOutputStream}
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(final FastByteArrayOutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BufferedInputStream}
	 * @since 4.0.10
	 */
	public static BufferedInputStream toBuffered(final InputStream in) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in         {@link InputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedInputStream}
	 * @since 5.6.1
	 */
	public static BufferedInputStream toBuffered(final InputStream in, final int bufferSize) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in, bufferSize);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @return {@link BufferedOutputStream}
	 * @since 4.0.10
	 */
	public static BufferedOutputStream toBuffered(final OutputStream out) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out        {@link OutputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedOutputStream}
	 * @since 5.6.1
	 */
	public static BufferedOutputStream toBuffered(final OutputStream out, final int bufferSize) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out, bufferSize);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader {@link Reader}
	 * @return {@link BufferedReader}
	 * @since 5.6.1
	 */
	public static BufferedReader toBuffered(final Reader reader) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader     {@link Reader}
	 * @param bufferSize buffer size
	 * @return {@link BufferedReader}
	 * @since 5.6.1
	 */
	public static BufferedReader toBuffered(final Reader reader, final int bufferSize) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader, bufferSize);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer {@link Writer}
	 * @return {@link BufferedWriter}
	 * @since 5.6.1
	 */
	public static BufferedWriter toBuffered(final Writer writer) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer     {@link Writer}
	 * @param bufferSize buffer size
	 * @return {@link BufferedWriter}
	 * @since 5.6.1
	 */
	public static BufferedWriter toBuffered(final Writer writer, final int bufferSize) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer, bufferSize);
	}

	/**
	 * 将{@link InputStream}转换为支持mark标记的流<br>
	 * 若原流支持mark标记，则返回原流，否则使用{@link BufferedInputStream} 包装之
	 *
	 * @param in 流
	 * @return {@link InputStream}
	 * @since 4.0.9
	 */
	public static InputStream toMarkSupport(final InputStream in) {
		if (null == in) {
			return null;
		}
		if (!in.markSupported()) {
			return new BufferedInputStream(in);
		}
		return in;
	}

	/**
	 * 将{@link Reader}转换为支持mark标记的Reader<br>
	 * 若原Reader支持mark标记，则返回原Reader，否则使用{@link BufferedReader} 包装之
	 *
	 * @param reader {@link Reader}
	 * @return {@link Reader}
	 */
	public static Reader toMarkSupport(final Reader reader) {
		if (null == reader) {
			return null;
		}
		if (!reader.markSupported()) {
			return new BufferedReader(reader);
		}
		return reader;
	}

	/**
	 * 获得{@link PushbackReader}<br>
	 * 如果是{@link PushbackReader}强转返回，否则新建
	 *
	 * @param reader       普通Reader
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackReader}
	 * @since 3.1.0
	 */
	public static PushbackReader toPushBackReader(final Reader reader, final int pushBackSize) {
		return (reader instanceof PushbackReader) ? (PushbackReader) reader : new PushbackReader(reader, pushBackSize);
	}

	/**
	 * 转换为{@link PushbackInputStream}<br>
	 * 如果传入的输入流已经是{@link PushbackInputStream}，强转返回，否则新建一个
	 *
	 * @param in           {@link InputStream}
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackInputStream}
	 * @since 3.1.0
	 */
	public static PushbackInputStream toPushbackStream(final InputStream in, final int pushBackSize) {
		return (in instanceof PushbackInputStream) ? (PushbackInputStream) in : new PushbackInputStream(in, pushBackSize);
	}

	/**
	 * 将指定{@link InputStream} 转换为{@link InputStream#available()}方法可用的流。<br>
	 * 在Socket通信流中，服务端未返回数据情况下{@link InputStream#available()}方法始终为{@code 0}<br>
	 * 因此，在读取前需要调用{@link InputStream#read()}读取一个字节（未返回会阻塞），一旦读取到了，{@link InputStream#available()}方法就正常了。<br>
	 * 需要注意的是，在网络流中，是按照块来传输的，所以 {@link InputStream#available()} 读取到的并非最终长度，而是此次块的长度。<br>
	 * 此方法返回对象的规则为：
	 *
	 * <ul>
	 *     <li>FileInputStream 返回原对象，因为文件流的available方法本身可用</li>
	 *     <li>其它InputStream 返回PushbackInputStream</li>
	 * </ul>
	 *
	 * @param in 被转换的流
	 * @return 转换后的流，可能为{@link PushbackInputStream}
	 * @since 5.5.3
	 */
	public static InputStream toAvailableStream(final InputStream in) {
		if (in instanceof FileInputStream) {
			// FileInputStream本身支持available方法。
			return in;
		}

		final PushbackInputStream pushbackInputStream = toPushbackStream(in, 1);
		try {
			final int available = pushbackInputStream.available();
			if (available <= 0) {
				//此操作会阻塞，直到有数据被读到
				final int b = pushbackInputStream.read();
				pushbackInputStream.unread(b);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return pushbackInputStream;
	}
	// endregion -------------------------------------------------------------------------------------- toStream

	// region ----------------------------------------------------------------------------------------------- write

	/**
	 * 将byte[]写到流中，并关闭目标流
	 *
	 * @param out     输出流
	 * @param content 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeClose(final OutputStream out, final byte[] content) throws IORuntimeException {
		write(out, true, content);
	}

	/**
	 * 将byte[]写到流中，并关闭目标流
	 *
	 * @param out     输出流
	 * @param content 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void write(final OutputStream out, final byte[] content) throws IORuntimeException {
		write(out, false, content);
	}

	/**
	 * 将byte[]写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param content    写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void write(final OutputStream out, final boolean isCloseOut, final byte[] content) throws IORuntimeException {
		StreamWriter.of(out, isCloseOut).write(content);
	}

	/**
	 * 将多部分内容写到流中，自动转换为UTF-8字符串
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static void writeUtf8(final OutputStream out, final boolean isCloseOut, final Object... contents) throws IORuntimeException {
		write(out, CharsetUtil.UTF_8, isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param out        输出流
	 * @param charset    写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void write(final OutputStream out, final Charset charset, final boolean isCloseOut, final Object... contents) throws IORuntimeException {
		StreamWriter.of(out, isCloseOut).writeStr(charset, contents);
	}

	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeObjects(final OutputStream out, final boolean isCloseOut, final Object... contents) throws IORuntimeException {
		StreamWriter.of(out, isCloseOut).writeObj(contents);
	}
	// endregion ----------------------------------------------------------------------------------------------- write

	/**
	 * 从缓存中刷出数据
	 *
	 * @param flushable {@link Flushable}
	 * @since 4.2.2
	 */
	public static void flush(final Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			} catch (final Exception e) {
				// 静默刷出
			}
		}
	}

	/**
	 * 尝试关闭指定对象<br>
	 * 判断对象如果实现了{@link AutoCloseable}，则调用之
	 *
	 * @param obj 可关闭对象
	 * @since 4.3.2
	 */
	public static void closeIfPossible(final Object obj) {
		if (obj instanceof AutoCloseable) {
			closeQuietly((AutoCloseable) obj);
		}
	}

	/**
	 * 按照给定顺序连续关闭一系列对象<br>
	 * 这些对象必须按照顺序关闭，否则会出错。
	 *
	 * @param closeables 需要关闭的对象
	 */
	public static void closeQuietly(final AutoCloseable... closeables) {
		for (final AutoCloseable closeable : closeables) {
			if (null != closeable) {
				try {
					closeable.close();
				} catch (final Exception e) {
					// 静默关闭
				}
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败抛出{@link IOException}异常
	 *
	 * @param closeable 被关闭的对象
	 * @throws IOException IO异常
	 */
	public static void nullSafeClose(final Closeable closeable) throws IOException {
		if (null != closeable) {
			closeable.close();
		}
	}

	/**
	 * 对比两个流内容是否相同<br>
	 * 内部会转换流为 {@link BufferedInputStream}
	 *
	 * @param input1 第一个流
	 * @param input2 第二个流
	 * @return 两个流的内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEquals(InputStream input1, InputStream input2) throws IORuntimeException {
		if (!(input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (!(input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}

		try {
			int ch = input1.read();
			while (EOF != ch) {
				final int ch2 = input2.read();
				if (ch != ch2) {
					return false;
				}
				ch = input1.read();
			}

			final int ch2 = input2.read();
			return ch2 == EOF;
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 对比两个Reader的内容是否一致<br>
	 * 内部会转换流为 {@link BufferedInputStream}
	 *
	 * @param input1 第一个reader
	 * @param input2 第二个reader
	 * @return 两个流的内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEquals(Reader input1, Reader input2) throws IORuntimeException {
		input1 = toBuffered(input1);
		input2 = toBuffered(input2);

		try {
			int ch = input1.read();
			while (EOF != ch) {
				final int ch2 = input2.read();
				if (ch != ch2) {
					return false;
				}
				ch = input1.read();
			}

			final int ch2 = input2.read();
			return ch2 == EOF;
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 对比两个流内容是否相同，忽略EOL字符<br>
	 * 内部会转换流为 {@link BufferedInputStream}
	 *
	 * @param input1 第一个流
	 * @param input2 第二个流
	 * @return 两个流的内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEqualsIgnoreEOL(final Reader input1, final Reader input2) throws IORuntimeException {
		final BufferedReader br1 = toBuffered(input1);
		final BufferedReader br2 = toBuffered(input2);

		try {
			String line1 = br1.readLine();
			String line2 = br2.readLine();
			while (line1 != null && line1.equals(line2)) {
				line1 = br1.readLine();
				line2 = br2.readLine();
			}
			return Objects.equals(line1, line2);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 返回行遍历器
	 * <pre>
	 * LineIterator it = null;
	 * try {
	 * 	it = IoUtil.lineIter(reader);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// do something with line
	 *    }
	 * } finally {
	 * 		it.close();
	 * }
	 * </pre>
	 *
	 * @param reader {@link Reader}
	 * @return {@link LineIter}
	 * @since 5.6.1
	 */
	public static LineIter lineIter(final Reader reader) {
		return new LineIter(reader);
	}

	/**
	 * 返回行遍历器
	 * <pre>
	 * LineIterator it = null;
	 * try {
	 * 	it = IoUtil.lineIter(in, CharsetUtil.CHARSET_UTF_8);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// do something with line
	 *    }
	 * } finally {
	 * 		it.close();
	 * }
	 * </pre>
	 *
	 * @param in      {@link InputStream}
	 * @param charset 编码
	 * @return {@link LineIter}
	 * @since 5.6.1
	 */
	public static LineIter lineIter(final InputStream in, final Charset charset) {
		return new LineIter(in, charset);
	}

	/**
	 * {@link ByteArrayOutputStream} 转换为String
	 *
	 * @param out     {@link ByteArrayOutputStream}
	 * @param charset 编码
	 * @return 字符串
	 * @since 5.7.17
	 */
	public static String toStr(final ByteArrayOutputStream out, final Charset charset) {
		try {
			return out.toString(charset.name());
		} catch (final UnsupportedEncodingException e) {
			throw new IORuntimeException(e);
		}
	}
}
