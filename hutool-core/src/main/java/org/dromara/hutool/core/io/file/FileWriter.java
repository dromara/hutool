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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 文件写入器
 *
 * @author Looly
 */
public class FileWriter extends FileWrapper {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建 FileWriter
	 *
	 * @param file    文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 * @return FileWriter
	 */
	public static FileWriter of(final File file, final Charset charset) {
		return new FileWriter(file, charset);
	}

	/**
	 * 创建 FileWriter, 编码：{@link FileWrapper#DEFAULT_CHARSET}
	 *
	 * @param file 文件
	 * @return FileWriter
	 */
	public static FileWriter of(final File file) {
		return new FileWriter(file);
	}

	// ------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param file    文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 */
	public FileWriter(final File file, final Charset charset) {
		super(file, charset);
		checkFile();
	}

	/**
	 * 构造
	 *
	 * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
	 * @param charset  编码，使用 {@link CharsetUtil}
	 */
	public FileWriter(final String filePath, final Charset charset) {
		this(FileUtil.file(filePath), charset);
	}

	/**
	 * 构造
	 *
	 * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
	 * @param charset  编码，使用 {@link CharsetUtil#charset(String)}
	 */
	public FileWriter(final String filePath, final String charset) {
		this(FileUtil.file(filePath), CharsetUtil.charset(charset));
	}

	/**
	 * 构造<br>
	 * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
	 *
	 * @param file 文件
	 */
	public FileWriter(final File file) {
		this(file, DEFAULT_CHARSET);
	}

	/**
	 * 构造<br>
	 * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
	 *
	 * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
	 */
	public FileWriter(final String filePath) {
		this(filePath, DEFAULT_CHARSET);
	}
	// ------------------------------------------------------- Constructor end

	/**
	 * 将String写入文件
	 *
	 * @param content  写入的内容
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public File write(final String content, final boolean isAppend) throws IORuntimeException {
		BufferedWriter writer = null;
		try {
			writer = getWriter(isAppend);
			writer.write(content);
			writer.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(writer);
		}
		return file;
	}

	/**
	 * 将String写入文件，覆盖模式
	 *
	 * @param content 写入的内容
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public File write(final String content) throws IORuntimeException {
		return write(content, false);
	}

	/**
	 * 将String写入文件，追加模式
	 *
	 * @param content 写入的内容
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public File append(final String content) throws IORuntimeException {
		return write(content, true);
	}

	/**
	 * 将列表写入文件，覆盖模式
	 *
	 * @param <T>  集合元素类型
	 * @param list 列表
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public <T> File writeLines(final Iterable<T> list) throws IORuntimeException {
		return writeLines(list, false);
	}

	/**
	 * 将列表写入文件，追加模式
	 *
	 * @param <T>  集合元素类型
	 * @param list 列表
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public <T> File appendLines(final Iterable<T> list) throws IORuntimeException {
		return writeLines(list, true);
	}

	/**
	 * 将列表写入文件
	 *
	 * @param <T>      集合元素类型
	 * @param list     列表
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public <T> File writeLines(final Iterable<T> list, final boolean isAppend) throws IORuntimeException {
		return writeLines(list, null, isAppend);
	}

	/**
	 * 将列表写入文件
	 *
	 * @param <T>           集合元素类型
	 * @param list          列表
	 * @param lineSeparator 换行符枚举（Windows、Mac或Linux换行符）
	 * @param isAppend      是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public <T> File writeLines(final Iterable<T> list, final LineSeparator lineSeparator, final boolean isAppend) throws IORuntimeException {
		try (final PrintWriter writer = getPrintWriter(isAppend)) {
			boolean isFirst = true;
			for (final T t : list) {
				if (null != t) {
					if(isFirst){
						isFirst = false;
						if(isAppend && FileUtil.isNotEmpty(this.file)){
							// 追加模式下且文件非空，补充换行符
							printNewLine(writer, lineSeparator);
						}
					} else{
						printNewLine(writer, lineSeparator);
					}
					writer.print(t);

					writer.flush();
				}
			}
		}
		return this.file;
	}

	/**
	 * 将Map写入文件，每个键值对为一行，一行中键与值之间使用kvSeparator分隔
	 *
	 * @param map         Map
	 * @param kvSeparator 键和值之间的分隔符，如果传入null使用默认分隔符" = "
	 * @param isAppend    是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 4.0.5
	 */
	public File writeMap(final Map<?, ?> map, final String kvSeparator, final boolean isAppend) throws IORuntimeException {
		return writeMap(map, null, kvSeparator, isAppend);
	}

	/**
	 * 将Map写入文件，每个键值对为一行，一行中键与值之间使用kvSeparator分隔
	 *
	 * @param map           Map
	 * @param lineSeparator 换行符枚举（Windows、Mac或Linux换行符）
	 * @param kvSeparator   键和值之间的分隔符，如果传入null使用默认分隔符" = "
	 * @param isAppend      是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 4.0.5
	 */
	public File writeMap(final Map<?, ?> map, final LineSeparator lineSeparator, String kvSeparator, final boolean isAppend) throws IORuntimeException {
		if (null == kvSeparator) {
			kvSeparator = " = ";
		}
		try (final PrintWriter writer = getPrintWriter(isAppend)) {
			for (final Entry<?, ?> entry : map.entrySet()) {
				if (null != entry) {
					writer.print(StrUtil.format("{}{}{}", entry.getKey(), kvSeparator, entry.getValue()));
					printNewLine(writer, lineSeparator);
					writer.flush();
				}
			}
		}
		return this.file;
	}

	/**
	 * 写入数据到文件
	 *
	 * @param data 数据
	 * @param off  数据开始位置
	 * @param len  数据长度
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public File write(final byte[] data, final int off, final int len) throws IORuntimeException {
		return write(data, off, len, false);
	}

	/**
	 * 追加数据到文件
	 *
	 * @param data 数据
	 * @param off  数据开始位置
	 * @param len  数据长度
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public File append(final byte[] data, final int off, final int len) throws IORuntimeException {
		return write(data, off, len, true);
	}

	/**
	 * 写入数据到文件
	 *
	 * @param data     数据
	 * @param off      数据开始位置
	 * @param len      数据长度
	 * @param isAppend 是否追加模式
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public File write(final byte[] data, final int off, final int len, final boolean isAppend) throws IORuntimeException {
		try (final FileOutputStream out = new FileOutputStream(FileUtil.touch(file), isAppend)) {
			out.write(data, off, len);
			out.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return file;
	}

	/**
	 * 将流的内容写入文件<br>
	 * 此方法会自动关闭输入流
	 *
	 * @param in 输入流，不关闭
	 * @return dest
	 * @throws IORuntimeException IO异常
	 */
	public File writeFromStream(final InputStream in) throws IORuntimeException {
		return writeFromStream(in, true);
	}

	/**
	 * 将流的内容写入文件
	 *
	 * @param in        输入流，不关闭
	 * @param isCloseIn 是否关闭输入流
	 * @return dest
	 * @throws IORuntimeException IO异常
	 * @since 5.5.2
	 */
	public File writeFromStream(final InputStream in, final boolean isCloseIn) throws IORuntimeException {
		OutputStream out = null;
		try {
			out = Files.newOutputStream(FileUtil.touch(file).toPath());
			IoUtil.copy(in, out);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(out);
			if (isCloseIn) {
				IoUtil.closeQuietly(in);
			}
		}
		return file;
	}

	/**
	 * 获得一个输出流对象
	 *
	 * @return 输出流对象
	 * @throws IORuntimeException IO异常
	 */
	public BufferedOutputStream getOutputStream() throws IORuntimeException {
		try {
			return new BufferedOutputStream(Files.newOutputStream(FileUtil.touch(file).toPath()));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得一个带缓存的写入对象
	 *
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public BufferedWriter getWriter(final boolean isAppend) throws IORuntimeException {
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileUtil.touch(file), isAppend), charset));
		} catch (final Exception e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得一个打印写入对象，可以有print
	 *
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IORuntimeException IO异常
	 */
	public PrintWriter getPrintWriter(final boolean isAppend) throws IORuntimeException {
		return new PrintWriter(getWriter(isAppend));
	}

	/**
	 * 检查文件
	 *
	 * @throws IORuntimeException IO异常
	 */
	private void checkFile() throws IORuntimeException {
		Assert.notNull(file, "File to write content is null !");
		if (this.file.exists() && ! file.isFile()) {
			throw new IORuntimeException("File [{}] is not a file !", this.file.getAbsoluteFile());
		}
	}

	/**
	 * 打印新行
	 *
	 * @param writer        Writer
	 * @param lineSeparator 换行符枚举
	 * @since 4.0.5
	 */
	private void printNewLine(final PrintWriter writer, final LineSeparator lineSeparator) {
		if (null == lineSeparator) {
			//默认换行符
			writer.println();
		} else {
			//自定义换行符
			writer.print(lineSeparator.getValue());
		}
	}
}
