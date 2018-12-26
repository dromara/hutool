package cn.hutool.core.text.csv;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * CSV数据写出器
 *
 * @author Looly
 * @since 4.0.5
 */
public final class CsvWriter implements Closeable, Flushable {

	/** 写出器 */
	private final Writer writer;
	/** 写出配置 */
	private final CsvWriteConfig config;
	/** 是否处于新行开始 */
	private boolean newline = true;

	// --------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，覆盖已有文件（如果存在），默认编码UTF-8
	 * 
	 * @param filePath File CSV文件路径
	 */
	public CsvWriter(String filePath) {
		this(FileUtil.file(filePath));
	}

	/**
	 * 构造，覆盖已有文件（如果存在），默认编码UTF-8
	 * 
	 * @param file File CSV文件
	 */
	public CsvWriter(File file) {
		this(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 构造，覆盖已有文件（如果存在）
	 * 
	 * @param filePath File CSV文件路径
	 * @param charset 编码
	 */
	public CsvWriter(String filePath, Charset charset) {
		this(FileUtil.file(filePath), charset);
	}

	/**
	 * 构造，覆盖已有文件（如果存在）
	 * 
	 * @param file File CSV文件
	 * @param charset 编码
	 */
	public CsvWriter(File file, Charset charset) {
		this(file, charset, false);
	}

	/**
	 * 构造
	 * 
	 * @param filePath File CSV文件路径
	 * @param charset 编码
	 * @param isAppend 是否追加
	 */
	public CsvWriter(String filePath, Charset charset, boolean isAppend) {
		this(FileUtil.file(filePath), charset, isAppend);
	}

	/**
	 * 构造
	 * 
	 * @param file CSV文件
	 * @param charset 编码
	 * @param isAppend 是否追加
	 */
	public CsvWriter(File file, Charset charset, boolean isAppend) {
		this(file, charset, isAppend, null);
	}

	/**
	 * 构造
	 * 
	 * @param filePath CSV文件路径
	 * @param charset 编码
	 * @param isAppend 是否追加
	 * @param config 写出配置，null则使用默认配置
	 */
	public CsvWriter(String filePath, Charset charset, boolean isAppend, CsvWriteConfig config) {
		this(FileUtil.file(filePath), charset, isAppend, config);
	}

	/**
	 * 构造
	 * 
	 * @param file CSV文件
	 * @param charset 编码
	 * @param isAppend 是否追加
	 * @param config 写出配置，null则使用默认配置
	 */
	public CsvWriter(File file, Charset charset, boolean isAppend, CsvWriteConfig config) {
		this(FileUtil.getWriter(file, charset, isAppend), config);
	}

	/**
	 * 构造，使用默认配置
	 * 
	 * @param writer {@link Writer}
	 */
	public CsvWriter(Writer writer) {
		this(writer, null);
	}

	/**
	 * 构造
	 * 
	 * @param writer Writer
	 * @param config 写出配置，null则使用默认配置
	 */
	public CsvWriter(Writer writer, CsvWriteConfig config) {
		this.writer = (writer instanceof BufferedWriter) ? writer : new BufferedWriter(writer);
		this.config = ObjectUtil.defaultIfNull(config, CsvWriteConfig.defaultConfig());
	}
	// --------------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 设置是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 * 
	 * @param alwaysDelimitText 是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 * @return this
	 */
	public CsvWriter setAlwaysDelimitText(boolean alwaysDelimitText) {
		this.config.setAlwaysDelimitText(alwaysDelimitText);
		return this;
	}

	/**
	 * 设置换行符
	 * 
	 * @param lineDelimiter 换行符
	 * @return this
	 */
	public CsvWriter setLineDelimiter(char[] lineDelimiter) {
		this.config.setLineDelimiter(lineDelimiter);
		return this;
	}

	/**
	 * 将多行写出到Writer
	 * 
	 * @param lines 多行数据
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public CsvWriter write(String[]... lines) throws IORuntimeException {
		if (ArrayUtil.isNotEmpty(lines)) {
			for (final String[] values : lines) {
				appendLine(values);
			}
			flush();
		}
		return this;
	}

	/**
	 * 将多行写出到Writer
	 * 
	 * @param lines 多行数据
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public CsvWriter write(Collection<String[]> lines) throws IORuntimeException {
		if (CollUtil.isNotEmpty(lines)) {
			for (final String[] values : lines) {
				appendLine(values);
			}
			flush();
		}
		return this;
	}

	/**
	 * 追加新行（换行）
	 *
	 * @throws IORuntimeException IO异常
	 */
	public void writeLine() throws IORuntimeException {
		try {
			writer.write(config.lineDelimiter);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		newline = true;
	}

	@Override
	public void close() {
		IoUtil.close(this.writer);
	}

	@Override
	public void flush() throws IORuntimeException {
		try {
			writer.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// --------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 追加一行，末尾会自动换行，但是追加前不会换行
	 *
	 * @param fields 字段列表 ({@code null} 值会被做为空值追加)
	 * @throws IORuntimeException IO异常
	 */
	private void appendLine(final String... fields) throws IORuntimeException {
		try {
			doAppendLine(fields);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 追加一行，末尾会自动换行，但是追加前不会换行
	 *
	 * @param fields 字段列表 ({@code null} 值会被做为空值追加)
	 * @throws IOException IO异常
	 */
	private void doAppendLine(final String... fields) throws IOException {
		if (null != fields) {
			for (int i = 0; i < fields.length; i++) {
				appendField(fields[i]);
			}
			writer.write(config.lineDelimiter);
			newline = true;
		}
	}

	/**
	 * 在当前行追加字段值，自动添加字段分隔符，如果有必要，自动包装字段
	 *
	 * @param value 字段值，{@code null} 会被做为空串写出
	 * @throws IOException IO异常
	 */
	private void appendField(final String value) throws IOException {
		boolean alwaysDelimitText = config.alwaysDelimitText;
		char textDelimiter = config.textDelimiter;
		char fieldSeparator = config.fieldSeparator;

		if (false == newline) {
			writer.write(fieldSeparator);
		} else {
			newline = false;
		}

		if (null == value) {
			if (alwaysDelimitText) {
				writer.write(new char[] { textDelimiter, textDelimiter });
			}
			return;
		}

		final char[] valueChars = value.toCharArray();
		boolean needsTextDelimiter = alwaysDelimitText;
		boolean containsTextDelimiter = false;

		for (final char c : valueChars) {
			if (c == textDelimiter) {
				// 字段值中存在包装符
				containsTextDelimiter = needsTextDelimiter = true;
				break;
			} else if (c == fieldSeparator || c == CharUtil.LF || c == CharUtil.CR) {
				// 包含分隔符或换行符需要包装符包装
				needsTextDelimiter = true;
			}
		}

		// 包装符开始
		if (needsTextDelimiter) {
			writer.write(textDelimiter);
		}

		// 正文
		if (containsTextDelimiter) {
			for (final char c : valueChars) {
				// 转义文本包装符
				if (c == textDelimiter) {
					writer.write(textDelimiter);
				}
				writer.write(c);
			}
		} else {
			writer.write(valueChars);
		}

		// 包装符结尾
		if (needsTextDelimiter) {
			writer.write(textDelimiter);
		}
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}
