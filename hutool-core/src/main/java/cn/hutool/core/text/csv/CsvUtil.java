package cn.hutool.core.text.csv;

import java.io.File;
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
	 * 获取CSV读取器
	 *
	 * @param config 配置, 允许为空.
	 * @return {@link CsvReader}
	 */
	public static CsvReader getReader(CsvReadConfig config) {
		return new CsvReader(config);
	}

	/**
	 * 获取CSV读取器
	 *
	 * @return {@link CsvReader}
	 */
	public static CsvReader getReader() {
		return new CsvReader();
	}

	//----------------------------------------------------------------------------------------------------------- Writer

	/**
	 * 获取CSV生成器（写出器），使用默认配置，覆盖已有文件（如果存在）
	 *
	 * @param filePath File CSV文件路径
	 * @param charset  编码
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(String filePath, Charset charset) {
		return new CsvWriter(filePath, charset);
	}

	/**
	 * 获取CSV生成器（写出器），使用默认配置，覆盖已有文件（如果存在）
	 *
	 * @param file    File CSV文件
	 * @param charset 编码
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(File file, Charset charset) {
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
	public static CsvWriter getWriter(String filePath, Charset charset, boolean isAppend) {
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
	public static CsvWriter getWriter(File file, Charset charset, boolean isAppend) {
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
	public static CsvWriter getWriter(File file, Charset charset, boolean isAppend, CsvWriteConfig config) {
		return new CsvWriter(file, charset, isAppend, config);
	}

	/**
	 * 获取CSV生成器（写出器）
	 *
	 * @param writer Writer
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(Writer writer) {
		return new CsvWriter(writer);
	}

	/**
	 * 获取CSV生成器（写出器）
	 *
	 * @param writer Writer
	 * @param config 写出配置，null则使用默认配置
	 * @return {@link CsvWriter}
	 */
	public static CsvWriter getWriter(Writer writer, CsvWriteConfig config) {
		return new CsvWriter(writer, config);
	}
}
