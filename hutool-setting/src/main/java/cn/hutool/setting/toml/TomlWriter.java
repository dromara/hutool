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

package cn.hutool.setting.toml;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.setting.SettingException;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class for writing TOML v0.4.0.
 * <h1>DateTimes support</h1>
 * <p>
 * Any {@link TemporalAccessor} may be added in a Map passed to this writer, this writer can only write three
 * kind of datetimes: {@link LocalDate}, {@link LocalDateTime} and {@link ZonedDateTime}.
 * </p>
 * <h1>Lenient bare keys</h1>
 * <p>
 * The {@link TomlWriter} always outputs data that strictly follows the TOML specification. Any key that
 * contains one
 * or more non-strictly valid character is surrounded by quotes.
 * </p>
 *
 * @author TheElectronWill
 */
public class TomlWriter {

	private final Writer writer;
	private final int indentSize;
	private final char indentCharacter;
	private final String lineSeparator;
	private final LinkedList<String> tablesNames = new LinkedList<>();
	private int lineBreaks = 0, indentationLevel = -1;// -1 to prevent indenting the first level

	/**
	 * Creates a new TomlWriter with the defaults parameters. The system line separator is used (ie '\n' on
	 * Linux and OSX, "\r\n" on Windows). This is exactly the same as
	 * {@code TomlWriter(writer, 1, false, System.lineSeparator()}.
	 *
	 * @param writer where to write the data
	 */
	public TomlWriter(final Writer writer) {
		this(writer, 1, false, System.lineSeparator());
	}

	/**
	 * Creates a new TomlWriter with the specified parameters. The system line separator is used (ie '\n' on
	 * Linux and OSX, "\r\n" on Windows). This is exactly the same as
	 * {@code TomlWriter(writer, indentSize, indentWithSpaces, System.lineSeparator())}.
	 *
	 * @param writer           where to write the data
	 * @param indentSize       the size of each indent
	 * @param indentWithSpaces true to indent with spaces, false to indent with tabs
	 */
	public TomlWriter(final Writer writer, final int indentSize, final boolean indentWithSpaces) {
		this(writer, indentSize, indentWithSpaces, System.lineSeparator());
	}

	/**
	 * Creates a new TomlWriter with the specified parameters.
	 *
	 * @param writer           where to write the data
	 * @param indentSize       the size of each indent
	 * @param indentWithSpaces true to indent with spaces, false to indent with tabs
	 * @param lineSeparator    the String to write to break lines
	 */
	public TomlWriter(final Writer writer, final int indentSize, final boolean indentWithSpaces, final String lineSeparator) {
		this.writer = writer;
		this.indentSize = indentSize;
		this.indentCharacter = indentWithSpaces ? CharUtil.SPACE : CharUtil.TAB;
		this.lineSeparator = lineSeparator;
	}

	/**
	 * Closes the underlying writer, flushing it first.
	 *
	 * @throws IOException if an error occurs
	 */
	public void close() throws IOException {
		writer.close();
	}

	/**
	 * Flushes the underlying writer.
	 *
	 * @throws IOException if an error occurs
	 */
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 * Writes the specified data in the TOML format.
	 *
	 * @param data the data to write
	 * @throws IOException if an error occurs
	 */
	public void write(final Map<String, Object> data) throws IOException {
		writeTableContent(data);
	}

	private void writeTableName() throws IOException {
		final Iterator<String> it = tablesNames.iterator();
		while (it.hasNext()) {
			final String namePart = it.next();
			writeKey(namePart);
			if (it.hasNext()) {
				write('.');
			}
		}
	}

	private void writeTableContent(final Map<String, Object> table) throws IOException {
		writeTableContent(table, true);
		writeTableContent(table, false);
	}

	/**
	 * Writes the content of a table.
	 *
	 * @param table        the table to write
	 * @param simpleValues true to write only the simple values (and the normal arrays), false to write only
	 *                     the tables
	 *                     (and the arrays of tables).
	 */
	@SuppressWarnings("unchecked")
	private void writeTableContent(final Map<String, Object> table, final boolean simpleValues) throws IOException {
		for (final Map.Entry<String, Object> entry : table.entrySet()) {
			final String name = entry.getKey();
			final Object value = entry.getValue();
			if (value instanceof Collection) {// array
				final Collection<?> c = (Collection<?>) value;
				if (false == c.isEmpty() && c.iterator().next() instanceof Map) {// array of tables
					if (simpleValues) {
						continue;
					}
					tablesNames.addLast(name);
					indentationLevel++;
					for (final Object element : c) {
						indent();
						write("[[");
						writeTableName();
						write("]]\n");
						final Map<String, Object> map = (Map<String, Object>) element;
						writeTableContent(map);
					}
					indentationLevel--;
					tablesNames.removeLast();
				} else {// normal array
					if (false == simpleValues) {
						continue;
					}
					indent();
					writeKey(name);
					write(" = ");
					writeArray(c);
				}
			} else if (value instanceof Object[]) {// array
				final Object[] array = (Object[]) value;
				if (array.length > 0 && array[0] instanceof Map) {// array of tables
					if (simpleValues) {
						continue;
					}
					tablesNames.addLast(name);
					indentationLevel++;
					for (final Object element : array) {
						indent();
						write("[[");
						writeTableName();
						write("]]\n");
						final Map<String, Object> map = (Map<String, Object>) element;
						writeTableContent(map);
					}
					indentationLevel--;
					tablesNames.removeLast();
				} else {// normal array
					if (false == simpleValues) {
						continue;
					}
					indent();
					writeKey(name);
					write(" = ");
					writeString(ArrayUtil.toString(array));
				}
			} else if (value instanceof Map) {// table
				if (simpleValues) {
					continue;
				}
				tablesNames.addLast(name);
				indentationLevel++;

				indent();
				write('[');
				writeTableName();
				write(']');
				newLine();
				writeTableContent((Map<String, Object>) value);

				indentationLevel--;
				tablesNames.removeLast();
			} else {// simple value
				if (!simpleValues) {
					continue;
				}
				indent();
				writeKey(name);
				write(" = ");
				writeValue(value);
			}
			newLine();
		}
		newLine();
	}

	private void writeKey(final String key) throws IOException {
		for (int i = 0; i < key.length(); i++) {
			final char c = key.charAt(i);
			if (false == isValidCharOfKey(c)) {
				// 含有非法字符，包装之
				writeString(key);
				return;
			}
		}
		write(key);
	}

	private static boolean isValidCharOfKey(final char c) {
		return (c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				c == '-' ||
				c == '_';
	}

	private void writeString(final String str) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append('"');
		for (int i = 0; i < str.length(); i++) {
			final char c = str.charAt(i);
			addEscaped(c, sb);
		}
		sb.append('"');
		write(sb.toString());
	}

	private void writeArray(final Collection<?> c) throws IOException {
		write('[');
		for (final Object element : c) {
			writeValue(element);
			write(", ");
		}
		write(']');
	}

	private void writeValue(final Object value) throws IOException {
		if (value instanceof String) {
			writeString((String) value);
		} else if (value instanceof Number || value instanceof Boolean) {
			write(value.toString());
		} else if (value instanceof TemporalAccessor) {
			String formatted = Toml.DATE_FORMATTER.format((TemporalAccessor) value);
			if (formatted.endsWith("T"))// If the last character is a 'T'
			{
				formatted = formatted.substring(0, formatted.length() - 1);// removes it because it's invalid.
			}
			write(formatted);
		} else if (value instanceof Collection) {
			writeArray((Collection<?>) value);
		} else if (ArrayUtil.isArray(value)) {
			write(ArrayUtil.toString(value));
		} else if (value instanceof Map) {// should not happen because an array of tables is detected by
			// writeTableContent()
			throw new IOException("Unexpected value " + value);
		} else {
			throw new SettingException("Unsupported value of type " + value.getClass().getCanonicalName());
		}
	}

	private void newLine() throws IOException {
		if (lineBreaks <= 1) {
			writer.write(lineSeparator);
			lineBreaks++;
		}
	}

	private void write(final char c) throws IOException {
		writer.write(c);
		lineBreaks = 0;
	}

	private void write(final String str) throws IOException {
		writer.write(str);
		lineBreaks = 0;
	}

	private void indent() throws IOException {
		for (int i = 0; i < indentationLevel; i++) {
			for (int j = 0; j < indentSize; j++) {
				write(indentCharacter);
			}
		}
	}

	static void addEscaped(final char c, final StringBuilder sb) {
		switch (c) {
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '"':
				sb.append("\\\"");
				break;
			default:
				sb.append(c);
				break;
		}
	}

}
