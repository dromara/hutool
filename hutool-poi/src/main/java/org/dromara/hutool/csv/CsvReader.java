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

package org.dromara.hutool.csv;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.lang.func.SerConsumer;

import java.io.Closeable;
import java.io.File;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * CSV文件读取器，参考：FastCSV
 *
 * @author Looly
 * @since 4.0.1
 */
public class CsvReader extends CsvBaseReader implements Iterable<CsvRow>, Closeable {
	private static final long serialVersionUID = 1L;

	private final Reader reader;

	//--------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，使用默认配置项
	 */
	public CsvReader() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param config 配置项
	 */
	public CsvReader(final CsvReadConfig config) {
		this((Reader) null, config);
	}

	/**
	 * 构造，默认{@link #DEFAULT_CHARSET}编码
	 *
	 * @param file   CSV文件路径，null表示不设置路径
	 * @param config 配置项，null表示默认配置
	 * @since 5.0.4
	 */
	public CsvReader(final File file, final CsvReadConfig config) {
		this(file, DEFAULT_CHARSET, config);
	}

	/**
	 * 构造，默认{@link #DEFAULT_CHARSET}编码
	 *
	 * @param path   CSV文件路径，null表示不设置路径
	 * @param config 配置项，null表示默认配置
	 * @since 5.0.4
	 */
	public CsvReader(final Path path, final CsvReadConfig config) {
		this(path, DEFAULT_CHARSET, config);
	}

	/**
	 * 构造
	 *
	 * @param file    CSV文件路径，null表示不设置路径
	 * @param charset 编码
	 * @param config  配置项，null表示默认配置
	 * @since 5.0.4
	 */
	public CsvReader(final File file, final Charset charset, final CsvReadConfig config) {
		this(FileUtil.getReader(file, charset), config);
	}

	/**
	 * 构造
	 *
	 * @param path    CSV文件路径，null表示不设置路径
	 * @param charset 编码
	 * @param config  配置项，null表示默认配置
	 * @since 5.0.4
	 */
	public CsvReader(final Path path, final Charset charset, final CsvReadConfig config) {
		this(FileUtil.getReader(path, charset), config);
	}

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}，null表示不设置默认reader
	 * @param config 配置项，null表示默认配置
	 * @since 5.0.4
	 */
	public CsvReader(final Reader reader, final CsvReadConfig config) {
		super(config);
		this.reader = reader;
	}
	//--------------------------------------------------------------------------------------------- Constructor end
	/**
	 * 读取CSV文件，此方法只能调用一次<br>
	 * 调用此方法的前提是构造中传入文件路径或Reader
	 *
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read() throws IORuntimeException {
		return read(this.reader);
	}

	/**
	 * 读取CSV数据，此方法只能调用一次<br>
	 * 调用此方法的前提是构造中传入文件路径或Reader
	 *
	 * @param rowHandler 行处理器，用于一行一行的处理数据
	 * @throws IORuntimeException IO异常
	 * @since 5.0.4
	 */
	public void read(final SerConsumer<CsvRow> rowHandler) throws IORuntimeException {
		read(this.reader, rowHandler);
	}

	/**
	 * 根据Reader创建{@link Stream}，以便使用stream方式读取csv行
	 *
	 * @return {@link Stream}
	 * @since 5.7.14
	 */
	public Stream<CsvRow> stream() {
		return StreamSupport
				.stream(spliterator(), false)
				.onClose(this::close);
	}

	@Override
	public Iterator<CsvRow> iterator() {
		return parse(this.reader);
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.reader);
	}
}
