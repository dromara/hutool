package cn.hutool.core.text.csv;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CSV行解析器，参考：FastCSV
 *
 * @author Looly
 */
public final class CsvParser implements Closeable, Serializable {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_ROW_CAPACITY = 10;

	private final Reader reader;
	private final CsvReadConfig config;

	private final Buffer buf = new Buffer(IoUtil.DEFAULT_LARGE_BUFFER_SIZE);
	/**
	 * 前一个特殊分界字符
	 */
	private int preChar = -1;
	/**
	 * 是否在引号包装内
	 */
	private boolean inQuotes;
	/**
	 * 当前读取字段
	 */
	private final StrBuilder currentField = new StrBuilder(512);

	/**
	 * 标题行
	 */
	private CsvRow header;
	/**
	 * 当前行号
	 */
	private long lineNo = -1;
	/**
	 * 引号内的行数
	 */
	private long inQuotesLineCount;
	/**
	 * 第一行字段数，用于检查每行字段数是否一致
	 */
	private int firstLineFieldCount = -1;
	/**
	 * 最大字段数量，用于初始化行，减少扩容
	 */
	private int maxFieldCount;
	/**
	 * 是否读取结束
	 */
	private boolean finished;

	/**
	 * CSV解析器
	 *
	 * @param reader Reader
	 * @param config 配置，null则为默认配置
	 */
	public CsvParser(final Reader reader, CsvReadConfig config) {
		this.reader = Objects.requireNonNull(reader, "reader must not be null");
		this.config = ObjectUtil.defaultIfNull(config, CsvReadConfig.defaultConfig());
	}

	/**
	 * 获取头部字段列表，如果containsHeader设置为false则抛出异常
	 *
	 * @return 头部列表
	 * @throws IllegalStateException 如果不解析头部或者没有调用nextRow()方法
	 */
	public List<String> getHeader() {
		if (false == config.containsHeader) {
			throw new IllegalStateException("No header available - header parsing is disabled");
		}
		if (lineNo < config.beginLineNo) {
			throw new IllegalStateException("No header available - call nextRow() first");
		}
		return header.fields;
	}

	/**
	 * 读取下一行数据
	 *
	 * @return CsvRow
	 * @throws IORuntimeException IO读取异常
	 */
	public CsvRow nextRow() throws IORuntimeException {
		List<String> currentFields;
		int fieldCount;
		while (false == finished) {
			currentFields = readLine();
			fieldCount = currentFields.size();
			if (fieldCount < 1) {
				// 空List表示读取结束
				break;
			}

			// 读取范围校验
			if(lineNo < config.beginLineNo){
				// 未达到读取起始行，继续
				continue;
			}
			if(lineNo > config.endLineNo){
				// 超出结束行，读取结束
				break;
			}

			// 跳过空行
			if (config.skipEmptyRows && fieldCount == 1 && currentFields.get(0).isEmpty()) {
				// [""]表示空行
				continue;
			}

			// 检查每行的字段数是否一致
			if (config.errorOnDifferentFieldCount) {
				if (firstLineFieldCount < 0) {
					firstLineFieldCount = fieldCount;
				} else if (fieldCount != firstLineFieldCount) {
					throw new IORuntimeException(String.format("Line %d has %d fields, but first line has %d fields", lineNo, fieldCount, firstLineFieldCount));
				}
			}

			// 记录最大字段数
			if (fieldCount > maxFieldCount) {
				maxFieldCount = fieldCount;
			}

			//初始化标题
			if (config.containsHeader && null == header) {
				initHeader(currentFields);
				// 作为标题行后，此行跳过，下一行做为第一行
				continue;
			}

			return new CsvRow(lineNo, null == header ? null : header.headerMap, currentFields);
		}

		return null;
	}

	/**
	 * 当前行做为标题行
	 *
	 * @param currentFields 当前行字段列表
	 */
	private void initHeader(final List<String> currentFields) {
		final Map<String, Integer> localHeaderMap = new LinkedHashMap<>(currentFields.size());
		for (int i = 0; i < currentFields.size(); i++) {
			final String field = currentFields.get(i);
			if (StrUtil.isNotEmpty(field) && false == localHeaderMap.containsKey(field)) {
				localHeaderMap.put(field, i);
			}
		}

		header = new CsvRow(this.lineNo, Collections.unmodifiableMap(localHeaderMap), Collections.unmodifiableList(currentFields));
	}

	/**
	 * 读取一行数据，如果读取结束，返回size为0的List<br>
	 * 空行是size为1的List，唯一元素是""
	 *
	 * <p>
	 *     行号要考虑注释行和引号包装的内容中的换行
	 * </p>
	 *
	 * @return 一行数据
	 * @throws IORuntimeException IO异常
	 */
	private List<String> readLine() throws IORuntimeException {
		// 矫正行号
		// 当一行内容包含多行数据时，记录首行行号，但是读取下一行时，需要把多行内容的行数加上
		if(inQuotesLineCount > 0){
			this.lineNo += this.inQuotesLineCount;
			this.inQuotesLineCount = 0;
		}

		final List<String> currentFields = new ArrayList<>(maxFieldCount > 0 ? maxFieldCount : DEFAULT_ROW_CAPACITY);

		final StrBuilder currentField = this.currentField;
		final Buffer buf = this.buf;
		int preChar = this.preChar;//前一个特殊分界字符
		int copyLen = 0; //拷贝长度
		boolean inComment = false;

		while (true) {
			if (false == buf.hasRemaining()) {
				// 此Buffer读取结束，开始读取下一段
				if (copyLen > 0) {
					buf.appendTo(currentField, copyLen);
					// 此处无需mark，read方法会重置mark
				}
				if (buf.read(this.reader) < 0) {
					// CSV读取结束
					finished = true;

					if (currentField.hasContent() || preChar == config.fieldSeparator) {
						//剩余部分作为一个字段
						addField(currentFields, currentField.toStringAndReset());
					}
					break;
				}

				//重置
				copyLen = 0;
			}

			final char c = buf.get();

			// 注释行标记
			if(preChar < 0 || preChar == CharUtil.CR || preChar == CharUtil.LF){
				// 判断行首字符为指定注释字符的注释开始，直到遇到换行符
				// 行首分两种，1是preChar < 0表示文本开始，2是换行符后紧跟就是下一行的开始
				if(c == this.config.commentCharacter){
					inComment = true;
				}
			}
			// 注释行处理
			if(inComment){
				if (c == CharUtil.CR || c == CharUtil.LF) {
					// 注释行以换行符为结尾
					lineNo++;
					inComment = false;
				}
				// 跳过注释行中的任何字符
				buf.mark();
				preChar = c;
				continue;
			}

			if (inQuotes) {
				//引号内，作为内容，直到引号结束
				if (c == config.textDelimiter) {
					// End of quoted text
					inQuotes = false;
				} else {
					// 字段内容中新行
					if (isLineEnd(c)) {
						inQuotesLineCount++;
					}
				}
				// 普通字段字符
				copyLen++;
			} else {
				// 非引号内
				if (c == config.fieldSeparator) {
					//一个字段结束
					if (copyLen > 0) {
						buf.appendTo(currentField, copyLen);
						copyLen = 0;
					}
					buf.mark();
					addField(currentFields, currentField.toStringAndReset());
				} else if (c == config.textDelimiter) {
					// 引号开始
					inQuotes = true;
					copyLen++;
				} else if (c == CharUtil.CR) {
					// \r，直接结束
					if (copyLen > 0) {
						buf.appendTo(currentField, copyLen);
					}
					buf.mark();
					addField(currentFields, currentField.toStringAndReset());
					preChar = c;
					break;
				} else if (c == CharUtil.LF) {
					// \n
					if (preChar != CharUtil.CR) {
						if (copyLen > 0) {
							buf.appendTo(currentField, copyLen);
						}
						buf.mark();
						addField(currentFields, currentField.toStringAndReset());
						preChar = c;
						break;
					}
					// 前一个字符是\r，已经处理过这个字段了，此处直接跳过
					buf.mark();
				} else {
					// 普通字符
					copyLen++;
				}
			}

			preChar = c;
		}

		// restore fields
		this.preChar = preChar;

		lineNo++;
		return currentFields;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	/**
	 * 将字段加入字段列表并自动去包装和去转义
	 *
	 * @param currentFields 当前的字段列表（即为行）
	 * @param field         字段
	 */
	private void addField(List<String> currentFields, String field) {
		final char textDelimiter = this.config.textDelimiter;

		// 忽略多余引号后的换行符
		field = StrUtil.trim(field, 1, (c-> c == CharUtil.LF || c == CharUtil.CR));

		field = StrUtil.unWrap(field, textDelimiter);
		field = StrUtil.replace(field, "" + textDelimiter + textDelimiter, textDelimiter + "");
		currentFields.add(field);
	}

	/**
	 * 是否行结束符
	 * @param c 符号
	 * @return 是否结束
	 * @since 5.7.4
	 */
	private boolean isLineEnd(char c){
		return (c == CharUtil.CR || c == CharUtil.LF) && preChar != CharUtil.CR;
	}

	/**
	 * 内部Buffer
	 *
	 * @author looly
	 */
	private static class Buffer implements Serializable{
		private static final long serialVersionUID = 1L;

		final char[] buf;

		/**
		 * 标记位置，用于读数据
		 */
		private int mark;
		/**
		 * 当前位置
		 */
		private int position;
		/**
		 * 读取的数据长度，一般小于buf.length，-1表示无数据
		 */
		private int limit;

		Buffer(int capacity) {
			buf = new char[capacity];
		}

		/**
		 * 是否还有未读数据
		 *
		 * @return 是否还有未读数据
		 */
		public final boolean hasRemaining() {
			return position < limit;
		}

		/**
		 * 读取到缓存
		 *
		 * @param reader {@link Reader}
		 */
		int read(Reader reader) {
			int length;
			try {
				length = reader.read(this.buf);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
			this.mark = 0;
			this.position = 0;
			this.limit = length;
			return length;
		}

		/**
		 * 先获取当前字符，再将当前位置后移一位<br>
		 * 此方法不检查是否到了数组末尾，请自行使用{@link #hasRemaining()}判断。
		 *
		 * @return 当前位置字符
		 * @see #hasRemaining()
		 */
		char get() {
			return this.buf[this.position++];
		}

		/**
		 * 标记位置记为下次读取位置
		 */
		void mark() {
			this.mark = this.position;
		}

		/**
		 * 将数据追加到{@link StrBuilder}，追加结束后需手动调用{@link #mark()} 重置读取位置
		 *
		 * @param builder {@link StrBuilder}
		 * @param length  追加的长度
		 * @see #mark()
		 */
		void appendTo(StrBuilder builder, int length) {
			builder.append(this.buf, this.mark, length);
		}
	}
}
