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

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.func.SerConsumer;
import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * 文件读取器
 *
 * @author Looly
 *
 */
public class FileReader extends FileWrapper {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建 FileReader
	 * @param file 文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 * @return FileReader
	 */
	public static FileReader of(final File file, final Charset charset){
		return new FileReader(file, charset);
	}

	/**
	 * 创建 FileReader, 编码：{@link FileWrapper#DEFAULT_CHARSET}
	 * @param file 文件
	 * @return FileReader
	 */
	public static FileReader of(final File file){
		return new FileReader(FileUtil.file(file), DEFAULT_CHARSET);
	}

	// ------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param file 文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 */
	public FileReader(final File file, final Charset charset) {
		super(file, charset);
		checkFile();
	}
	// ------------------------------------------------------- Constructor end

	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过 {@link Integer#MAX_VALUE}
	 *
	 * @return 字节码
	 * @throws IORuntimeException IO异常
	 */
	public byte[] readBytes() throws IORuntimeException {
		final long len = this.file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IORuntimeException("File is larger then max array size");
		}

		final byte[] bytes = new byte[(int) len];
		InputStream in = null;
		final int readLength;
		try {
			in = FileUtil.getInputStream(this.file);
			readLength = in.read(bytes);
			if(readLength < len){
				throw new IOException(StrUtil.format("File length is [{}] but read [{}]!", len, readLength));
			}
		} catch (final Exception e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(in);
		}

		return bytes;
	}

	/**
	 * 读取文件内容
	 *
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public String readString() throws IORuntimeException{
		return new String(readBytes(), this.charset);
	}

	/**
	 * 从文件中读取每一行数据
	 *
	 * @param <T> 集合类型
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public <T extends Collection<String>> T readLines(final T collection) throws IORuntimeException {
		return readLines(collection, null);
	}

	/**
	 * 从文件中读取每一行数据
	 *
	 * @param <T> 集合类型
	 * @param collection 集合
	 * @param predicate 断言，断言为真的加入到提供的集合中
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public <T extends Collection<String>> T readLines(final T collection, final Predicate<String> predicate) throws IORuntimeException {
		readLines((SerConsumer<String>) s -> {
			if(null == predicate || predicate.test(s)){
				collection.add(s);
			}
		});
		return collection;
	}

	/**
	 * 按照行处理文件内容
	 *
	 * @param lineHandler 行处理器
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public void readLines(final SerConsumer<String> lineHandler) throws IORuntimeException{
		BufferedReader reader = null;
		try {
			reader = FileUtil.getReader(file, charset);
			IoUtil.readLines(reader, lineHandler);
		} finally {
			IoUtil.closeQuietly(reader);
		}
	}

	/**
	 * 从文件中读取每一行数据
	 *
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public List<String> readLines() throws IORuntimeException {
		return readLines(new ArrayList<>());
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 *
	 * @param <T> 读取的结果对象类型
	 * @param readerHandler Reader处理类
	 * @return 从文件中read出的数据
	 * @throws IORuntimeException IO异常
	 */
	public <T> T read(final SerFunction<BufferedReader, T> readerHandler) throws IORuntimeException {
		BufferedReader reader = null;
		T result;
		try {
			reader = FileUtil.getReader(this.file, charset);
			result = readerHandler.applying(reader);
		} catch (final Exception e) {
			if(e instanceof IOException){
				throw new IORuntimeException(e);
			} else if(e instanceof RuntimeException){
				throw (RuntimeException)e;
			} else{
				throw new UtilException(e);
			}
		} finally {
			IoUtil.closeQuietly(reader);
		}
		return result;
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public BufferedReader getReader() throws IORuntimeException {
		return IoUtil.toReader(getInputStream(), this.charset);
	}

	/**
	 * 获得输入流
	 *
	 * @return 输入流
	 * @throws IORuntimeException IO异常
	 */
	public BufferedInputStream getInputStream() throws IORuntimeException {
		try {
			return new BufferedInputStream(Files.newInputStream(this.file.toPath()));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 将文件写入流中，此方法不会关闭比输出流
	 *
	 * @param out 流
	 * @return 写出的流byte数
	 * @throws IORuntimeException IO异常
	 */
	public long writeToStream(final OutputStream out) throws IORuntimeException {
		return writeToStream(out, false);
	}

	/**
	 * 将文件写入流中
	 *
	 * @param out 流
	 * @param isCloseOut 是否关闭输出流
	 * @return 写出的流byte数
	 * @throws IORuntimeException IO异常
	 * @since 5.5.2
	 */
	public long writeToStream(final OutputStream out, final boolean isCloseOut) throws IORuntimeException {
		try (final FileInputStream in = new FileInputStream(this.file)){
			return IoUtil.copy(in, out);
		}catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally{
			if(isCloseOut){
				IoUtil.closeQuietly(out);
			}
		}
	}

	/**
	 * 检查文件
	 *
	 * @throws IORuntimeException IO异常
	 */
	private void checkFile() throws IORuntimeException {
		if (false == file.exists()) {
			throw new IORuntimeException("File not exist: " + file);
		}
		if (false == file.isFile()) {
			throw new IORuntimeException("Not a file:" + file);
		}
	}
}
