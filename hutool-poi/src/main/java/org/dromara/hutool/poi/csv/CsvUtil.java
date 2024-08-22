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

package org.dromara.hutool.poi.csv;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * CSV工具
 *
 * @author looly
 * @since 4.0.5
 */
public class CsvUtil {

	//----------------------------------------------------------------------------------------------------------- Reader

	/**
	 * 获取CSV读取器，调用此方法创建的Reader须自行指定读取的资源
	 *
	 * @param config 配置, 允许为空.
	 * @return {@link CsvReader}
	 */
	public static CsvReader getReader(final CsvReadConfig config) {
		return new CsvReader(config);
	}

	/**
	 * 获取CSV读取器，调用此方法创建的Reader须自行指定读取的资源
	 *
	 * @return {@link CsvReader}
	 */
	public static CsvReader getReader() {
		return new CsvReader();
	}

	/**
	 * 获取CSV读取器
	 *
	 * @param reader {@link Reader}
	 * @param config 配置, {@code null}表示默认配置
	 * @return {@link CsvReader}
	 * @since 5.7.14
	 */
	public static CsvReader getReader(final Reader reader, final CsvReadConfig config) {
		return new CsvReader(reader, config);
	}

	/**
	 * 获取CSV读取器
	 *
	 * @param reader {@link Reader}
	 * @return {@link CsvReader}
	 * @since 5.7.14
	 */
	public static CsvReader getReader(final Reader reader) {
		return getReader(reader, null);
	}

	//----------------------------------------------------------------------------------------------------------- Writer

	/**
	 * 获取CSV生成器（写出器），使用默认配置，覆盖已有文件（如果存在）
	 *
	 * @param filePath File CSV文件路径
	 * @param charset  编码
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final String filePath, final Charset charset) {
		return new CsvWriter(filePath, charset);
	}

	/**
	 * 获取CSV生成器（写出器），使用默认配置，覆盖已有文件（如果存在）
	 *
	 * @param file    File CSV文件
	 * @param charset 编码
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final File file, final Charset charset) {
		return new CsvWriter(file, charset);
	}

	/**
	 * 获取CSV生成器（写出器），使用默认配置
	 *
	 * @param filePath File CSV文件路径
	 * @param charset  编码
	 * @param isAppend 是否追加
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final String filePath, final Charset charset, final boolean isAppend) {
		return new CsvWriter(filePath, charset, isAppend);
	}

	/**
	 * 获取CSV生成器（写出器），使用默认配置
	 *
	 * @param file     File CSV文件
	 * @param charset  编码
	 * @param isAppend 是否追加
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final File file, final Charset charset, final boolean isAppend) {
		return new CsvWriter(file, charset, isAppend);
	}

	/**
	 * 获取CSV生成器（写出器）
	 *
	 * @param file     File CSV文件
	 * @param charset  编码
	 * @param isAppend 是否追加
	 * @param config   写出配置，null则使用默认配置
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final File file, final Charset charset, final boolean isAppend, final CsvWriteConfig config) {
		return new CsvWriter(file, charset, isAppend, config);
	}

	/**
	 * 获取CSV生成器（写出器）
	 *
	 * @param writer Writer
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final Writer writer) {
		return new CsvWriter(writer);
	}

	/**
	 * 获取CSV生成器（写出器）
	 *
	 * @param writer Writer
	 * @param config 写出配置，null则使用默认配置
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(final Writer writer, final CsvWriteConfig config) {
		return new CsvWriter(writer, config);
	}
}
