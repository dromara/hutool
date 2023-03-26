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

package cn.hutool.poi.csv;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.SerConsumer;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjUtil;

import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CSV文件读取器基础类，提供灵活的文件、路径中的CSV读取，一次构造可多次调用读取不同数据，参考：FastCSV
 *
 * @author Looly
 * @since 5.0.4
 */
public class CsvBaseReader implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认编码
	 */
	protected static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	private final CsvReadConfig config;

	//--------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，使用默认配置项
	 */
	public CsvBaseReader() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param config 配置项
	 */
	public CsvBaseReader(final CsvReadConfig config) {
		this.config = ObjUtil.defaultIfNull(config, CsvReadConfig::defaultConfig);
	}
	//--------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 设置字段分隔符，默认逗号','
	 *
	 * @param fieldSeparator 字段分隔符，默认逗号','
	 */
	public void setFieldSeparator(final char fieldSeparator) {
		this.config.setFieldSeparator(fieldSeparator);
	}

	/**
	 * 设置 文本分隔符，文本包装符，默认双引号'"'
	 *
	 * @param textDelimiter 文本分隔符，文本包装符，默认双引号'"'
	 */
	public void setTextDelimiter(final char textDelimiter) {
		this.config.setTextDelimiter(textDelimiter);
	}

	/**
	 * 设置是否首行做为标题行，默认false
	 *
	 * @param containsHeader 是否首行做为标题行，默认false
	 */
	public void setContainsHeader(final boolean containsHeader) {
		this.config.setContainsHeader(containsHeader);
	}

	/**
	 * 设置是否跳过空白行，默认true
	 *
	 * @param skipEmptyRows 是否跳过空白行，默认true
	 */
	public void setSkipEmptyRows(final boolean skipEmptyRows) {
		this.config.setSkipEmptyRows(skipEmptyRows);
	}

	/**
	 * 设置每行字段个数不同时是否抛出异常，默认false
	 *
	 * @param errorOnDifferentFieldCount 每行字段个数不同时是否抛出异常，默认false
	 */
	public void setErrorOnDifferentFieldCount(final boolean errorOnDifferentFieldCount) {
		this.config.setErrorOnDifferentFieldCount(errorOnDifferentFieldCount);
	}

	/**
	 * 读取CSV文件，默认UTF-8编码
	 *
	 * @param file CSV文件
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(final File file) throws IORuntimeException {
		return read(file, DEFAULT_CHARSET);
	}

	/**
	 * 从字符串中读取CSV数据
	 *
	 * @param csvStr CSV字符串
	 * @return {@link CsvData}，包含数据列表和行信息
	 */
	public CsvData readFromStr(final String csvStr) {
		return read(new StringReader(csvStr));
	}

	/**
	 * 从字符串中读取CSV数据
	 *
	 * @param csvStr     CSV字符串
	 * @param rowHandler 行处理器，用于一行一行的处理数据
	 */
	public void readFromStr(final String csvStr, final SerConsumer<CsvRow> rowHandler) {
		read(parse(new StringReader(csvStr)), rowHandler);
	}


	/**
	 * 读取CSV文件
	 *
	 * @param file    CSV文件
	 * @param charset 文件编码，默认系统编码
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(final File file, final Charset charset) throws IORuntimeException {
		return read(Objects.requireNonNull(file.toPath(), "file must not be null"), charset);
	}

	/**
	 * 读取CSV文件，默认UTF-8编码
	 *
	 * @param path CSV文件
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(final Path path) throws IORuntimeException {
		return read(path, DEFAULT_CHARSET);
	}

	/**
	 * 读取CSV文件
	 *
	 * @param path    CSV文件
	 * @param charset 文件编码，默认系统编码
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(final Path path, final Charset charset) throws IORuntimeException {
		Assert.notNull(path, "path must not be null");
		return read(FileUtil.getReader(path, charset));
	}

	/**
	 * 从Reader中读取CSV数据，读取后关闭Reader
	 *
	 * @param reader Reader
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(final Reader reader) throws IORuntimeException {
		final CsvParser csvParser = parse(reader);
		final List<CsvRow> rows = new ArrayList<>();
		read(csvParser, rows::add);
		final List<String> header = config.headerLineNo > -1 ? csvParser.getHeader() : null;

		return new CsvData(header, rows);
	}

	/**
	 * 从Reader中读取CSV数据，结果为Map，读取后关闭Reader。<br>
	 * 此方法默认识别首行为标题行。
	 *
	 * @param reader Reader
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public List<Map<String, String>> readMapList(final Reader reader) throws IORuntimeException {
		// 此方法必须包含标题
		this.config.setContainsHeader(true);

		final List<Map<String, String>> result = new ArrayList<>();
		read(reader, (row) -> result.add(row.getFieldMap()));
		return result;
	}

	/**
	 * 从Reader中读取CSV数据并转换为Bean列表，读取后关闭Reader。<br>
	 * 此方法默认识别首行为标题行。
	 *
	 * @param <T>    Bean类型
	 * @param reader Reader
	 * @param clazz  Bean类型
	 * @return Bean列表
	 */
	public <T> List<T> read(final Reader reader, final Class<T> clazz) {
		// 此方法必须包含标题
		this.config.setContainsHeader(true);

		final List<T> result = new ArrayList<>();
		read(reader, (row) -> result.add(row.toBean(clazz)));
		return result;
	}

	/**
	 * 从字符串中读取CSV数据并转换为Bean列表，读取后关闭Reader。<br>
	 * 此方法默认识别首行为标题行。
	 *
	 * @param <T>    Bean类型
	 * @param csvStr csv字符串
	 * @param clazz  Bean类型
	 * @return Bean列表
	 */
	public <T> List<T> read(final String csvStr, final Class<T> clazz) {
		// 此方法必须包含标题
		this.config.setContainsHeader(true);

		final List<T> result = new ArrayList<>();
		read(new StringReader(csvStr), (row) -> result.add(row.toBean(clazz)));
		return result;
	}

	/**
	 * 从Reader中读取CSV数据，读取后关闭Reader
	 *
	 * @param reader     Reader
	 * @param rowHandler 行处理器，用于一行一行的处理数据
	 * @throws IORuntimeException IO异常
	 */
	public void read(final Reader reader, final SerConsumer<CsvRow> rowHandler) throws IORuntimeException {
		read(parse(reader), rowHandler);
	}

	//--------------------------------------------------------------------------------------------- Private method start

	/**
	 * 读取CSV数据，读取后关闭Parser
	 *
	 * @param csvParser  CSV解析器
	 * @param rowHandler 行处理器，用于一行一行的处理数据
	 * @throws IORuntimeException IO异常
	 * @since 5.0.4
	 */
	private void read(final CsvParser csvParser, final SerConsumer<CsvRow> rowHandler) throws IORuntimeException {
		try {
			while (csvParser.hasNext()){
				rowHandler.accept(csvParser.next());
			}
		} finally {
			IoUtil.closeQuietly(csvParser);
		}
	}

	/**
	 * 构建 {@link CsvParser}
	 *
	 * @param reader Reader
	 * @return CsvParser
	 * @throws IORuntimeException IO异常
	 */
	protected CsvParser parse(final Reader reader) throws IORuntimeException {
		return new CsvParser(reader, this.config);
	}
	//--------------------------------------------------------------------------------------------- Private method start
}
