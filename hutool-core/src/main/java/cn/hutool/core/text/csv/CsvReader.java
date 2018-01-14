package cn.hutool.core.text.csv;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * CSV文件读取器，参考：FastCSV
 *
 * @author Looly
 * @since 4.0.1
 */
public final class CsvReader {

	CsvReadConfig config;

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
	public CsvReader(CsvReadConfig config) {
		this.config = ObjectUtil.defaultIfNull(config, CsvReadConfig.defaultConfig());
	}

	/**
	 * 设置字段分隔符，默认逗号','
	 * 
	 * @param fieldSeparator 字段分隔符，默认逗号','
	 */
	public void setFieldSeparator(char fieldSeparator) {
		this.config.setFieldSeparator(fieldSeparator);
	}

	/**
	 * 设置 文本分隔符，文本包装符，默认双引号'"'
	 * 
	 * @param textDelimiter 文本分隔符，文本包装符，默认双引号'"'
	 */
	public void setTextDelimiter(char textDelimiter) {
		this.config.setTextDelimiter(textDelimiter);
	}

	/**
	 * 设置是否首行做为标题行，默认false
	 * 
	 * @param containsHeader 是否首行做为标题行，默认false
	 */
	public void setContainsHeader(boolean containsHeader) {
		this.config.setContainsHeader(containsHeader);
	}

	/**
	 * 设置是否跳过空白行，默认true
	 * 
	 * @param skipEmptyRows 是否跳过空白行，默认true
	 */
	public void setSkipEmptyRows(boolean skipEmptyRows) {
		this.config.setSkipEmptyRows(skipEmptyRows);
	}

	/**
	 * 设置每行字段个数不同时是否抛出异常，默认false
	 * 
	 * @param errorOnDifferentFieldCount 每行字段个数不同时是否抛出异常，默认false
	 */
	public void setErrorOnDifferentFieldCount(boolean errorOnDifferentFieldCount) {
		this.setErrorOnDifferentFieldCount(errorOnDifferentFieldCount);
	}

	/**
	 * 读取CSV文件，默认UTF-8编码
	 *
	 * @param file CSV文件
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(File file) throws IORuntimeException {
		return read(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取CSV文件
	 *
	 * @param file CSV文件
	 * @param charset 文件编码，默认系统编码
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(File file, Charset charset) throws IORuntimeException {
		return read(Objects.requireNonNull(file.toPath(), "file must not be null"), charset);
	}

	/**
	 * 读取CSV文件，默认UTF-8编码
	 *
	 * @param path CSV文件
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(Path path) throws IORuntimeException {
		return read(path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取CSV文件
	 *
	 * @param path CSV文件
	 * @param charset 文件编码，默认系统编码
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(Path path, Charset charset) throws IORuntimeException {
		Assert.notNull(path, "path must not be null");
		try (Reader reader = FileUtil.getReader(path, charset)) {
			return read(reader);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 从Reader中读取CSV数据
	 *
	 * @param reader Reader
	 * @return {@link CsvData}，包含数据列表和行信息
	 * @throws IORuntimeException IO异常
	 */
	public CsvData read(Reader reader) throws IORuntimeException {
		final CsvParser csvParser = parse(reader);

		final List<CsvRow> rows = new ArrayList<>();
		CsvRow csvRow;
		while ((csvRow = csvParser.nextRow()) != null) {
			rows.add(csvRow);
		}

		final List<String> header = config.containsHeader ? csvParser.getHeader() : null;
		return new CsvData(header, rows);
	}

	/**
	 * 构建 {@link CsvParser}
	 *
	 * @param reader Reader
	 * @return CsvParser
	 * @throws IORuntimeException IO异常
	 */
	private CsvParser parse(Reader reader) throws IORuntimeException {
		return new CsvParser(reader, config);
	}
}
