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
