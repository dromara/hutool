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

package org.dromara.hutool.setting.toml;

import org.dromara.hutool.setting.SettingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * TOML文件读取<br>
 * 来自：https://github.com/TheElectronWill/TOML-javalib
 * <p>
 * 日期格式支持：
 * <ul>
 *     <li>2015-03-20                转为：{@link LocalDate}</li>
 *     <li>2015-03-20T19:04:35       转为：{@link LocalDateTime}</li>
 *     <li>2015-03-20T19:04:35+01:00 转为：{@link ZonedDateTime}</li>
 * </ul>
 * <p>
 * 此类支持更加宽松的key，除了{@code A-Za-z0-9_- }，还支持' ','.', '[', ']' 和 '='
 *
 * @author TheElectronWill
 */
public class TomlReader {

	private final String data;
	private final boolean strictAsciiBareKeys;
	private int pos = 0;// current position
	private int line = 1;// current line

	/**
	 * Creates a new TomlReader.
	 *
	 * @param data                the TOML data to read
	 * @param strictAsciiBareKeys {@code true} to allow only strict bare keys, {@code false} to allow lenient ones.
	 */
	public TomlReader(final String data, final boolean strictAsciiBareKeys) {
		this.data = data;
		this.strictAsciiBareKeys = strictAsciiBareKeys;
	}

	private boolean hasNext() {
		return pos < data.length();
	}

	private char next() {
		return data.charAt(pos++);
	}

	private char nextUseful(final boolean skipComments) {
		char c = ' ';
		while (hasNext() && (c == ' ' || c == '\t' || c == '\r' || c == '\n' || (c == '#' && skipComments))) {
			c = next();
			if (skipComments && c == '#') {
				final int nextLinebreak = data.indexOf('\n', pos);
				if (nextLinebreak == -1) {
					pos = data.length();
				} else {
					pos = nextLinebreak + 1;
					line++;
				}
			} else if (c == '\n') {
				line++;
			}
		}
		return c;
	}

	private char nextUsefulOrLinebreak() {
		char c = ' ';
		while (c == ' ' || c == '\t' || c == '\r') {
			if (!hasNext())// fixes error when no '\n' at the end of the file
				return '\n';
			c = next();
		}
		if (c == '\n')
			line++;
		return c;
	}

	private Object nextValue(final char firstChar) {
		switch (firstChar) {
			case '+':
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return nextNumberOrDate(firstChar);
			case '"':
				if (pos + 1 < data.length()) {
					final char c2 = data.charAt(pos);
					final char c3 = data.charAt(pos + 1);
					if (c2 == '"' && c3 == '"') {
						pos += 2;
						return nextBasicMultilineString();
					}
				}
				return nextBasicString();
			case '\'':
				if (pos + 1 < data.length()) {
					final char c2 = data.charAt(pos);
					final char c3 = data.charAt(pos + 1);
					if (c2 == '\'' && c3 == '\'') {
						pos += 2;
						return nextLiteralMultilineString();
					}
				}
				return nextLiteralString();
			case '[':
				return nextArray();
			case '{':
				return nextInlineTable();
			case 't':// Must be "true"
				if (pos + 3 > data.length() || next() != 'r' || next() != 'u' || next() != 'e') {
					throw new SettingException("Invalid value at line " + line);
				}
				return true;
			case 'f':// Must be "false"
				if (pos + 4 > data.length() || next() != 'a' || next() != 'l' || next() != 's' || next() != 'e') {
					throw new SettingException("Invalid value at line " + line);
				}
				return false;
			default:
				throw new SettingException("Invalid character '" + toString(firstChar) + "' at line " + line);
		}
	}

	/**
	 * 读取TOML
	 *
	 * @return TOML
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> read() {
		final Map<String, Object> map = nextTableContent();

		if (!hasNext() && pos > 0 && data.charAt(pos - 1) == '[')
			throw new SettingException("Invalid table declaration at line " + line + ": it never ends");

		while (hasNext()) {
			char c = nextUseful(true);
			final boolean twoBrackets;
			if (c == '[') {
				twoBrackets = true;
				c = nextUseful(false);
			} else {
				twoBrackets = false;
			}
			pos--;

			// --- Reads the key --
			final List<String> keyParts = new ArrayList<>(4);
			boolean insideSquareBrackets = true;
			while (insideSquareBrackets) {
				if (!hasNext())
					throw new SettingException("Invalid table declaration at line " + line + ": it never ends");

				String name = null;
				final char nameFirstChar = nextUseful(false);
				switch (nameFirstChar) {
					case '"': {
						if (pos + 1 < data.length()) {
							final char c2 = data.charAt(pos);
							final char c3 = data.charAt(pos + 1);
							if (c2 == '"' && c3 == '"') {
								pos += 2;
								name = nextBasicMultilineString();
							}
						}
						if (name == null) {
							name = nextBasicString();
						}
						break;
					}
					case '\'': {
						if (pos + 1 < data.length()) {
							final char c2 = data.charAt(pos);
							final char c3 = data.charAt(pos + 1);
							if (c2 == '\'' && c3 == '\'') {
								pos += 2;
								name = nextLiteralMultilineString();
							}
						}
						if (name == null) {
							name = nextLiteralString();
						}
						break;
					}
					default:
						pos--;// to include the first (already read) non-space character
						name = nextBareKey(']', '.').trim();
						if (data.charAt(pos) == ']') {
							if (!name.isEmpty())
								keyParts.add(name);
							insideSquareBrackets = false;
						} else if (name.isEmpty()) {
							throw new SettingException("Invalid empty key at line " + line);
						}

						pos++;// to go after the character we stopped at in nextBareKey()
						break;
				}
				if (insideSquareBrackets)
					keyParts.add(name.trim());
			}

			// -- Checks --
			if (keyParts.isEmpty())
				throw new SettingException("Invalid empty key at line " + line);

			if (twoBrackets && next() != ']') {// 2 brackets at the start but only one at the end!
				throw new SettingException("Missing character ']' at line " + line);
			}

			// -- Reads the value (table content) --
			final Map<String, Object> value = nextTableContent();

			// -- Saves the value --
			Map<String, Object> valueMap = map;// the map that contains the value
			for (int i = 0; i < keyParts.size() - 1; i++) {
				final String part = keyParts.get(i);
				final Object child = valueMap.get(part);
				final Map<String, Object> childMap;
				if (child == null) {// implicit table
					childMap = new LinkedHashMap<>(4);
					valueMap.put(part, childMap);
				} else if (child instanceof Map) {// table
					childMap = (Map<String, Object>) child;
				} else {// array
					final List<Map<String, Object>> list = (List<Map<String, Object>>) child;
					childMap = list.get(list.size() - 1);
				}
				valueMap = childMap;
			}
			if (twoBrackets) {// element of a table array
				final String name = keyParts.get(keyParts.size() - 1);
				Collection<Map<String, Object>> tableArray = (Collection<Map<String, Object>>) valueMap.get(name);
				if (tableArray == null) {
					tableArray = new ArrayList<>(2);
					valueMap.put(name, tableArray);
				}
				tableArray.add(value);
			} else {// just a table
				valueMap.put(keyParts.get(keyParts.size() - 1), value);
			}

		}
		return map;
	}

	private List<Object> nextArray() {
		final ArrayList<Object> list = new ArrayList<>();
		while (true) {
			final char c = nextUseful(true);
			if (c == ']') {
				pos++;
				break;
			}
			final Object value = nextValue(c);
			if (!list.isEmpty() && !(list.get(0).getClass().isAssignableFrom(value.getClass())))
				throw new SettingException("Invalid array at line " + line + ": all the values must have the same type");
			list.add(value);

			final char afterEntry = nextUseful(true);
			if (afterEntry == ']') {
				pos++;
				break;
			}
			if (afterEntry != ',') {
				throw new SettingException("Invalid array at line " + line + ": expected a comma after each value");
			}
		}
		pos--;
		list.trimToSize();
		return list;
	}

	private Map<String, Object> nextInlineTable() {
		final Map<String, Object> map = new LinkedHashMap<>();
		while (true) {
			final char nameFirstChar = nextUsefulOrLinebreak();
			String name = null;
			switch (nameFirstChar) {
				case '}':
					return map;
				case '"': {
					if (pos + 1 < data.length()) {
						final char c2 = data.charAt(pos);
						final char c3 = data.charAt(pos + 1);
						if (c2 == '"' && c3 == '"') {
							pos += 2;
							name = nextBasicMultilineString();
						}
					}
					if (name == null)
						name = nextBasicString();
					break;
				}
				case '\'': {
					if (pos + 1 < data.length()) {
						final char c2 = data.charAt(pos);
						final char c3 = data.charAt(pos + 1);
						if (c2 == '\'' && c3 == '\'') {
							pos += 2;
							name = nextLiteralMultilineString();
						}
					}
					if (name == null)
						name = nextLiteralString();
					break;
				}
				default:
					pos--;// to include the first (already read) non-space character
					name = nextBareKey(' ', '\t', '=');
					if (name.isEmpty())
						throw new SettingException("Invalid empty key at line " + line);
					break;
			}

			final char separator = nextUsefulOrLinebreak();// tries to find the '=' sign
			if (separator != '=')
				throw new SettingException("Invalid character '" + toString(separator) + "' at line " + line + ": expected '='");

			final char valueFirstChar = nextUsefulOrLinebreak();
			final Object value = nextValue(valueFirstChar);
			map.put(name, value);

			final char after = nextUsefulOrLinebreak();
			if (after == '}' || !hasNext()) {
				return map;
			} else if (after != ',') {
				throw new SettingException("Invalid inline table at line " + line + ": missing comma");
			}
		}
	}

	private Map<String, Object> nextTableContent() {
		final Map<String, Object> map = new LinkedHashMap<>();
		while (true) {
			final char nameFirstChar = nextUseful(true);
			if (!hasNext() || nameFirstChar == '[') {
				return map;
			}
			String name = null;
			switch (nameFirstChar) {
				case '"': {
					if (pos + 1 < data.length()) {
						final char c2 = data.charAt(pos);
						final char c3 = data.charAt(pos + 1);
						if (c2 == '"' && c3 == '"') {
							pos += 2;
							name = nextBasicMultilineString();
						}
					}
					if (name == null) {
						name = nextBasicString();
					}
					break;
				}
				case '\'': {
					if (pos + 1 < data.length()) {
						final char c2 = data.charAt(pos);
						final char c3 = data.charAt(pos + 1);
						if (c2 == '\'' && c3 == '\'') {
							pos += 2;
							name = nextLiteralMultilineString();
						}
					}
					if (name == null) {
						name = nextLiteralString();
					}
					break;
				}
				default:
					pos--;// to include the first (already read) non-space character
					name = nextBareKey(' ', '\t', '=');
					if (name.isEmpty())
						throw new SettingException("Invalid empty key at line " + line);
					break;
			}
			final char separator = nextUsefulOrLinebreak();// tries to find the '=' sign
			if (separator != '=')// an other character
				throw new SettingException("Invalid character '" + toString(separator) + "' at line " + line + ": expected '='");

			final char valueFirstChar = nextUsefulOrLinebreak();
			if (valueFirstChar == '\n') {
				throw new SettingException("Invalid newline before the value at line " + line);
			}
			final Object value = nextValue(valueFirstChar);

			final char afterEntry = nextUsefulOrLinebreak();
			if (afterEntry == '#') {
				pos--;// to make the next nextUseful() call read the # character
			} else if (afterEntry != '\n') {
				throw new SettingException("Invalid character '" + toString(afterEntry) + "' after the value at line " + line);
			}
			if (map.containsKey(name))
				throw new SettingException("Duplicate key \"" + name + "\"");

			map.put(name, value);
		}
	}

	private Object nextNumberOrDate(final char first) {
		boolean maybeDouble = true, maybeInteger = true, maybeDate = true;
		final StringBuilder sb = new StringBuilder();
		sb.append(first);
		char c;
		whileLoop:
		while (hasNext()) {
			c = next();
			switch (c) {
				case ':':
				case 'T':
				case 'Z':
					maybeInteger = maybeDouble = false;
					break;
				case 'e':
				case 'E':
					maybeInteger = maybeDate = false;
					break;
				case '.':
					maybeInteger = false;
					break;
				case '-':
					if (pos != 0 && data.charAt(pos - 1) != 'e' && data.charAt(pos - 1) != 'E')
						maybeInteger = maybeDouble = false;
					break;
				case ',':
				case ' ':
				case '\t':
				case '\n':
				case '\r':
				case ']':
				case '}':
					pos--;
					break whileLoop;
			}
			if (c == '_')
				maybeDate = false;
			else
				sb.append(c);
		}
		final String valueStr = sb.toString();
		try {
			if (maybeInteger) {
				if (valueStr.length() < 10)
					return Integer.parseInt(valueStr);
				return Long.parseLong(valueStr);
			}

			if (maybeDouble)
				return Double.parseDouble(valueStr);

			if (maybeDate)
				return Toml.DATE_FORMATTER.parseBest(valueStr, ZonedDateTime::from, LocalDateTime::from, LocalDate::from);

		} catch (final Exception ex) {
			throw new SettingException("Invalid value: \"" + valueStr + "\" at line " + line, ex);
		}

		throw new SettingException("Invalid value: \"" + valueStr + "\" at line " + line);
	}

	private String nextBareKey(final char... allowedEnds) {
		final String keyName;
		for (int i = pos; i < data.length(); i++) {
			final char c = data.charAt(i);
			for (final char allowedEnd : allowedEnds) {
				if (c == allowedEnd) {// checks if this character allowed to end this bare key
					keyName = data.substring(pos, i);
					pos = i;
					return keyName;
				}
			}
			if (strictAsciiBareKeys) {
				if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-'))
					throw new SettingException("Forbidden character '" + toString(c) + "' in strict bare-key at line " + line);
			} else if (c <= ' ' || c == '#' || c == '=' || c == '.' || c == '[' || c == ']') {// lenient bare key
				throw new SettingException("Forbidden character '" + toString(c) + "' in lenient bare-key at line " + line);
			} // else continue reading
		}
		throw new SettingException(
			"Invalid key/value pair at line " + line + " end of data reached before the value attached to the key was found");
	}

	private String nextLiteralString() {
		final int index = data.indexOf('\'', pos);
		if (index == -1)
			throw new SettingException("Invalid literal String at line " + line + ": it never ends");

		final String str = data.substring(pos, index);
		if (str.indexOf('\n') != -1)
			throw new SettingException("Invalid literal String at line " + line + ": newlines are not allowed here");

		pos = index + 1;
		return str;
	}

	private String nextLiteralMultilineString() {
		final int index = data.indexOf("'''", pos);
		if (index == -1)
			throw new SettingException("Invalid multiline literal String at line " + line + ": it never ends");
		final String str;
		if (data.charAt(pos) == '\r' && data.charAt(pos + 1) == '\n') {// "\r\n" at the beginning of the string
			str = data.substring(pos + 2, index);
			line++;
		} else if (data.charAt(pos) == '\n') {// '\n' at the beginning of the string
			str = data.substring(pos + 1, index);
			line++;
		} else {
			str = data.substring(pos, index);
		}
		for (int i = 0; i < str.length(); i++) {// count lines
			final char c = str.charAt(i);
			if (c == '\n')
				line++;
		}
		pos = index + 3;// goes after the 3 quotes
		return str;
	}

	private String nextBasicString() {
		final StringBuilder sb = new StringBuilder();
		boolean escape = false;
		while (hasNext()) {
			final char c = next();
			if (c == '\n' || c == '\r')
				throw new SettingException("Invalid basic String at line " + line + ": newlines not allowed");
			if (escape) {
				sb.append(unescape(c));
				escape = false;
			} else if (c == '\\') {
				escape = true;
			} else if (c == '"') {
				return sb.toString();
			} else {
				sb.append(c);
			}
		}
		throw new SettingException("Invalid basic String at line " + line + ": it nerver ends");
	}

	private String nextBasicMultilineString() {
		final StringBuilder sb = new StringBuilder();
		boolean first = true, escape = false;
		while (hasNext()) {
			final char c = next();
			if (first && (c == '\r' || c == '\n')) {
				if (c == '\r' && hasNext() && data.charAt(pos) == '\n')// "\r\n"
					pos++;// so that it is NOT read by the next call to next()
				else
					line++;
				first = false;
				continue;
			}
			if (escape) {
				if (c == '\r' || c == '\n' || c == ' ' || c == '\t') {
					if (c == '\r' && hasNext() && data.charAt(pos) == '\n')// "\r\n"
						pos++;
					else if (c == '\n')
						line++;
					nextUseful(false);
					pos--;// so that it is read by the next call to next()
				} else {
					sb.append(unescape(c));
				}
				escape = false;
			} else if (c == '\\') {
				escape = true;
			} else if (c == '"') {
				if (pos + 1 >= data.length())
					break;
				if (data.charAt(pos) == '"' && data.charAt(pos + 1) == '"') {
					pos += 2;
					return sb.toString();
				}
			} else if (c == '\n') {
				line++;
				sb.append(c);
			} else {
				sb.append(c);
			}
		}
		throw new SettingException("Invalid multiline basic String at line " + line + ": it never ends");
	}

	private char unescape(final char c) {
		switch (c) {
			case 'b':
				return '\b';
			case 't':
				return '\t';
			case 'n':
				return '\n';
			case 'f':
				return '\f';
			case 'r':
				return '\r';
			case '"':
				return '"';
			case '\\':
				return '\\';
			case 'u': {// unicode uXXXX
				if (data.length() - pos < 5)
					throw new SettingException("Invalid unicode code point at line " + line);
				final String unicode = data.substring(pos, pos + 4);
				pos += 4;
				try {
					final int hexVal = Integer.parseInt(unicode, 16);
					return (char) hexVal;
				} catch (final NumberFormatException ex) {
					throw new SettingException("Invalid unicode code point at line " + line, ex);
				}
			}
			case 'U': {// unicode UXXXXXXXX
				if (data.length() - pos < 9)
					throw new SettingException("Invalid unicode code point at line " + line);
				final String unicode = data.substring(pos, pos + 8);
				pos += 8;
				try {
					final int hexVal = Integer.parseInt(unicode, 16);
					return (char) hexVal;
				} catch (final NumberFormatException ex) {
					throw new SettingException("Invalid unicode code point at line " + line, ex);
				}
			}
			default:
				throw new SettingException("Invalid escape sequence: \"\\" + c + "\" at line " + line);
		}
	}

	/**
	 * Converts a char to a String. The char is escaped if needed.
	 */
	private String toString(final char c) {
		switch (c) {
			case '\b':
				return "\\b";
			case '\t':
				return "\\t";
			case '\n':
				return "\\n";
			case '\r':
				return "\\r";
			case '\f':
				return "\\f";
			default:
				return String.valueOf(c);
		}
	}

}
